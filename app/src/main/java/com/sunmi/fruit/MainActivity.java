package com.sunmi.fruit;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.sunmi.fruit.animutils.AnimationsContainer;
import com.sunmi.fruit.util.CircleProgressView;
import com.sunmi.fruit.util.HttpUtil;
import com.sunmi.fruit.util.LogSunmi;
import com.sunmi.fruit.util.PrinterManagerUtil;
import com.sunmi.fruit.util.ScaleManagerUtil;
import com.sunmi.fruit.util.SharedPreferencesUtil;
import com.sunmi.fruit.util.SoundPoolUtil;
import com.sunmi.fruit.util.SwitchCompatEx;
import com.sunmi.fruit.util.Utils;

import java.util.Locale;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private CircleProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView ai_title = findViewById(R.id.ai_title);
        TextView ai_title1 = findViewById(R.id.ai_title1);
        TextView ai_title2 = findViewById(R.id.ai_title2);
        TextView btn_text = findViewById(R.id.btn_text);

        SwitchCompatEx switchCompatEx = findViewById(R.id.switchs);
        if (Constant.locName.equals("en")) {
            switchCompatEx.setChecked(true);
        } else {
            switchCompatEx.setChecked(false);
        }
        switchCompatEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.locName = "en";
                } else {
                    Constant.locName = "zh";
                }
                LogSunmi.e("locName", "====" + Constant.locName);
                SharedPreferencesUtil.setString(MyApp.mApp, Constant.locName, "locname");
                rebootApp();
            }
        });

        ai_title.setText(MyApp.mApp.getString(R.string.ai_title));
        ai_title1.setText(MyApp.mApp.getString(R.string.ai_title1));
        ai_title2.setText(MyApp.mApp.getString(R.string.ai_title2));
        btn_text.setText(MyApp.mApp.getString(R.string.jump_start));
        progressView=findViewById(R.id.progress);

        findViewById(R.id.btn_text).setOnClickListener(v -> {
            ScaleManagerUtil.getInstance().destory();
            startActivity(new Intent(this, MainActivity2.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ScaleManagerUtil.getInstance().destory();
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void rebootApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        startActivity(intent);
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

