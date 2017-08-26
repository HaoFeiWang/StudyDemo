package com.whf.customprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by WHF on 2016/5/29.
 */
public class CustomRoundProgressBar extends CustomHorizontalProgressBar {

    private static final int DEFAULT_RADIO_SIZE = 20;//dp

    private int mRadioSize = dp2px(DEFAULT_RADIO_SIZE);
    private int mMaxPaintWidth;

    public CustomRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundProgressBar(Context context) {
        this(context, null);
    }

    public CustomRoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomRoundProgressBar);
        mRadioSize = (int) array.getDimension(
                R.styleable.CustomRoundProgressBar_progress_radio_size, mRadioSize);
        array.recycle();
    }

    @Override

    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        mMaxPaintWidth = Math.max(mReachHeight, mUnreachHeight);
        //view的大小,假设四个Padding的大小相等
        int expect = mRadioSize * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        //resolveSize会根据expect和widhtMeasureSpec两个值给出测量的距离
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        int readWidth = Math.min(width, height);
        //圆的半径
        mRadioSize = (readWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        setMeasuredDimension(readWidth, readWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);
        int progress = getProgress();
        float radio = progress * 1.0f / getMax();
        float angle = radio * 360;
        String text = progress + "%";//显示的百分比

        int textWidth = (int) mPaint.measureText(text);
        int textHeight = (int) (mPaint.descent() + mPaint.ascent());

        //绘制UnReach
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mRadioSize, mRadioSize, mRadioSize, mPaint);
        //绘制Reach
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(0, 0, mRadioSize * 2, mRadioSize * 2), 0, angle, false, mPaint);
        //绘制Text
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, mRadioSize - textWidth / 2, mRadioSize - textHeight / 2, mPaint);

        canvas.restore();
    }
}
