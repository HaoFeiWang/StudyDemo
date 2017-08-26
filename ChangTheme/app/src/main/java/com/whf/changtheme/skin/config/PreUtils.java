package com.whf.changtheme.skin.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 换肤状态保存
 * Created by WHF on 2017/3/3.
 */

public class PreUtils {
    private Context mContext;

    public PreUtils(Context context){
        this.mContext = context;
    }

    /**
     * 保存换肤插件包的路径
     */
    public void savePluginPath(String pluginPath){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constants.SKIN_PATH_KEY_NAME,pluginPath).apply();
    }

    /**
     * 保存换肤插件包的包名
     */
    public void savePluginPkg(String pluginPkgName){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constants.SKIN_PKG_KEY_NAME,pluginPkgName).apply();
    }

    /**
     * 获取换肤插件包的路径
     */
    public String getPluginPath(){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constants.SKIN_PATH_KEY_NAME,"");
    }

    /**
     * 获取换肤插件包的路径
     */
    public String getPluginPkgName(){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constants.SKIN_PKG_KEY_NAME,"");
    }

    /**
     * 清空保存换肤数据的SharedPreferences的所有信息
     */
    public void clear() {
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    /**
     * 保存应用内换肤的资源的后缀名
     */
    public void saveSuffix(String suffix){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(Constants.SUFFIX_KEY_NAME,suffix).apply();
    }

    /**
     * 获取应用内换肤的资源的后缀名
     */
    public String getSuffix(){
        SharedPreferences sp = mContext
                .getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString(Constants.SUFFIX_KEY_NAME,"");
    }
}
