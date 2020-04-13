package com.androidybp.basics.entity;

public class UserEntity {
    /**
     * token : 787e6ef0e5914841be260ef8aa0cce47
     * isSetPwd : 1
     * isSetPhone : 1
     * phone : 18678392031
     * selfRecommend : 99KeAo9KB
     * isSetTradePassword : 0
     */

    private String token;
    private int isSetPwd;
    private int isSetPhone;
    private String phone;
    private String selfRecommend;
    private int isSetTradePassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIsSetPwd() {
        return isSetPwd;
    }

    public void setIsSetPwd(int isSetPwd) {
        this.isSetPwd = isSetPwd;
    }

    public int getIsSetPhone() {
        return isSetPhone;
    }

    public void setIsSetPhone(int isSetPhone) {
        this.isSetPhone = isSetPhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSelfRecommend() {
        return selfRecommend;
    }

    public void setSelfRecommend(String selfRecommend) {
        this.selfRecommend = selfRecommend;
    }

    public int getIsSetTradePassword() {
        return isSetTradePassword;
    }

    public void setIsSetTradePassword(int isSetTradePassword) {
        this.isSetTradePassword = isSetTradePassword;
    }
}
