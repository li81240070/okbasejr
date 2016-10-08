package com.hx.jrperson.aboutnewprogram.thirdversion.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.MyOrdorEntity;
import com.hx.jrperson.ui.activity.NegotiatePriceActivity;
import com.hx.jrperson.ui.activity.PayActivity;
import com.hx.jrperson.ui.activity.PaySuccessActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/26.
 */
public class OrderAdapterForAll extends BaseAdapter {
    private ArrayList<MyOrdorEntity.DataMapBean.OrdersBean> data;
    private Context context;

    public OrderAdapterForAll(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<MyOrdorEntity.DataMapBean.OrdersBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data != null && data.size() > 0 ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data != null && data.size() > 0 ? data.get(position) : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        viewholder viewholder=null;
        if (convertView==null){

            convertView= LayoutInflater.from(context).inflate(R.layout.myorderdetilpage,parent,false);
            viewholder=new viewholder(convertView);
            convertView.setTag(viewholder);

        }else{

            viewholder= (OrderAdapterForAll.viewholder) convertView.getTag();

        }
       //进行数据添加
        viewholder.orderName.setText(data.get(position).getService());
        viewholder.orderPosition.setText(data.get(position).getAddress());
        viewholder.orderPrice.setText("￥"+data.get(position).getPrice());
       switch (data.get(position).getOrder_status()){
           case "0":viewholder.orderState.setText("已取消");

               viewholder.orderState.setTextColor(0xff868686);
               break;
           case "1":viewholder.orderState.setText("未接单");
               viewholder.orderState.setTextColor(0xff3399ff);
               break;
           case "2":viewholder.orderState.setText("进行中");
               viewholder.orderState.setTextColor(0xffffa95d);
               break;
           case "3":viewholder.orderState.setText("进行中");
               viewholder.orderState.setTextColor(0xffffa95d);
               break;
           case "4":viewholder.orderState.setText("进行中");
               viewholder.orderState.setTextColor(0xff3399ff);

               break;

           case "5":viewholder.orderState.setText("待付款");
               viewholder.orderState.setTextColor(0xff3399ff);

               break;
           case "6":viewholder.orderState.setText("待评价");
               viewholder.orderState.setTextColor(0xffffa95d);
               break;
           case "7":viewholder.orderState.setText("待评价");
               viewholder.orderState.setTextColor(0xffffa95d);
               break;
           case "8":viewholder.orderState.setText("待评价");
               viewholder.orderState.setTextColor(0xffffa95d);
               break;
           case "99":viewholder.orderState.setText("已完成");
               viewholder.orderState.setTextColor(0xff333333);
               break;
           default:viewholder.orderState.setText("状态异常");
               break;

       }
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1=new Date( Long.parseLong(data.get(position).getAppoint_time().substring(0,data.get(position).getAppoint_time().length())));
        String t1=format.format(d1);
        viewholder.orderTime.setText(t1);
        viewholder.goOrderDetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (position == allList.size() + 1) {
//                    return;
//                }
                String status = data.get(position).getOrder_status();//订单状态
                if (status.equals("1")) {//未接单
                    String time = String.valueOf(data.get(position).getAppoint_time());
                    Intent intent2 = new Intent(context, NegotiatePriceActivity.class);
                    Bundle bundle = new Bundle();
                    MyOrdorEntity.DataMapBean.OrdersBean ordersBean=data.get(position);
                    bundle.putSerializable("orderEntity",ordersBean);
                    bundle.putString("staute", status);
                    intent2.putExtras(bundle);
                    context.startActivity(intent2);
                } else if (status.equals("2") || status.equals("3") || status.equals("4")) {//进行中
                    String time = String.valueOf(data.get(position).getAppoint_time());
                    Intent intent2 = new Intent(context, NegotiatePriceActivity.class);
                    Bundle bundle = new Bundle();
                    MyOrdorEntity.DataMapBean.OrdersBean ordersBean=data.get(position);
                    bundle.putSerializable("orderEntity",ordersBean);
                    bundle.putString("staute", status);
                    intent2.putExtras(bundle);
                    context.startActivity(intent2);
                } else if (status.equals("5")) {//待付款
                    Intent intent3 = new Intent(context, PayActivity.class);
                    Bundle bundle = new Bundle();
                    MyOrdorEntity.DataMapBean.OrdersBean ordersBean=data.get(position);
                    bundle.putSerializable("orderEntity",ordersBean);
                    bundle.putInt("position", position);
                    bundle.putString("SUBJECT",data.get(position).getComment());
                    bundle.putString("BODY", data.get(position).getComment());
                    bundle.putString("PRICE", data.get(position).getPrice());
                    bundle.putString("ORDER_ID",data.get(position).getOrder_id());
                    intent3.putExtras(bundle);
                    context.startActivity(intent3);
                } else if (status.equals("8") || status.equals("7") || status.equals("6")) {//待评价
                    Intent intent4 = new Intent(context, PaySuccessActivity.class);
                    Bundle bundle = new Bundle();
                    MyOrdorEntity.DataMapBean.OrdersBean ordersBean=data.get(position);
                    bundle.putSerializable("orderEntity",ordersBean);
                    bundle.putString("staute", status);
                    intent4.putExtras(bundle);
                    context.startActivity(intent4);
                } else if("99".equals(status)) {
                    Toast.makeText(context,"您的订单已完成",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    //内部类,用来保存item中的各个组件
    public class viewholder {
       //控件声明
        TextView orderPosition,orderTime,orderState,orderPrice,orderName;
        LinearLayout goOrderDetil;

        public viewholder(View itemView) {

           //进行控件绑定
            orderPosition= (TextView) itemView.findViewById(R.id.orderPosition);
            orderTime= (TextView) itemView.findViewById(R.id.orderTime);
            orderState= (TextView) itemView.findViewById(R.id.orderState);
            orderPrice= (TextView) itemView.findViewById(R.id.orderPrice);
            orderName= (TextView) itemView.findViewById(R.id.orderName);
            goOrderDetil= (LinearLayout) itemView.findViewById(R.id.goOrderDetil);
        }
    }
}
