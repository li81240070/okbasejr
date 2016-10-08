package com.hx.jrperson.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.utils.JrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单适配器
 * Created by ge on 2016/3/1.
 */
public class OrderAdapter extends BaseAdapter {

    private List<MyOrdorEntity.DataMapBean.OrdersBean> list = new ArrayList<>();
    private Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<MyOrdorEntity.DataMapBean.OrdersBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataLoad(List<MyOrdorEntity.DataMapBean.OrdersBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
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

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ordor, null);
            viewHolder = new ViewHolder();
            viewHolder.ordor_gut_data = (TextView) convertView.findViewById(R.id.order_date_TV);
            viewHolder.ordor_gut_state = (TextView) convertView.findViewById(R.id.order_state_TV);
            viewHolder.ordor_gut_subject = (TextView) convertView.findViewById(R.id.order_subject_TV);
            viewHolder.ordor_gut_address = (TextView) convertView.findViewById(R.id.order_addr_TV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            if (list.get(position).getOrder_time() != null) {
                String timeStr = list.get(position).getAppoint_time();
                String timeBefore = timeStr.substring(0, timeStr.length() - 3);
                String time = JrUtils.times(timeBefore);
                viewHolder.ordor_gut_data.setText(context.getString(R.string.string_time).concat(time));
            }
            String statuebefor = list.get(position).getOrder_status();
            String statue = JrUtils.orderStaute(list.get(position).getOrder_status());
            Resources resource = context.getResources();
            ColorStateList csl = resource.getColorStateList(R.color.material_green_400);
            ColorStateList csl1 = resource.getColorStateList(R.color.color_yelow);
            ColorStateList csl2 = resource.getColorStateList(R.color.material_red_800);
            ColorStateList csl3 = resource.getColorStateList(R.color.material_grey_400);
            switch (statuebefor) {
                case "0":
                case "99":
                case "7":
                case "8":
                    viewHolder.ordor_gut_state.setTextColor(csl3);
                    break;
                case "1":
                case "2":
                case "3":
                    viewHolder.ordor_gut_state.setTextColor(csl);
                    break;
                case "4":
                    viewHolder.ordor_gut_state.setTextColor(csl1);
                    break;
                case "5":
                    viewHolder.ordor_gut_state.setTextColor(csl2);
                    break;
            }
            viewHolder.ordor_gut_state.setText(statue);
            viewHolder.ordor_gut_address.setText(list.get(position).getAddress());
            viewHolder.ordor_gut_subject.setText(list.get(position).getService());
        }
        return convertView;
    }

    private static class ViewHolder {
        private TextView ordor_gut_data, ordor_gut_state, ordor_gut_subject, ordor_gut_address;

        public ViewHolder getHolder(View view) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder();
                viewHolder.ordor_gut_data = (TextView) view.findViewById(R.id.order_date_TV);
                viewHolder.ordor_gut_state = (TextView) view.findViewById(R.id.order_state_TV);
                viewHolder.ordor_gut_subject = (TextView) view.findViewById(R.id.order_subject_TV);
                viewHolder.ordor_gut_address = (TextView) view.findViewById(R.id.order_addr_TV);
            }
            return viewHolder;
        }
    }
}
