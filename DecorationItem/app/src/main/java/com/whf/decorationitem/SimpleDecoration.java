package com.whf.decorationitem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WHF on 2017/3/6.
 */

public class SimpleDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    public SimpleDecoration(Context context) {
        mContext = context;
    }

    /**
     * 在RecyclerView绘制后执行，用于绘制前景色
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

    /**
     * 在RecyclerView绘制之前执行，用于绘制前背景色
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);

        Paint dividerPaint = new Paint();
        dividerPaint.setColor(mContext.getResources().getColor(android.R.color.holo_orange_light));
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();//左边的坐标
        int right = parent.getWidth() - parent.getPaddingRight();//右边的坐标

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();//上边起点
            float bottom = view.getBottom() + 5;//下边起点
            c.drawRect(left, top, right, bottom, dividerPaint);//绘制矩形
        }
        //c.drawColor(mContext.getResources().getColor(android.R.color.holo_orange_light));
    }

    /**
     * 绘制Item的边距
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0,0,0,5);
    }
}
