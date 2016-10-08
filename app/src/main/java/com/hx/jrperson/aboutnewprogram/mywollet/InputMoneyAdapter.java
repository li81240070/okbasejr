package com.hx.jrperson.aboutnewprogram.mywollet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 * 该文件为
 */
public class InputMoneyAdapter extends RecyclerView.Adapter<InputMoneyAdapter.myViewHolder> {
    private Context context;
    private ArrayList<PreferentialClass.DataMapBean.DataBean> data;
    //需要回调的接口
    public ChangeMyColor changeMyColor;

    // 传递接口信息
    public void setChangeMyColor(ChangeMyColor changeMyColor) {
        this.changeMyColor = changeMyColor;
    }

    public InputMoneyAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.inputmoneydetilpage, parent, false);
        myViewHolder holder = new myViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        //该部分为根据屏幕大小适配优惠信息的大小的相关代码
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams params = holder.introduceBack.getLayoutParams();
        params.width = width / 2 - 50;
        holder.introduceBack.setLayoutParams(params);
        //向外传递该位置上点击事件,同时附带该位置的各种参数和属性
        setAction(holder, position);
        //对显示部分进行数据绑定
        holder.chooseHowMuch.setText(data.get(position).getCoupon_content() + "\n" + "售价" + data.get(position).getNeed_amount());
    }
    //确定显示文件长度
    @Override
    public int getItemCount() {
        return data != null && data.size() > 0 ? data.size() : 0;
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView chooseHowMuch;
        LinearLayout introduceBack;

        public myViewHolder(View itemView) {

            super(itemView);
            //视图绑定
            chooseHowMuch = (TextView) itemView.findViewById(R.id.chooseHowMuch);
            introduceBack = (LinearLayout) itemView.findViewById(R.id.introduceBack);
        }
    }

    //向外传递接口信息
    private void setAction(final myViewHolder view, final int position) {
        view.introduceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeMyColor != null) {
                    changeMyColor.clickMe(position, view);

                }
            }
        });
    }
}
