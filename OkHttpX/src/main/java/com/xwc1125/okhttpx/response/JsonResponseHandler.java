package com.xwc1125.okhttpx.response;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

/**
 * json类型的回调接口
 *
 * @author tsy
 * @date 16/8/15
 */
public abstract class JsonResponseHandler implements IResponseHandler {

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

        try {
            final Object result = new JSONTokener(finalResponseBodyStr).nextValue();
            if (result instanceof JSONObject) {
                OkHttpX.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONObject) result);
                    }
                });
            } else if (result instanceof JSONArray) {
                OkHttpX.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), (JSONArray) result);
                    }
                });
            } else {
                LogUtils.e("onResponse fail parse jsonObject, body=" + finalResponseBodyStr);
                OkHttpX.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onFailure(response.code(), "fail parse jsonObject, body=" + finalResponseBodyStr);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("onResponse fail parse jsonObject, body=" + finalResponseBodyStr);
            OkHttpX.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse jsonObject, body=" + finalResponseBodyStr);
                }
            });
        }
    }

    public void onSuccess(int statusCode, JSONObject response) {
        LogUtils.w("onSuccess(int statusCode, JSONObject response) was not overriden, but callback was received");
    }

    public void onSuccess(int statusCode, JSONArray response) {
        LogUtils.w("onSuccess(int statusCode, JSONArray response) was not overriden, but callback was received");
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
