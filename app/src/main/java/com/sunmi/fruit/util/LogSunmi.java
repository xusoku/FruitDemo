package com.sunmi.fruit.util;

import android.util.Log;

public class LogSunmi {
    private static final String TAG = "LogUtil";
    public static int LEVEL = Log.VERBOSE;

    public static void v(String TAG, String msg) {
        if (LEVEL <= Log.VERBOSE) {
            logWithLink(Log.VERBOSE, TAG, msg);
        }
    }

    public static void d(String TAG, String msg) {
        if (LEVEL <= Log.DEBUG) {
            logWithLink(Log.DEBUG, TAG, msg);
        }
    }

    public static void i(String TAG, String msg) {
        if (LEVEL <= Log.INFO) {
            logWithLink(Log.INFO, TAG, msg);
        }
    }

    public static void w(String TAG, String msg) {
        if (LEVEL <= Log.WARN) {
            logWithLink(Log.WARN, TAG, msg);
        }
    }

    public static void e(String TAG, String msg) {
        if (LEVEL <= Log.ERROR) {
            logWithLink(Log.ERROR, TAG, msg);
        }
    }

    public static void v(String msg) {
        if (LEVEL <= Log.VERBOSE) {
            logWithLink(Log.VERBOSE, TAG, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= Log.DEBUG) {
            logWithLink(Log.DEBUG, TAG, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= Log.INFO) {
            logWithLink(Log.INFO, TAG, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= Log.WARN) {
            logWithLink(Log.WARN, TAG, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= Log.ERROR) {
            logWithLink(Log.ERROR, TAG, msg);
        }
    }

    //Log输出的最大长度
    private static int LOG_MAX_LENGTH = 2000;//2000

    /**
     * 循环打印log，解决msg过长不打印问题
     */
    private static void LogWrapperLoop(int logPriority, String TAG, String msg) {
        int msgLength = msg.length();
        /**
         * adb  lgh=3
         *  ^
         *  |   index=1
         * print a
         */
        int index = 0;//输出字符的位置
        while (index < msgLength) {
            if (index + LOG_MAX_LENGTH > msgLength) {
                LogWrapper(logPriority, TAG, msg.substring(index, msgLength));
            } else {
                LogWrapper(logPriority, TAG, msg.substring(index, index + LOG_MAX_LENGTH));
            }
            index += LOG_MAX_LENGTH;
        }
    }

    private static void LogWrapper(int logPriority, String TAG, String msg) {
        switch (logPriority) {
            case Log.VERBOSE:
                Log.v(TAG, msg);
                break;
            case Log.DEBUG:
                Log.d(TAG, msg);
                break;
            case Log.INFO:
                Log.i(TAG, msg);
                break;
            case Log.WARN:
                Log.w(TAG, msg);
                break;
            case Log.ERROR:
                Log.e(TAG, msg);
                break;
            case Log.ASSERT:
                break;
            default:
                break;
        }
    }

    /**
     * 带有链接的log打印
     */
    private static void logWithLink(int logPriority, String TAG, String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");
        stringBuilder.append(msg);
        String logStr = stringBuilder.toString();
        LogWrapperLoop(logPriority, TAG, logStr);
    }
}
