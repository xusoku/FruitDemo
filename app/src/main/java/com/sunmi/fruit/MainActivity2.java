package com.sunmi.fruit;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.sunmi.fruit.animutils.AnimationsContainer;
import com.sunmi.fruit.util.AutoFitTextureView;
import com.sunmi.fruit.util.CameraManagerUtil;
import com.sunmi.fruit.util.CircleProgressView;
import com.sunmi.fruit.util.CustomDialog;
import com.sunmi.fruit.util.LogSunmi;
import com.sunmi.fruit.util.PrinterManagerUtil;
import com.sunmi.fruit.util.ScaleManagerUtil;
import com.sunmi.fruit.util.SoundPoolUtil;
import com.sunmi.fruit.util.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class MainActivity2 extends BaseActivity {
    private final static String TAG = MainActivity2.class.getSimpleName();
    private ArrayList<String> arrayList = new ArrayList(Arrays.asList(new String[Constant.arrayType.size()]));
    ;

    AutoFitTextureView mTexture;
    //    CardView cardview;
    LinearLayout linear_type, linear_weight, linear_price;
    TextView text_game_title, text_game_title2, text_game_type, text_game_weight, text_game_price, text_price, text_weight, text_type, text_game_tip, text_type1, text_game_type1;
    Button btn_game;
    CircleProgressView progress;
    private int countSecond = 0;
    private String name;
    private int currentLevel = 1;
    private boolean isEn = false;
    private boolean isEnd = false;

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            LogSunmi.e(TAG, " ===" + what + "   " + countSecond);
            if (what == 1) {
                this.sendEmptyMessageDelayed(1, 1000);
                countSecond--;
                progress.setProgress(countSecond);
                if (countSecond == 0) {
                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                    this.removeMessages(1);
                    showNextDialog(false);
                }
            } else if (what == 2) {
                this.sendEmptyMessageDelayed(2, 1000);
                countSecond--;
                progress.setProgress(countSecond);
                if (countSecond == 0) {
                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                    this.removeMessages(2);
                    showNextDialog(false);
                }
            } else if (what == 3) {
                this.sendEmptyMessageDelayed(3, 1000);
                countSecond--;
                progress.setProgress(countSecond);
                if (countSecond == 0) {
                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                    this.removeMessages(3);
                    showNextDialog(false);
                }
            } else if (what == 4) {
                this.sendEmptyMessageDelayed(4, 1000);
                countSecond--;
                ((Button) (customEndDialog.getViews().get(0))).setText(MyApp.mApp.getString(R.string.back_home) + countSecond + "s");
                if (countSecond == 0) {
                    this.removeMessages(4);
                    customEndDialog.dismiss();
                    desHandler();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        isEnd = false;
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Utils.getLoacle();
        isEn = Constant.locName.equals("en");
        mTexture = findViewById(R.id.textureView);
        btn_game = findViewById(R.id.btn_game);
        linear_type = findViewById(R.id.linear_type);
        linear_weight = findViewById(R.id.linear_weight);
        linear_price = findViewById(R.id.linear_price);
        text_game_type = findViewById(R.id.text_game_type);
        text_game_weight = findViewById(R.id.text_game_weight);
        text_game_price = findViewById(R.id.text_game_price);
        text_game_title = findViewById(R.id.text_game_title);
        text_game_title2 = findViewById(R.id.text_game_title2);
        text_price = findViewById(R.id.text_price);
        text_weight = findViewById(R.id.text_weight);
        text_type = findViewById(R.id.text_type);
        text_type1 = findViewById(R.id.text_type1);
        text_game_type1 = findViewById(R.id.text_game_type1);
        text_game_tip = findViewById(R.id.text_game_tip);
        progress = findViewById(R.id.progress);

        setTranslate();

        Collections.copy(arrayList, Constant.arrayType);

        mTexture.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CameraManagerUtil.getInstance().init(mTexture);
                    }
                }).start();
            }
        }, 100);

        fristLevel();


        ScaleManagerUtil.getInstance().scaleInit(new ScaleManagerUtil.ScaleResults() {
            @Override
            public void getResult(int net, int tare, boolean isStable) {
                LogSunmi.e(TAG, " ===" + net + " ===" + tare + " ===" + isStable);

                if (isEnd||TextUtils.isEmpty(name) || (customEndDialog != null && customEndDialog.isShowing())) {
                    return;
                }

                float price = Constant.hashPrice.get(name) * net;
                float objectPrice = Constant.hasBreadPrice.get(name);
                float objectWeight = Constant.hasfruitWeight.get(name);
                float factorPrice = objectPrice * 0.1f;
                float factorWeight = objectWeight * 0.2f;
                float minPrice = objectPrice - factorPrice;
                float maxPrice = objectPrice + factorPrice;
                float minWeight = objectWeight - factorWeight;
                float maxWeight = objectWeight + factorWeight;

                LogSunmi.e(TAG, "objectWeight =" + objectWeight + "  objectPrice =" + objectPrice + "  price =" + price + "  factorPrice =" + factorPrice + "  factorWeight =" + factorWeight + "  minPrice =" + minPrice + "  maxPrice=" + maxPrice + " minWeight=" + minWeight + " maxWeight=" + maxWeight);

                CameraManagerUtil.getInstance().takePicture(nameFF -> {

                    if (isEnd|| (customEndDialog != null && customEndDialog.isShowing())) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String nameF = nameFF;
                            if (TextUtils.isEmpty(nameF)) {
                                text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
                                text_game_type.setTextColor(Color.RED);
                                return;
                            }
                            if (Constant.hashName.get(nameF) == null) {
                                text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
                                text_game_type.setTextColor(Color.RED);
                                return;
                            }
                            text_game_type.setText(isEn ? Constant.hashNameEn.get(nameF) : Constant.hashName.get(nameF));
                            if (currentLevel == 1) {
                                if (nameF.equals(name) && (price >= minPrice && price <= maxPrice)) {
                                    isEnd = true;
                                    text_game_type.setTextColor(Color.GREEN);
                                    text_game_price.setTextColor(Color.GREEN);
                                    text_game_weight.setTextColor(Color.GREEN);
                                    progress.setProgress(0);
                                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                                    handler.removeMessages(1);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showNextDialog(true);
                                        }
                                    }, 1000);
                                } else {
                                    text_game_type.setTextColor(Color.RED);
                                    text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                    text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                }
                            } else if (currentLevel == 2) {
                                if (nameF.equals(name) && (net >= minWeight && net <= maxWeight)) {
                                    isEnd = true;
                                    text_game_type.setTextColor(Color.GREEN);
                                    text_game_price.setTextColor(Color.GREEN);
                                    text_game_weight.setTextColor(Color.GREEN);
                                    progress.setProgress(0);
                                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                                    handler.removeMessages(2);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showNextDialog(true);
                                        }
                                    }, 1000);

                                } else {
                                    text_game_type.setTextColor(Color.RED);
                                    text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                    text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                }
                            } else if (currentLevel == 3) {
                                if (nameF.equals(name) && (price >= minPrice && price <= maxPrice)) {
                                    isEnd = true;
                                    text_game_type.setTextColor(Color.GREEN);
                                    text_game_price.setTextColor(Color.GREEN);
                                    text_game_weight.setTextColor(Color.GREEN);
                                    progress.setProgress(0);
                                    btn_game.setText(MyApp.mApp.getString(R.string.jump_start));
                                    handler.removeMessages(3);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showEndDialog();
                                        }
                                    }, 1000);
                                } else {
                                    text_game_type.setTextColor(Color.RED);
                                    text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                    text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                }
                            }
                            if (nameF.equals(name) ) {
                                text_game_type.setTextColor(Color.GREEN);
                            }
                        }
                    });
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_game_weight.setText(net + "");
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        String p = decimalFormat.format(price);//format 返回的是字符串
                        text_game_price.setText(p + "");
                    }
                });

            }

            @Override
            public void getResultRate(int net, int tare, boolean isStable) {

                LogSunmi.e("TAG", isEnd+"  秤 结果=  获取称量净重=" + net + "  获取称量⽪重=" + tare + "   秤稳定状态=" + isStable);
                if (isEnd||TextUtils.isEmpty(name) || (customEndDialog != null && customEndDialog.isShowing())) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isStable) {
                            if (net >= 0) {
                                text_game_type.setText(MyApp.mApp.getResources().getString(R.string.recing));
                                if (net == 0) {
                                    text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
                                }
                                text_game_type.setTextColor(getResources().getColor(R.color.color_9D9D9D));
                                float price = Constant.hashPrice.get(name) * net;
                                text_game_weight.setText(net + "");
                                DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                                String p = decimalFormat.format(price);//format 返回的是字符串
                                text_game_price.setText(p + "");
                            }
                        }
                    }
                });
            }
        });

        if (MyApp.animation == null) {
            MyApp.animation = new AnimationsContainer(R.array.loading_anim, 358);
        }
        if (MyApp.loading_anim_bg == null) {
            MyApp.loading_anim_bg = new AnimationsContainer(R.array.loading_anim_bg, 358);
        }
    }

    private void setTranslate() {
        btn_game.setText(MyApp.mApp.getText(R.string.jump_start));
        text_price.setText(MyApp.mApp.getText(R.string.price));
        text_weight.setText(MyApp.mApp.getText(R.string.weight));
        text_type.setText(MyApp.mApp.getText(R.string.type));
        text_type1.setText(MyApp.mApp.getText(R.string.single_type));
        text_game_tip.setText(MyApp.mApp.getText(R.string.take_shop));
    }


    private void fristLevel() {
        isEnd = false;
        text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
        text_game_type.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        currentLevel = 1;
        int i = new Random().nextInt(Constant.arrayBreadType.size());
        name = Constant.arrayBreadType.get(i);
//        name="apple";
        text_game_weight.setText("000");
        text_game_price.setText("0.00");
        countSecond = 40;
        progress.setMax(countSecond);
        progress.setProgress(countSecond);
        float single_price = (Constant.hashPrice.get(name) * 500);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(single_price);//format 返回的是字符串
        text_game_type1.setText(p + "");
        String title = String.format(MyApp.mApp.getString(R.string.gane_title1), Constant.hasBreadPrice.get(name), isEn ? Constant.hashNameEn.get(name) : Constant.hashName.get(name));
        String subTitle = MyApp.mApp.getString(R.string.gane_title_sub);
        SpannableString spanColor = new SpannableString(title);
//        if (isEn) {
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 7, 8 + name.length(), 0);
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 29, 32, 0);
//        } else {
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 0, 3, 0);
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 8, 8 + Constant.hashName.get(name).length(), 0);
//        }
        text_game_title.setText(spanColor);
        text_game_title2.setText("");
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private void secondLevel() {
        isEnd = false;
        text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
        text_game_type.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        currentLevel = 2;

        int i = new Random().nextInt(arrayList.size());
        name = arrayList.get(i);
//        name="apple";
        text_game_weight.setText("000");
        text_game_price.setText("0.00");
        countSecond = 30;
        progress.setMax(countSecond);
        progress.setProgress(countSecond);
        float single_price = (Constant.hashPrice.get(name) * 500);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(single_price);//format 返回的是字符串
        text_game_type1.setText(p + "");
        String title = String.format(MyApp.mApp.getString(R.string.gane_title2), Constant.hasfruitWeight.get(name), isEn ? Constant.hashNameEn.get(name) : Constant.hashName.get(name));
        String subTitle = MyApp.mApp.getString(R.string.gane_title2_1);
        SpannableString spanColor = new SpannableString(title);
//        if (isEn) {
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 22, 27, 0);
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 31, 31 + (name).length(), 0);
//
//        } else {
//            spanColor.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.color_Yellow))), 1, 6, 0);
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 9, 9 + Constant.hashName.get(name).length(), 0);
//        }
        text_game_title.setText(spanColor);
        SpannableString spanColorSub = new SpannableString(subTitle);
