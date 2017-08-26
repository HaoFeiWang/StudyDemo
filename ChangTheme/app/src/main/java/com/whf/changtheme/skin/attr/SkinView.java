package com.whf.changtheme.skin.attr;

import android.view.View;

import java.util.List;

/**
 *
 * Created by WHF on 2017/3/1.
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mAttrs;

    public SkinView(View mView, List<SkinAttr> mAttrs) {
        this.mView = mView;
        this.mAttrs = mAttrs;
    }

    public void apply(){
        for(SkinAttr attr : mAttrs){
            attr.apply(mView);
        }
    }

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    public List<SkinAttr> getAttrs() {
        return mAttrs;
    }

    public void setAttrs(List<SkinAttr> mAttrs) {
        this.mAttrs = mAttrs;
    }
}
