package com.escort.carriage.android.http.download;

import androidx.annotation.Keep;

/**
 * 下载信息javabean
 */
@Keep
public class DownloadInfo {

    /**
     * 开始下载
     */
    public static final String DOWNLOAD_START = "start";
    /**
     * 下载中
     */
    public static final String DOWNLOAD = "download";
    /**
     * 下载暂停
     */
    public static final String DOWNLOAD_PAUSE = "pause";
    /**
     * 等待下载
     */
    public static final String DOWNLOAD_WAIT = "wait";
    /**
     * 下载取消
     */
    public static final String DOWNLOAD_CANCEL = "cancel";
    /**
     * 下载结束
     */
    public static final String DOWNLOAD_OVER = "over";
    /**
     * 下载出错
     */
    public static final String DOWNLOAD_ERROR = "error";

    public static final long TOTAL_ERROR = -1;//获取进度失败

    private String url;
    private String fileName;
    /**
     * 文件路径
     */
    private String filePath;
    private String downloadStatus;
    private long total;
    private long progress;

    public DownloadInfo(String url) {
        this.url = url;
    }

    public DownloadInfo(String url, String downloadStatus) {
        this.url = url;
        this.downloadStatus = downloadStatus;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
