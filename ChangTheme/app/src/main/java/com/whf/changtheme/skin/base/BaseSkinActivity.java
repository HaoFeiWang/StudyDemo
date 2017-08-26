package com.whf.changtheme.skin.base;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.whf.changtheme.skin.attr.SkinAttr;
import com.whf.changtheme.skin.attr.SkinAttrSupport;
import com.whf.changtheme.skin.attr.SkinView;
import com.whf.changtheme.skin.callback.ISkinChangedListener;
import com.whf.changtheme.skin.config.SkinManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseSkinActivity extends AppCompatActivity implements ISkinChangedListener{

    private final Object[] mConstructorArgs = new Object[2];
    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();
    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private Method mCreateViewMethod = null;
    private static final Class<?>[] sCreateViewSignature = new Class[]{
            View.class, String.class, Context.class, AttributeSet.class
    };
    private final Object[] mCreateViewArgs = new Object[4];



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        SkinManager.getInstances().registerListener(this);
        //Factory是创建View时需要使用的类（在LayoutInflater的creatViewFromTag()中可以查看）
        LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
            /**
             * 完成View的构造
             *
             * @param parent
             * @param name
             * @param context
             * @param attrs
             * @return
             */
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                View view = null;
                List<SkinAttr> skinAttrs = null;

                //完成AppCompatViewInflater的onCreateView()方法
                //返回AppCompatDelegateImplV9的子类
                AppCompatDelegate delegate = getDelegate();
                try {
                    if (mCreateViewMethod == null) {
                        mCreateViewMethod = delegate.getClass().getMethod("onCreateView"
                                , sCreateViewSignature);
                    }
                    mCreateViewArgs[0] = parent;
                    mCreateViewArgs[1] = name;
                    mCreateViewArgs[2] = context;
                    mCreateViewArgs[3] = attrs;
                    view = (View) mCreateViewMethod.invoke(delegate,mCreateViewArgs);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                skinAttrs = SkinAttrSupport.getSkinAttrs(attrs,context);
                //当前View没有需要换肤的属性
                if(skinAttrs.isEmpty()){
                    return null;
                }

                if (view == null) {
                    view = createViewFromTag(context, name, attrs);
                }
                if (view != null){
                    injectSkin(view,skinAttrs);
                }
                return view;
            }
        });
        super.onCreate(savedInstanceState);
    }

    /**
     * AppCompatViewInflate的createViewFromTag()方法
     *
     * @param context
     * @param name
     * @param attrs
     * @return
     */

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createView(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    /**
     * 将需要换肤的控件加入到List<SkinView>中
     * @param view
     * @param attrs
     */
    private void injectSkin(View view, List<SkinAttr> attrs) {
        List<SkinView> skinViews = SkinManager.getInstances().getSkinViews(this);
        if(skinViews == null){
            skinViews = new ArrayList<SkinView>();
            SkinManager.getInstances().addSkinViews(this,skinViews);
        }
        skinViews.add(new SkinView(view,attrs));

        if (SkinManager.getInstances().needChangeSkin()){
            SkinManager.getInstances().skinChanged(this);
        }
    }

    @Override
    public void onSkinChanged() {

    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstances().unRegisterListener(this);
        super.onDestroy();
    }
}

