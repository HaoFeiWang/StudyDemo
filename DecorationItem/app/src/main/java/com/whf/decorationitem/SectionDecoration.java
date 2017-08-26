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

public class SectionDecoration extends RecyclerView.ItemDecoration{

    private DecorationCallback mCallback;


    public SectionDecoration(DecorationCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();

        for(int i = 0;i<count;i++){
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);

            Paint titlePaint = new Paint();
            titlePaint.setTypeface(Typeface.DEFAULT_BOLD);//设置加粗
            titlePaint.setAntiAlias(true);//抗锯齿
            titlePaint.setTextSize(80);
            titlePaint.setColor(Color.BLACK);
            //titlePaint.getFontMetrics(new Paint.FontMetrics());
            titlePaint.setTextAlign(Paint.Align.LEFT);//文字对齐方式
            Paint linePaint = new Paint();
            linePaint.setColor(Color.YELLOW);

            if(isFirstInGroup(position)){
                int left = 0;
                int right = view.getWidth();
                int top = view.getTop() - 60;
                int bottom = view.getTop() ;

                c.drawRect(left,top,right,bottom,linePaint);
                String str = mCallback.getGroupFirstLine(position);
                c.drawText(str,left,bottom,titlePaint);
            }else{
                int left = 0;
                int right = view.getWidth();
                int top = view.getTop()-5;
                int bottom = view.getTop();
                c.drawRect(left,top,right,bottom,linePaint);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);//获取该View在Adapter中的位置
        if(isFirstInGroup(position)){
            outRect.set(0,60,0,0);
        }else{
            outRect.set(0,5,0,0);
        }
    }

    private boolean isFirstInGroup(int position) {
        if(position == 0){
            return true;
        }else{
            long preGroupId = mCallback.getGroupId(position - 1);//上一组的Id
            long curGropId = mCallback.getGroupId(position);//当前组的ID
            return preGroupId != curGropId;
        }
    }
}
