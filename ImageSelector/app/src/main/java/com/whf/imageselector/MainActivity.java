package com.whf.imageselector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whf.imageselector.Activitys.ListImageDirPopupWindow;
import com.whf.imageselector.Adapters.ImageAdapter;
import com.whf.imageselector.Bean.FolderBean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements ListImageDirPopupWindow.OnDirSelectedListener{

    private GridView mGridView;
    private List<String> mImages;

    private RelativeLayout mButtomLayout;
    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolders=new ArrayList<FolderBean>();

    ImageAdapter imageAdapter;

    private ListImageDirPopupWindow popupWindow;
    private ProgressDialog mProgressDialog;
    private static final int UIHANDLER_WHAT=0x2;

    //文件名过滤器
    private FilenameFilter filenameFilter=new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            if(filename.endsWith(".jpg")||filename.endsWith(".png")||filename.endsWith(".jpeg"))
                return true;
            return false;
        }
    };

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initDatas();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }

    private Handler mUiHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==UIHANDLER_WHAT){
                mProgressDialog.dismiss();
                data2View();
                initDirPopupWindow();
            }
        }
    };

    /**
     * 绑定数据至View中(感觉有点问题？？？？？？？)
     */
    private void data2View() {
        if(mCurrentDir==null){
            Toast.makeText(this,"未扫描到图片",Toast.LENGTH_SHORT).show();
        }
        //获取mCurrentDir目录下所有文件名
        mImages= Arrays.asList(mCurrentDir.list(filenameFilter));
        imageAdapter=new ImageAdapter(this,mImages,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(imageAdapter);
        mDirCount.setText(""+mMaxCount);
        mDirName.setText(mCurrentDir.getName());

    }

    /**
     * 初始化PopupWindow
     */
    private void initDirPopupWindow() {
        popupWindow=new ListImageDirPopupWindow(this,mFolders);
        popupWindow.setOnDirSelectedListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
    }

    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }
    /**
     * 内容区域变亮
     */
    private void lightOff() {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mGridView= (GridView) findViewById(R.id.image_gridView);
        mButtomLayout= (RelativeLayout) findViewById(R.id.bottom_layout);
        mDirCount= (TextView) findViewById(R.id.dir_count_textView);
        mDirName= (TextView) findViewById(R.id.dir_name_textView);
    }
    /**
     * 初始化数据,利用ContentProvider扫描手机中的所有图片
     */
    private void initDatas() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"当前存储卡不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        mProgressDialog=ProgressDialog.show(this,null,"正在加载...");

        new Thread(){
            @Override
            public void run() {
                Uri imgUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver imageResolver=MainActivity.this.getContentResolver();
                //只查询JPEG和PNG格式的图片
                Cursor cursor=imageResolver.query(imgUri,null,
                        MediaStore.Images.Media.MIME_TYPE+"=? or "+
                                MediaStore.Images.Media.MIME_TYPE+"=?",
                        new String[]{"image/jpeg","image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths=new HashSet<String>();
                while (cursor.moveToNext()){
                    String path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile=new File(path).getParentFile();
                    if(parentFile==null){
                        continue;
                    }
                    String dirPath=parentFile.getAbsolutePath();
                    FolderBean folderBean=null;
                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        folderBean=new FolderBean();
                        folderBean.setDirPath(dirPath);
                        folderBean.setFirstImgPath(path);
                    }
                    if(parentFile==null){
                        continue;
                    }
                    //过滤查看ParentFile目录下图片的数量
                    int imgCounts=parentFile.list(filenameFilter).length;
                    folderBean.setCount(imgCounts);
                    mFolders.add(folderBean);

                    if(imgCounts>mMaxCount){
                        mMaxCount=imgCounts;
                        mCurrentDir=parentFile;
                    }
                }
                cursor.close();
                mUiHandler.sendEmptyMessage(UIHANDLER_WHAT);
            }
        }.start();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mButtomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.setAnimationStyle(R.style.dir_popupwindow_anim);
                popupWindow.showAsDropDown(mButtomLayout,0,0);
                lightOff();
            }
        });
    }

    @Override
    public void onSelect(FolderBean folderBean) {
        mCurrentDir=new File(folderBean.getDirPath());
        mImages= Arrays.asList(mCurrentDir.list(filenameFilter));
        imageAdapter=new ImageAdapter(this,mImages,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(imageAdapter);
        mDirName.setText(folderBean.getName());
        mDirCount.setText(folderBean.getCount()+"");
        popupWindow.dismiss();
    }
}
