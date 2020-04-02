package com.escort.carriage.android.entity.response.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

import java.util.List;

public class QuListBean extends ResponceJsonEntity<List<QuListBean.DataBean>> {
    /**
     * data : [{"id":"1348","areaCode":"370101","area":"市辖区","cityCode":"370100"},{"id":"1349","areaCode":"370102","area":"历下区","cityCode":"370100"},{"id":"1350","areaCode":"370103","area":"市中区","cityCode":"370100"},{"id":"1351","areaCode":"370104","area":"槐荫区","cityCode":"370100"},{"id":"1352","areaCode":"370105","area":"天桥区","cityCode":"370100"},{"id":"1353","areaCode":"370112","area":"历城区","cityCode":"370100"},{"id":"1354","areaCode":"370113","area":"长清区","cityCode":"370100"},{"id":"1355","areaCode":"370124","area":"平阴县","cityCode":"370100"},{"id":"1356","areaCode":"370125","area":"济阳县","cityCode":"370100"},{"id":"1357","areaCode":"370126","area":"商河县","cityCode":"370100"},{"id":"1358","areaCode":"370181","area":"章丘市","cityCode":"370100"}]
     * code : null
     * message : null
     * timestamp : 78
     * orgMessage : null
     */

    @JSONField(name = "code")
    private Object codeX;
    @JSONField(name = "message")
    private Object messageX;
    @JSONField(name = "timestamp")
    private String timestampX;
    private Object orgMessage;

    public Object getCodeX() {
        return codeX;
    }

    public void setCodeX(Object codeX) {
        this.codeX = codeX;
    }

    public Object getMessageX() {
        return messageX;
    }

    public void setMessageX(Object messageX) {
        this.messageX = messageX;
    }

    public String getTimestampX() {
        return timestampX;
    }

    public void setTimestampX(String timestampX) {
        this.timestampX = timestampX;
    }

    public Object getOrgMessage() {
        return orgMessage;
    }

    public void setOrgMessage(Object orgMessage) {
        this.orgMessage = orgMessage;
    }


    public static class DataBean {
        /**
         * id : 1348
         * areaCode : 370101
         * area : 市辖区
         * cityCode : 370100
         */

        private String id;
        private String areaCode;
        private String area;
        private String cityCode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
    }
}
