package com.hx.jrperson.bean.entity;

import java.util.List;

/**
 * 消息列表数据实体类
 * Created by ge on 2016/5/17.
 */
public class NewInforEntity {


    /**
     * code : 200
     * time : 1463465685117
     * message : null
     * dataMap : {"activitylist":[{"update_id":0,"activity_id":2,"create_id":0,"update_time":1464503983000,"status":true,"activity_picture_url":"http://img15.3lian.com/2015/h1/275/127.jpg","create_time":1463380777000,"activity_content":null,"activity_describe":"呵呵","activity_type":true}]}
     */

    private int code;
    private long time;
    private Object message;
    private DataMapBean dataMap;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public DataMapBean getDataMap() {
        return dataMap;
    }

    public void setDataMap(DataMapBean dataMap) {
        this.dataMap = dataMap;
    }

    public static class DataMapBean {
        /**
         * update_id : 0
         * activity_id : 2
         * create_id : 0
         * update_time : 1464503983000
         * status : true
         * activity_picture_url : http://img15.3lian.com/2015/h1/275/127.jpg
         * create_time : 1463380777000
         * activity_content : null
         * activity_describe : 呵呵
         * activity_type : true
         */

        private List<ActivitylistBean> activitylist;

        public List<ActivitylistBean> getActivitylist() {
            return activitylist;
        }

        public void setActivitylist(List<ActivitylistBean> activitylist) {
            this.activitylist = activitylist;
        }

        public static class ActivitylistBean {
            private int update_id;
            private int activity_id;
            private int create_id;
            private long update_time;
            private boolean status;
            private String activity_picture_url;
            private long create_time;
            private Object activity_content;
            private String activity_describe;
            private boolean activity_type;

            public int getUpdate_id() {
                return update_id;
            }

            public void setUpdate_id(int update_id) {
                this.update_id = update_id;
            }

            public int getActivity_id() {
                return activity_id;
            }

            public void setActivity_id(int activity_id) {
                this.activity_id = activity_id;
            }

            public int getCreate_id() {
                return create_id;
            }

            public void setCreate_id(int create_id) {
                this.create_id = create_id;
            }

            public long getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(long update_time) {
                this.update_time = update_time;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public String getActivity_picture_url() {
                return activity_picture_url;
            }

            public void setActivity_picture_url(String activity_picture_url) {
                this.activity_picture_url = activity_picture_url;
            }

            public long getCreate_time() {
                return create_time;
            }

            public void setCreate_time(long create_time) {
                this.create_time = create_time;
            }

            public Object getActivity_content() {
                return activity_content;
            }

            public void setActivity_content(Object activity_content) {
                this.activity_content = activity_content;
            }

            public String getActivity_describe() {
                return activity_describe;
            }

            public void setActivity_describe(String activity_describe) {
                this.activity_describe = activity_describe;
            }

            public boolean isActivity_type() {
                return activity_type;
            }

            public void setActivity_type(boolean activity_type) {
                this.activity_type = activity_type;
            }
        }
    }
}
