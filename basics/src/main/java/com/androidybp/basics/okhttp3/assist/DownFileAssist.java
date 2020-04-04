package com.androidybp.basics.okhttp3.assist;


import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.utils.thread.ThreadUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用OKHttp  下载文件的辅助类
 */
public abstract class DownFileAssist {
    private String url;
    private String downPath;
    private boolean isDown = true;

    public DownFileAssist(String url, String downPath) {
        this.downPath = downPath;
        initOkhttp(url);
    }

    private void initOkhttp(String url) {
        try {
            OkHttpManager.getManager().get(url, new DownFileCallback());
        } catch (Exception e) {
            downLoser();
        }
    }

    /**
     * 取消的当前下载的状态
     *
     * @param flag
     */
    public void deleteDownFile(boolean flag) {
        isDown = flag;
    }

    /**
     * 下载失败调用
     */
    public abstract void downLoser();

    /**
     * 要下载文件的大小
     *
     * @param length
     */
    public abstract void downFileLength(long length);

    /**
     * 当前下载的进度 下载的大小
     *
     * @param count
     */
    public abstract void downNumber(long count);

    /**
     *
     */
    public abstract void downFileAccomplish();

    class DownFileCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            downLoser();
        }

        @Override
        public void onResponse(Call call, Response response) {
            ResponseBody body = response.body();
            downFileLength(body.contentLength());

            InputStream is = body.byteStream();

            File file = new File(downPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                int count = 0;
                byte buf[] = new byte[2048];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    downNumber(count);
                    if (numread <= 0) {
                        ThreadUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                downFileAccomplish();
                            }
                        });

                        return;
                    }
                    fos.write(buf, 0, numread);
                } while (isDown);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (Exception e) {
                downLoser();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                    if (fos != null) {
                        fos.close();
                        fos = null;
                    }
                } catch (Exception e) {

                }
            }
        }
    }
}
