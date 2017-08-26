package com.whf.sounddemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by WHF on 2017/3/7.
 */

public class SoundViewHolder extends RecyclerView.ViewHolder {

    public TextView mSoundName;
    public ImageView mItemMenu;
    public View mItemView;

    public SoundViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mSoundName = (TextView) itemView.findViewById(R.id.text_name);
        mItemMenu = (ImageView) itemView.findViewById(R.id.icon_menu);
    }
}
