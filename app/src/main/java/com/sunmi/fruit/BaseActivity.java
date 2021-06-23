package com.sunmi.fruit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.sunmi.fruit.util.LogSunmi;

import java.util.Locale;

import static com.sunmi.fruit.Constant.locName;

public class BaseActivity extends AppCompatActivity {

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(updateResources(newBase, locName));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
//        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                //布局位于状态栏下方
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                //全屏
//                View.SYSTEM_UI_FLAG_FULLSCREEN |
//                //隐藏导航栏
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        if (Build.VERSION.SDK_INT >= 19) {
//            uiOptions |= 0x00001000;
//        } else {
//            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        }
//        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }




    @TargetApi(Build.VERSION_CODES.N)
    public static Context updateResources(Context context, String language) {
        Resources resources = context.getResources();
        Locale locale = new Locale(language);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }

    /**
     * 判断当前的语言是否是简体中文
     *
     * @param context context
     * @return boolean
     */
    private static boolean currentLanguageIsSimpleChinese(Context context) {
        String language;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = context.getResources().getConfiguration().getLocales().get(0).getLanguage();
        } else {
            language = context.getResources().getConfiguration().locale.getLanguage();
        }
        LogSunmi.e("LanguageUtil", "language = " + language);
        return TextUtils.equals("zh", language);
    }

}
