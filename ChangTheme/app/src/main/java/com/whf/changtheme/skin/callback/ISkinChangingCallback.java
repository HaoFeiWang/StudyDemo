package com.whf.changtheme.skin.callback;

/**
 * 异步换肤的回调接口
 * Created by WHF on 2017/3/2.
 */

public interface ISkinChangingCallback {
    void onStart();

    void onError(Exception e);

    void onComplete();

    public static DefaultSkinChangingCallback DEFAULT_CALLBACK
            = new DefaultSkinChangingCallback();

    public class DefaultSkinChangingCallback implements ISkinChangingCallback{

        @Override
        public void onStart() {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
