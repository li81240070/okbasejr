package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.CancleBecauseEntity;

import java.util.List;

/**
 * 取消订单原因适配器
 * Created by ge on 2016/3/16.
 */
public class CancleOrdorBecauseAdapter extends RecyclerView.Adapter<CancleOrdorBecauseAdapter.CancleOrdorViewHolder> {

    private Context context;
    private List<CancleBecauseEntity> list;

    public void addData(List<CancleBecauseEntity> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public CancleOrdorBecauseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CancleOrdorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CancleOrdorViewHolder holder = null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cancle_ordor, null);
        holder = new CancleOrdorViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CancleOrdorViewHolder holder, final int position) {
        if (list != null) {
            holder.cancleBecauseBtn.setText(list.get(position).getContent());
            holder.cancleBecauseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickCancleBecauseListener != null) {
                            if (list.get(position).getIsClick() == 1){
                                list.get(position).setIsClick(0);
                                holder.cancleBecauseBtn.setBackgroundResource(R.drawable.shape_cancle_btn_because_press);
                            }else {
                                list.get(position).setIsClick(1);
                                holder.cancleBecauseBtn.setBackgroundResource(R.drawable.shape_cancle_btn_because);
                            }
                            onClickCancleBecauseListener.onClickCancleBecause(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    class CancleOrdorViewHolder extends RecyclerView.ViewHolder {

        Button cancleBecauseBtn;

        public CancleOrdorViewHolder(View itemView) {
            super(itemView);
            cancleBecauseBtn = (Button) itemView.findViewById(R.id.cancleBecauseBtn);
        }
    }

    private OnClickCancleBecauseListener onClickCancleBecauseListener;

    public void setOnClickCancleBecauseListener(OnClickCancleBecauseListener onClickCancleBecauseListener) {
        this.onClickCancleBecauseListener = onClickCancleBecauseListener;
    }

    public interface OnClickCancleBecauseListener {
        void onClickCancleBecause(View view, int position);
    }


}
