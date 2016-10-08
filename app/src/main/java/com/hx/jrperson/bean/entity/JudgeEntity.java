package com.hx.jrperson.bean.entity;



/**
 * 评价订单标签是实体类
 * Created by ge on 2016/4/5.
 */
public class JudgeEntity {

    private String[] code;

    private String[] gut;

    private int isClick = 0;

    public int getIsClick() {
        return isClick;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    public String[] getCode() {
        return code;
    }

    public void setCode(String[] code) {
        this.code = code;
    }

    public String[] getGut() {
        return gut;
    }

    public void setGut(String[] gut) {
        this.gut = gut;
    }
}
