package com.escort.carriage.android.entity.response.home;

import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

import java.util.List;

public class ShengListBean extends ResponceJsonEntity<List<ShengListBean.DataBean>> {
    /**
     * data : [{"id":"1","provinceCode":"110000","province":"北京市"},{"id":"2","provinceCode":"120000","province":"天津市"},{"id":"3","provinceCode":"130000","province":"河北省"},{"id":"4","provinceCode":"140000","province":"山西省"},{"id":"5","provinceCode":"150000","province":"内蒙古自治区"},{"id":"6","provinceCode":"210000","province":"辽宁省"},{"id":"7","provinceCode":"220000","province":"吉林省"},{"id":"8","provinceCode":"230000","province":"黑龙江省"},{"id":"9","provinceCode":"310000","province":"上海市"},{"id":"10","provinceCode":"320000","province":"江苏省"},{"id":"11","provinceCode":"330000","province":"浙江省"},{"id":"12","provinceCode":"340000","province":"安徽省"},{"id":"13","provinceCode":"350000","province":"福建省"},{"id":"14","provinceCode":"360000","province":"江西省"},{"id":"15","provinceCode":"370000","province":"山东省"},{"id":"16","provinceCode":"410000","province":"河南省"},{"id":"17","provinceCode":"420000","province":"湖北省"},{"id":"18","provinceCode":"430000","province":"湖南省"},{"id":"19","provinceCode":"440000","province":"广东省"},{"id":"20","provinceCode":"450000","province":"广西壮族自治区"},{"id":"21","provinceCode":"460000","province":"海南省"},{"id":"22","provinceCode":"500000","province":"重庆市"},{"id":"23","provinceCode":"510000","province":"四川省"},{"id":"24","provinceCode":"520000","province":"贵州省"},{"id":"25","provinceCode":"530000","province":"云南省"},{"id":"26","provinceCode":"540000","province":"西藏自治区"},{"id":"27","provinceCode":"610000","province":"陕西省"},{"id":"28","provinceCode":"620000","province":"甘肃省"},{"id":"29","provinceCode":"630000","province":"青海省"},{"id":"30","provinceCode":"640000","province":"宁夏回族自治区"},{"id":"31","provinceCode":"650000","province":"新疆维吾尔自治区"},{"id":"32","provinceCode":"710000","province":"台湾省"},{"id":"33","provinceCode":"810000","province":"香港特别行政区"},{"id":"34","provinceCode":"820000","province":"澳门特别行政区"}]
     * code : null
     * message : null
     * timestamp : 62
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
         * id : 1
         * provinceCode : 110000
         * province : 北京市
         */

        private String id;
        private String provinceCode;
        private String province;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }
    }
}
