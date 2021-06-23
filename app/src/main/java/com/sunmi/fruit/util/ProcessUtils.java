package com.sunmi.fruit.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;



import java.util.List;


/**
 * 进程相关工具
 */
public class ProcessUtils {

    private static final String TAG = "ProcessUtils";

    /**
     * 获取指定应用的本地版本号
     */
    public static int getApplicationVersionCode(Context context, String pkgName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(pkgName, 0);
            if (info != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    return (int) info.getLongVersionCode();
                } else {
                    return info.versionCode;
                }

            }
        } catch (Exception e) {
            LogSunmi.e(TAG, pkgName + "应用没有安装");
        }
        return -1;
    }

    /**
     * 获取指定应用的本地版本名称
     */
    public static String getApplicationVersionName(Context context, String pkgName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(pkgName, 0);
            if (info != null) {
                return info.versionName;
            }
        } catch (Exception e) {
//            LogUtil.e(TAG, pkgName + "应用没有安装");
        }
        return null;
    }

    /**
     * 打开应用
     */
    public static void launchApp(final Context context, String packageName) {
        final int code = getApplicationVersionCode(context, packageName);
        PackageManager pm = context.getPackageManager();
        if (code != -1) {
            Intent i = new Intent(Intent.ACTION_MAIN, null);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setPackage(packageName);
            List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
            if (activities == null || activities.isEmpty()) {
                return;
            }
        }
        // 获取目标应用安装包的Intent
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setPackage(null);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(intent);
        }
    }

    /**
     * 判断应用是否安装
     */
    public static boolean isApkInstall(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception e) {
            packageInfo = null;
            LogSunmi.e(TAG, pkgName + "应用没有安装");
        }
        return packageInfo != null;
    }

    /**
     * 判断Intent是否存在
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        @SuppressLint("WrongConstant")
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }


}
