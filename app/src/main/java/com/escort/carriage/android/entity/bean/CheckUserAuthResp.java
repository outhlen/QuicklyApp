package com.escort.carriage.android.entity.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

public class CheckUserAuthResp extends ResponceJsonEntity<String> {
    /**
     * data : 2
     * code : null
     * message : null
     * orgMessage : null
     */

    @JSONField(name = "code")
    private Object codeX;
    @JSONField(name = "message")
    private Object messageX;
    private Object orgMessage;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

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

    public Object getOrgMessage() {
        return orgMessage;
    }

    public void setOrgMessage(Object orgMessage) {
        this.orgMessage = orgMessage;
    }
}
