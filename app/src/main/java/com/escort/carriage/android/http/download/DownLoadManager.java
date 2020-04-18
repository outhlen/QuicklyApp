package com.escort.carriage.android.http.download;

import android.os.Environment;
import android.text.TextUtils;

import com.androidybp.basics.utils.hint.LogUtils;
import com.escort.carriage.android.utils.CloseUtils;
import com.escort.carriage.android.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import cn.jpush.android.helper.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 文件下载管理类
 */

public class DownLoadManager {

    private static final String HTTP = "http";

    private OkHttpClient mClient;
    //用来存放各个下载的请求
    private volatile HashMap<String, Call> downCalls;
    private static DownLoadManager instance;


    public static DownLoadManager getInstance() {
        // 双重校验法实例化单列模式
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }


    private DownLoadManager() {
        downCalls = new HashMap<>();
        mClient = new OkHttpClient.Builder().build();
    }

    /**
     * 文件下载工具
     *
     * @param url      - 下载链接地址
     * @param fileName - 文件名称
     * @param listener - 下载回调
     */
    public DisposableObserver download(String url, final String fileName, final DownloadListener listener) {
        if (taskRunning(url)) {
            return null;
        }
        return Observable.just(url)
                //  在子线程中执行
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String url) {
                        // 过滤 call的map中已经有了,就证明正在下载,则这次不下载
                        return !downCalls.containsKey(url);
                    }
                }).flatMap(new Function<String, ObservableSource<DownloadInfo>>() { // 下载
                    @Override
                    public ObservableSource<DownloadInfo> apply(String url) {
                        return Observable.create(new DownloadSubscribe(url, fileName));
                    }
                })// 在主线程中回调
                .observeOn(AndroidSchedulers.mainThread())
                //  添加观察者，监听下载进度
                .subscribeWith(createObserver(listener));
    }

    /**
     * 判断某个下载任务是否正在执行
     *
     * @param taskUrl - 下载地址
     * @return - true:正在执行，false:未执行
     */
    private synchronized boolean taskRunning(final String taskUrl) {
        return downCalls.containsKey(taskUrl);
    }

    /**
     * 创建下载观察者
     *
     * @return - 异步操作观察者
     */
    private static DisposableObserver<DownloadInfo> createObserver(final DownloadListener listener) {
        return new DisposableObserver<DownloadInfo>() {
            @Override
            public void onNext(DownloadInfo downloadInfo) {
                if (listener != null) {
                    String downloadStatus = downloadInfo.getDownloadStatus();
                    switch (downloadStatus) {
                        // 下载开始
                        case DownloadInfo.DOWNLOAD_START:
                            listener.onStart();
                            break;
                        // 下载进行中
                        case DownloadInfo.DOWNLOAD:
                            // 当前下载文件
                            long progress = downloadInfo.getProgress();
                            // 文件总长度
                            long fileLength = downloadInfo.getTotal();
                            listener.onProgress((int) (progress * 100 / fileLength));
                            break;
                        // 下载完成
                        case DownloadInfo.DOWNLOAD_OVER:
                            listener.onSuccess(downloadInfo.getFilePath());
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                String message = "下载失败";
                if (e instanceof DownLoadException) {
                    message = e.getMessage();
                }
                if (listener != null) {
                    listener.onFailed(message);
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }


    /**
     * 取消下载
     */
    public void cancelDownload(String url) {
        Call call = downCalls.get(url);
        downCalls.remove(url);
        if (call != null) {
            call.cancel();//取消
        }
    }


    /**
     * 下载核心逻辑
     */
    private class DownloadSubscribe implements ObservableOnSubscribe<DownloadInfo> {

        /**
         * 下载链接地址
         */
        private String url;
        private String fileName;

        public DownloadSubscribe(String url, String fileName) {
            this.url = url;
            this.fileName = fileName;
        }

        @Override
        public void subscribe(ObservableEmitter<DownloadInfo> e) {
            InputStream is = null;
            FileOutputStream fileOutputStream = null;
            File tempFile = null;
            try {
                DownloadInfo downloadInfo = new DownloadInfo(url);
                //已经下载好的长度
                long downloadLength = 0;
                downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_START);
                downloadInfo.setProgress(0);
                //初始进度信息
                e.onNext(downloadInfo);
                if (TextUtils.isEmpty(url) || !url.startsWith(HTTP)) {
                    e.onError(new Exception("下载失败"));
                    return;
                }
                Request request = new Request.Builder()
                        //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                        // .addHeader("RANGE", "bytes=" + downloadLength + "-" + contentLength)
                        .url(url)
                        .build();
                Call call = mClient.newCall(request);
                downCalls.put(url, call);//把这个添加到call里,方便取消
                Response response = call.execute();
                // 文件总长度
                long contentLength = response.body().contentLength();
                downloadInfo.setTotal(contentLength);
                // 获取sd可用空间
                long sdCardAllSize = SDCardUtils.getSDCardAllSize();
                if (contentLength >= sdCardAllSize) {
                    throw new DownLoadException("SD卡存储空间不足");
                }

                // 创建临时文件
                tempFile = createFileByName("temp" + fileName);
                if (tempFile.exists()) {
                    tempFile.delete();
                }
                downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD);
                downloadInfo.setFilePath(tempFile.getAbsolutePath());
                is = response.body().byteStream();
                fileOutputStream = new FileOutputStream(tempFile);
                //缓冲数组2kB
                byte[] buffer = new byte[3072];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    downloadLength += len;
                    downloadInfo.setProgress(downloadLength);
                    e.onNext(downloadInfo);
                }
                fileOutputStream.flush();
                // 重名名文件
                File newFile = FileUtils.renameFile(tempFile, fileName);
                if (newFile == null) {
                    newFile = tempFile;
                }
                // 下载完成
                downloadInfo.setDownloadStatus(DownloadInfo.DOWNLOAD_OVER);
                downloadInfo.setFilePath(newFile.getAbsolutePath());
                e.onNext(downloadInfo);
                downCalls.remove(url);
            } catch (Exception excption) {
                String errMsg = excption.getMessage();
                LogUtils.showE("LHF", "下载失败：" + errMsg);
                if (!downCalls.containsKey(url)) {
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                    e.onError(new DownLoadException("下载取消"));
                } else {
                    // 下载失败
                    downCalls.remove(url);
                    e.onError(excption);
                }
                return;
            } finally {
                //关闭IO流
                CloseUtils.closeIO(is, fileOutputStream);
            }
            //完成
            e.onComplete();
        }
    }


    /**
     * 根据文件名称构造File文件
     *
     * @param fileName - 文件名称
     */
    public static File createFileByName(String fileName) {
        // 文件名称
//        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File downloadDir = new File(Environment.getExternalStorageDirectory()+"/gsports");
        if (!downloadDir.exists() || !downloadDir.isDirectory()) {
            downloadDir.mkdirs();
        }
        return new File(downloadDir, fileName);
    }

    /**
     * 自定义异常
     */
    private static class DownLoadException extends Exception {
        DownLoadException(String message) {
            super(message);
        }
    }
}
