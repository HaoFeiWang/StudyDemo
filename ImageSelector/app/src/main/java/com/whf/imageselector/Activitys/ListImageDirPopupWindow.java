package com.whf.imageselector.Activitys;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.whf.imageselector.Adapters.DirListAdapter;
import com.whf.imageselector.Bean.FolderBean;
import com.whf.imageselector.R;

import java.util.List;

/**
 * Created by WHF on 2016/6/4.
 */
public class ListImageDirPopupWindow extends PopupWindow{
    private Context context;
    private int mWidth;
    private int mHeight;
    private View mContentView;
    private ListView mListView;
    private List<FolderBean> mDatas;

    public interface OnDirSelectedListener{
        void onSelect(FolderBean folderBean);
    }

    private OnDirSelectedListener onDirSelectedListener;

    public void setOnDirSelectedListener(OnDirSelectedListener onDirSelectedListener) {
        this.onDirSelectedListener = onDirSelectedListener;
    }

    public ListImageDirPopupWindow(Context context, List<FolderBean> datas) {
        setWidthAndHeight(context);
        mContentView=LayoutInflater.from(context).inflate(R.layout.popup_main,null);
        mDatas=datas;
        this.context=context;

        //给PopupWindow设置View以及宽高
        setContentView(mContentView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);//可获得焦点
        setTouchable(true);//可点击
        setOutsideTouchable(true);//可点击旁边
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView();
        initEvent();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mListView= (ListView) mContentView.findViewById(R.id.dir_listView);
        DirListAdapter dirListAdapter=new DirListAdapter(context,mDatas);
        mListView.setAdapter(dirListAdapter);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onDirSelectedListener!=null){
                    onDirSelectedListener.onSelect(mDatas.get(position));
                }
            }
        });
    }

    /**
     * 计算PopupWindow的宽高
     */
    private void setWidthAndHeight(Context context) {
        WindowManager manager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mWidth=metrics.widthPixels;
        mHeight= (int) (metrics.heightPixels*0.6);
    }

}
