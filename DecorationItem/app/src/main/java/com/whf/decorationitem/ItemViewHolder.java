package com.whf.decorationitem;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by WHF on 2017/3/6.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView mName,mNumber;

    public ItemViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View view) {
        mName = (TextView) view.findViewById(R.id.name);
        mNumber = (TextView) view.findViewById(R.id.number);
    }


}
