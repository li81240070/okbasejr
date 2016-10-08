package com.hx.jrperson.aboutnewprogram.mywollet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hx.jrperson.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/22.
 * 查询余额功能中listview的适配器文件
 */
public class RecordAdapter extends BaseAdapter {
    //联系文件
    private Context context;
    //余额查询相关数据类
    private ArrayList<RecordDetilClass.DataMapBean.DataBean> data;
    //对外提供联系文件
    public RecordAdapter(Context context) {
        this.context = context;
    }
    //对外提供数据文件
    public void setData(ArrayList<RecordDetilClass.DataMapBean.DataBean> data) {
        this.data = data;
    }
    //获取当前数据的长度
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
    //进行视图绑定
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder viewholder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.recorddetilpage,parent,false);
            viewholder=new viewholder(convertView);
            convertView.setTag(viewholder);
        }else{

            viewholder= (RecordAdapter.viewholder) convertView.getTag();
        }
        //listview中依据数据的各种视图绑定
        //交易名称的判断
        if (data.get(position).getTrade_type()==1){
            viewholder.recordName.setText("充值");
        }else if (data.get(position).getTrade_type()==2){
            viewholder.recordName.setText("消费");
        }
        viewholder.recordDate.setText(TimeStamp2Date(String.valueOf(data.get(position).getCreate_time()),"yyyy-MM-dd HH:mm:ss"));
        //充值具体信息
        if (data.get(position).getContent().equals("充值失败")){
            viewholder.recordContent.setText("充值失败");
        }else{
            viewholder.recordContent.setText("+"+data.get(position).getAmount());
        }
        return convertView;
    }
    //内部类,用来保存item中的各个组件
    public class viewholder {
        TextView recordContent,recordTime,recordDate,recordName;
        public viewholder(View itemView) {
            //充值名称
            recordName= (TextView) itemView.findViewById(R.id.recordName);
            //充值日期
            recordDate= (TextView) itemView.findViewById(R.id.recordDate);
            //充值时间
            recordTime= (TextView) itemView.findViewById(R.id.recordTime);
            //充值具体内容
            recordContent= (TextView) itemView.findViewById(R.id.recordContent);
        }
    }
    //将数据返回的日期格式转成正常的日期格式的方法
    public String TimeStamp2Date(String timestampString, String formats){
        Long timestamp = Long.parseLong(timestampString);
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }
}
