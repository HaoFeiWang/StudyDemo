package com.whf.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * 自定义水平的ProgressBar
 * Created by WHF on 2016/5/28.
 */
public class CustomHorizontalProgressBar extends ProgressBar {

    /*
        定义进度条属性的默认值
     */
    private static final int DEFAULT_TEXT_SIZE = 10;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 3;//dp
    private static final int DEFAULT_REACH_COLOR = 0xFFFC00D3;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp

    /*
        定义进度条属性使用值
     */
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    protected int mUnreachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnreachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);

    protected Paint mPaint = new Paint();
    private int mRealWidth;//进度条实际显示的宽度


    public CustomHorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CustomHorizontalProgressBar(Context context) {
        this(context, null);
    }

    public CustomHorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.CustomHorizontalProgressBar);

        mTextSize = (int) typedArray.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_text_size, mTextSize);

        mTextColor = typedArray.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_text_color, mTextColor);

        mTextOffset = (int) typedArray.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_text_offest, mTextOffset);

        mUnreachHeight = (int) typedArray.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_unreach_height, mUnreachHeight);

        mUnreachColor = typedArray.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_unreach_color, mUnreachColor);

        mReachHeight = (int) typedArray.getDimension(
                R.styleable.CustomHorizontalProgressBar_progress_reach_height, mReachHeight);

        mReachColor = typedArray.getColor(
                R.styleable.CustomHorizontalProgressBar_progress_reach_color, mReachColor);

        typedArray.recycle();

        mPaint.setTextSize(mTextSize);

    }

    /**
     * 布局的测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSize = MeasureHeight(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

    }

    /**
     * 测量进度条的高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int MeasureHeight(int heightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            result = heightSize;
        } else {
            //获取字体的高度
            int textSize = (int) (mPaint.descent() - mPaint.ascent());

            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mReachHeight, mUnreachHeight), Math.abs(textSize));

            if (heightMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, heightSize);
            }
        }
        return result;
    }

    /**
     * 绘制进度条
     *
     * @param canvas
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();

        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noNeedUnReah = false;

        String text = getProgress() + "%";
        int mTextWidth = (int) mPaint.measureText(text);
        int mTextAndOffest = mTextWidth + mTextOffset;

        float radio = getProgress() * 1.0f / getMax();//进度的百分比
        float progressX = radio * mRealWidth;//进度值

        if (progressX + mTextWidth / 2 >= mRealWidth) {
            progressX = mRealWidth - mTextWidth / 2;
            noNeedUnReah = true;
        }

        //绘制ReachBar
        if (progressX - mTextWidth / 2 > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, progressX - mTextAndOffest / 2, 0, mPaint);
        }

        //绘制文本
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX - mTextWidth / 2 < 0 ? 0 : progressX - mTextWidth / 2, y, mPaint);

        //绘制UnReachBar
        if (!noNeedUnReah) {
            float start = progressX +
                    (progressX - mTextWidth / 2 < 0 ? mTextWidth + mTextOffset / 2 : mTextAndOffest / 2);
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }

    /**
     * 将dp转换成px
     *
     * @param dpval
     * @return
     */
    protected int dp2px(int dpval) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpval, getResources().getDisplayMetrics());
    }

    /**
     * 将sp转换成px
     *
     * @param spval
     * @return
     */
    protected int sp2px(int spval) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spval, getResources().getDisplayMetrics());
    }
}
