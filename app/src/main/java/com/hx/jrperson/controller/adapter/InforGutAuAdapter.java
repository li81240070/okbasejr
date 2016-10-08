package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.NewInforEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.views.RoundAngleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页右上角按钮点击进入消息页面
 * 本类是消息页面列表的适配器
 * Created by ge on 2016/3/17.
 */
public class InforGutAuAdapter extends BaseAdapter {

    private Context context;
    private List<NewInforEntity.DataMapBean.ActivitylistBean> list = new ArrayList();

    public InforGutAuAdapter(Context context) {
        super();
        this.context = context;
    }

    public void addData(List<NewInforEntity.DataMapBean.ActivitylistBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addDataLoad(List<NewInforEntity.DataMapBean.ActivitylistBean> list){
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_infor_gut, null);
            viewHolder = new ViewHolder();
            viewHolder.inforGutIV = (RoundAngleImageView) convertView.findViewById(R.id.inforGutIV);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewInforEntity.DataMapBean.ActivitylistBean msgListBean;
        if (null != list){
            msgListBean = list.get(position);
            String imgUrl = API.NEWMESSAGEINFORHEAD.concat(msgListBean.getActivity_picture_url()).concat(".jpg");
            int wid = PreferencesUtils.getInt(context, Consts.WID);
            viewHolder.inforGutIV.setMinimumHeight((wid * 2 / 5));
            viewHolder.inforGutIV.setMaxHeight((wid * 2 / 5));
            if (!"".equals(imgUrl)){
                Picasso.with(context).load(imgUrl)
                        .placeholder(R.mipmap.loading_infor)
                        .config(Bitmap.Config.RGB_565)
                        .resize((wid * 2 / 5), (wid * 2 / 5))
                        .error(R.mipmap.loading_infor)
                        .into(viewHolder.inforGutIV);
            }else {
                viewHolder.inforGutIV.setImageResource(R.mipmap.loading_infor);
            }
        }
        return convertView;
    }

    class ViewHolder{

        private RoundAngleImageView inforGutIV;

        public ViewHolder getHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null){
                viewHolder = new ViewHolder();
                viewHolder.inforGutIV = (RoundAngleImageView) view.findViewById(R.id.inforGutIV);
            }
            return viewHolder;
        }
    }

}
