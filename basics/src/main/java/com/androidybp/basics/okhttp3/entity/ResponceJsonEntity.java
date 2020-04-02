package com.androidybp.basics.okhttp3.entity;

/**
 * 全部解析后的bean对象
 */

public class ResponceJsonEntity<T> {
    public boolean mark;// 接入状态，当请求接口成功后true
    public boolean success;// 处理结果，当程序成功执行后true，否则false，(通过该字段判断结果)
    public String code; // 错误编码
    public String message;  // 错误信息
    public long timestamp;  // 接口调用消耗时间（毫秒）
    public T data;


    public ResponceJsonEntity() {
    }


    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
