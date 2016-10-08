package com.hx.jrperson.aboutnewprogram;

/**
 * Created by Administrator on 2016/7/27.
 * 第二版中主页数据类,目前已被第三版内容覆盖,放置即可
 */
public class HomePageBean {

    private Integer pictureNum;
    private String introduce;

    public HomePageBean(Integer pictureNum, String introduce) {
        this.pictureNum = pictureNum;
        this.introduce = introduce;
    }

    public HomePageBean() {
    }

    public Integer getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(Integer pictureNum) {
        this.pictureNum = pictureNum;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}

