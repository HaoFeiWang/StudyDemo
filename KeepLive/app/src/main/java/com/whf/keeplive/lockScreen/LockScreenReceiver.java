package com.whf.keeplive.lockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.whf.keeplive.KeepLiveManager;

public class LockScreenReceiver extends BroadcastReceiver {
    public LockScreenReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(Intent.ACTION_SCREEN_OFF)){
            Log.e("::::::::","屏幕熄灭");
            KeepLiveManager.getInstance().startLockScreenActivity(context);
        }else if(action.equals(Intent.ACTION_SCREEN_ON)){
            Log.e("::::::::","屏幕点亮");
            KeepLiveManager.getInstance().finishLockScreenActivity();
        }
        KeepLiveManager.getInstance().startKeepServiceLive(context);
    }
}
