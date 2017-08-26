package com.whf.changtheme.skin.config;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;


/**
 * 获取插件包中的资源
 * Created by WHF on 2017/3/1.
 */

public class ResourceManager {

    private Resources mResources;
    private String mPkgName;

    private String mSuffix;

    public ResourceManager(Resources rs,String pkgName,String suffix){

        this.mResources = rs;
        this.mPkgName = pkgName;
        this.mSuffix = suffix;

        if(suffix==null){
            mSuffix = "";
        }
    }

    /**
     * 根据资源名获取Drawable
     * @param name
     * @return
     */
    public Drawable getDrawableByName(String name){

        name = appendSuffix(name);
        try {
            Drawable drawable = mResources.getDrawable(mResources
                    .getIdentifier(name,"drawable",mPkgName));
            return drawable;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据资源名获取Color
     * @param name
     * @return
     */
    public ColorStateList getColor(String name){
        name = appendSuffix(name);
        try {
            ColorStateList colorStateList = mResources.getColorStateList(mResources
                    .getIdentifier(name,"color",mPkgName));
            return colorStateList;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String appendSuffix(String name) {
        if(!TextUtils.isEmpty(mSuffix)){
            name += ("_"+mSuffix);
        }
        return name;
    }

}
