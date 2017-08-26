package com.whf.imageselector.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whf.imageselector.Bean.FolderBean;
import com.whf.imageselector.R;
import com.whf.imageselector.Utils.ImageLoader;

import java.util.List;

/**
 * Created by WHF on 2016/6/4.
 */
public class DirListAdapter extends BaseAdapter{

    private Context context;
    private List<FolderBean> folderBeens;

    public DirListAdapter(Context context, List<FolderBean> folderBeens){
        this.context=context;
        this.folderBeens=folderBeens;
    }

    @Override
    public int getCount() {
        return folderBeens.size();
    }

    @Override
    public Object getItem(int position) {
        return folderBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FolderBean folderBean=folderBeens.get(position);
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.list_item_imageView);
            viewHolder.dirName= (TextView) convertView.findViewById(R.id.textView_dir_name);
            viewHolder.dirCount= (TextView) convertView.findViewById(R.id.textView_dir_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.drawable.pictures_no);
        viewHolder.dirName.setText(folderBean.getName());
        viewHolder.dirCount.setText(""+folderBean.getCount());
        ImageLoader.getInstance(10, ImageLoader.Type.LIFO).
                loadImage(folderBean.getFirstImgPath(),viewHolder.imageView);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView dirName;
        TextView dirCount;
    }
}
