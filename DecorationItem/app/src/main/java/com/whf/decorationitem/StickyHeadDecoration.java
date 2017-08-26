package com.whf.decorationitem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WHF on 2017/3/7.
 */

public class StickyHeadDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback mCallback;
    private Paint titlePaint;
    private Paint linePaint;

    public StickyHeadDecoration(DecorationCallback callback) {
        mCallback = callback;
        titlePaint = new Paint();
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);//设置加粗
        titlePaint.setAntiAlias(true);//抗锯齿
        titlePaint.setTextSize(80);
        titlePaint.setColor(Color.BLACK);
        //titlePaint.getFontMetrics(new Paint.FontMetrics());
        titlePaint.setTextAlign(Paint.Align.LEFT);//文字对齐方式
        linePaint= new Paint();
        linePaint.setColor(Color.YELLOW);
    }

    /**
     * 每次滑动都会调用（可能是，至少在滑出新的Item时是会调用，onDraw()同理）
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int count = parent.getChildCount();//当前页面的条目数
        int itemCount = state.getItemCount();//所有数据的个数
        long preGroupId,curGroupId = -1;

        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            int left = 0;
            int right = view.getWidth();
            int bottom = view.getBottom();

            preGroupId = curGroupId;
            curGroupId = mCallback.getGroupId(position);
            //使得除过每一组的顶部和当前页面的顶部其他地方不绘制
            if (preGroupId==curGroupId){
                continue;
            }
            //使标题吸顶
            float textY = Math.max(60,view.getTop());
            //使两个紧挨的标题能够向上推，而不是覆盖
            if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                long nextGroupId = mCallback.getGroupId(position + 1);
                if (nextGroupId != curGroupId && bottom < textY ) {//组内最后一个view进入了header
                    textY = bottom;
                }
            }

            c.drawRect(left, textY - 60, right, textY, linePaint);
            String str = mCallback.getGroupFirstLine(position);
            c.drawText(str, left, textY, titlePaint);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            if (!isFirstInGroup(position)) {
                int left = 0;
                int right = view.getWidth();
                int top = view.getTop() - 5;
                int bottom = view.getTop();
                c.drawRect(left, top, right, bottom, linePaint);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);//获取该View在Adapter中的位置
        if (isFirstInGroup(position)) {
            outRect.set(0, 60, 0, 0);
        } else {
            outRect.set(0, 5, 0, 0);
        }
    }

    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        } else {
            long preGroupId = mCallback.getGroupId(position - 1);//上一组的Id
            long curGropId = mCallback.getGroupId(position);//当前组的ID
            return preGroupId != curGropId;
        }
    }
}
