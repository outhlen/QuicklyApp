package com.escort.carriage.android.entity.request.home;

import java.util.List;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-19 17:06
 */
public class AddCircuitEntity {

    private String id;
    private String startProvinceCode;
    private String startCityCode;
    private String startAreaCode;
    private String endProvinceCode;
    private String endCityCode;
    private String endAreaCode;
    private int ststus;//是否启用线路 0否 1是 默认是启动状态
    private List<ListBean> list;

    public String getStartProvinceCode() {
        return startProvinceCode;
    }

    public void setStartProvinceCode(String startProvinceCode) {
        this.startProvinceCode = startProvinceCode;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getStartAreaCode() {
        return startAreaCode;
    }

    public void setStartAreaCode(String startAreaCode) {
        this.startAreaCode = startAreaCode;
    }

    public String getEndProvinceCode() {
        return endProvinceCode;
    }

    public void setEndProvinceCode(String endProvinceCode) {
        this.endProvinceCode = endProvinceCode;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public String getEndAreaCode() {
        return endAreaCode;
    }

    public void setEndAreaCode(String endAreaCode) {
        this.endAreaCode = endAreaCode;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public int getStstus() {
        return ststus;
    }

    public void setStstus(int ststus) {
        this.ststus = ststus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ListBean {
        /**
         * cityCode :
         * cityName :
         */

        private String cityCode;
        private String cityName;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
