package com.xwc1125.okhttpx;

import com.xwc1125.okhttpx.builder.*;
import com.xwc1125.okhttpx.util.SchedulerThreadHelper;
import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * OkHttpX
 *
 * @author tsy
 * @date 16/9/14
 */
public class OkHttpX {

    private static OkHttpClient mOkHttpClient;

    public static SchedulerThreadHelper mHandler = new SchedulerThreadHelper();

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * construct
     */
    public OkHttpX() {
        this(null);
    }

    /**
     * construct
     *
     * @param okHttpClient custom okhttpclient
     */
    public OkHttpX(OkHttpClient okHttpClient) {
        if (mOkHttpClient == null) {
            synchronized (OkHttpX.class) {
                if (mOkHttpClient == null) {
                    if (okHttpClient == null) {
                        mOkHttpClient = new OkHttpClient();
                    } else {
                        mOkHttpClient = okHttpClient;
                    }
                }
            }
        }
    }

    public GetBuilder get() {
        return new GetBuilder(this);
    }

    public PostBuilder post() {
        return new PostBuilder(this);
    }

    public PutBuilder put() {
        return new PutBuilder(this);
    }

    public PatchBuilder patch() {
        return new PatchBuilder(this);
    }

    public DeleteBuilder delete() {
        return new DeleteBuilder(this);
    }

    public UploadBuilder upload() {
        return new UploadBuilder(this);
    }

    public DownloadBuilder download() {
        return new DownloadBuilder(this);
    }

    /**
     * do cacel by tag
     *
     * @param tag tag
     */
    public void cancel(Object tag) {
        Dispatcher dispatcher = mOkHttpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }
}
