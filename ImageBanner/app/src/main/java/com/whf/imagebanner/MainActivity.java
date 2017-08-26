package com.whf.imagebanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mStartTimer;
    private Button mStopTimer;
    private ImageBanner imageBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageBanner();
        mStartTimer = (Button)findViewById(R.id.button_start);
        mStopTimer = (Button) findViewById(R.id.button_stop);
        mStopTimer.setOnClickListener(this);
        mStartTimer.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void initImageBanner(){
        imageBanner= (ImageBanner) findViewById(R.id.imageBanner);
        imageBanner.addImage(getImageView(R.drawable.res1));
        imageBanner.addImage(getImageView(R.drawable.res2));
        imageBanner.addImage(getImageView(R.drawable.res3));
        imageBanner.addImage(getImageView(R.drawable.res4));
    }
    private ImageView getImageView(int resId){
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                imageBanner.startTimer();
                break;
            case R.id.button_stop:
                imageBanner.stopTimer();
                break;
        }
    }
}
