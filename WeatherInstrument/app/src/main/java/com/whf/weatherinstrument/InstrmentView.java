package com.whf.weatherinstrument;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WHF on 2017/3/22.
 */

public class InstrmentView extends View {

    //跨过的角度
    private static final int SWEEP_ANGLE = 300;
    //指标个数
    private static final int COUNT = 50;
    //开始的角度
    private static final int START_ANGLE = 120;
    //参考的起始角度，用于绘制起始标线
    private static final int REFER_START_ANGLE = 270;
    //温度范围跨越的角度
    private static final int COVER_ANGLE = 90;

    //线的高度
    private static final int LINE_HEIGHT = 15;
    //边界线的高度
    private static final int FIRST_LINE_HEIGHT = 20;
    //标线圆的的外边距，用于留空绘制温度的起点和终点
    private static final int CIRCLE_MARGIN = 25;
    //普通标线的宽度
    private static final int LINE_WEIGHT = 1;
    //高亮标线的宽度
    private static final int COVER_LINE_WEIGHT = 2;

    //中间温度字体大小
    private static final int TEXT_SIZE_COUVER = 15;
    //范围温度字体大小
    private static final int TEXT_SIZE_CUR = 100;

    private int mCurTemp;
    private int mMimTemp;
    private int mMaxTemp;
    private Paint mPaint;

    private int mRadiu;

    public InstrmentView(Context context) {
        super(context);
        init();
    }

