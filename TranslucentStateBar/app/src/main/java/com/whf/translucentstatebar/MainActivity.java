package com.whf.translucentstatebar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        translucent();
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    public void translucent(){
        //4.4-5.0是全透明，5.0及以上是半透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //API21,Android 5.0状态栏全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色
            window.setStatusBarColor(Color.TRANSPARENT);
        }


        //设置Actionbar为透明色
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(android.R.color.transparent)));
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,TwoActivity.class));
    }
}
