package com.whf.gaussianblue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.support.v8.renderscript.Type;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private Button mButton;
    private RenderScript rs;
    private int blurRadius = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image);
        mButton = (Button) findViewById(R.id.button);

        rs = RenderScript.create(this);

        mButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryRenderScript();
    }

    //这段代码的效果就是每点击一次按钮，高斯模糊半径blurRadius就+1，
    //然后在RxJava的Schedulers.computation()线程中进行Bitmap的高斯模糊化，
    //接着在onNext()中将处理后获得的图片设置显示。
    //也就是上图的效果
    @Override
    public void onClick(View v) {
        if (blurRadius <= 25) {
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                    Log.e("tag", "当前blurRadius = " + blurRadius);
                    //对图片进行高斯模糊处理
                    Bitmap bitmap = blur(blurRadius);
                    blurRadius++;
                    if (blurRadius == 25) {
                        blurRadius = 1;
                    }
                    e.onNext(bitmap);
                    e.onComplete();
                }
            })
                    .subscribeOn(Schedulers.computation()) //指定运算线程
                    .observeOn(AndroidSchedulers.mainThread()) //切换回主线程
                    .subscribeWith(new DisposableObserver<Bitmap>() {
                        @Override
                        public void onNext(Bitmap bitmap) {
                            mButton.setText("高斯模糊"+(blurRadius-1)+"++");
                            mImageView.setImageBitmap(bitmap); //展示图片
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }

    /**
     * 将图片高斯模糊化
     *
     * @param radius         模糊半径，由于性能限制，这个值的取值区间为(0,25f]
     */
    public Bitmap blur(float radius) {
        //Bitmap bmp = Bitmap.createBitmap(bitmapOriginal);//只是创建了一个指向原Bitmap的索引
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(),R.drawable.image);
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        final Allocation input = Allocation.createFromBitmap(rs, bmp);
        //Type: “一个Type描述了一个Allocation或者并行操作的Element和dimensions ”
        Type type = input.getType();
        final Allocation output = Allocation.createTyped(rs, type);
        //创建一个模糊效果的RenderScript的工具对象
        //第二个参数Element相当于一种像素处理的算法，高斯模糊的话用这个就好
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //设置渲染的模糊程度, 25f是最大模糊度
        script.setRadius(radius);
        // 设置blurScript对象的输入内存
        script.setInput(input);
        // 将输出数据保存到输出刚刚创建的输出内存中
        script.forEach(output);
        // 将数据填充到bitmap中
        output.copyTo(bmp);

        //销毁它们释放内存
        input.destroy();
        output.destroy();
        script.destroy();
        type.destroy();
        return bmp;
    }

    public void destoryRenderScript() {
        this.rs.destroy();
    }
}
