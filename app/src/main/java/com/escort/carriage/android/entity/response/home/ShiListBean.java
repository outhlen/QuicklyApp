package com.escort.carriage.android.entity.response.home;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

import java.util.List;

public class ShiListBean extends ResponceJsonEntity<List<ShiListBean.DataBean>> {
    /**
     * data : [{"id":"136","cityCode":"370100","city":"济南市","provinceCode":"370000"},{"id":"137","cityCode":"370200","city":"青岛市","provinceCode":"370000"},{"id":"138","cityCode":"370300","city":"淄博市","provinceCode":"370000"},{"id":"139","cityCode":"370400","city":"枣庄市","provinceCode":"370000"},{"id":"140","cityCode":"370500","city":"东营市","provinceCode":"370000"},{"id":"141","cityCode":"370600","city":"烟台市","provinceCode":"370000"},{"id":"142","cityCode":"370700","city":"潍坊市","provinceCode":"370000"},{"id":"143","cityCode":"370800","city":"济宁市","provinceCode":"370000"},{"id":"144","cityCode":"370900","city":"泰安市","provinceCode":"370000"},{"id":"145","cityCode":"371000","city":"威海市","provinceCode":"370000"},{"id":"146","cityCode":"371100","city":"日照市","provinceCode":"370000"},{"id":"147","cityCode":"371200","city":"莱芜市","provinceCode":"370000"},{"id":"148","cityCode":"371300","city":"临沂市","provinceCode":"370000"},{"id":"149","cityCode":"371400","city":"德州市","provinceCode":"370000"},{"id":"150","cityCode":"371500","city":"聊城市","provinceCode":"370000"},{"id":"151","cityCode":"371600","city":"滨州市","provinceCode":"370000"},{"id":"152","cityCode":"371700","city":"荷泽市","provinceCode":"370000"}]
     * code : null
     * message : null
     * timestamp : 32
     * orgMessage : null
     */

    private Object orgMessage;


    public Object getOrgMessage() {
        return orgMessage;
    }

    public void setOrgMessage(Object orgMessage) {
        this.orgMessage = orgMessage;
    }


    public static class DataBean {
        /**
         * id : 136
         * cityCode : 370100
         * city : 济南市
         * provinceCode : 370000
         */

        private String id;
        private String cityCode;
        private String city;
        private String provinceCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }
    }
}
