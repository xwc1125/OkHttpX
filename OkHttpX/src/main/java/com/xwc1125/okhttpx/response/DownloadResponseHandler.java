package com.xwc1125.okhttpx.response;

import java.io.File;

/**
 * 下载回调
 *
 * @author tsy
 * @date 16/8/16
 */
public abstract class DownloadResponseHandler {

    /**
     * 开始
     *
     * @param totalBytes
     */
    public void onStart(long totalBytes) {

    }

    /**
     * 取消
     */
    public void onCancel() {

    }

    /**
     * 结束下载
     *
     * @param downloadFile
     */
    public abstract void onFinish(File downloadFile);

    /**
     * 下载进度
     *
     * @param currentBytes
     * @param totalBytes
     */
    public abstract void onProgress(long currentBytes, long totalBytes);

    /**
     * 下载出错
     *
     * @param error_msg
     */
    public abstract void onFailure(String error_msg);
}
