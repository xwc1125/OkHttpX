package com.xwc1125.okhttpx.response;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Gson类型的回调接口
 *
 * @author tsy
 * @date 16/8/16
 */
public abstract class GsonResponseHandler<T> implements IResponseHandler {

    private Type mType;

    public GsonResponseHandler() {
        //反射获取带泛型的class
        Type myclass = getClass().getGenericSuperclass();
        if (myclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        //获取所有泛型
        ParameterizedType parameter = (ParameterizedType) myclass;
        //将泛型转为type
        mType = $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

    private Type getType() {
        return mType;
    }

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
            Gson gson = new Gson();
            final T gsonResponse = (T) gson.fromJson(finalResponseBodyStr, getType());
            OkHttpX.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(response.code(), gsonResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("onResponse fail parse gson, body=" + finalResponseBodyStr);
            OkHttpX.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(response.code(), "fail parse gson, body=" + finalResponseBodyStr);
                }
            });

        }
    }

    public abstract void onSuccess(int statusCode, T response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
