package com.hx.jrperson.aboutnewprogram.homepagefragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hx.jrperson.R;
import com.hx.jrperson.ui.activity.MainActivity;

/**
 * Created by Administrator on 2016/8/5.
 * 第四个闪屏碎片文件
 */
public class FourFragment extends android.support.v4.app.Fragment {
    private Context context;
    private ImageView myview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fourfragment, null);
        myview = (ImageView) view.findViewById(R.id.myview);
        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }
}
