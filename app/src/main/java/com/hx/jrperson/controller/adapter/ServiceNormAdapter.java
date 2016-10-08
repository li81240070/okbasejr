package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.ServiceThreeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务标准列表
 * Created by ge on 2016/3/22.
 */
public class ServiceNormAdapter extends BaseAdapter {

    private List<ServiceThreeEntity.DataMapBean.ServicesBean> list = new ArrayList<>();
    private Context context;

    public void addData(List<ServiceThreeEntity.DataMapBean.ServicesBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ServiceNormAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_noram, null);
            viewHolder = new ViewHolder();
            viewHolder.subject_nameTV = (TextView) convertView.findViewById(R.id.subject_nameTV);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.serviceGutTV);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null){
            if (list.get(position).getService() != null && list.get(position).getDecription() != null){
                viewHolder.subject_nameTV.setText(list.get(position).getService());
                viewHolder.textView.setText(list.get(position).getDecription());
            }
        }
        return convertView;
    }

    class ViewHolder{

        private TextView subject_nameTV, textView;

        public ViewHolder getHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null){
                viewHolder = new ViewHolder();
                viewHolder.subject_nameTV = (TextView) view.findViewById(R.id.subject_nameTV);
                viewHolder.textView = (TextView) view.findViewById(R.id.serviceGutTV);
            }
            return viewHolder;
        }
    }

//    public ServiceNormAdapter(Context context, List list, int what) {
//        super(context, list, what);
//        this.context = context;
//        this.list = list;
//    }
//
//    @Override
//    public int getItemResource() {
//        return R.layout.adapter_service_noram;
//    }
//
//    @Override
//    public View getItemView(int position, View convertView, BaseViewHolder holder) {
//        TextView subject_nameTV = (TextView) convertView.findViewById(R.id.subject_nameTV);//项目名称
//        TextView textView = (TextView) convertView.findViewById(R.id.serviceGutTV);//项目标准说明
//        if (list != null){
//            subject_nameTV.setText(list.get(position).getService());
//            textView.setText(list.get(position).getDecription());
//        }
//        return convertView;
//    }
}
