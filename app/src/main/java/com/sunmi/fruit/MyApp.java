package com.sunmi.fruit;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunmi.fruit.animutils.AnimationsContainer;
import com.sunmi.fruit.bean.ResultBean;
import com.sunmi.fruit.util.HttpUtil;
import com.sunmi.fruit.util.LogSunmi;
import com.sunmi.fruit.util.PrinterManagerUtil;
import com.sunmi.fruit.util.SharedPreferencesUtil;
import com.sunmi.fruit.util.Utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sunmi.fruit.Constant.locName;

public class MyApp extends Application {
    public static MyApp mApp;
    public static AnimationsContainer animation;
    public static AnimationsContainer loading_anim_bg;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        init();
    }
    public void init() {

        PrinterManagerUtil.getInstance().printInit();
//        Utils.getLoacle();
//        BaseActivity.initLanguage();
//        Utils.getLoacle();
    }
    @Override
    protected void attachBaseContext(Context newBase) {

        String name= SharedPreferencesUtil.getString(newBase,"locname");
        if(!TextUtils.isEmpty(name)){
            locName=name;
        }
        LogSunmi.e("====="+locName);

        super.attachBaseContext(BaseActivity.updateResources(newBase, locName));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogSunmi.e(newConfig.locale.toString());
    }


}
