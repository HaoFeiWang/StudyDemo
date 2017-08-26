package com.whf.keeplive;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.whf.keeplive.foregroundService.ForegroundService;
import com.whf.keeplive.lockScreen.LockScreenActivity;

import java.lang.ref.WeakReference;

/**
 * Created by WHF on 2017/3/6.
 */

public class KeepLiveManager {

    private static final int FOREGROUND_ID = 0x1;
    private static KeepLiveManager sKeepLiveManager;
    public WeakReference<LockScreenActivity> mWeakActivity = null;
    private KeepLiveManager(){

    }

    public static KeepLiveManager getInstance(){
        if(sKeepLiveManager == null){
            synchronized (KeepLiveManager.class){
                if(sKeepLiveManager == null){
                    sKeepLiveManager = new KeepLiveManager();
                }
            }
        }
        return sKeepLiveManager;
    }

    public void startLockScreenActivity(Context context){
        Log.e(":::::::::","启动Activity");
        Intent intent = new Intent(context,LockScreenActivity.class);
        //在Activity之外startActivity（）需要传入FLAG_ACTIVITY_NEW_TASK
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    public void finishLockScreenActivity(){
        if(mWeakActivity == null){
            return;
        }
        LockScreenActivity activity = mWeakActivity.get();
        if(activity!=null){
            activity.finish();
            mWeakActivity.clear();
        }
        mWeakActivity = null;
    }

    public void startKeepServiceLive(Context context){
        context.startService(new Intent(context, ForegroundService.class));
        context.startService( new Intent(context,ForegroundService.InnerService.class));
    }

    /**
     * 提升Service的优先级为前台Service，且不被用户感知
     */
    public void setForeground(final Service service,final Service innerService){
        if(service!=null && innerService!=null){
            Log.e("::::::::::","前台");
            service.startForeground(FOREGROUND_ID,new Notification());

            innerService.startForeground(FOREGROUND_ID,new Notification());
            //可以使通知栏的显示消失，并且Service的优先级处于1，仅次于与用户交互的Activity的优先级
            innerService.stopSelf();
        }
    }



}
