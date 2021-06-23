package com.sunmi.fruit.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.sunmi.fruit.MyApp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import sunmi.os.SystemPropertiesUtils;


public class DeviceUtils {

    @SuppressLint("MissingPermission")
    public static String getDeviceModel() {
        String model = Build.MODEL;
        LogSunmi.e("nsz", "model = " + model);
        return model;
//         return "L2";
        // return "M1";
        // return "P1";
//         return "T2";
//         return "D1";
    }

    public static String getDeviceSubModel(Context context) {
//        LogUtil.e("nsz", "sub_model = " + DscReadDataUtils.getSubModel(context));
//        if (isDoubleScreen(context)) {
//            return DscReadDataUtils.getSubModel(context);
//        } else {
        return "";
//        }
        // return "V1S";
        // return "M1";
        // return "P1";
        // return "T1";
    }

    /**
     * 获取是否有副屏:
     * -1,未设置; 0 不带副屏版本; 1 带副屏
     *
     * @param context
     * @return true 表示为双屏否则不是
     */
    public static boolean isDoubleScreen(Context context) {
        int doubleScreen = -2;
        try {
            doubleScreen = Settings.Global.getInt(context.getContentResolver(), "sunmi_sub");
        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
        }
        return doubleScreen == 1;
    }

    /**
     * 机具是否支持3G
     *
     * @param context
     * @return true 支持
     */
    public static boolean is3GModemByROM(Context context) {
        boolean hasMobileNet = false;

        int checked = Settings.Global.getInt(context.getContentResolver(), "sunmi_hardwarecheck", 0);
        int modemStatus = Settings.Global.getInt(context.getContentResolver(), "sunmi_3g", -1);
        if (1 == checked && 1 == modemStatus) {
            hasMobileNet = true;
        }
        return hasMobileNet;
    }

    /**
     * 机具SIM有几个
     *
     * @param context
     * @return true 支持
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean getAvailableSimCardCount(Context context) {
        int count = 0;
        SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
        for (int i = 0; i < getSimCardCount(context); i++) {
            @SuppressLint("MissingPermission") SubscriptionInfo sir = mSubscriptionManager
                    .getActiveSubscriptionInfoForSimSlotIndex(i);
            if (sir != null) {
                count++;
            }
        }
        return count == 0;
    }

    public static int getSimCardCount(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Class cls = mTelephonyManager.getClass();
        try {
            Method mMethod = cls.getMethod("getSimCount");
            mMethod.setAccessible(true);
            return (int) mMethod.invoke(mTelephonyManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 判断是否支持电话功能
     */
    public static boolean supportMobile() {

        boolean isSupport = false;
        try {
            if (isAndroidQ()) {
                ConnectivityManager cm = (ConnectivityManager) MyApp.mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
                Method isNetworkSupported = ConnectivityManager.class.getMethod("isNetworkSupported", Integer.TYPE);
                isSupport = (boolean) isNetworkSupported.invoke(cm, ConnectivityManager.TYPE_MOBILE);
            } else {
                ConnectivityManager cm = (ConnectivityManager) MyApp.mApp.getSystemService(Context.CONNECTIVITY_SERVICE);
                Method isNetworkSupported = ConnectivityManager.class.getMethod("isNetworkSupported", Integer.TYPE);
                isSupport = (boolean) isNetworkSupported.invoke(cm, ConnectivityManager.TYPE_MOBILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSupport;
    }

    @SuppressLint("MissingPermission")
    public static String getMSN() {
        String msn = "";
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                msn = Build.getSerial();
            } else {
                msn = SystemPropertiesUtils.get("ro.serialno");
            }

            // msn = "15c2705047";
            // msn = "P101172P00012";
//            msn = "V101163B00667";
//             msn = "D101175C00044";
//            msn="T208D94L40141";
            LogSunmi.e("TAG", "getMSN: " + msn);

        } catch (Throwable e) {
            e.printStackTrace();
        }
//        msn = "L220180620002";
        return msn;
    }

    public static String getNewRom() {
        String v2 = "";//ro.sunmi.sec_sign=v2
        try {
            v2 = SystemPropertiesUtils.get("ro.sunmi.sec_sign");
            LogSunmi.e("TAG", "getv2: " + v2);
            if (TextUtils.isEmpty(v2)) {
                v2 = "";
            }
        } catch (Throwable e) {
            v2 = "";
            e.printStackTrace();
        }
        return v2;
    }

    public static boolean removeBuglyRom() {
        String flag = "";//ro.sunmi.remove_bugly=1
        try {
            flag = SystemPropertiesUtils.get("ro.sunmi.remove_bugly");
            LogSunmi.e("TAG", "remove_bugly: " + flag);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return flag.equals("1");
    }

    //是否是工场模式
    public static boolean isReset() {
        boolean isFact = false;
        try {
            isFact = SystemPropertiesUtils.getInt("ro.sunmi.welcome", 0) == 1;
        } catch (Throwable e) {

        }
        return isFact;
    }

    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

}
