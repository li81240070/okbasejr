package com.hx.jrperson.aboutnewprogram.preferential;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/5.
 * 用于显示优惠券信息的适配器文件
 */
public class PostCardDetilAdapter extends BaseAdapter {
    //联系文件
    private Context context;
    //发挥数据的具体数据类
    private ArrayList<PostCardClass.DataMapBean.CouponsBean> data;
    //需要回调的接口
    public ChooseCard chooseCard;

    public void setChooseCard(ChooseCard chooseCard) {
        this.chooseCard = chooseCard;
    }

    public PostCardDetilAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<PostCardClass.DataMapBean.CouponsBean> data) {
        this.data = data;
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

    //获取当前的显示视图
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewholder viewholder = null;
        //进行回收复用的相关代码
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.postcardlistview, parent, false);
            viewholder = new viewholder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (PostCardDetilAdapter.viewholder) convertView.getTag();
        }
        //进行点击事件的向外传递
        setAction(viewholder, position, data);
        //获得优惠券名称
        viewholder.postname.setText(data.get(position).getCoupon_name());
        //获得优惠券状态(暂时弃用)
//            if (data.get(position).getCoupon_share_state() == 0) {
//                viewholder.postState.setText("可赠送");
//            } else if (data.get(position).getCoupon_share_state() == 1) {
//                viewholder.postState.setText("不可赠送");
//                viewholder.postState.setBackgroundResource(R.mipmap.notsendback);
//            }
        //当前数据为已过期情况下的视图处理
        if (data.get(position).getUse_state() == 2) {
            viewholder.postBack.setBackgroundResource(R.mipmap.or_gary);
            viewholder.isOver.setVisibility(View.VISIBLE);
            //   viewholder.postState.setBackgroundResource(R.mipmap.gray);
        }
        viewholder.postDataStart.setText(data.get(position).getCoupon_start_time());
        viewholder.postEndData.setText(data.get(position).getCoupon_end_time());
        //优惠券面值匹配
        String howHuch = "￥" + data.get(position).getCoupon_price();
        viewholder.postprice.setText(howHuch);
        if (data.get(position).getRule_kind() == 1) {
            viewholder.postUseRule1.setText("1 全场通用");
        } else {
            viewholder.postUseRule1.setText("1 " + data.get(position).getCoupon_content() + "专用");
        }
        if (data.get(position).getRule_condition() == 1) {
            viewholder.postUseRule2.setText("2 无最低消费限制");
        } else {
            viewholder.postUseRule2.setText("2 满" + data.get(position).getRule_price() + "可用");
        }
        return convertView;
    }

    //内部类,用来保存item中的各个组件
    public class viewholder {
        Button postState;
        TextView postprice, postname, postDataStart, postEndData, postUseRule1, postUseRule2;
        RelativeLayout postBack, noneData, chooseMe;
        ImageView isOver;

        public viewholder(View itemView) {
            //视图相关组件的绑定
            postState = (Button) itemView.findViewById(R.id.postState);
            postprice = (TextView) itemView.findViewById(R.id.postprice);
            postname = (TextView) itemView.findViewById(R.id.postname);
            postDataStart = (TextView) itemView.findViewById(R.id.postDataStart);
            postEndData = (TextView) itemView.findViewById(R.id.postEndData);
            postUseRule1 = (TextView) itemView.findViewById(R.id.postUseRule1);
            postUseRule2 = (TextView) itemView.findViewById(R.id.postUseRule2);
            postBack = (RelativeLayout) itemView.findViewById(R.id.postBack);
            isOver = (ImageView) itemView.findViewById(R.id.isOver);
            noneData = (RelativeLayout) itemView.findViewById(R.id.noneData);
            chooseMe = (RelativeLayout) itemView.findViewById(R.id.chooseMe);

        }
    }

    //向外传递接口信息
    private void setAction(final viewholder view, final int position, final ArrayList<PostCardClass.DataMapBean.CouponsBean> data) {
        view.chooseMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseCard != null) {
                    chooseCard.chooseOne(position, data);
                }
            }
        });
    }
}
