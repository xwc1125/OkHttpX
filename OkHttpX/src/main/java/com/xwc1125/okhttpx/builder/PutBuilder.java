package com.xwc1125.okhttpx.builder;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.callback.Callback;
import com.xwc1125.okhttpx.response.IResponseHandler;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * put builder
 *
 * @author tsy
 * @date 16/12/06
 */
public class PutBuilder extends OkHttpRequestBuilder<PutBuilder> {

    public PutBuilder(OkHttpX okHttpX) {
        super(okHttpX);
    }

    @Override
    public void enqueue(IResponseHandler responseHandler) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl);
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            builder.put(RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), ""));
            Request request = builder.build();
            mOkHttpX.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new Callback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Put enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }
}
