package com.hx.jrperson.bean.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/5/14.
 */
public class InforGutEntity {


    /**
     * status : 200
     * msgList : [{"msgId":1,"msgPicUrl":"http://www.dognohouse.com/upload/image/2UGRv.jpg"},{"msgId":2,"msgPicUrl":"http://www.dognohouse.com/upload/image/2UGRv.jpg"},{"msgId":3,"msgPicUrl":""},{"msgId":4,"msgPicUrl":"No internet or testing of image can not be accessed."}]
     */

    private int status;
    /**
     * msgId : 1
     * msgPicUrl : http://www.dognohouse.com/upload/image/2UGRv.jpg
     */

    private List<MsgListBean> msgList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MsgListBean> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<MsgListBean> msgList) {
        this.msgList = msgList;
    }

    public static class MsgListBean {
        private int msgId;
        private String msgPicUrl;

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public String getMsgPicUrl() {
            return msgPicUrl;
        }

        public void setMsgPicUrl(String msgPicUrl) {
            this.msgPicUrl = msgPicUrl;
        }
    }
}
