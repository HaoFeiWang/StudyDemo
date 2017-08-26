package com.whf.imageselector.Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * ImageLoader类
 * 功能：图片同步加载、图片异步加载、图片压缩、内存缓存
 * 实现：1.ImageLoader使用单例模式
 *      2.使用线程池ExcutorService执行加载图片的任务，默认为一个线程
 *      3.使用LinkedList<Runnable>作为任务列表，其中队列的调度方式可分为两种：LIFO、FIFO
 *      4.创建后台轮询线程，查看队列是否含有任务
 *      5.创建后台轮询线程的Handler以及UI线程的Handler
 *      6.创建LruCache管理图片缓存
 * Created by WHF on 2016/6/3.
 */
public class ImageLoader {

    private static ImageLoader imageLoader;
    /**
     * 管理图片的内存缓存
     */
    private LruCache<String,Bitmap> mLruCache;
    /**
     * 执行图片加载任务的线程池
     */
    private ExecutorService mThreadPool;
    /**
     * 线程池的默认线程数
     */
    private static final int DEFAULT_THREAD_COUNT=1;
    /**
     * 任务队列的调度方式的枚举类
     */
    public enum Type{
        LIFO,FIFO;
    }
    /**
     * 任务队列的调度方式
     */
    private Type mType;
    /**
     * 任务队列，用来存放加载图片的任务
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 任务队列的后台轮询线程,用于取出任务队列的任务放入线程池执行
     */
    private Thread mPoolThread;
    /**
     * 后台轮询线程的Handler
     */
    private Handler mPoolThreadHandler;
    /**
     * 后台轮询线程Handler发送消息的标记
     */
    private static final int POOLTHREAD_HANDLER_WHAT=0x1;
    /**
     * UI线程的Handler
     */
    private Handler mUIHandler;
    /**
     * 轮询线程Handler:mPoolThreadHandler的信号量，用于该变量的并行处理
     */
    private Semaphore mPoolThreadHandlerSemaphore=new Semaphore(0);
    /**
     * 线程池mThreadPool的信号量
     */
    private Semaphore mThreadPoolSemphore;

    private ImageLoader(int threadCount,Type type){
        init(threadCount,type);
    }

    /**
     * 单例工厂方法
     * @return
     */
    public static ImageLoader getInstance(int threadCount,Type type){
        if(imageLoader==null){
            synchronized (ImageLoader.class){
                if(imageLoader==null){
                    imageLoader=new ImageLoader(threadCount,type);
                }
            }
        }
        return imageLoader;
    }

    /**
     * 初始化操作
     * @param threadCount   线程池的线程数
     * @param type  任务队列的调度方式
     */
    private void init(int threadCount, Type type) {
        initPoolThread();
        initLruCache();
        //创建线程池
        mThreadPool= Executors.newFixedThreadPool(threadCount);
        mThreadPoolSemphore=new Semaphore(threadCount);
        //初始化任务队列
        mTaskQueue=new LinkedList<Runnable>();
        //初始化任务队列的调度方式
        mType=Type.LIFO;
    }

    /**
     * 初始化LruCache
     */
    private void initLruCache() {
        int maxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheMemory=maxMemory/8;
        mLruCache=new LruCache<String,Bitmap>(cacheMemory){
            //获取图片的内存
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    /**
     * 初始化后台轮询线程
     */
    private void initPoolThread(){
        mPoolThread=new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //从线程池中取出一个任务并执行
                        mThreadPool.execute(getTask());
                        try {
                            //当线程数超过线程池指定线程个数，发生阻塞，只有前面的任务线程释放，才会执行下面
                            mThreadPoolSemphore.acquire();
                        } catch (InterruptedException e) {
                        }
                    }
                };
                //释放一个信号量为1
                mPoolThreadHandlerSemaphore.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
    }

    /**
     * 根据任务队列的调度方式取出一个任务
     * @return
     */
    private Runnable getTask(){
        if(mType==Type.LIFO){
            return mTaskQueue.removeLast();
        }else if(mType==Type.FIFO){
            return mTaskQueue.removeFirst();
        }
        return null;
    }

