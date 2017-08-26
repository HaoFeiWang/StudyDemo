package com.whf.decorationitem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by WHF on 2017/3/7.
 */

public class LabelDecoration extends RecyclerView.ItemDecoration{

    public LabelDecoration() {
        super();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        Paint leftPaint = new Paint();
        leftPaint.setColor(Color.GREEN);
        Paint rightPaint = new Paint();
        rightPaint.setColor(Color.RED);

        for(int i= 0;i<childCount;i++){
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            if(position%2==0){
                int right = view.getPaddingLeft();
                int top = view.getTop();
                int bottom = view.getBottom();
                c.drawRect(0,top,right,bottom,leftPaint);
            }else{
                int left = view.getWidth() - view.getPaddingLeft();
                int right = view.getWidth();
                int top = view.getTop();
                int bottom = view.getBottom();
                c.drawRect(left,top,right,bottom,rightPaint);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }
}
