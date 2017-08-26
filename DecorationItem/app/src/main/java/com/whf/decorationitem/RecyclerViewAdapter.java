package com.whf.decorationitem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by WHF on 2017/3/6.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Person> personList;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Person> personList,Context context) {
        this.personList = personList;
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.mName.setText(personList.get(position).getName());
        holder.mNumber.setText(personList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }
}
