package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.ServicesParentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 维修项目列表 适配器
 * Created by ge on 2016/3/30.
 */
public class ServiceParentsAdapter extends RecyclerView.Adapter<ServiceParentsAdapter.ServiceParentsViewHolder> {

    private Context context;
    private List<ServicesParentEntity.DataMapBean.ServicesBean> list = new ArrayList<ServicesParentEntity.DataMapBean.ServicesBean>();
    private int position;

    public void addData(List emm) {
        list.clear();
        list.addAll(emm);
        notifyDataSetChanged();
    }

    public ServiceParentsAdapter(Context context){
        this.context = context;
    }


    @Override
    public ServiceParentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ServiceParentsViewHolder holder = null;


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_gut, null);
        holder = new ServiceParentsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ServiceParentsViewHolder holder, final int position) {


        if (list != null && list.size() > 0){


            ServicesParentEntity.DataMapBean.ServicesBean bean = list.get(position);
            String service = bean.getService();
            holder.service_subkect_nameTV.setText(service);
            holder.itemServiceRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (onClickServiceParentsLisener != null){



                        onClickServiceParentsLisener.OnClickServiceParents(v,position);
                 }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    class ServiceParentsViewHolder extends RecyclerView.ViewHolder{

        private TextView service_subkect_nameTV;
        private RelativeLayout itemServiceRL;
        //第二次更新内容
        private RelativeLayout changeMeToGray;

        public ServiceParentsViewHolder(View itemView) {
            super(itemView);
            service_subkect_nameTV = (TextView) itemView.findViewById(R.id.service_subkect_nameTV);
            itemServiceRL = (RelativeLayout) itemView.findViewById(R.id.itemServiceRL);
            //第二次更新内容
            changeMeToGray= (RelativeLayout) itemView.findViewById(R.id.changeMeToGray);
        }
    }

    private OnClickServiceParentsLisener onClickServiceParentsLisener;

    public void setOnClickServiceParentsLisener(OnClickServiceParentsLisener onClickServiceParentsLisener){
        this.onClickServiceParentsLisener = onClickServiceParentsLisener;
    }

    public interface OnClickServiceParentsLisener{
        void OnClickServiceParents(View v, int Position);
    }

}
