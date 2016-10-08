package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 * 打折卡中lsitview的适配器
 */
public class ValueCardAdapter extends BaseAdapter {
    //联系文件
    private Context context;
    //请求数据的实体类
    private ArrayList<PostCardClass.DataMapBean.CouponsBean> data;
    //点击事件向外传递参数需要回调的接口
    public ChooseValue chooseValue;

    public void setChooseValue(ChooseValue chooseValue) {
        this.chooseValue = chooseValue;
    }

    public ValueCardAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<PostCardClass.DataMapBean.CouponsBean> data) {
        this.data = data;
    }

    //记录当前数据的数量
    @Override
    public int getCount() {
        return data != null && data.size() > 0 ? data.size() : 0;
    }

    //记录当前数据的位置
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
        //适配器相关复用代码
        viewholder viewholder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.valuecardlistview, parent, false);
            viewholder = new viewholder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ValueCardAdapter.viewholder) convertView.getTag();
        }
        //进行点击事件的向外传递
        setAction(viewholder, position);
        //获得优惠券名称
        viewholder.postnameValue.setText(data.get(position).getCoupon_name());
        //获得优惠券状态(暂时弃用)
//        if (data.get(position).getCouponssharestate()==0) {
//            viewholder.postStateValue.setText("可赠送");
//        }else{
//            viewholder.postStateValue.setText("不可赠送");
//            viewholder.postStateValue.setBackgroundResource(R.mipmap.zengsong_gray);
//        }
        if (data.get(position).getUse_state() == 3) {
            viewholder.postBackValue.setBackgroundResource(R.mipmap.valueback);
        }
        viewholder.postDataStartValue.setText(data.get(position).getCoupon_start_time());
        viewholder.postEndDataValue.setText(data.get(position).getCoupon_end_time());
        //价格所在位置
        String howHuch = "￥" + data.get(position).getCoupon_price();
        viewholder.postpriceValue.setText(howHuch);
        if (data.get(position).getRule_kind() == 1) {
            viewholder.postUseRule1Value.setText("1 全场通用");
        } else {
            viewholder.postUseRule1Value.setText("1 " + data.get(position).getCoupon_content() + "专用");
        }
        return convertView;
    }

    //内部类,用来保存item中的各个组件
    public class viewholder {
        Button postStateValue;
        TextView postpriceValue, postnameValue, postDataStartValue, postEndDataValue, postUseRule1Value, postUseRule2Value;
        RelativeLayout postBackValue;
        LinearLayout chooseMeMe;


        public viewholder(View itemView) {
            //绑定视图中的各个组件
            postStateValue = (Button) itemView.findViewById(R.id.postStateValue);
            postpriceValue = (TextView) itemView.findViewById(R.id.postpriceValue);
            postnameValue = (TextView) itemView.findViewById(R.id.postnameValue);
            postDataStartValue = (TextView) itemView.findViewById(R.id.postDataStartValue);
            postEndDataValue = (TextView) itemView.findViewById(R.id.postEndDataValue);
            postUseRule1Value = (TextView) itemView.findViewById(R.id.postUseRule1Value);
            postUseRule2Value = (TextView) itemView.findViewById(R.id.postUseRule2Value);
            postBackValue = (RelativeLayout) itemView.findViewById(R.id.postBackValue);
            chooseMeMe = (LinearLayout) itemView.findViewById(R.id.chooseMeMe);
        }
    }

    //向外传递接口信息,包括当前被点击的位置和视图参数等
    private void setAction(final viewholder view, final int position) {
        view.chooseMeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseValue != null) {
                    chooseValue.chooseOne(position);
                }
            }
        });
    }
}
