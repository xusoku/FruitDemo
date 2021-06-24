package com.sunmi.fruit.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.res.ResourcesCompat;

import com.sunmi.fruit.MyApp;
import com.sunmi.fruit.R;


public class CircleProgressView extends View {

    private final int mTxtHeight;
    /**
     * 进度条最大值，默认为100
     */
    private int maxValue = 100;

    /**
     * 当前进度值
     */
    private int currentValue = 0;

    /**
     * 每次扫过的角度，用来设置进度条圆弧所对应的圆心角，alphaAngle=(currentValue/maxValue)*360
     */
    private float alphaAngle;

    /**
     * 底部圆弧的颜色，默认为Color.LTGRAY
     */
    private int firstColor;

    /**
     * 进度条圆弧块的颜色
     */
    private int secondColor;

    /**
     * 圆环的宽度
     */
    private int circleWidth;


    private int bottomCitcleWidth = Utils.dp2px(15);
    private int topCitcleWidth = Utils.dp2px(15);
    private int one = Utils.dp2px(1);


    /**
     * 画圆弧的画笔
     */
    private Paint circlePaint;

    /**
     * 画文字的画笔
     */
    private Paint textPaint;

    /**
     * 渐变圆周颜色数组
     */
    private int[] colorArray = new int[]{Color.parseColor("#27B197"), Color.parseColor("#00A6D5")};//
    private float mTxtWidth;
    private ValueAnimator mAnimator;

    /**
     * 通过代码创建时才使用
     *
     * @param context
     */
    public CircleProgressView(Context context) {
        this(context, null);
    }

    /**
     * 当从xml中加载view的时候，这个构造器才会被调用。其第二个参数中就包含自定义的属性。
     *
     * @param context 上下文
     * @param attrs   自定义属性
     */
    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 从xml加载时执行和应用一个特定的风格。这里有两种方式，一是从theme中获得，二是从style中获得。        
     * 第三个参数官方有这样的说明： defStyle - The default style to apply to this view. If 0,
     * no style will be applied (beyond what is included in the theme). This may
     * either be an attribute resource, whose value will be retrieved from the
     * current theme, or an explicit style resource.
     * 默认的风格会被应用到这个view上。如果是0，没有风格将会被应用
     * （除了被包含在主题中）。这个也许是一个属性的资源，它的值是从当前的主题中检索，或者是一个明确的风格资源。
     *
     * @param context      上下文
     * @param attrs        自定义的属性
     * @param defStyleAttr 自定义风格
     */
    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView,
                defStyleAttr, 0);
        int n = ta.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.CircleProgressView_cpv_bottomColor) {
                firstColor = ta.getColor(attr, Color.LTGRAY); // 默认底色为亮灰色

            } else if (attr == R.styleable.CircleProgressView_cpv_topColor) {
                secondColor = ta.getColor(attr, Color.BLUE); // 默认进度条颜色为蓝色

            } else if (attr == R.styleable.CircleProgressView_cpv_circleWidth) {
                circleWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics())); // 默认圆弧宽度为6dp

            } else if (attr == R.styleable.CircleProgressView_cpv_max) {
                maxValue = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

            }
        }
        ta.recycle();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true); // 抗锯齿
        circlePaint.setDither(true); // 防抖动
        circlePaint.setStrokeWidth(bottomCitcleWidth);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        Typeface plain = ResourcesCompat.getFont(MyApp.mApp, R.font.heavy);
