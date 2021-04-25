package com.xwc1125.okhttpx.builder;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.callback.Callback;
import com.xwc1125.okhttpx.response.IResponseHandler;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Request;

/**
 * delete builder
 *
 * @author tsy
 * @date 2016/12/6
 */
public class DeleteBuilder extends OkHttpRequestBuilder<DeleteBuilder> {

    public DeleteBuilder(OkHttpX okHttpX) {
        super(okHttpX);
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            Request.Builder builder = new Request.Builder().url(mUrl).delete();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            mOkHttpX.getOkHttpClient()
                    .newCall(request)
                    .enqueue(new Callback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Delete enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }
}

