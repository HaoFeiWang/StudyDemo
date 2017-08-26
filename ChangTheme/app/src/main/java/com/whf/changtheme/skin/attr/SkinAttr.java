package com.whf.changtheme.skin.attr;

import android.view.View;

/**
 * Created by WHF on 2017/3/1.
 */

public class SkinAttr {

    private String mResName;
    private SkinAttrType mType;

    public SkinAttr(String mResName, SkinAttrType mType) {
        this.mResName = mResName;
        this.mType = mType;
    }

    public void apply(View view) {
        mType.apply(view,mResName);
    }

    public String getResName() {
        return mResName;
    }

    public void setResName(String mResName) {
        this.mResName = mResName;
    }

    public SkinAttrType getType() {
        return mType;
    }

    public void setType(SkinAttrType mType) {
        this.mType = mType;
    }
}
