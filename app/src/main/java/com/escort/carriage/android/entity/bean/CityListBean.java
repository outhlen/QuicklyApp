package com.escort.carriage.android.entity.bean;

import java.util.List;

public class CityListBean {

    private String status;
    private List<CityBean> districts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CityBean> getDistricts() {
        return districts;
    }

    public void setDistricts(List<CityBean> districts) {
        this.districts = districts;
    }

    public class CityBean{
        private String citycode;
        private String adcode;
        private String name;
        private String center;

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getAdcode() {
            return adcode;
        }

        public void setAdcode(String adcode) {
            this.adcode = adcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCenter() {
            return center;
        }

        public void setCenter(String center) {
            this.center = center;
        }
    }



}
