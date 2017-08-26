package com.whf.changtheme.skin.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by WHF on 2017/3/1.
 */

public class Constants {
    /**
     * 插件包的存放路径
     */
    public static final String PLUGIN_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "Skin_Plugin.apk";
    /**
     * 插件包的包名
     */
    public static final String PLUGIN_PACKAGE_NAME = "com.whf.skin_plugin";

    /**
     * 需要换肤的资源前缀
     */
    public static final String SKIN_PREFIX="skin_";

    /**
     * 保存换肤相关数据的SharedPreference的名字
     */
    public static final String PREF_NAME = "skin";

    /**
     * 换肤插件包路径在SharedPreference的key值
     */
    public static final String SKIN_PATH_KEY_NAME = "plugin_path";

    /**
     * 换肤插件包包名在SharedPreference的key值
     */
    public static final String SKIN_PKG_KEY_NAME = "plugin_package_name";

    /**
     * 保存应用内换肤资源的后缀名
     */
    public static final String SUFFIX_KEY_NAME = "suffix";
}
