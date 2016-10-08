package com.hx.jrperson.aboutnewprogram.thirdversion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hx.jrperson.R;
import com.hx.jrperson.aboutnewprogram.thirdversion.order.AllOrderFragment;
import com.hx.jrperson.aboutnewprogram.thirdversion.order.EvaluationOrderPage;
import com.hx.jrperson.aboutnewprogram.thirdversion.order.FinishOrderFragment;
import com.hx.jrperson.aboutnewprogram.thirdversion.order.LoginOrderPage;
import com.hx.jrperson.aboutnewprogram.thirdversion.order.WaitOrderPage;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 * 主页面中切换的我的订单碎片部分,嵌入在mainactivity中,内容显示具体内容下发到各个子碎片中进行处理
 */
public class MainOrderFragment extends Fragment {
    //联系文件
    private Context context;
    //用于切换的tablayout
    private TabLayout tabInMainOrder;
    //用于展示子碎片文件的viewpager
    private ViewPager viewpagerInMainOrder;
    //处理子碎片文件显示的适配器文件
    private MainAdapter adapter;
    //数据类
    private ArrayList fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.mainorderpage, null);
        tabInMainOrder = (TabLayout) view.findViewById(R.id.tabInMainOrder);
        viewpagerInMainOrder = (ViewPager) view.findViewById(R.id.viewpagerInMainOrder);
        adapter = new MainAdapter(getChildFragmentManager());
        fragments = new ArrayList<>();
        //各个碎片文件中的内容发放到碎片文件中进行处理
        //全部订单碎片
        fragments.add(new AllOrderFragment());
        //预约中订单碎片
        fragments.add(new LoginOrderPage());
        //待付款订单碎片
        fragments.add(new WaitOrderPage());
        //待评价订单碎片
        fragments.add(new EvaluationOrderPage());
        //已完成订单碎片
        fragments.add(new FinishOrderFragment());
        String[] titles = {"全部", "预约中", "待付款", "待评价", "已完成"};
        adapter.setTitles(titles);
        adapter.setFragments(fragments);
        viewpagerInMainOrder.setAdapter(adapter);
        //给tabLayout设置viewpager
        tabInMainOrder.setupWithViewPager(viewpagerInMainOrder);
        //设置引导线颜色
        tabInMainOrder.setSelectedTabIndicatorColor(0xff3399ff);
        //改变选中字体和未选中字体颜色
        tabInMainOrder.setTabTextColors(0xff333333, 0xff3399ff);
        return view;
    }
}
