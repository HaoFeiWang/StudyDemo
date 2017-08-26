package com.whf.keeplive.lockScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.whf.keeplive.KeepLiveManager;
import com.whf.keeplive.R;

import java.lang.ref.WeakReference;

/**
 * 在屏幕关闭时启动该Activity,在屏幕亮时销毁该Activity
 * 目的：将进程的优先级从4调到1，防止被系统管理工具销毁（为了省电，在屏幕关闭5分钟内杀死所有后台进程）
 * 缺陷：尚未实现多进程
 */
public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        Window window = getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        WindowManager.LayoutParams params =  window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);

        KeepLiveManager.getInstance().mWeakActivity = new WeakReference<>(this);

        Log.e(":::::::::","我活了");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("::::::::::","我死了");
    }
}
