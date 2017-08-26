package com.whf.sounddemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHF on 2017/3/7.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    private List<Sound> mSoundList;
    private LayoutInflater mInflater;
    private OnRecyclerItemClickLinstener mItemClickListener = null;
    private OnItemMenuClickLinstener mItemMenuClickListener = null;

    public SoundAdapter(ArrayList<Sound> soundList, Context context){
        this.mSoundList = soundList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_sound,parent,false);
        return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoundViewHolder holder, int position) {
        final Sound sound = mSoundList.get(position);
        holder.mSoundName.setText(sound.getName());
        holder.mItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemMenuClickListener!=null){
                    mItemMenuClickListener.onItemMenuClick(v,sound);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener!=null){
                    mItemClickListener.onItemClick(v,sound);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSoundList.size();
    }

    public void setSoundList(ArrayList<Sound> soundList){
        this.mSoundList = soundList;
        notifyDataSetChanged();
    }

    public void addItemClickListener(OnRecyclerItemClickLinstener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public void addItemMenuClickListener(OnItemMenuClickLinstener onItemMenuClickListener){
        this.mItemMenuClickListener = onItemMenuClickListener;
    }


    public interface OnRecyclerItemClickLinstener{
        void onItemClick(View view,Sound sound);
    }
    public interface OnItemMenuClickLinstener{
        void onItemMenuClick(View view,Sound sound);
    }
}