    public InstrmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRadiu = (int) ((getWidth() - dp2px(CIRCLE_MARGIN * 2)) /  2);
    }

    private void init() {
        mMimTemp = 10;
        mMaxTemp = 26;
        mCurTemp = 14;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        drawBitmap(canvas);
        drawCurTemp(canvas);
        drawTempText(canvas);
    }

    private void drawLine(Canvas canvas) {

        canvas.save();
        float itemSweepAngle = (float) SWEEP_ANGLE / COUNT;
        int width = getWidth()/2;
        int height = getHeight()/2;
        int margin = (int) dp2px(CIRCLE_MARGIN);
        int linHeight = (int) dp2px(LINE_HEIGHT);
        int firstLineHeight = (int) dp2px(FIRST_LINE_HEIGHT);
        int curLineWeight = (int) dp2px(COVER_LINE_WEIGHT);
        int linWeight = (int) dp2px(LINE_WEIGHT);

        //值为正是逆时针旋转
        canvas.rotate(START_ANGLE - REFER_START_ANGLE, width, height);
        for (int i = 0; i <= COUNT; i++) {
            if (i == 0 || i == COUNT) {
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(linWeight);
                canvas.drawLine(width, margin, width, firstLineHeight+margin, mPaint);

            } else if (i >= getStartLineIndex() && i <= getEndLineIndex()) {
                mPaint.setColor(getRealColor());
                mPaint.setStrokeWidth(curLineWeight);
                canvas.drawLine(width, margin, width, linHeight+margin, mPaint);

            } else {
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(linWeight);
                canvas.drawLine(width, margin, width, linHeight+margin, mPaint);
            }
            canvas.rotate(itemSweepAngle, width , height);
        }
        canvas.restore();
    }

    /**
     * 绘制当前温度
     *
     * @param canvas
     */
    private void drawCurTemp(Canvas canvas) {

        mPaint.setTextSize(sp2px(TEXT_SIZE_CUR));
        mPaint.setStyle(Paint.Style.FILL);

        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float top = metrics.top;
        float bottom = metrics.bottom;
        float baseLine = (getHeight() - (top + bottom)) / 2;

        //ALT0176是°符号
        canvas.drawText(mCurTemp + "°", getWidth() / 2, baseLine, mPaint);

    }

    /**
     * 绘制天气图片
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow);
        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, getHeight() - bitmap.getHeight(), mPaint);
    }

    /**
     * 根据角度获取X的长度
     *
     * @param angle
     * @return
     */
    private double getXFromAngle(int angle) {
        return Math.cos(angle * Math.PI / 180) * mRadiu;
    }

    /**
     * 根据角度获取Y的长度
     *
     * @param angle
     * @return
     */
    private double getYFromAngle(int angle) {
        return Math.sin(angle * Math.PI / 180) * mRadiu;
    }

    /**
     * 根据角度获取真实的X坐标
     *
     * @param angle
     * @return
     */
    private double getRealX(int angle) {
        double x = getXFromAngle(angle);
        if (x > 0) {
            x = getWidth() / 2 + x + dp2px(2*CIRCLE_MARGIN / 3);
        } else {
            x = getWidth() / 2 + x - dp2px(CIRCLE_MARGIN / 3);
        }

        return x;
    }

    /**
     * 根据角度获取真实的Y坐标
     *
     * @param angle
     * @return
     */
    private double getRealY(int angle) {
        double y = getYFromAngle(angle);
        if (y > 0) {
            y = getHeight() / 2 + y + dp2px(2*CIRCLE_MARGIN /3);
        } else {
            y = getHeight() / 2 + y - dp2px(CIRCLE_MARGIN /3);
        }
        return y;
    }

    /**
     * 获取最小温度的角度
     *
     * @return
     */
    private int getStartAngle() {
        int offset = COVER_ANGLE / (mMaxTemp - mMimTemp) * mMimTemp;
        if (offset > 90) {
            offset = 90;
        } else if (offset < -90) {
            offset = -90;
        }
        return 230 + offset;
    }

    /**
     * 绘制最小温度和最大温度
     *
     * @param canvas
     */
    private void drawTempText(Canvas canvas) {

        mPaint.setTextSize(sp2px(TEXT_SIZE_COUVER));

        int firstAngle = getStartAngle();
        canvas.drawText(mMimTemp + "°", (float) getRealX(firstAngle)
                , (float) getRealY(firstAngle), mPaint);

        firstAngle += 90;
        canvas.drawText(mMaxTemp + "°", (float) getRealX(firstAngle)
                , (float) getRealY(firstAngle), mPaint);

    }

    /**
     * 根据温度返回颜色值
     *
     * @return
     */
    public int getRealColor() {
        if (mMaxTemp <= 0) {
            return Color.parseColor("#00008B");//深海蓝
        } else if (mMimTemp <= 0 && mMaxTemp > 0) {
            return Color.parseColor("#4169E1");//黄君兰
        } else if (mMimTemp > 0 && mMaxTemp < 15) {
            return Color.parseColor("#40E0D0");//宝石绿
        } else if (mMimTemp >= 15 && mMaxTemp < 25) {
            return Color.parseColor("#00FF00");//酸橙绿
        } else if (mMimTemp >= 25 && mMaxTemp < 30) {
            return Color.parseColor("#FFD700");//金色
        } else if (mMimTemp >= 30) {
            return Color.parseColor("#CD5C5C");//印度红
        }

        return Color.parseColor("#00FF00");//酸橙绿;
    }

    /**
     * 获取当天温度范围起始的标线索引
     *
     * @return
     */
    private int getStartLineIndex() {
        return (getStartAngle() - START_ANGLE) / (SWEEP_ANGLE / COUNT);
    }

    /**
     * 获取当天温度范围终止的标线索引
     *
     * @return
     */
    private int getEndLineIndex() {
        return (getStartAngle() + COVER_ANGLE - START_ANGLE) / (SWEEP_ANGLE / COUNT);
    }

//    private int drawCurTemp(int curTemp) {
//
//    }

    /**
     * density = 密度/160
     *
     * @param numDp
     * @return
     */
    private float dp2px(int numDp) {
        return numDp * getResources().getDisplayMetrics().density;
    }

    private float sp2px(int numSp) {
        return numSp * getResources().getDisplayMetrics().scaledDensity;
    }
}
