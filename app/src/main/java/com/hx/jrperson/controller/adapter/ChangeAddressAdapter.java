package com.hx.jrperson.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.AddressEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择地址省列表适配器
 * Created by Administrator on 2016/3/17.
 */
public class ChangeAddressAdapter extends BaseAdapter {

    private Context context;
    private int what;
    private List<AddressEntity.DataMapBean.PostCodesBean> list = new ArrayList<>();
    private List<AddressEntity.DataMapBean.PostCodesBean.SubBean> cityList = new ArrayList<>();
    private List<AddressEntity.DataMapBean.PostCodesBean.SubBean.SubTwoBean> alearList = new ArrayList<>();

    public ChangeAddressAdapter(Context context){
        this.context = context;
    }

    public void addData(List<AddressEntity.DataMapBean.PostCodesBean> list, int what){
        this.list = list;
        this.what = what;
        notifyDataSetChanged();
    }
    public void addDataCity(List<AddressEntity.DataMapBean.PostCodesBean.SubBean> cityList, int what){
        this.cityList = cityList;
        this.what = what;
        notifyDataSetChanged();
    }
    public void addDataAlear(List<AddressEntity.DataMapBean.PostCodesBean.SubBean.SubTwoBean> alearList, int what){
        this.alearList = alearList;
        this.what = what;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (what == 1){
            return list != null && list.size() > 0 ? list.size() : 0;
        }else if (what == 2){
            return cityList != null && cityList.size() > 0 ? cityList.size() : 0;
        }else{
            return alearList != null && alearList.size() > 0 ? alearList.size() : 0;
        }
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_change_address, null);
            viewHolder = new ViewHolder();
            viewHolder.provinceTV = (TextView) convertView.findViewById(R.id.provinceTV);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (what == 1){//省
            AddressEntity.DataMapBean.PostCodesBean entity = list.get(position);
            viewHolder.provinceTV.setText(entity.getName());
        }else if (what == 2){//市
            AddressEntity.DataMapBean.PostCodesBean.SubBean entity = cityList.get(position);
            viewHolder.provinceTV.setText(entity.getName());
        }else if (what == 3){//区
            AddressEntity.DataMapBean.PostCodesBean.SubBean.SubTwoBean entity = alearList.get(position);
            viewHolder.provinceTV.setText(entity.getName());
        }
        return convertView;
    }

    class ViewHolder{

        private TextView provinceTV;

        public ViewHolder getHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (viewHolder == null){
                viewHolder = new ViewHolder();
                viewHolder.provinceTV = (TextView) view.findViewById(R.id.provinceTV);
            }
            return viewHolder;
        }
    }

//    public ChangeAddressAdapter(Context context, List list, int what) {
//        super(context, list, what);
//        this.context = context;
//        this.what = what;
//    }
//
//    @Override
//    public int getItemResource() {
//        return R.layout.adapter_change_address;
//    }
//
//    @Override
//    public View getItemView(int position, View convertView, BaseViewHolder holder) {
//        TextView provinceTV = (TextView) holder.getView(R.id.provinceTV);
//        if (what == 1){//省
//            ProvideCityAreaEntity entity = (ProvideCityAreaEntity) getItem(position);
//            provinceTV.setText(entity.getName());
//        }else if (what == 2){//市
//            ProvideCityAreaEntity.CityEntity cityEntity = (ProvideCityAreaEntity.CityEntity) getItem(position);
//            provinceTV.setText(cityEntity.getName());
//        }else if (what == 3){//区
//            ProvideCityAreaEntity.CityEntity.AlearEntity alearEntity = (ProvideCityAreaEntity.CityEntity.AlearEntity) getItem(position);
//            provinceTV.setText(alearEntity.getName());
//        }
//        return convertView;
//    }
}
