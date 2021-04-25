package com.xwc1125.okhttpx.download_mgr;

import com.xwc1125.okhttpx.OkHttpX;
import com.xwc1125.okhttpx.response.DownloadResponseHandler;
import okhttp3.Call;

import java.io.File;

/**
 * 下载任务
 *
 * @author tangsiyuan
 * @date 2016/11/23
 */
public class DownloadTask {

    private OkHttpX mOkHttpX;

    /**
     * task id
     */
    private String mTaskId;
    /**
     * 下载url
     */
    private String mUrl;
    /**
     * 保存文件path
     */
    private String mFilePath;
    /**
     * 断点续传 已经完成的bytes
     */
    private long mCompleteBytes;
    /**
     * 当前总共下载了的bytes
     */
    private long mCurrentBytes;
    /**
     * 文件总bytes
     */
    private long mTotalBytes;
    /**
     * Task状态
     */
    private DownloadStatus mStatus;
    /**
     * 距离下次保存下载进度的bytes
     */
    private long mNextSaveBytes = 0L;

    /**
     * 本次请求
     */
    private Call mCall;
    /**
     * task监听事件
     */
    private DownloadTaskListener mDownloadTaskListener;
    /**
     * 下载监听
     */
    private DownloadResponseHandler mDownloadResponseHandler;

    public DownloadTask() {

        mTaskId = "";
        mUrl = "";
        mFilePath = "";
        mCompleteBytes = 0L;
        mCurrentBytes = 0L;
        mTotalBytes = 0L;
        mStatus = DownloadStatus.STATUS_DEFAULT;
        mNextSaveBytes = 0L;

        // okhttpx的下载监听
        mDownloadResponseHandler = new DownloadResponseHandler() {
            @Override
            public void onStart(long totalBytes) {
                //下载总bytes等于上次下载的bytes加上这次断点续传的总bytes
                mTotalBytes = mCompleteBytes + totalBytes;
                mDownloadTaskListener.onStart(mTaskId, mCompleteBytes, mTotalBytes);
            }

            @Override
            public void onFinish(File download_file) {
                mStatus = DownloadStatus.STATUS_FINISH;
                mCurrentBytes = mTotalBytes;
                mCompleteBytes = mTotalBytes;
                mDownloadTaskListener.onFinish(mTaskId, download_file);
            }

            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                if (mStatus == DownloadStatus.STATUS_DOWNLOADING) {
                    //叠加每次增加的bytes
                    mNextSaveBytes += mCompleteBytes + currentBytes - mCurrentBytes;
                    //当前已经下载好的bytes
                    mCurrentBytes = mCompleteBytes + currentBytes;
                    mDownloadTaskListener.onProgress(mTaskId, mCurrentBytes, mTotalBytes);
                } else if (mStatus == DownloadStatus.STATUS_PAUSE) {
                    mCompleteBytes = mCurrentBytes;
                    if (!mCall.isCanceled()) {
                        mCall.cancel();
                    }
                } else {
                    mCompleteBytes = mCurrentBytes;
                    if (!mCall.isCanceled()) {
                        mCall.cancel();
                    }
                }
            }

            @Override
            public void onCancel() {
                mDownloadTaskListener.onPause(mTaskId, mCurrentBytes, mTotalBytes);
            }

            @Override
            public void onFailure(String error_msg) {
                mStatus = DownloadStatus.STATUS_FAIL;

                mDownloadTaskListener.onFailure(mTaskId, error_msg);
            }
        };
    }

    /**
     * 开始下载
     *
     * @return
     */
    public boolean doStart() {
        if (mStatus == DownloadStatus.STATUS_DOWNLOADING || mStatus == DownloadStatus.STATUS_FINISH) {
            return false;
        }

        mStatus = DownloadStatus.STATUS_DOWNLOADING;

        mCall = mOkHttpX.download()
                .url(mUrl)
                .filePath(mFilePath)
                .setCompleteBytes(mCompleteBytes)
                .enqueue(mDownloadResponseHandler);

        return true;
    }

    /**
     * 暂停下载
     */
    public void doPause() {
        if (mStatus == DownloadStatus.STATUS_PAUSE || mStatus == DownloadStatus.STATUS_FINISH) {
            return;
        }

        if (mStatus == DownloadStatus.STATUS_DOWNLOADING) {
            mStatus = DownloadStatus.STATUS_PAUSE;
            mCall.cancel();
        } else {
            mStatus = DownloadStatus.STATUS_PAUSE;
        }
    }

    public void doDestroy() {
        mDownloadTaskListener = null;
        mDownloadResponseHandler = null;

        if (mCall != null) {
            if (!mCall.isCanceled()) {
                mCall.cancel();
            }
            mCall = null;
        }
    }

    public OkHttpX getOkHttpX() {
        return mOkHttpX;
    }

    public void setOkHttpX(OkHttpX okHttpX) {
        mOkHttpX = okHttpX;
    }

    public String getTaskId() {
        return mTaskId;
    }

    public void setTaskId(String taskId) {
        mTaskId = taskId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public Long getCompleteBytes() {
        return mCompleteBytes;
    }

    public void setCompleteBytes(Long completeBytes) {
        mCompleteBytes = completeBytes;
        mCurrentBytes = mCompleteBytes;
    }

    public Long getTotalBytes() {
        return mTotalBytes;
    }

    public int getStatus() {
        return mStatus.code;
    }

    public void setStatus(DownloadStatus status) {
        mStatus = status;
    }

    public long getCurrentBytes() {
        return mCurrentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        mCurrentBytes = currentBytes;
    }

    public long getNextSaveBytes() {
        return mNextSaveBytes;
    }

    public void setNextSaveBytes(long nextSaveBytes) {
        mNextSaveBytes = nextSaveBytes;
    }

    public void setDownloadTaskListener(DownloadTaskListener downloadTaskListener) {
        mDownloadTaskListener = downloadTaskListener;
    }
}
