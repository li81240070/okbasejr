package com.hx.jrperson.aboutnewprogram;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.ui.activity.ServiceGutActivity;
import com.squareup.okhttp.MediaType;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/27.
 * 第二次改变的主页面各个大类选项展示的适配器
 * 已经被第三版显示页面所替代,置放即可
 */
public class AdapterForHomePage extends RecyclerView.Adapter<AdapterForHomePage.myViewHolder> {
    private Context context;
    private ArrayList<HomePageBean> data;
    public void setData(ArrayList<HomePageBean> data) {
        this.data = data;
    }

    public AdapterForHomePage(Context context) {
        this.context = context;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recyclerviewinhomepage, parent, false);
        myViewHolder myViewHolder = new myViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        //setdata in recyclerview
        holder.recyclerForPicture.setImageResource(data.get(position).getPictureNum());

        holder.recyclerForIntroduce.setText(data.get(position).getIntroduce());
        //calculate the detil for view
        ViewGroup.LayoutParams params = holder.relativeLayoutInHomePageDetil.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int heightForWin = wm.getDefaultDisplay().getHeight();
        int width = wm.getDefaultDisplay().getWidth();
        params.width = width / 4;
        params.height = (int) (heightForWin / ((1.43 + 7.75 + 1 + 7.75) / 7.75) / 3) - 10;
        holder.relativeLayoutInHomePageDetil.setLayoutParams(params);

        //give every item clicklistener
        holder.relativeLayoutInHomePageDetil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //old version
                switch (position) {
                    case 0:
                        Intent intent = new Intent(context, ServiceGutActivity.class);
                        intent.putExtra("title", "我家装修");
                        intent.putExtra("parentCode", "6001");
                        context.startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(context, ServiceGutActivity.class);
                        intent1.putExtra("title", "水维修");
                        intent1.putExtra("parentCode", "1001");
                        context.startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(context, ServiceGutActivity.class);
                        intent2.putExtra("title", "电维修");
                        intent2.putExtra("parentCode", "2001");
                        context.startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, ServiceGutActivity.class);
                        intent3.putExtra("title", "装修监控");
                        intent3.putExtra("parentCode", "5001");
                        context.startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, ServiceGutActivity.class);
                        intent4.putExtra("title", "居家小修");
                        intent4.putExtra("parentCode", "3001");
                        context.startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(context, ServiceGutActivity.class);
                        intent5.putExtra("title", "居家安装");
                        intent5.putExtra("parentCode", "4001");
                        context.startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(context, ServiceGutActivity.class);
                        intent6.putExtra("title", "货车力工");
                        intent6.putExtra("parentCode", "8001");
                        context.startActivity(intent6);
                        break;
                    case 7:

                        Intent intent7 = new Intent(context, ServiceGutActivity.class);
                        intent7.putExtra("title", "家电清洗");
                        intent7.putExtra("parentCode", "7001");
                        context.startActivity(intent7);
                        break;
                    case 8:
                        clickCallPhone();
                        break;
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return data != null && data.size() > 0 ? data.size() : 0;
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        TextView recyclerForIntroduce;
        ImageView recyclerForPicture;
        LinearLayout relativeLayoutInHomePageDetil,lineForHome;
        TextView lineForHomePageDetil;


        public myViewHolder(View itemView) {
            super(itemView);
            recyclerForIntroduce = (TextView) itemView.findViewById(R.id.recyclerForIntroduce);
            recyclerForPicture = (ImageView) itemView.findViewById(R.id.recyclerForPicture);
            relativeLayoutInHomePageDetil = (LinearLayout) itemView.findViewById(R.id.relativeLayoutInHomePageDetil);
            lineForHomePageDetil = (TextView) itemView.findViewById(R.id.lineForHomePageDetil);
            lineForHome = (LinearLayout) itemView.findViewById(R.id.lineForHome);

        }
    }

    //给匠人打电话
    private void clickCallPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "041184542809"));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(callIntent);
    }

    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


}

