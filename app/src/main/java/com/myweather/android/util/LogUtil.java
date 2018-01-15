package com.myweather.android.util;

import android.util.Log;

/**
 * TODO
 * Created by kaler-chen on 2018/1/14.
 * com.myweather.android.util
 */

public class LogUtil {

    private static int LOG_LEVEL = 1;
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARNING = 4;
    private static final int ERROR = 5;

    public static void v(String tag, String msg, Throwable tr){
        if (LOG_LEVEL <= VERBOSE){
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg, Throwable tr){
        if (LOG_LEVEL <= DEBUG){
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr){
        if (LOG_LEVEL <= INFO){
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr){
        if (LOG_LEVEL <= WARNING){
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr){
        if (LOG_LEVEL <= ERROR){
            Log.e(tag, msg, tr);
        }
    }

}
