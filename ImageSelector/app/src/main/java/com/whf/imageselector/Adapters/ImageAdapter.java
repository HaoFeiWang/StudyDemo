package com.whf.imageselector.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.whf.imageselector.R;
import com.whf.imageselector.Utils.ImageLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by WHF on 2016/6/4.
 */
public class ImageAdapter extends BaseAdapter{

    private Context context;
    private List<String> mImagesPath;
    private String dirPath;
    private LayoutInflater mInfater;
    //保存选中的图片的路径
    private static Set<String> mSelectImgs=new HashSet<String>();

    public ImageAdapter(Context context, List<String> mDatas,String dirPath){
        this.context=context;
        this.mImagesPath=mDatas;
        this.dirPath=dirPath;
        this.mInfater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImagesPath.size();
    }

    @Override
    public Object getItem(int position) {
        return mImagesPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHold viewHold;
        final String path=dirPath+"/"+mImagesPath.get(position);
        if(convertView==null){
            viewHold=new ViewHold();
            convertView=mInfater.inflate(R.layout.gridview_item,parent,false);
            viewHold.imageView= (ImageView) convertView.findViewById(R.id.imageView_item);
            viewHold.imageButton= (ImageButton) convertView.findViewById(R.id.imageButton_item);
            convertView.setTag(viewHold);
        }else{
            viewHold= (ViewHold) convertView.getTag();
        }
        viewHold.imageView.setImageResource(R.drawable.pictures_no);

        if(mSelectImgs.contains(path)){
            viewHold.imageView.setColorFilter(Color.parseColor("#77000000"));
            viewHold.imageButton.setImageResource(R.drawable.pictures_selected);
        }else {
            viewHold.imageView.setColorFilter(null);
            viewHold.imageButton.setImageResource(R.drawable.picture_unselected);
        }

        ImageLoader.getInstance(10, ImageLoader.Type.LIFO).
                loadImage(dirPath+"/"+mImagesPath.get(position),viewHold.imageView);

        viewHold.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectImgs.contains(path)){
                    //已经被选中
                    mSelectImgs.remove(path);
                    viewHold.imageView.setColorFilter(null);
                    viewHold.imageButton.setImageResource(R.drawable.picture_unselected);
                }else{
                    //未被选中
                    mSelectImgs.add(path);
                    viewHold.imageView.setColorFilter(Color.parseColor("#77000000"));
                    viewHold.imageButton.setImageResource(R.drawable.pictures_selected);
                }
            }
        });
        return convertView;
    }
    class ViewHold{
        ImageView imageView;
        ImageButton imageButton;
    }
}
