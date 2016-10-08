package com.hx.jrperson.bean.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 省市区数据实体类
 * Created by ge on 2016/3/17.
 */
public class ProvideCityAreaEntity implements Serializable{

    private String code;
    private String name;
    /**
     * code : 110100
     * name : 大连市
     * children : [{"code":"110101","name":"沙河口区"},{"code":"110102","name":"高新园区"},{"code":"110102","name":"甘井子区"}]
     */

    private List<CityEntity> city;

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<CityEntity> city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<CityEntity> getChildren() {
        return city;
    }

    public static class CityEntity implements Serializable{
        private String code;
        private String name;
        /**
         * code : 110101
         * name : 沙河口区
         */

        private List<AlearEntity> alear;

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChildren(List<AlearEntity> alear) {
            this.alear = alear;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public List<AlearEntity> getChildren() {
            return alear;
        }

        public static class AlearEntity implements Serializable{
            private String code;
            private String name;

            public void setCode(String code) {
                this.code = code;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public String getName() {
                return name;
            }
        }
    }
}
