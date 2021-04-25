package com.xwc1125.okhttpx.builder;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.callback.Callback;
import com.xwc1125.okhttpx.response.IResponseHandler;
import com.xwc1125.okhttpx.util.LogUtils;
import okhttp3.Request;

import java.util.Map;

/**
 * Get Builder
 *
 * @author tsy
 * @date 2016/12/6
 */
public class GetBuilder extends OkHttpRequestBuilderHasParam<GetBuilder> {

    public GetBuilder(OkHttpX okHttpX) {
        super(okHttpX);
    }

    @Override
    public void enqueue(final IResponseHandler responseHandler) {
        try {
            if (mUrl == null || mUrl.length() == 0) {
                throw new IllegalArgumentException("url can not be null !");
            }

            if (mParams != null && mParams.size() > 0) {
                mUrl = appendParams(mUrl, mParams);
            }

            Request.Builder builder = new Request.Builder().url(mUrl).get();
            appendHeaders(builder, mHeaders);

            if (mTag != null) {
                builder.tag(mTag);
            }

            Request request = builder.build();

            mOkHttpX.getOkHttpClient().
                    newCall(request).
                    enqueue(new Callback(responseHandler));
        } catch (Exception e) {
            LogUtils.e("Get enqueue error:" + e.getMessage());
            responseHandler.onFailure(0, e.getMessage());
        }
    }

    /**
     * append params to url
     *
     * @param url
     * @param params
     * @return
     */
    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
