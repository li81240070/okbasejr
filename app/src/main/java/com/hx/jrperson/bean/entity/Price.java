package com.hx.jrperson.bean.entity;

/**
 * 项目价格列表实体类
 * Created by ge on 2016/3/7.
 */
public class Price {

    int price;
    String PriceName;
    int befor;


    public Price(int price, String priceName, int befor) {
        this.price = price;
        PriceName = priceName;
        this.befor = befor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPriceName() {
        return PriceName;
    }

    public void setPriceName(String priceName) {
        PriceName = priceName;
    }

    public int getBefor() {
        return befor;
    }

    public void setBefor(int befor) {
        this.befor = befor;
    }
}
