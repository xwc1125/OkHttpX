package com.xwc1125.okhttpx.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tsy
 * @date 16/8/15
 */
public class LogUtils {

    private static Logger log = LoggerFactory.getLogger(LogUtils.class);

    private static final String TAG = "OkHttpX";
    private static boolean LOG_ENABLE = true;

    private static String buildMsg(String msg) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(msg);

        return buffer.toString();
    }

    /**
     * 设置是否显示Log
     *
     * @param enable true-显示 false-不显示
     */
    public static void setLogEnable(boolean enable) {
        LOG_ENABLE = enable;
    }

    /**
     * verbose log
     *
     * @param msg log msg
     */
    public static void v(String msg) {
        if (LOG_ENABLE) {
            log.debug(TAG, buildMsg(msg));
        }
    }

    /**
     * verbose log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            log.debug(tag, buildMsg(msg));
        }
    }

    /**
     * debug log
     *
     * @param msg log msg
     */
    public static void d(String msg) {
        if (LOG_ENABLE) {
            log.debug(TAG, buildMsg(msg));
        }
    }

    /**
     * debug log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            log.debug(tag, buildMsg(msg));
        }
    }

    /**
     * info log
     *
     * @param msg log msg
     */
    public static void i(String msg) {
        if (LOG_ENABLE) {
            log.info(TAG, buildMsg(msg));
        }
    }

    /**
     * info log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            log.info(tag, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param msg log msg
     */
    public static void w(String msg) {
        if (LOG_ENABLE) {
            log.warn(TAG, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param msg log msg
     * @param e   exception
     */
    public static void w(String msg, Exception e) {
        if (LOG_ENABLE) {
            log.warn(TAG, buildMsg(msg), e);
        }
    }

    /**
     * warning log
     *
     * @param tag tag
     * @param msg log msg
     */
    public static void w(String tag, String msg) {
        if (LOG_ENABLE) {
            log.warn(tag, buildMsg(msg));
        }
    }

    /**
     * warning log
     *
     * @param tag tag
     * @param msg log msg
     * @param e   exception
     */
    public static void w(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            log.warn(tag, buildMsg(msg), e);
        }
    }

    /**
     * error log
     *
     * @param msg log msg
     */
    public static void e(String msg) {
        if (LOG_ENABLE) {
            log.error(TAG, buildMsg(msg));
        }
    }

    /**
     * error log
     *
     * @param msg log msg
     * @param e   exception
     */
    public static void e(String msg, Exception e) {
        if (LOG_ENABLE) {
            log.error(TAG, buildMsg(msg), e);
        }
    }

    /**
     * error log
     *
     * @param tag tag
     * @param msg msg
     */
    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            log.error(tag, buildMsg(msg));
        }
    }

    /**
     * error log
     *
     * @param tag tag
     * @param msg log msg
     * @param e   exception
     */
    public static void e(String tag, String msg, Exception e) {
        if (LOG_ENABLE) {
            log.error(tag, buildMsg(msg), e);
        }
    }
}
