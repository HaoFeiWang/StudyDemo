package com.whf.imagebanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义图片轮播卡的View
 * Created by WHF on 2016/11/14.
 */

public class ImageBanner extends RelativeLayout {

    //两个child
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;

    private ArrayList<ImageView> mImageViewList;
    private ArrayList<Dot> mDotList;
    private MyPagerAdapter mMyPagerAdapter;
    private static final int DEFAULT_DOT_COLOR = Color.GRAY;
    private static final int SELECT_DOT_COLOR = Color.RED;
    private boolean isFirst=true;
    private Context mContext;
    private Timer mTimer;
    private MyTimerTask mTimerTask;
    private Handler mHandler;

    public ImageBanner(Context context) {
        this(context,null,0);
    }

    public ImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ImageBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;

        mImageViewList=new ArrayList<ImageView>();
        mDotList = new ArrayList<Dot>();
        mMyPagerAdapter=new MyPagerAdapter();

        initViewPager();
        initLinearLayout();

    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager(){
        mViewPager=new ViewPager(mContext);
        mViewPager.setAdapter(mMyPagerAdapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        setDefaultDuration();
        this.addView(mViewPager);
    }

    /**
     * 初始化LinearLayout
     */
    private void initLinearLayout(){
        mLinearLayout=new LinearLayout(mContext);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,20,20,20);
        layoutParams.addRule(CENTER_HORIZONTAL);//让将要添加的子元素水平居中
        layoutParams.addRule(ALIGN_PARENT_BOTTOM);

        this.addView(mLinearLayout,layoutParams);
    }

    /**
     * 开始自动轮询
     */
    public void startTimer(){
        Log.e("start","start");
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1,true);
                }
            }
        };
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
        mTimer.schedule(mTimerTask,2500,2500);
    }
    /**
     * 关闭自动轮询
     */
    public void stopTimer(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    /**
     * 添加图片
     * @param imageView
     */
    public void addImage(ImageView imageView){
        this.mImageViewList.add(imageView);
        Dot dot = new Dot(mContext);
        mDotList.add(dot);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,0,5,0);
        if(isFirst){
            dot.setDotColor(SELECT_DOT_COLOR);
            isFirst=false;
        }
        mLinearLayout.addView(dot,layoutParams);
        mMyPagerAdapter.notifyDataSetChanged();
    }

    /**
     * 调整自动轮播时的动画时间
     */
    private void setDefaultDuration(){
        try {
            Field field=ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            Scroller scroller=new Scroller(mContext,new LinearInterpolator()){
                @Override
                public void startScroll(int startX, int startY, int dx, int dy) {
                    super.startScroll(startX, startY, dx, dy,700);
                }

                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    duration=700;
                    super.startScroll(startX, startY, dx, dy, duration);
                }
            };
            field.set(mViewPager,scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * ViewPager的Adapter
     */
    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            position = position % mImageViewList.size();
            container.removeView(mImageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mImageViewList.size();
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }
    }

    /**
     * ViewPager的监听器
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ImageView preImageView = mImageViewList.get(position%mImageViewList.size());
            ImageView nextImageView = mImageViewList.get((position+1)%mImageViewList.size());
            preImageView.setScaleX(1-positionOffset);
            preImageView.setScaleY(1-positionOffset);
            preImageView.setAlpha(1-positionOffset);
            nextImageView.setScaleX(positionOffset);
            nextImageView.setScaleY(positionOffset);
            nextImageView.setAlpha(positionOffset);
        }

        @Override
        public void onPageSelected(int position) {

            position = position % mImageViewList.size();
            for (Dot dot:mDotList){
                dot.setDotColor(DEFAULT_DOT_COLOR);
            }
            Dot dot = (Dot) mLinearLayout.getChildAt(position);
            dot.setDotColor(SELECT_DOT_COLOR);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    }

    /**
     * 定义Dot类，可以表示当前viewpager处于第几个页面的小圆点
     */
    private class Dot extends View {
        private Paint dotPaint;
        private final int DEFAULT_DOT_SIZE = 30;
        public Dot(Context context) {
            super(context);
            dotPaint = new Paint();
            dotPaint.setColor(DEFAULT_DOT_COLOR);
            dotPaint.setAntiAlias(true);//抗锯齿
            dotPaint.setDither(true);//抖动，使高质量图片在低质量的屏幕上也能较好地显示
            dotPaint.setStyle(Paint.Style.FILL);//完全填充
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, dotPaint);
        }
        //这一步的作用是把dot的宽度和高度定死
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_DOT_SIZE, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_DOT_SIZE, MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
        //修改dot的颜色，并且让整个dot重绘
        public void setDotColor(int color) {
            dotPaint.setColor(color);
            invalidate();
        }
    }
}

