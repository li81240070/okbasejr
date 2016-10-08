package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.JudgeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加印象选项 适配器
 * Created by ge on 2016/3/16.
 */
public class AddImpressAdapter extends RecyclerView.Adapter<AddImpressAdapter.AddImpressViewHolder> {

    private Context context;
    private List<JudgeEntity> list = new ArrayList<>();

    public AddImpressAdapter(Context context){
        this.context = context;
    }

    public void addData(List<JudgeEntity> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public AddImpressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddImpressViewHolder viewHolder = null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_add_impress, null);
        viewHolder = new AddImpressViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddImpressViewHolder holder, final int position) {
        if (list != null && list.size() > 0){
            String[] gut = list.get(position).getGut();
            String str = "";
            for (int i = 0;i < gut.length;i++){
                str = str + gut[i];
            }
            holder.addImpressBtn.setText(str);
            holder.addImpressBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onAddImpressClickListener != null) {
                        if (list.get(position).getIsClick() == 0) {
                            list.get(position).setIsClick(1);
                            holder.addImpressBtn.setBackgroundResource(R.drawable.shape_cancle_btn_because_press);
                        } else {
                            list.get(position).setIsClick(0);
                            holder.addImpressBtn.setBackgroundResource(R.drawable.shape_cancle_btn_because);
                        }
                        onAddImpressClickListener.onAddImpressClick(v, position, list);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    class AddImpressViewHolder extends RecyclerView.ViewHolder{
        Button addImpressBtn;
        public AddImpressViewHolder(View itemView) {
            super(itemView);
            addImpressBtn = (Button) itemView.findViewById(R.id.addImpressBtn);
        }
    }

    private OnAddImpressClickListener onAddImpressClickListener;

    public void setOnAddImpressClickListener(OnAddImpressClickListener onAddImpressClickListener){
        this.onAddImpressClickListener = onAddImpressClickListener;
    }

    public interface OnAddImpressClickListener{
        void onAddImpressClick(View view, int position, List<JudgeEntity> list);
    }

}
