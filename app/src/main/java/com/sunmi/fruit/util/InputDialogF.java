package com.sunmi.fruit.util;

/**
 * Created by xushengfu on 2019/8/9.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sunmi.fruit.R;


public class InputDialogF extends Dialog implements View.OnClickListener {

    private TextView mTitleTextView;
    private ImageView mDeleteImageView;
    private ImageView mEyeImageView;
    private EditText mEditText;
    private TextView mCancelTextView;
    private TextView mMiddleTextView;
    private TextView mSureTextView;
    private RelativeLayout mInputLayout;
    private View mLineView;
    private View mImageLineView;

    private Context mContext;
    private DialogOnClickCallback mCallback;
    private boolean isHide = false;
    private boolean hasMiddleBtn = false; // 是否有3个按钮

    private Params params;


    public InputDialogF(@NonNull Context context, Params params) {
        super(context, R.style.defaultDialogStyle);
        mContext = context;
        this.params = params;
        init();
    }

    public InputDialogF(@NonNull Context context, int themeResId, Params params) {
        super(context, themeResId);
        mContext = context;
        this.params = params;
        init();
    }

    private void init() {
        setContentView(R.layout.input_dialog_debug);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setCanceledOnTouchOutside(false);
        mTitleTextView = findViewById(R.id.tv_title);
        mDeleteImageView = findViewById(R.id.iv_delete);
        mEyeImageView = findViewById(R.id.iv_eye);
        mEditText = findViewById(R.id.et_input);
        mCancelTextView = findViewById(R.id.tv_cancel);
        mMiddleTextView = findViewById(R.id.tv_middle);
        mSureTextView = findViewById(R.id.tv_sure);
        mInputLayout = findViewById(R.id.rl_input);
        mLineView = findViewById(R.id.divide_line);
        mImageLineView = findViewById(R.id.line);
        if (params == null) {
            return;
        } else {
            setHasMiddleBtn(params.hasMiddleBtn);
            setInputEnable(params.enable);
            if (params.callback != null) {
                setCallback(params.callback);
            }
            if (!TextUtils.isEmpty(params.title)) {
                setTitle(params.title);
            }
            if (!TextUtils.isEmpty(params.hint)) {
                setHiht(params.hint);
            }
            if (!TextUtils.isEmpty(params.leftText)) {
                setLeftText(params.leftText);
            }
            if (!TextUtils.isEmpty(params.middleText)) {
                setMiddleText(params.middleText);
            }
            if (!TextUtils.isEmpty(params.rightText)) {
                setRightText(params.rightText);
            }
            if (!TextUtils.isEmpty(params.editText)) {
                setEditText(params.editText);
            }
            if (params.resId != 0) {
                setWindowAnimations(params.resId);
            }
        }
        mDeleteImageView.setOnClickListener(this);
        mEyeImageView.setOnClickListener(this);
//        mEyeImageView.setVisibility(View.GONE);
        mCancelTextView.setOnClickListener(this);
        mMiddleTextView.setOnClickListener(this);
        mSureTextView.setOnClickListener(this);

        mMiddleTextView.setVisibility(hasMiddleBtn ? View.VISIBLE : View.GONE);
        mLineView.setVisibility(hasMiddleBtn ? View.VISIBLE : View.GONE);

//        mEditText.setInputType(isHide ? EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
//                : EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mInputLayout.setBackgroundColor(Color.WHITE);
                    mDeleteImageView.setVisibility(View.VISIBLE);
                    mImageLineView.setVisibility(View.VISIBLE);
                } else {
                    mInputLayout.setBackgroundColor(Color.WHITE);
                    mDeleteImageView.setVisibility(View.GONE);
                    mImageLineView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_delete) {
            mEditText.setText("");
        } else if (v.getId() == R.id.iv_eye) {
            isHide = !isHide;
            mEditText.setInputType(isHide ? EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    : EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mEditText.setSelection(mEditText.length());
        } else if (v.getId() == R.id.tv_cancel) {
            if (mCallback != null) {
                mCallback.left(mEditText.getText().toString());
            }

//           mEditText.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    LaunchUtils.forceStopPackage(MyApplication.getContext(),MyApplication.getContext().getPackageName());
                    dismiss();
//                }
//            },1000);
        } else if (v.getId() == R.id.tv_sure) {
            if (mCallback != null) {
                mCallback.right(mEditText.getText().toString());
            }

            mEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            },1000);
        } else if (v.getId() == R.id.tv_middle) {
            if (mCallback != null) {
                mCallback.middle(mEditText.getText().toString());
            }
        }
    }

    /**
     * 如果输入有误时调用该方法
     */
    public void inputError() {
    }

    /**
     * 输入框禁用状态
     *
     * @param enable
     */
    public void setInputEnable(boolean enable) {
        mEditText.setEnabled(enable);
        mEyeImageView.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (!enable) {
            mDeleteImageView.setVisibility(View.GONE);
            mImageLineView.setVisibility(View.GONE);
        }
    }


    /**
     * 设置标题
     *
     * @param text
     */
    public void setTitle(String text) {
        mTitleTextView.setText(text);
    }
    /**
     *
     */
    public TextView getTitle() {
       return mTitleTextView;
    }

    /**
     * 设置是否有3个按钮
     *
     * @param hasMiddleBtn
     */
    private void setHasMiddleBtn(boolean hasMiddleBtn) {
        this.hasMiddleBtn = hasMiddleBtn;
    }

    /**
     * 设置输入框默认内容
     *
     * @param text
     */
    public void setHiht(String text) {
        mEditText.setHint(text);
    }

    /**
     * 设置左边按钮内容
     *
     * @param text
     */
    public void setLeftText(String text) {
        mCancelTextView.setText(text);
    }

    /**
     * 设置中间按钮内容
     *
     * @param text
     */
    public void setMiddleText(String text) {
        mMiddleTextView.setText(text);
    }
    public void setEditText(String text) {
        mEditText.setText(text);
        mEditText.setSelection(text.length());
    }

    /**
     * 设置右边按钮内容
     *
     * @param text
     */
    public void setRightText(String text) {
        mSureTextView.setText(text);
    }

    /**
     * 设置左边按钮enable状态
     *
     * @param enable
     */
    public void setLeftBtnEnable(boolean enable) {
        mCancelTextView.setEnabled(enable);
    }

    /**
     * 设置中间按钮enable状态
     *
     * @param enable
     */
    public void setMiddleBtnEnable(boolean enable) {
        mMiddleTextView.setEnabled(enable);
    }

    /**
     * 设置右边按钮enable状态
     *
     * @param enable
     */
    public void setRightBtnEnable(boolean enable) {
        mSureTextView.setEnabled(enable);
    }

    /**
     * 设置左边按钮字体颜色
     *
     * @param color
     */
    public void setLeftBtnColor(int color) {
        mCancelTextView.setTextColor(color);
    }

    /**
     * 设置中间按钮字体颜色
     *
     * @param color
     */
    public void setMiddleBtnColor(int color) {
        mMiddleTextView.setTextColor(color);
    }

    /**
     * 设置右边按钮字体颜色
     *
     * @param color
     */
    public void setRightBtnColor(int color) {
        mSureTextView.setTextColor(color);
    }

    /**
     * 设置按钮监听
     *
     * @param mCallback
     */
    public void setCallback(DialogOnClickCallback mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * 设置弹窗弹出/隐藏动画效果
     *
     * @param resId
     */
    private void setWindowAnimations(int resId) {
        if (getWindow() != null) {
            getWindow().setWindowAnimations(resId);
        }
    }

    public static class Params {
        public String leftText;
        public String middleText;
        public String rightText;
        public String editText;
        public String title;
        public String hint;
        public boolean hasMiddleBtn = false;
        public DialogOnClickCallback callback;
        public int resId;
        public boolean enable = true;
    }

    /**
     * 弹框建造类
     */
    public static class Builder {

        private Params params;

        public Builder() {
            params = new Params();
        }

        public Builder setLeftText(String text) {
            params.leftText = text;
            return this;
        }

        public Builder setMiddleText(String text) {
            params.middleText = text;
            return this;
        }

        public Builder setRightText(String text) {
            params.rightText = text;
            return this;
        }
        public Builder setEditText(String text){
            params.editText=text;
            return this;
        }

        public Builder setTitle(String text) {
            params.title = text;
            return this;
        }

        public Builder setHint(String text) {
            params.hint = text;
            return this;
        }

        public Builder setHasMiddleBtn(boolean hasMiddleBtn) {
            params.hasMiddleBtn = hasMiddleBtn;
            return this;
        }

        public Builder setCallBack(DialogOnClickCallback callBack) {
            params.callback = callBack;
            return this;
        }

        public Builder setWindowAnimations(int resId) {
            params.resId = resId;
            return this;
        }

        public Builder setInputEnable(boolean enable) {
            params.enable = enable;
            return this;
        }

        public InputDialogF build(Context context) {
            return new InputDialogF(context, params);
        }

        public InputDialogF build(Context context, int themeResId) {
            return new InputDialogF(context, themeResId, params);
        }
    }

    public interface DialogOnClickCallback {
        void left(String text);

        void middle(String text);

        void right(String text);
    }
}

