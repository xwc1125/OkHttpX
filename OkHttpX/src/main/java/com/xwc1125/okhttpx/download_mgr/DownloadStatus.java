package com.xwc1125.okhttpx.download_mgr;

/**
 * 下载状态
 *
 * @author tsy
 * @date 2016/11/25
 */
public enum DownloadStatus {
    STATUS_DEFAULT(-1, "初始状态"),
    STATUS_WAIT(0, "队列等待中"),
    STATUS_PAUSE(1, "暂停"),
    STATUS_DOWNLOADING(2, "下载中"),
    STATUS_FINISH(3, "下载完成"),
    STATUS_FAIL(4, "下载失败"),
    ;
    public int code;
    public String msg;

    DownloadStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static DownloadStatus parse(int code) {
        if (STATUS_DEFAULT.code == code) {
            return STATUS_DEFAULT;
        }
        if (STATUS_WAIT.code == code) {
            return STATUS_WAIT;
        }
        if (STATUS_PAUSE.code == code) {
            return STATUS_PAUSE;
        }
        if (STATUS_DOWNLOADING.code == code) {
            return STATUS_DOWNLOADING;
        }
        if (STATUS_FINISH.code == code) {
            return STATUS_FINISH;
        }
        if (STATUS_FAIL.code == code) {
            return STATUS_FAIL;
        }
        return STATUS_DEFAULT;
    }
}
