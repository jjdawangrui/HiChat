package com.itheima.hichat.utils;

import android.util.Log;

/**
 * Created by X599 on 2016/11/11.
 */
public class MyLogger {
    /**
     * Application TAG,use "logcat -s TAG"
     */
    private static String TAG = "MyLogger";


    private static boolean IS_FULL_CLASSNAME;

    /**
     * log level
     */
    private static int LOG_LEVEL = Log.VERBOSE;

    /**
     * print full class name or not
     *
     * @param isFullClassName
     */
    public static void setFullClassName(boolean isFullClassName) {
        MyLogger.IS_FULL_CLASSNAME = isFullClassName;
    }

    /**
     * set log level, default Log.VERBOSE
     *
     * @param level
     */
    public static void setLogLevel(int level) {
        MyLogger.LOG_LEVEL = level;
    }

    /**
     * set application TAG, default "MyLogger"
     *
     * @param tag
     */
    public static void setAppTAG(String tag) {
        MyLogger.TAG = tag;
    }


    public static void v(String msg) {
        if (LOG_LEVEL <= Log.VERBOSE) {
            Log.v(TAG, getLogTitle() + msg);
        }
    }


    public static void d(String msg) {
        if (LOG_LEVEL <= Log.DEBUG) {
            Log.d(TAG, getLogTitle() + msg);
        }
    }

    public static void i(String msg) {
        if (LOG_LEVEL <= Log.INFO) {
            Log.i(TAG, getLogTitle() + msg);
        }
    }

    public static void w(String msg) {
        if (LOG_LEVEL <= Log.WARN) {
            Log.w(TAG, getLogTitle() + msg);
        }
    }

    public static void e(String msg) {
        if (LOG_LEVEL <= Log.ERROR) {
            Log.e(TAG, getLogTitle() + msg);
        }
    }

    /**
     * make log title
     *
     * @return
     */
    private static String getLogTitle() {
        StackTraceElement elm = Thread.currentThread().getStackTrace()[4];
        String className = elm.getClassName();
        if (!IS_FULL_CLASSNAME) {
            int dot = className.lastIndexOf('.');
            if (dot != -1) {
                className = className.substring(dot + 1);
            }
        }
        return className + "." + elm.getMethodName() + "(" + elm.getLineNumber() + ")" + ": ";
    }


}