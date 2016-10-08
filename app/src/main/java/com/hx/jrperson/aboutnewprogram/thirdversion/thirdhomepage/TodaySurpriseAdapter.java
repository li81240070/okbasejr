package com.hx.jrperson.aboutnewprogram.thirdversion.thirdhomepage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/28.
 * 今日特价中recyclerview的适配器
 */
public class TodaySurpriseAdapter extends RecyclerView.Adapter<TodaySurpriseAdapter.myViewHolder> {
    //联系文件
    private Context context;
    //临时数据类
    private ArrayList data;

    public TodaySurpriseAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //新建一个itemView用来绑定行布局
        View itemView = LayoutInflater.from(context).inflate(R.layout.todaysurprisepage, parent, false);
        myViewHolder holder = new myViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        //获取屏幕大小,方便以后的适配环节
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        //临时数据
        holder.todayImage.setBackgroundResource(R.mipmap.maintenance);
        holder.todayIntruce.setText("空调清洗");
    }

    //获取当前数据的大小
    @Override
    public int getItemCount() {
        return data != null && data.size() > 0 ? data.size() : 0;
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        //声明组件
        ImageView todayImage;
        TextView todayIntruce;
        RelativeLayout todayAll;

        public myViewHolder(View itemView) {
            super(itemView);
            //进行组件的相关绑定
            todayIntruce = (TextView) itemView.findViewById(R.id.todayIntruce);
            todayImage = (ImageView) itemView.findViewById(R.id.todayImage);
            todayAll = (RelativeLayout) itemView.findViewById(R.id.todayAll);
        }
    }
}
