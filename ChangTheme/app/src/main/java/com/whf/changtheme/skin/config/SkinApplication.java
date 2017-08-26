package com.whf.changtheme.skin.config;

import android.app.Application;

/**
 * Created by WHF on 2017/3/3.
 */

public class SkinApplication extends Application {
    /**
     * 每次应用启动时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstances().init(this);
    }
}