    /**
     * 根据指定的Path读取图片,并设置给指定的ImageView
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if(mUIHandler==null){
            mUIHandler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //给imageView 设置Bitmap
                    ImgBeanHolder imgBeanHolder= (ImgBeanHolder) msg.obj;
                    Bitmap holderBitmap=imgBeanHolder.bitmap;
                    ImageView holderImageView=imgBeanHolder.imageView;
                    String holderPath=imgBeanHolder.path;
                    //不甚理解？？？
                    if(holderImageView.getTag().toString().equals(holderPath)){
                        holderImageView.setImageBitmap(holderBitmap);
                    }
                }
            };
        }

        Bitmap bitmap= getBitmapFromLruCache(path);
        if(bitmap!=null){
            refreshBitmap(path, imageView, bitmap);
        }else{
            addTasks(new Runnable() {
                @Override
                public void run() {
                    //图片的压缩
                    ImageSize imageSize=getImageViewSize(imageView);
                    Bitmap bm=decodeSampleBitmapFromPath(path,imageSize.width,imageSize.height);
                    addBitmapToLruCache(path,bm);
                    refreshBitmap(path,imageView,bm);
                    //执行完成一个线程任务，释放信号量
                    mThreadPoolSemphore.release();
                }
            });
        }
    }

    /**
     * 向UIHandler发送path,imageView,bitmap
     * @param path
     * @param imageView
     * @param bitmap
     */
    private void refreshBitmap(String path, ImageView imageView, Bitmap bitmap) {
        Message message=mUIHandler.obtainMessage();

        ImgBeanHolder imgBeanHolder=new ImgBeanHolder();
        imgBeanHolder.bitmap=bitmap;
        imgBeanHolder.imageView=imageView;
        imgBeanHolder.path=path;

        message.obj=imgBeanHolder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入LruCache
     * @param path
     * @param bitmap
     */
    private void addBitmapToLruCache(String path,Bitmap bitmap) {
        if(getBitmapFromLruCache(path)==null){
            if(bitmap!=null){
                mLruCache.put(path,bitmap);
            }
        }
    }

    /**
     * 根据图片路径和ImageView的显示大小压缩图片
     * @param path
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeSampleBitmapFromPath(String path,int width,int height){
        //预测量，不加载至内存
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize=caculateInSampleSize(options,width,height);
        //压缩加载至内存
        options.inJustDecodeBounds=false;
        Bitmap bitmap=BitmapFactory.decodeFile(path,options);
        return bitmap;
    }

    /**
     * 根据ImageView的显示大小以及Bitmap的原始大小获取inSampleSize
     * @param options
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width=options.outWidth;
        int height=options.outHeight;
        int sampleSize=1;
        if(width>=reqWidth||height>=reqHeight){
            int widthRadio=Math.round(width*1.0f/reqWidth);
            int heightRaido=Math.round(height*1.0f/reqHeight);
            sampleSize=Math.max(widthRadio,heightRaido);
        }
        return sampleSize;
    }

    /**
     * 获取ImageView适当的显示大小
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        DisplayMetrics metrics=imageView.getContext().getResources().getDisplayMetrics();
        ImageSize imageSize=new ImageSize();
        ViewGroup.LayoutParams layoutParams=imageView.getLayoutParams();

        int width=imageView.getWidth();
        if(width<=0){
            width=layoutParams.width;
        }
        if(width<=0){
            width=getFieldValue(imageView,"mMaxWidth");
        }
        if(width<=0){
            width=metrics.widthPixels;
        }

        int height=imageView.getHeight();
        if(height<=0){
            height=layoutParams.height;
        }
        if(height<=0){
            height=getFieldValue(imageView,"mMaxHeight");
        }
        if(height<=0){
            height=metrics.heightPixels;
        }
        imageSize.width=width;
        imageSize.height=height;
        return imageSize;
    }

    /**
     * 通过反射获取ImageView的指定属性值
     * @param imageView
     * @param fieldName
     * @return
     */
    private static int getFieldValue(ImageView imageView,String fieldName){
        int value=0;
        try {
            Class clazz=ImageView.class;
            Field field=clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue=field.getInt(imageView);
            if(fieldValue>0&&fieldValue<Integer.MAX_VALUE){
                value=fieldValue;
            }
        } catch (Exception e) {
        }
        return value;
    }

    /**
     * 包装图片的大小的类
     */
    private class ImageSize{
        int width;
        int height;
    }

    /**
     * 根据key从LruCache中查找图片
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    /**
     * 用来将Bitmap、ImageView、path包装成一个对象，通过Handler发送
     */
    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 向任务队列添加任务
     * @param runnable
     */
    private synchronized void addTasks(Runnable runnable){
        mTaskQueue.add(runnable);
        if(mPoolThreadHandler==null){
            try {
                //当信号量为零时发生阻塞（当其未初始化完成会在此处阻塞）
                mPoolThreadHandlerSemaphore.acquire();
            } catch (InterruptedException e) {
            }
        }
        mPoolThreadHandler.sendEmptyMessage(POOLTHREAD_HANDLER_WHAT);
    }


}
