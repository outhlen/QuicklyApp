package com.escort.carriage.android.entity.response.home;

import com.alibaba.fastjson.annotation.JSONField;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

import java.util.List;

public class NewsTitleListBean extends ResponceJsonEntity<List<NewsTitleListBean.DataBean>> {

    private Object orgMessage;


    public Object getOrgMessage() {
        return orgMessage;
    }

    public void setOrgMessage(Object orgMessage) {
        this.orgMessage = orgMessage;
    }



    public static class DataBean {

        private String title;
        private String subTitle;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }
    }
}
