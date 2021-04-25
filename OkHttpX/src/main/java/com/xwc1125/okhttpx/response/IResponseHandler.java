package com.xwc1125.okhttpx.response;

import okhttp3.Response;

/**
 * @author tsy
 * @date 16/8/15
 */
public interface IResponseHandler {

    /**
     * 成功
     *
     * @param response
     */
    void onSuccess(Response response);

    /**
     * 失败
     *
     * @param statusCode
     * @param error_msg
     */
    void onFailure(int statusCode, String error_msg);

    /**
     * 进度
     *
     * @param currentBytes
     * @param totalBytes
     */
    void onProgress(long currentBytes, long totalBytes);
}
