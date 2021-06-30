package com.sunmi.fruit.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunmi.fruit.MyApp;
import com.sunmi.fruit.R;
import com.sunmi.fruit.animutils.AnimationsContainer;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private Context context;      // 上下文
    private int mLayoutResId;      // 布局文件id
    private int[] mIds = new int[]{};  // 要监听的控件id
    private int mAnimationResId = 0;//主题style
    private OnCustomDialogItemClickListener listener;
    private boolean mIsDismiss = true;//是否默认所有按钮点击后取消dialog显示，false时需要在点击事件后手动调用dismiss
    private boolean mIsDismissTouchOut = false;//是否允许触摸dialog外部区域取消显示dialog
    private int mPosition = 0; //Dialog 相对页面显示的位置
    private List<View> mViews = new ArrayList<>();//监听的View的集合

    AnimationsContainer.FramesSequenceAnimation bg_anim;
    AnimationsContainer.FramesSequenceAnimation fg_anim;

    private int flag;
    private AnimationsContainer.FramesSequenceAnimation animation;

    public void setOnDialogItemClickListener(OnCustomDialogItemClickListener listener) {
        this.listener = listener;
    }

    public CustomDialog(Context context, int layoutResID) {
        super(context, R.style.Custom_Dialog_Style);
        this.context = context;
        this.mLayoutResId = layoutResID;

    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems, int flag) {
        super(context, R.style.TransparentDialogStyle); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
        this.flag = flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //Window window = getDialog().getWindow();如果是在activity中则用这段代码
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //window.requestWindowFeature(Window.FEATURE_NO_TITLE); 用在activity中，去标题
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);

        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        if (0 == mPosition) {
            window.setGravity(Gravity.CENTER); // dialog默认显示的位置为居中
        } else {
            window.setGravity(mPosition);// 设置自定义的dialog位置
        }


        if (mAnimationResId == 0) {
//            window.setWindowAnimations(R.style.bottom_animation); // 添加默认动画效果
//        } else {
//            window.setWindowAnimations(mAnimationResId);//添加自定义动画
        }
        setContentView(mLayoutResId);

        TextView dialog_title = findViewById(R.id.dialog_title);
        TextView end_text = findViewById(R.id.end_text);
        TextView end_text1 = findViewById(R.id.end_text1);
        ImageView iv_flag = findViewById(R.id.iv_flag);
        ImageView iv_bg = findViewById(R.id.iv_bg);
        ImageView iv_fg = findViewById(R.id.iv_fg);

        if (flag == 1) {
            bg_anim = MyApp.loading_anim_bg.createProgressDialogAnim(iv_bg);
            bg_anim.setOnAnimStopListener(new AnimationsContainer.OnAnimationStoppedListener() {
                @Override
                public void AnimationStopped() {
                    LogSunmi.e("CustomDialog", "bg_anim_AnimationStopped");
                }
            });
            bg_anim.start();
        } else if (flag == 3) {
            fg_anim = MyApp.animation.createProgressDialogAnim(iv_fg);
            fg_anim.setOnAnimStopListener(new AnimationsContainer.OnAnimationStoppedListener() {
                @Override
                public void AnimationStopped() {
                    LogSunmi.e("CustomDialog", "fg_anim_AnimationStopped");
                }
            });
            fg_anim.start();
        }

        if (flag == 1) {
            iv_flag.setImageResource(R.mipmap.icon_success);
        } else if(flag==2) {
            iv_flag.setImageResource(R.mipmap.icon_fail);
        }else if(flag==3){

        }
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        lp.gravity = Gravity.CENTER;
        lp.width = displayMetrics.widthPixels; // 设置dialog宽度为屏幕的4/5
        lp.height = displayMetrics.heightPixels; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(mIsDismissTouchOut);
        //遍历控件id,添加点击事件，添加资源到集合
        for (int id : mIds) {
            View view = findViewById(id);
            view.setOnClickListener(this);
            mViews.add(view);
        }
        if (dialog_title != null)
            mViews.add(dialog_title);
        if (end_text != null)
            mViews.add(end_text);
        if (end_text1 != null)
            mViews.add(end_text1);
    }

    public void setMessage() {

    }

    /**
     * 获取需要监听的View集合
     *
     * @return
     */
    public List<View> getViews() {
        return mViews;
    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }

    public interface OnCustomDialogItemClickListener {
        void OnCustomDialogItemClick(CustomDialog dialog, View view);
    }

    @Override
    public void onClick(View view) {
        //是否默认所有按钮点击后取消dialog显示，false是需要在点击事件后手动调用dismiss。
        if (mIsDismiss) {
            dismiss();
        }
        listener.OnCustomDialogItemClick(this, view);
    }

    public void onDestory() {
        if (bg_anim != null)
            bg_anim.onDestory();
        if (fg_anim != null)
            fg_anim.onDestory();
    }
}