//        if (isEn) {
//            spanColorSub.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 6, 9, 0);
//        } else {
//            spanColorSub.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 0, 3, 0);
//        }
        text_game_title2.setText("");
        handler.sendEmptyMessageDelayed(2, 1000);
    }

    private void thridLevel() {
        isEnd = false;
        text_game_type.setText(MyApp.mApp.getResources().getString(R.string.unkonw));
        text_game_type.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_price.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        text_game_weight.setTextColor(getResources().getColor(R.color.color_9D9D9D));
        currentLevel = 3;

        int i = new Random().nextInt(arrayList.size());
        name = arrayList.get(i);
//        name="apple";
        text_game_weight.setText("000");
        text_game_price.setText("0.00");
        countSecond = 20;
        progress.setMax(countSecond);
        progress.setProgress(countSecond);
        float single_price = (Constant.hashPrice.get(name) * 500);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(single_price);//format 返回的是字符串
        text_game_type1.setText(p + "");
        String title = String.format(MyApp.mApp.getString(R.string.gane_title3), Constant.hasBreadPrice.get(name), isEn ? Constant.hashNameEn.get(name) : Constant.hashName.get(name));
        String subTitle = MyApp.mApp.getString(R.string.gane_title_sub);
        SpannableString spanColor = new SpannableString(title);
//        if (isEn) {
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 16, 21, 0);
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 31 + name.length(), 35 + name.length(), 0);
//        } else {
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 3, 6, 0);
//            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 9, 12, 0);
////            spanColor.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_Yellow)), 13, 13 + Constant.hashName.get(name).length(), 0);
//        }
        text_game_title.setText(spanColor);
        text_game_title2.setText(subTitle);
        handler.sendEmptyMessageDelayed(3, 1000);
    }


    public void showNextDialog(boolean isRight) {
        if (customEndDialog != null && customEndDialog.isShowing()) {
            customEndDialog.dismiss();
        }
        customEndDialog = new CustomDialog(this,
                R.layout.dialog_normal_layout,
                new int[]{R.id.confirm_btn, R.id.cancel_btn}, isRight ? 1 : 2);
        customEndDialog.setOnDialogItemClickListener(new CustomDialog.OnCustomDialogItemClickListener() {
            @Override
            public void OnCustomDialogItemClick(CustomDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm_btn:
                        finish();
                        if(customEndDialog!=null){
                            customEndDialog.onDestory();
                        }
                        break;
                    case R.id.cancel_btn:
                        if (isRight) {
                            if (currentLevel == 1) {
                                arrayList.remove(name);
                                arrayList.remove("bread");
                                secondLevel();
                            } else if (currentLevel == 2) {
                                arrayList.remove(name);
                                thridLevel();
                            }
                        } else {
                            if (currentLevel == 1) {
                                fristLevel();
                            } else if (currentLevel == 2) {
                                secondLevel();
                            } else if (currentLevel == 3) {
                                thridLevel();
                            }
                        }
                        if(customEndDialog!=null){
                            customEndDialog.onDestory();
                        }
                        break;
                }
            }
        });
        customEndDialog.show();
        if (isRight) {
            SoundPoolUtil.getInstance().load(R.raw.success);
            ((TextView) (customEndDialog.getViews().get(2))).setText(MyApp.mApp.getString(R.string.dialog_text));
            ((Button) (customEndDialog.getViews().get(1))).setText(MyApp.mApp.getString(R.string.next_level));
            ((Button) (customEndDialog.getViews().get(0))).setText(MyApp.mApp.getString(R.string.back_home));
        } else {
            SoundPoolUtil.getInstance().load(R.raw.fail);
            ((TextView) (customEndDialog.getViews().get(2))).setText(MyApp.mApp.getString(R.string.try_again));
            ((Button) (customEndDialog.getViews().get(1))).setText(MyApp.mApp.getString(R.string.try_again_btn));
            ((Button) (customEndDialog.getViews().get(0))).setText(MyApp.mApp.getString(R.string.back_home));
        }
    }

    CustomDialog customEndDialog;

    public void showEndDialog() {
        if (customEndDialog != null && customEndDialog.isShowing()) {
            customEndDialog.dismiss();
        }
        SoundPoolUtil.getInstance().load(R.raw.pass);
        PrinterManagerUtil.getInstance().print(null);
        customEndDialog = new CustomDialog(this,
                R.layout.dialog_end_layout,
                new int[]{R.id.confirm_btn}, 3);
        customEndDialog.setCancelable(false);
        customEndDialog.setCanceledOnTouchOutside(false);
        customEndDialog.setOnDialogItemClickListener(new CustomDialog.OnCustomDialogItemClickListener() {
            @Override
            public void OnCustomDialogItemClick(CustomDialog dialog, View view) {
                desHandler();
                finish();
            }
        });
        customEndDialog.show();
        ((TextView) (customEndDialog.getViews().get(0))).setText(MyApp.mApp.getString(R.string.back_home));
        ((TextView) (customEndDialog.getViews().get(2))).setText(MyApp.mApp.getString(R.string.tip_lucky));
        ((TextView) (customEndDialog.getViews().get(1))).setText(MyApp.mApp.getString(R.string.game_over));
        countSecond = 15;
        handler.sendEmptyMessageDelayed(4, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrinterManagerUtil.getInstance().onDestroy();
        ScaleManagerUtil.getInstance().destory();
        CameraManagerUtil.getInstance().releaseCamera();
        MyApp.animation = null;
        MyApp.loading_anim_bg = null;
        desHandler();
        if(customEndDialog!=null){
            customEndDialog.onDestory();
        }
    }

    @Override
    public void finish() {
        super.finish();
        PrinterManagerUtil.getInstance().onDestroy();
        ScaleManagerUtil.getInstance().destory();
        CameraManagerUtil.getInstance().releaseCamera();
        MyApp.animation = null;
        MyApp.loading_anim_bg = null;
        desHandler();
        if(customEndDialog!=null){
            customEndDialog.onDestory();
        }
    }

    private void desHandler(){
        if (handler != null) {
            handler.removeMessages(1);
            handler.removeMessages(2);
            handler.removeMessages(3);
            handler.removeMessages(4);
        }
    }
}