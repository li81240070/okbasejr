package com.hx.jrperson.aboutnewprogram.mywollet;

/**
 * Created by Administrator on 2016/9/22.
 * 为钱包内选择优惠选项时传递相关信息的相关接口,在钱包内点击优惠的各个选项时启用,可向外传递
 * 当前点击的具体位置信息和将该位置的各种属性向外传递
 */
public interface ChangeMyColor {
    public void clickMe(int position, InputMoneyAdapter.myViewHolder viewHolder);
}
