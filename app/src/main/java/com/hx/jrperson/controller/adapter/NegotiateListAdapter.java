package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 价格协商列表适配器
 * Created by ge on 2016/3/3.
 */
public class NegotiateListAdapter extends BaseAdapter{

    private List<OrderEntity.DataMapBean.ServiceBean.ChildrenBean> list = new ArrayList<>();
    private Context context;

    public NegotiateListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void addData(List<OrderEntity.DataMapBean.ServiceBean.ChildrenBean> list){
        this.list.clear();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_negotiate, null);
            viewHolder = new ViewHolder();
            viewHolder.subjectTV = (TextView) convertView.findViewById(R.id.subjectTV);
            viewHolder.numberTV = (TextView) convertView.findViewById(R.id.numberTV);
            viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceTV);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null){
            double minPriceD = list.get(position).getUnit_price();
            String minPrice= String.format("%.2f", minPriceD);
            viewHolder.subjectTV.setText(list.get(position).getService_name());
            viewHolder.priceTV.setText(minPrice + "元");
            viewHolder.numberTV.setText("数量 " + list.get(position).getNum());
        }
        return convertView;
    }

    class ViewHolder{

        private TextView subjectTV, numberTV, priceTV;

        public ViewHolder getHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null){
                viewHolder = new ViewHolder();
                viewHolder.subjectTV = (TextView) view.findViewById(R.id.subjectTV);
                viewHolder.numberTV = (TextView) view.findViewById(R.id.numberTV);
                viewHolder.priceTV = (TextView) view.findViewById(R.id.priceTV);
            }
            return viewHolder;
        }

    }


}
