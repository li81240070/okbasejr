package com.hx.jrperson.aboutnewprogram.thirdversion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.jrperson.R;
import com.hx.jrperson.aboutnewprogram.AdapterForHomePage;
import com.hx.jrperson.aboutnewprogram.AdapterForMainViewPager;
import com.hx.jrperson.aboutnewprogram.HomePageBean;
import com.hx.jrperson.aboutnewprogram.thirdversion.thirdhomepage.TodaySurpriseAdapter;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/9/23.
 * 主界面进入后的第一个碎片布局,里面包括主页面的所有内容,嵌入在mainactivity中
 */
public class MainFirstPageFragment extends Fragment {
    //联系文件
    private Context context;
    //在主碎片下的各个分支碎片
    private ViewPager mainViewPager;
    //分支碎片适配器
    private AdapterForMainViewPager adapter2;
    //各个分支碎片的填充数据
    private ArrayList dataForViewPager;
    //判断用户是否触摸
    private boolean userTouch = false;
    //用来销毁线程的
    private boolean threadAlive = true;
    //刷新UI的
    private Handler handler2;
    //各个分类入口展示位
    private RecyclerView recyclerInMain;
    private AdapterForHomePage adapter;
    private ArrayList<HomePageBean> data;
    //实现今日特价的数据显示
    private RecyclerView todaySurprise;
    private TodaySurpriseAdapter adapterForToday;
    //今日特价倒计时
    private TextView hourContrl, minutesContrl, secondContrl;
    private CountDownTimer timer;
    //当前页面的刷新计数器
    private int getNum = 0, getNum2 = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mainfirstpage, null);
        mainViewPager = (ViewPager) view.findViewById(R.id.mainViewPager);
        recyclerInMain = (RecyclerView) view.findViewById(R.id.recyclerInMain);
        //加入各个项目的入口信息
        //暂时服务器未提供分支数据,所以在显示页面手动输入固定
        adapter = new AdapterForHomePage(getContext());
        data = new ArrayList<>();
        data.add(new HomePageBean(R.mipmap.myhome3, "我家装修"));
        data.add(new HomePageBean(R.mipmap.water3, "水维修"));
        data.add(new HomePageBean(R.mipmap.electricity3, "电维修"));
        data.add(new HomePageBean(R.mipmap.monitoring3, "装修监控"));
        data.add(new HomePageBean(R.mipmap.maintenance3, "居家小修"));
        data.add(new HomePageBean(R.mipmap.installation3, "居家安装"));
        data.add(new HomePageBean(R.mipmap.van3, "货车力工"));
        data.add(new HomePageBean(R.mipmap.appliance3, "家电清洗"));
        // data.add(new HomePageBean(R.mipmap.service, "联系客服"));
        adapter.setData(data);
        //对首屏碎片的recyclerview展示部分进行限定
        recyclerInMain.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerInMain.setAdapter(adapter);
        //设置本地轮播图相关逻辑
        dataForViewPager = new ArrayList();
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(R.mipmap.firstpicture);
        ImageView imageView2 = new ImageView(getActivity());
        imageView2.setBackgroundResource(R.mipmap.thirdpicture);
        ImageView imageView3 = new ImageView(getActivity());
        imageView3.setBackgroundResource(R.mipmap.secondpicture);
        dataForViewPager.add(imageView);
        dataForViewPager.add(imageView2);
        dataForViewPager.add(imageView3);
        adapter2 = new AdapterForMainViewPager();
        adapter2.setImageViewList(dataForViewPager);
        mainViewPager.setAdapter(adapter2);
        //触碰时的刷新逻辑
        mainViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当用户触摸轮播图时
                        userTouch = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        //当用户没有触摸轮播图时
                        userTouch = false;
                        break;
                }
                return false;
            }
        });
        if (getNum == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (threadAlive) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!userTouch) {
                            handler2.sendEmptyMessage(1);
                        }
                    }
                }
            }).start();
            getNum++;
        }
        handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //将viewPage刷新到下一页
                if (msg.what == 1) {
                    mainViewPager.setCurrentItem(mainViewPager.getCurrentItem() + 1);
                }
                return false;
            }
        });
        //实现今日特价展示功能
        todaySurprise = (RecyclerView) view.findViewById(R.id.todaySurprise);
        adapterForToday = new TodaySurpriseAdapter(getContext());
        ArrayList datas = new ArrayList<>();
        //今日特价的临时数据,日后需要替换
        for (int i = 0; i < 3; i++) {
            datas.add("666666" + i);
        }
        //添加数据
        adapterForToday.setData(datas);
        //今日特价recyclerview的格式规范,暂时定为3个
        todaySurprise.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        todaySurprise.setAdapter(adapterForToday);
        //绑定倒计时组件
        hourContrl = (TextView) view.findViewById(R.id.hourContrl);
        minutesContrl = (TextView) view.findViewById(R.id.minutesContrl);
        secondContrl = (TextView) view.findViewById(R.id.secondContrl);
        //开始今日秒杀的倒计时
        letCount();
        //展示页失去焦点,防止展示页更换时产生整体视角切换到展示页位置
        mainViewPager.setFocusable(false);
        //实现搜索功能
        SharedPreferences sp = getActivity().getSharedPreferences("search", getActivity().MODE_PRIVATE);
        //向硬盘中存储,需要获得editor对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("1001", "暂时无数据");
        editor.commit();
        return view;
    }

    //倒计时相关方法
    private void letCount() {
        if (getNum2 == 0) {
            //临时数据
            final int[] howManyL = {7200000};
            timer = new CountDownTimer(howManyL[0], 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    howManyL[0] = howManyL[0] - 1000;
                    String hours = String.valueOf(howManyL[0] / 3600000);
                    hourContrl.setText(hours);
                    String mins = String.valueOf(howManyL[0] % 3600000 / 60000);
                    minutesContrl.setText(mins);
                    String sec = String.valueOf(howManyL[0] % 60000 / 1000);
                    secondContrl.setText(sec);
                }

                @Override
                public void onFinish() {
                    timer.cancel();
                }
            }.start();
        }
        getNum2++;
    }
}
