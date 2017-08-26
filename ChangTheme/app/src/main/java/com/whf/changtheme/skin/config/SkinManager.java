package com.whf.changtheme.skin.config;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.whf.changtheme.skin.attr.SkinView;
import com.whf.changtheme.skin.callback.ISkinChangedListener;
import com.whf.changtheme.skin.callback.ISkinChangingCallback;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * （单例）
 * Created by WHF on 2017/3/1.
 */

public class SkinManager {

    private static SkinManager mSkinManager;
    private Context mContext;
    private ResourceManager mResourceManager;

    //将SkinView与Activity进行数据绑定
    private Map<ISkinChangedListener,List<SkinView>> mSkinViewMaps =
            new HashMap<ISkinChangedListener,List<SkinView>>();
    //防止Map造成的内存泄露
    private List<ISkinChangedListener> mListeners = new ArrayList<ISkinChangedListener>();

    private PreUtils mPreUtils;
    private String mCurSkinPath;
    private String mCurPkgName;
    private String mSuffix;

    private SkinManager(){

    }

    public static SkinManager getInstances(){
        if(mSkinManager==null){
            synchronized (SkinManager.class){
                if(mSkinManager == null){
                    mSkinManager = new SkinManager();
                }
            }
        }
        return mSkinManager;
    }

    /**
     * 在Application中进行初始化
     * @param context
     */
    public void init(Context context){
        //尽量使用全局的Context类型
        this.mContext = context.getApplicationContext();

        mPreUtils = new PreUtils(context);

        try {
            String pluginPath = mPreUtils.getPluginPath();
            String pkgName = mPreUtils.getPluginPkgName();
            mSuffix = mPreUtils.getSuffix();

            File pluginFile = new File(pluginPath);
            if(pluginFile.exists()){
                loadPlugin(pluginPath,pkgName);
            }


        } catch (Exception e) {
            e.printStackTrace();
            mPreUtils.clear();
        }
    }

    public ResourceManager getResourceManager(){
        //使用应用内换肤
        if (!usePlugin()){
            mResourceManager = new ResourceManager(mContext.getResources(),mContext.getPackageName(),mSuffix);
        }
        return mResourceManager;
    }

    /**
     * 插件式换肤时，初始化ReasourceManager对象（插件包的资源管理）
     */
    private void loadPlugin(String pluginPath,String pkgName){
        try {

            //如果需要换肤的状态和当前状态一致，则不进行换肤
            if(pluginPath.equals(mCurSkinPath)&&pkgName.equals(mCurPkgName)){
                return;
            }
            AssetManager mAssetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath",String.class);
            addAssetPathMethod.invoke(mAssetManager, pluginPath);
            Resources superResource = mContext.getResources();
            Resources resources = new Resources(mAssetManager,superResource.getDisplayMetrics()
                    ,superResource.getConfiguration());
            mResourceManager = new ResourceManager(resources,pkgName,null);

            //插件式资源管理获取成功，则保存数据
            mCurSkinPath = pluginPath;
            mCurPkgName = pkgName;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前Activity页面所有需要换肤的SkinView<view,List<attrs>>
     * @param listener
     * @return
     */
    public List<SkinView> getSkinViews(ISkinChangedListener listener){
        return mSkinViewMaps.get(listener);
    }

    public void addSkinViews(ISkinChangedListener listener,List<SkinView> skinViews){
        mSkinViewMaps.put(listener,skinViews);
    }

    public void registerListener(ISkinChangedListener listener){
        mListeners.add(listener);
    }

    /**
     * 在Activity页面onDestory()时调用，防止内存泄露
     * @param listener
     */
    public void unRegisterListener(ISkinChangedListener listener){
        mListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }

    /**
     * Activity页面调用，执行插件式换肤
     * @param skinPath
     * @param skinPkgName
     * @param callback
     */
    public void changeSkin(final String skinPath,final String skinPkgName
            ,ISkinChangingCallback callback) {

        if(callback==null){
            callback = ISkinChangingCallback.DEFAULT_CALLBACK;
        }
        final ISkinChangingCallback finalCallback = callback;

        finalCallback.onStart();

        new AsyncTask<Void, Void, Integer>() {
            /**
             * 异步子线程执行（非UI线程）
             * @param params
             * @return
             */
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(skinPath,skinPkgName);
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
                return 0;
            }

            /**
             * 在doInBackground()执行完执行，UI线程执行
             * @param integer
             */
            @Override
            protected void onPostExecute(Integer integer) {
                if (integer == -1){
                    finalCallback.onError(null);
                    return;
                }
                try {
                    notifyChangedListener();
                    finalCallback.onComplete();

                    mPreUtils.clear();//主要用于清空应用内换肤的数据
                    updatePluginInfo(skinPath,skinPkgName);
                } catch (Exception e) {
                    e.printStackTrace();
                    finalCallback.onError(e);
                }
            }

        }.execute();
    }

    /**
     * 更新插件式换肤的信息
     */
    private void updatePluginInfo(String path,String pkg) {
        mPreUtils.savePluginPath(path);
        mPreUtils.savePluginPkg(pkg);
    }


    /**
     * 根据捕获的需要换肤的Activity，进行逐个换肤
     */
    private void notifyChangedListener() {
        for(ISkinChangedListener listener : mListeners){
            skinChanged(listener);
            listener.onSkinChanged();
        }
    }

    /**
     * 执行当前页面下所有SkinView的apply()方法
     * @param listener
     */
    public void skinChanged(ISkinChangedListener listener) {
        List<SkinView> skinViews = mSkinViewMaps.get(listener);
        for(SkinView skinView : skinViews){
            skinView.apply();
        }
    }


    /**
     * 是否需要自动换肤
     * @return
     */
    public boolean needChangeSkin() {
        return usePlugin() || useSuffix();
    }

    /**
     * 判断是否需要插件式换肤
     * @return 需要返回true
     */
    private boolean usePlugin() {
        return mCurSkinPath != null && !mCurSkinPath.trim().isEmpty();
    }

    /**
     * 判断是否需要应用内换肤
     * @return 需要返回true
     */
    private boolean useSuffix(){
        return mSuffix!=null && !mSuffix.trim().isEmpty();
    }

    /**
     * 供Activity调用的应用内换肤(如果进行应用内换肤，必须清楚插件式换肤保存的所有信息)
     */
    public void changeInnerSkin(String suffix){
        clearPluginInfo();//主要用于清空插件式换肤的信息
        mSuffix = suffix;
        mPreUtils.saveSuffix(suffix);
        notifyChangedListener();
    }

    /**
     * 清空所有换肤信息
     */
    private void clearPluginInfo() {
        mCurPkgName = null;
        mCurSkinPath = null;
        mSuffix = null;

        mPreUtils.clear();
    }

    /**
     * 恢复默认
     */
    public void recover() {
        clearPluginInfo();
        changeInnerSkin(null);
    }
}
