package com.hx.jrperson.bean.entity;

/**
 * Created by ShangHuaQing on 2016/4/4.
 */
public class CancleBecauseEntity {

    private String code;
    private String content;
    private int isClick = 1;

    public int getIsClick() {
        return isClick;
    }

    public void setIsClick(int isClick) {
        this.isClick = isClick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
