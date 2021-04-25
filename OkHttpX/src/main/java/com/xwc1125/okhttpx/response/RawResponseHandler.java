package com.xwc1125.okhttpx.response;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * raw 字符串结果回调
 *
 * @author tsy
 * @date 16/8/15
 */
public abstract class RawResponseHandler implements IResponseHandler {

    @Override
    public final void onSuccess(final Response response) {
        ResponseBody responseBody = response.body();
        String responseBodyStr = "";

        try {
            responseBodyStr = responseBody.string();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("onResponse fail read response body");
            OkHttpX.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail read response body");
                }
            });
            return;
        } finally {
            responseBody.close();
        }

        final String finalResponseBodyStr = responseBodyStr;

        OkHttpX.mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response.code(), finalResponseBodyStr);
            }
        });
    }

    public abstract void onSuccess(int statusCode, String response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
