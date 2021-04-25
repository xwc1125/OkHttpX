package com.xwc1125.okhttpx.callback;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.response.IResponseHandler;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author tsy
 * @date 16/9/18
 */
public class Callback implements okhttp3.Callback {

    private IResponseHandler mResponseHandler;

    public Callback(IResponseHandler responseHandler) {
        mResponseHandler = responseHandler;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        LogUtils.e("onFailure", e);
        OkHttpX.mHandler.post(new Runnable() {
            @Override
            public void run() {
                mResponseHandler.onFailure(0, e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) {
        if (response.isSuccessful()) {
            mResponseHandler.onSuccess(response);
        } else {
            LogUtils.e("onResponse fail status=" + response.code());

            OkHttpX.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mResponseHandler.onFailure(response.code(), "fail status=" + response.code());
                }
            });
        }
    }
}
