package com.whf.sounddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WHF on 2017/3/7.
 */

public class Decoration extends RecyclerView.ItemDecoration {
    private int mLineWidth = 5;
    private Context mContext;

    public Decoration(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        Paint paint = new Paint();
        View view;
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            view = parent.getChildAt(i);
            int bottom = view.getBottom();
            //int paddingLeft = view.getPaddingLeft();
            int right = view.getWidth();
            int paddingRight = view.getPaddingRight();
            //int top = view.getTop();

            //绘制底边
            c.drawRect(0,bottom,right+paddingRight,bottom + mLineWidth, paint);
//            //绘制左边
//            c.drawRect(0,top,paddingLeft,bottom,paint);
//            //绘制右边
//            c.drawRect(right,top,right + paddingRight,bottom,paint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mLineWidth,0,mLineWidth,mLineWidth);
    }
}
