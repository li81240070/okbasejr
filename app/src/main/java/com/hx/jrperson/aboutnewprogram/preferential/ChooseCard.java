package com.hx.jrperson.aboutnewprogram.preferential;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/29.
 * 选择优惠券数据向外传递的接口
 */
public interface ChooseCard {
    public void chooseOne(int position, ArrayList<PostCardClass.DataMapBean.CouponsBean> data);
}
