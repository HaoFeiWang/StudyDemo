package com.whf.keeplive;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Process;
import android.util.Log;

import com.whf.keeplive.lockScreen.LockScreenReceiver;

/**
 * Created by WHF on 2017/3/6.
 */

public class LiveApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        //屏幕关闭和打开的广播必须动态注册，否则不生效
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(new LockScreenReceiver(),filter);


        Log.e(":::::::", Process.myPid()+"");
    }

}
