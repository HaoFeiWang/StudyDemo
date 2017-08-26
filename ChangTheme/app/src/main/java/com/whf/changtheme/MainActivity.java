package com.whf.changtheme;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.changtheme.skin.base.BaseSkinActivity;
import com.whf.changtheme.skin.callback.ISkinChangingCallback;

import com.whf.changtheme.skin.config.Constants;
import com.whf.changtheme.skin.config.SkinManager;


public class MainActivity extends BaseSkinActivity {

    private ListView mContent;
    private DrawerLayout mDrawerLayout;
    private String[] mDatas = {"Android","Java","Android","Java","Android"
            ,"Java","Android","Java","Android","Java"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSide();

    }

    /**
     * 给侧边栏设置Fragment，给DrawerLayout设置监听
     */
    private void initSide() {

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.side_layout);
        if(fragment == null){
            //给指定布局添加Fragment
            fragmentManager.beginTransaction().add(R.id.side_layout,new MenuLeftFragment()).commit();
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View content = mDrawerLayout.getChildAt(0);//主内容区
                View menu = drawerView;
                float scale = 1 - slideOffset;//slideOffset范围是0-1
                float rightScale =  0.8f + scale * 0.2f;//主内容区的缩小值
                if(menu.getTag().equals("left")){
                    float leftScale = 0.8f + slideOffset * 0.2f;//menu区的缩小值

                    menu.setScaleX(leftScale);
                    menu.setScaleY(leftScale);
                    menu.setAlpha(0.6f + 0.4f * slideOffset);

                    content.setTranslationX(menu.getMeasuredWidth() * slideOffset);
//                    content.setPivotX(0);
//                    content.setPivotY(content.getMeasuredHeight()/2);
//                    content.invalidate();//重新绘制content.invalidate();//重新绘制
                    content.setScaleX(rightScale);
                    content.setScaleY(rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {}

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    /**
     * 获取各控件，为主内容区的ListView设置Adapter
     */
    private void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mContent = (ListView) findViewById(R.id.content);

        mContent.setAdapter(new ArrayAdapter<String>(this,-1,mDatas){
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.list_item,parent,false);
                }
                TextView tv = (TextView) convertView.findViewById(R.id.item_text_sub);
                tv.setText(mDatas[position]);
                return convertView;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_change_bg:
                SkinManager.getInstances().changeSkin(Constants.PLUGIN_PATH
                        ,Constants.PLUGIN_PACKAGE_NAME,new ISkinChangingCallback() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this,"换肤成功",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.menu_recover:
                SkinManager.getInstances().recover();
                break;
        }
        return true;
    }

}
