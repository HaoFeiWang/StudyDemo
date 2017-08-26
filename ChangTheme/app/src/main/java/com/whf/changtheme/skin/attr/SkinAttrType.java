package com.whf.changtheme.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whf.changtheme.skin.config.ResourceManager;
import com.whf.changtheme.skin.config.SkinManager;

/**
 * 枚举需要换肤的类型，每个类型都有相应的apply()方法实现最终的资源替换
 * Created by WHF on 2017/3/1.
 */

public enum  SkinAttrType {

    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable!=null){
                view.setBackgroundDrawable(drawable);
            }
        }
    },SRC("src") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if(view instanceof ImageView){
                ImageView imageView = (ImageView)view;
                if(drawable!=null){
                    imageView.setImageDrawable(drawable);
                }
            }
        }
    },TEXT_COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList colorStateList = getResourceManager().getColor(resName);
            if(view instanceof TextView){
                TextView textView = (TextView)view;
                if (colorStateList!=null){
                    textView.setTextColor(colorStateList);
                }
            }
        }
    };

    String resType;

    public String getResType() {
        return resType;
    }

    public void setResType(String resType) {
        this.resType = resType;
    }

    SkinAttrType(String type) {
        resType = type;
    }

    /**
     * 给指定View切换资源文件
     * @param view
     * @param mResName
     */
    public abstract void apply(View view,String mResName);

    public ResourceManager getResourceManager(){
        return SkinManager.getInstances().getResourceManager();
    }
}
