package com.sunmi.fruit.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.sunmi.fruit.R;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private Context context;      // 上下文
    private int mLayoutResId;      // 布局文件id
    private int[] mIds = new int[]{};  // 要监听的控件id
    private int mAnimationResId = 0;//主题style
    private OnCustomDialogItemClickListener listener;
    private boolean mIsDismiss = true;//是否默认所有按钮点击后取消dialog显示，false时需要在点击事件后手动调用dismiss
    private boolean mIsDismissTouchOut =false;//是否允许触摸dialog外部区域取消显示dialog
    private int mPosition = 0; //Dialog 相对页面显示的位置
    private List<View> mViews = new ArrayList<>();//监听的View的集合

    public void setOnDialogItemClickListener(OnCustomDialogItemClickListener listener) {
        this.listener = listener;
    }

    public CustomDialog(Context context, int layoutResID) {
        super(context, R.style.Custom_Dialog_Style);
        this.context = context;
        this.mLayoutResId = layoutResID;

    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems) {
        super(context, R.style.Custom_Dialog_Style); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems, int animationResId) {
        super(context, R.style.Custom_Dialog_Style); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
        this.mAnimationResId = animationResId;
    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems, boolean isDismiss) {
        super(context, R.style.Custom_Dialog_Style); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
        this.mIsDismiss = isDismiss;
    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems, boolean isDismiss, boolean isDismissTouchOut) {
        super(context, R.style.Custom_Dialog_Style); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
        this.mIsDismiss = isDismiss;
        this.mIsDismissTouchOut = isDismissTouchOut;
    }

    public CustomDialog(Context context, int layoutResID, int[] listenedItems, boolean isDismiss, int position) {
        super(context, R.style.Custom_Dialog_Style); //dialog的样式
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = listenedItems;
        this.mPosition = position;
    }


    /**
     * @param context
     * @param layoutResID       布局Id
     * @param ids               需要监听的View id集合
     * @param animationResId    动画资源id
     * @param isDismiss         是否默认点击所有View 取消dialog显示
     * @param isDismissTouchOut 是否触摸dialog外部区域消失dialog显示
     * @param position          dialog显示的位置
     */
    public CustomDialog(Context context,
                        int layoutResID,
                        int[] ids,
                        int animationResId,
                        boolean isDismiss,
                        boolean isDismissTouchOut,
                        int position) {
        super(context, R.style.Custom_Dialog_Style);
        this.context = context;
        this.mLayoutResId = layoutResID;
        this.mIds = ids;
        this.mAnimationResId = animationResId;
        this.mIsDismiss = isDismiss;
        this.mIsDismissTouchOut = isDismissTouchOut;
        this.mPosition = position;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //Window window = getDialog().getWindow();如果是在activity中则用这段代码
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //window.requestWindowFeature(Window.FEATURE_NO_TITLE); 用在activity中，去标题
        int uiOptions =View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_IMMERSIVE
                |View.SYSTEM_UI_FLAG_FULLSCREEN;
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
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
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

        TextView dialog_title=findViewById(R.id.dialog_title);
        TextView end_text=findViewById(R.id.end_text);
        TextView end_text1=findViewById(R.id.end_text1);
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity=Gravity.CENTER;
//        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(mIsDismissTouchOut);
        //遍历控件id,添加点击事件，添加资源到集合
        for (int id : mIds) {
            View view = findViewById(id);
            view.setOnClickListener(this);
            mViews.add(view);
        }
        if(dialog_title!=null)
            mViews.add(dialog_title);
        if(end_text!=null)
            mViews.add(end_text);
        if(end_text1!=null)
            mViews.add(end_text1);
    }

    public void setMessage(){

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
}