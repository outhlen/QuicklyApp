package com.escort.carriage.android.http.download;

/**
 * 文件下载监听类
 * <p>
 * 回调均在主线程
 * </P>
 */
public interface DownloadListener {
    /**
     * 下载开始
     */
    void onStart();

    /**
     * 下载进度
     *
     * @param progress - 下载进度百分比
     */
    void onProgress(int progress);

    /**
     * 下载成功
     *
     * @param filePath - 文件下载路径
     */
    void onSuccess(String filePath);

    /**
     * 下载失败
     *
     * @param message -错误信息
     */
    void onFailed(String message);
}
