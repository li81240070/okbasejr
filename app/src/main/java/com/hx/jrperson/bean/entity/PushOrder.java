package com.hx.jrperson.bean.entity;

import java.io.Serializable;

/**
 * Created by ZY on 2016/4/4.
 */
public class PushOrder implements Serializable{

    /**
     * messageId : 1462087200372375351
     * typeId : 1
     * orderStatus : 0
     * orderId : 109
     */

    private String messageId;
    private String typeId;
    private String orderStatus;
    private String orderId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
