package com.hx.jrperson.bean.entity;

/**
 * 匠人位置 想x y
 * Created by ge on 2016/4/11.
 */
public class WorkerLocateEntity {


    /**
     * worker_no : 201502031102
     * y : 121.529606
     * x : 38.867306
     */

    private String worker_no;
    private double y;
    private double x;

    public String getWorker_no() {
        return worker_no;
    }

    public void setWorker_no(String worker_no) {
        this.worker_no = worker_no;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
