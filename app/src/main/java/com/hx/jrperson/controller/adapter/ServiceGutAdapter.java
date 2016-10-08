package com.hx.jrperson.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;

/**
 * 维修项目列表 适配器
 * Created by ge on 2016/3/4.
 */
public class ServiceGutAdapter extends BaseAdapter {

    public ServiceGutAdapter() {
        super();
    }

    //添加数据
    public void addData(){

    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_gut, null);
            viewHolder = new ViewHolder();
            viewHolder.service_subkect_nameTV = (TextView) convertView.findViewById(R.id.service_subkect_nameTV);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.service_subkect_nameTV.setText("水龙头");

        return convertView;
    }

    class ViewHolder{

        private TextView service_subkect_nameTV;

        public ViewHolder getTag(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.service_subkect_nameTV = (TextView) view.findViewById(R.id.service_subkect_nameTV);
            return viewHolder;
        }

    }


}