//        Typeface font = Typeface.create(plain, Typeface.BOLD);
        textPaint.setTypeface( plain );
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 分别获取期望的宽度和高度，并取其中较小的尺寸作为该控件的宽和高
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(measureWidth, measureHeight), Math.min(measureWidth, measureHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = this.getWidth() / 2;
        int radius = center - circleWidth / 2;

        drawCircle(canvas, center, radius); // 绘制进度圆弧

        drawText(canvas, center);//绘制中间文字

    }

    private void drawText(Canvas canvas, int center) {

        //字体
        String txt = currentValue + "";
        mTxtWidth = textPaint.measureText(txt, 0, txt.length());
        canvas.drawText(txt, center - mTxtWidth / 2, center + mTxtHeight / 3, textPaint);


    }

    /**
     * 绘制进度圆弧
     *
     * @param canvas 画布对象
     * @param center 圆心的x和y坐标
     * @param radius 圆的半径
     */
    private void drawCircle(Canvas canvas, int center, int radius) {
        circlePaint.setStrokeWidth(bottomCitcleWidth);
        circlePaint.setShader(null); // 清除上一次的shader
        circlePaint.setColor(firstColor); // 设置底部圆环的颜色，这里使用第一种颜色
        circlePaint.setStyle(Paint.Style.STROKE); // 设置绘制的圆为空心
        canvas.drawCircle(center, center, radius - one, circlePaint); // 画底部的空心圆
        RectF oval = new RectF(center - radius + one, center - radius + one, center + radius - one, center + radius - one); // 圆的外接正方形

       /* // 绘制颜色渐变圆环
        // shader类是Android在图形变换中非常重要的一个类。Shader在三维软件中我们称之为着色器，其作用是来给图像着色。
        LinearGradient linearGradient = new LinearGradient(circleWidth, circleWidth, getMeasuredWidth()
                - circleWidth, getMeasuredHeight() - circleWidth, colorArray, null, Shader.TileMode.MIRROR);*/
        //circlePaint.setShader(linearGradient);
        //  circlePaint.setShadowLayer(10, 10, 10, Color.RED);
        circlePaint.setStrokeWidth(topCitcleWidth);
        circlePaint.setColor(secondColor); // 设置圆弧的颜色
        circlePaint.setStrokeCap(Paint.Cap.ROUND); // 把每段圆弧改成圆角的

        alphaAngle = currentValue * 360.0f / maxValue * 1.0f; // 计算每次画圆弧时扫过的角度，这里计算要注意分母要转为float类型，否则alphaAngle永远为0
        canvas.drawArc(oval, -90, alphaAngle, false, circlePaint);
    }

    /**
     * 设置圆环的宽度
     *
     * @param width
     */
    public void setCircleWidth(int width) {
        this.circleWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources()
                .getDisplayMetrics());
        circlePaint.setStrokeWidth(circleWidth);
        invalidate();
    }
    public void setMax(int max) {
        this.maxValue=max;
        invalidate();
    }

    /**
     * 设置圆环的底色，默认为亮灰色LTGRAY
     *
     * @param color
     */
    public void setFirstColor(int color) {
        this.firstColor = color;
        circlePaint.setColor(firstColor);
        invalidate();
    }

    /**
     * 设置进度条的颜色，默认为蓝色<br>
     *
     * @param color
     */
    public void setSecondColor(int color) {
        this.secondColor = color;
        circlePaint.setColor(secondColor);
        invalidate();
    }

    /**
     * 设置进度条渐变色颜色数组
     *
     * @param colors 颜色数组，类型为int[]
     */
    public void setColorArray(int[] colors) {
        this.colorArray = colors;
        invalidate();
    }

    /**
     * 按进度显示百分比
     *
     * @param progress 进度，值通常为0到100
     */
    public void setProgress(int progress) {
        int percent = progress * maxValue / maxValue;
        LogSunmi.e("progress",percent+"---");
        if (percent < 0) {
            percent = 0;
        }
        if (percent > maxValue) {
            percent = maxValue;
        }
        this.currentValue = percent;
        int per80= (int) (maxValue*0.8f);
        int per60= (int) (maxValue*0.6f);
        int per50= (int) (maxValue*0.5f);
        int per40= (int) (maxValue*0.4f);
        int per35= (int) (maxValue*0.35f);

        LogSunmi.e("percent","  per80="+per80+"  per60="+per60+"  per50="+per50+"  per40="+per40+"  per35="+per35);
        if(percent==per80||percent==per60||percent==per50||percent==per40){
            Utils.animationScale(this);
        }else if(percent<=per35){
            Utils.animationScale(this,true);
        }
        if(percent==0){
            Utils.cancelAnimationScale();
        }
        invalidate();
    }


    public void restartAnim() {
        setProgress(currentValue, true);
    }

    /**
     * 按进度显示百分比，可选择是否启用数字动画
     *
     * @param progress     进度，值通常为0到100
     * @param useAnimation 是否启用动画，true为启用
     */
    public void setProgress(int progress, boolean useAnimation) {
        int percent = progress * maxValue / maxValue;
        if (percent < 0) {
            percent = 0;
        }
        if (percent > maxValue) {
            percent = maxValue;
        }
        if (useAnimation) // 使用动画
        {
            if (mAnimator != null) {
                mAnimator.cancel();
                mAnimator = null;
            }
            createAnim(percent);

            mAnimator.start();
        } else {
            setProgress(progress);
        }
    }

    private void createAnim(int percent) {
        mAnimator = ValueAnimator.ofInt(0, percent);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.setDuration(1000);
    }


}
