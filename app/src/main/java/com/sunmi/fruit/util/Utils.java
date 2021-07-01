package com.sunmi.fruit.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.animation.DecelerateInterpolator;


import com.sunmi.fruit.MyApp;
import com.sunmi.fruit.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;


public class Utils {

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
            LogSunmi.e("SunmiUtil", pkgName + "应用没有安装");
        }
        return -1;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap rawToBitmap() {
        return BitmapFactory.decodeStream(MyApp.mApp.getResources().openRawResource(R.raw.print));
    }

    public static void getLoacle() {
        Locale locale = MyApp.mApp.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String lc = language + "_" + country;
        String f_lauguage = SharedPreferencesUtil.getString(MyApp.mApp, "f_lauguage");
        LogSunmi.e("f_lauguage", f_lauguage + "   f_lauguage   " + lc);
    }

    /**
     * dip转pix
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        final float scale = MyApp.mApp.getResources().getDisplayMetrics().density;
        //  final float scale1 = AutoSizeConfig.getInstance().getInitDensity();
        //  LogUtil.d("dp2px","scale = " + scale);
        return (int) (dpValue * scale + 0.5f);
    }

    public static void animationScale(View v) {
        animationScale(v, false);
    }

    static AnimatorSet animatorSet;
    static ObjectAnimator animatorX;
    static ObjectAnimator animatorY;

    public static void cancelAnimationScale() {
        LogSunmi.e("a0","a0");
        if (animatorSet != null) {
            LogSunmi.e("a","a");
            animatorSet.cancel();
            animatorSet=null;
        }
        if (animatorX != null) {
            LogSunmi.e("a1","a1");
            animatorX.cancel();
            animatorX=null;
        }
        if (animatorY != null) {
            LogSunmi.e("a2","a1");
            animatorY.cancel();
            animatorY=null;
        }
    }

    public static void animationScale(View v, boolean isflag) {
        if (animatorX == null)
            animatorX = ObjectAnimator.ofFloat(v, "ScaleX", 0.9F, 0.6F, 1.0F, 0.9F);
        if (animatorY == null)
            animatorY = ObjectAnimator.ofFloat(v, "ScaleY", 0.9F, 0.6F, 1.0F, 0.9F);
        if (isflag) {
            animatorX.setRepeatCount(-1);
            animatorY.setRepeatCount(-1);
        }
        if (animatorSet == null)
            animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(isflag ? 300 : 500);
//        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }


    public static void FixMock(final Context context){
        InputDialogF mInputDialog= new InputDialogF.Builder()
                .setTitle("修改域名")
                .setLeftText("确定")
                .setRightText("取消")
                .setEditText("http://expo-fruit-det.test.sunmi.com/v1/fruit/recognition")
                .setHasMiddleBtn(false) // 是否显示中间的按钮
                .setInputEnable(true)  // 是否可以进行输入
                .setCallBack(new InputDialogF.DialogOnClickCallback() {
                    @Override
                    public void left(String text) { // 点击左边的按钮会回调该方法
                        if(!TextUtils.isEmpty(text)){
                            SharedPreferencesUtil.setString(context,text,"mock_env");
                        }
                    }

                    @Override
                    public void middle(String text) {  // 点击中间的按钮会回调该方法

                    }

                    @Override
                    public void right(String text) { // 点击右边的按钮会回调该方法
                    }
                })
                .build(context);
        mInputDialog.setLeftBtnEnable(true); // 设置左边按钮enable状态
        mInputDialog.setMiddleBtnEnable(true); // 设置中间按钮enable状态
        mInputDialog.setRightBtnEnable(true); // 设置右边按钮enable状态
        mInputDialog.show();
    }

}
