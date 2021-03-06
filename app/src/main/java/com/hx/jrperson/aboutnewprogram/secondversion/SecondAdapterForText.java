package com.hx.jrperson.aboutnewprogram.secondversion;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.bean.entity.ServiceThreeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 * 下单页面具体各个事件和布局的适配器,该适配器大部分内容为第一版内容,各种注释为粗略理解
 */
public class SecondAdapterForText extends BaseAdapter {
    //记录当前内容下的选中个数按钮
    private int before;
    //总价计数器
    private double allPrice;
    //联系上下文
    private Context context;
    //请求数据的实体类
    private List<ServiceThreeEntity.DataMapBean.ServicesBean> list;
    //设置计数器记录当前子类中的详情按钮为第几次点击
    private int numBus=0;
    private ArrayList<Integer> buttonNum=new ArrayList<>();
    private  int changeNum;
    private  int myNum;
    public SecondAdapterForText(Context context) {
        super();
        this.context = context;
    }
    public void addData(List<ServiceThreeEntity.DataMapBean.ServicesBean> list){
        this.list = list;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_issue_ordor, null);
            viewHolder = new ViewHolder();
            viewHolder.addAndSubTextView = (TextView) convertView.findViewById(R.id.addAndSubTextView);
            //  viewHolder.ordor_subjectTV = (TextView) convertView.findViewById(R.id.ordor_subjectTV);
            viewHolder.unitPriceTV = (TextView) convertView.findViewById(R.id.unitPriceTV);
            viewHolder.issue_addIB = (ImageButton) convertView.findViewById(R.id.issue_addIB);//加号
            viewHolder.issue_subIB = (ImageButton) convertView.findViewById(R.id.issue_subIB);//减号
            //////////////////////////////////////////////////////
            viewHolder.classificationName= (TextView) convertView.findViewById(R.id.classificationName);
            viewHolder.serviceGutTVs= (TextView) convertView.findViewById(R.id.subject_nameTVs);
            viewHolder.giveUsDetil= (ImageView) convertView.findViewById(R.id.giveUsDetil);
            viewHolder.subject_nameTVs= (TextView) convertView.findViewById(R.id.subject_nameTVs);
            //////////////////////////////////////
            //与详情一块出现的灰色细线
            viewHolder.giveALine= (TextView) convertView.findViewById(R.id.giveALine);
            viewHolder.serverStandard= (TextView) convertView.findViewById(R.id.serverStandard);
            viewHolder.letMeGoLine= (TextView) convertView.findViewById(R.id.letMeGoLine);
            ////////////////////////////////////////
            viewHolder.giveusdetil= (RelativeLayout) convertView.findViewById(R.id.giveusdetil);
            //下拉按钮弹出部分
            viewHolder.seviceDetil= (RelativeLayout) convertView.findViewById(R.id.seviceDetil);
            viewHolder.subject_nameTVs.setVisibility(View.GONE);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (list != null){
            ServiceThreeEntity.DataMapBean.ServicesBean bean = list.get(position);

            double minPriceD = bean.getPrice_min();
            String minPrice= String.format("%.2f", minPriceD);
//            double maxPriceD = bean.getPrice_max();
//            String maxPrice= String.format("%.2f", maxPriceD);
            viewHolder.unitPriceTV.setText(minPrice + " " + bean.getUnit());
            before = bean.getBeforCount();
            viewHolder.addAndSubTextView.setText(before + "");
            ///////////////////////////////////////////////////////////////////
            viewHolder.classificationName.setText(list.get(position).getService());

            //设置小屏幕时文字大小
            viewHolder.classificationName.setTextSize(12);
            //设置小页面时文字转行
            ViewGroup.LayoutParams params=viewHolder.classificationName.getLayoutParams();
            params.width=200;
            viewHolder.classificationName.setLayoutParams(params);
            viewHolder.letMeGoLine.setVisibility(View.GONE);
            viewHolder.giveALine.setVisibility(View.VISIBLE);



            viewHolder.serverStandard.setTextSize(12);
            viewHolder.serviceGutTVs.setText(list.get(position).getDecription());

            //////////////////////////////

            viewHolder.subject_nameTVs.setText(list.get(position).getDecription());
            viewHolder.subject_nameTVs.setTextSize(12);

            final ViewHolder finalViewHolder = viewHolder;
            //////////////////////////////////////////////////
//            viewHolder.giveusdetil.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (numBus==0){
//                        finalViewHolder.subject_nameTVs.setVisibility(View.VISIBLE);
//                        finalViewHolder.giveUsDetil.setBackgroundResource(R.mipmap.upwardarrow);
//                        ///////////////////////////////////////
//                        int aa=finalViewHolder.subject_nameTVs.getHeight();
//                        ////////////////////////////
//                        numBus=1;
//                        Intent intent=new Intent("com.hx.jrperson.broadcast.MY_BROAD");
//                        intent.putExtra("test","组件高度增加"+position);
//                        context. sendBroadcast(intent);
//
//                        /////////////////////////////////
//                    }else{
//                        int aa=finalViewHolder.subject_nameTVs.getHeight();
//                        finalViewHolder.subject_nameTVs.setVisibility(View.GONE);
//                        finalViewHolder.giveUsDetil.setBackgroundResource(R.mipmap.moretofind);
//                        Intent intent=new Intent("com.hx.jrperson.broadcast.MY_BROAD");
//                        intent.putExtra("test","组件高度减小"+position);
//                        context. sendBroadcast(intent);
//                        numBus=0;
//
//                        /////////////////////////////////
//                    }
//                }
//            });

            //该部分内容为查看详情按钮被点击时,向外传递信息,要求其余部分在外层产生对应的长度变化
            viewHolder.giveUsDetil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numBus==0){
                        int aa=finalViewHolder.subject_nameTVs.getText().length();
                        if (aa%20!=0){
                            changeNum=aa/20+1;
                        }
                        if (aa%20==0){
                            changeNum=aa/20;
                        }
                        finalViewHolder.subject_nameTVs.setVisibility(View.VISIBLE);
                        //灰色细线
                        finalViewHolder.giveALine.setVisibility(View.VISIBLE);
                        finalViewHolder.letMeGoLine.setVisibility(View.VISIBLE);
                        finalViewHolder.giveUsDetil.setBackgroundResource(R.mipmap.upwardarrow);
                        ///////////////////////////////////////
                        numBus=1;

                        Intent intent=new Intent("com.hx.jrperson.broadcast.MY_BROAD");
                        intent.putExtra("test","组件高度增加"+position+"*"+changeNum);
                        context. sendBroadcast(intent);

                        /////////////////////////////////
                    }else{
                        int aa=finalViewHolder.subject_nameTVs.getText().length();
                        if (aa%20!=0){
                            changeNum=aa/20+1;
                        }
                        if (aa%20==0){
                            changeNum=aa/20;
                        }

                        finalViewHolder.subject_nameTVs.setVisibility(View.GONE);
                        //灰色细线
                        finalViewHolder.letMeGoLine.setVisibility(View.GONE);
                        finalViewHolder.giveUsDetil.setBackgroundResource(R.mipmap.moretofind);
                        Intent intent=new Intent("com.hx.jrperson.broadcast.MY_BROAD");
                        intent.putExtra("test","组件高度减小"+position+"*"+changeNum);
                        context. sendBroadcast(intent);
                        numBus=0;
                    }
                }
            });
        }
        final ViewHolder finalViewHolder1 = viewHolder;
        //控制小球滚动距离(动态获取屏幕大小后进行的小球滚动数据判断)
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        final float myWidh= (float)-(width/5.2);
        viewHolder.issue_addIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finalViewHolder1.addAndSubTextView.setVisibility(View.VISIBLE);
                    //小球的滚动动画
                    finalViewHolder1.issue_subIB.setVisibility(View.VISIBLE);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(finalViewHolder1.issue_subIB, "rotation", 0f, -360f);
                    //小球的移动动画
                    float curTranslationX = finalViewHolder1.issue_subIB.getTranslationX();
                    ObjectAnimator animator2 = ObjectAnimator.ofFloat(finalViewHolder1.issue_subIB, "translationX", curTranslationX, myWidh);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.play(animator).with(animator2);
                    animSet.setDuration(1000);
                    animSet.start();
                allPrice = list.get(position).getPrice_min();
                before = list.get(position).getBeforCount();
                if (before < 99){
                    onClickIssueOrdorListener.onClickIssueOrdor(v, allPrice, position, before);
                }
            }
        });
        viewHolder.issue_subIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                before = list.get(position).getBeforCount();
                if (before >= 1){
                    allPrice = -list.get(position).getPrice_min();
                    onClickIssueOrdorListener.onClickIssueOrdor(v, allPrice, position, before);
                }
            }
        });
        return convertView;
    }

    private OnClickIssueOrdorListener onClickIssueOrdorListener;

    public void setOnClickIssueOrdorListener(OnClickIssueOrdorListener onClickIssueOrdorListener){
        this.onClickIssueOrdorListener = onClickIssueOrdorListener;
    }

    public interface OnClickIssueOrdorListener{
        void onClickIssueOrdor(View v, double all, int position, int befor);
    }

    class ViewHolder{

        private TextView addAndSubTextView;
        private TextView ordor_subjectTV, unitPriceTV,giveALine,serverStandard,letMeGoLine;
        private ImageButton issue_addIB, issue_subIB;
        ///////////////////////////
        private TextView classificationName,serviceGutTVs,subject_nameTVs;
        private ImageView giveUsDetil;
        RelativeLayout giveusdetil,seviceDetil;

        public ViewHolder getHolder(View view){
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            return viewHolder;
        }

    }


}

