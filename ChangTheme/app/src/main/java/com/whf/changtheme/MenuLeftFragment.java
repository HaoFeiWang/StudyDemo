package com.whf.changtheme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whf.changtheme.skin.config.SkinManager;


/**
 * Created by WHF on 2017/3/1.
 */
public class MenuLeftFragment extends Fragment implements View.OnClickListener{

    private RelativeLayout mGreenButton,mRedButton;
    private View menu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        menu = inflater.inflate(R.layout.layout_menu,container,false);
        initView(menu);
        return menu;
    }


    private void initView(View view){
        mGreenButton = (RelativeLayout) view.findViewById(R.id.layout_one);
        mRedButton = (RelativeLayout) view.findViewById(R.id.layout_two);

        mGreenButton.setOnClickListener(this);
        mRedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_one:
                SkinManager.getInstances().changeInnerSkin("red");
                break;
            case R.id.layout_two:
                SkinManager.getInstances().changeInnerSkin("green");
                break;
        }
    }


}
