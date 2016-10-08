package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 地址信息实体类
 * Created by ge on 2016/3/31.
 */
public class AddressEntity implements Serializable {


    /**
     * code : 200
     * time : 1459413891030
     * message : null
     * dataMap : {"postCodes":[{"code":130000,"name":"河北省","sub":[{"code":131000,"name":"廊坊市","sub":[{"code":131024,"name":"香河县","sub":null}]}]},{"code":210000,"name":"辽宁省","sub":[{"code":210200,"name":"大连市","sub":[{"code":210202,"name":"中山区","sub":null},{"code":210204,"name":"沙河口区","sub":null}]},{"code":210300,"name":"鞍山市","sub":[{"code":210321,"name":"台安县","sub":null}]}]},{"code":110000,"name":"北京市","sub":[{"code":110100,"name":"市辖区","sub":[{"code":110111,"name":"房山区","sub":null}]}]}]}
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

    public static class DataMapBean implements Serializable{
        /**
         * code : 130000
         * name : 河北省
         * sub : [{"code":131000,"name":"廊坊市","sub":[{"code":131024,"name":"香河县","sub":null}]}]
         */

        private List<PostCodesBean> postCodes;

        public List<PostCodesBean> getPostCodes() {
            return postCodes;
        }

        public void setPostCodes(List<PostCodesBean> postCodes) {
            this.postCodes = postCodes;
        }

        public static class PostCodesBean implements Serializable{
            private int code;
            private String name;
            /**
             * code : 131000
             * name : 廊坊市
             * sub : [{"code":131024,"name":"香河县","sub":null}]
             */

            private List<SubBean> sub;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<SubBean> getSub() {
                return sub;
            }

            public void setSub(List<SubBean> sub) {
                this.sub = sub;
            }

            public static class SubBean implements Serializable{
                private int code;
                private String name;
                /**
                 * code : 131024
                 * name : 香河县
                 * sub : null
                 */

                private List<SubTwoBean> sub;

                public int getCode() {
                    return code;
                }

                public void setCode(int code) {
                    this.code = code;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public List<SubTwoBean> getSub() {
                    return sub;
                }

                public void setSub(List<SubTwoBean> sub) {
                    this.sub = sub;
                }

                public static class SubTwoBean implements Serializable{
                    private int code;
                    private String name;
                    private Object sub;

                    public int getCode() {
                        return code;
                    }

                    public void setCode(int code) {
                        this.code = code;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public Object getSub() {
                        return sub;
                    }

                    public void setSub(Object sub) {
                        this.sub = sub;
                    }
                }
            }
        }
    }
}
