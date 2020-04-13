package com.androidybp.basics.cache.db.model;


import com.androidybp.basics.configuration.BaseProjectConfiguration;

/**
 * 本地缓存使用对象
 * V2.2.40 因需求改动添加一个  loginPassword 字段 记录用户输入的密码  2018-11-22 yangbp
 */
public class CacheDataEntity {
    public int phoneWidth;
    public int phoneHeight;
    public String versionName;
    public int versionCode; //当前程序的版本号
    public boolean firstOpen = true; //用于记录是否是第一次打开APP  false 不是第一次打开APP  默认为true
    public int dbVersion = BaseProjectConfiguration.DB_VERSION; //当前本地数据的版本号
    public String jsessionID;//当前用户的 jsessionID  后台用于验证用户超时判断
    public String token;//登录用户的令牌  用于自动登录判断使用
    public boolean selfmotionLogin; //是否自动的标识
    public String loginUserId;//登录用户的userId
    public String loginMobile;//登录的手机号码
    public String loginPassword;//登录用户的密码（这里都是密文）
    public String equipmentid;//安装手机的  唯一标识
    public int userLoginType;//用户上一次登录的 标识   0 -- 用户名密码   1 -- 手机验证码登录
    public String wyImToken;//网易IMtoken保存字段
    public boolean isNotification = true;//是否开启推送

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    /**
     * 配置信息状态
     * <p>
     * 1、变量为0   清空配置表里面所有数据，重新执行保存数据方法  把变量执为 1
     * 2、变量为1 什么都不干
     * 3、变量为 -1   执行更改接口  执行完成后 把变量执为1
     */
    public int comfigurationState;
    public boolean mCalloK;//将用户的手机号保存到了后台

    public int getPhoneWidth() {
        return phoneWidth;
    }

    public void setPhoneWidth(int phoneWidth) {
        this.phoneWidth = phoneWidth;
    }

    public int getPhoneHeight() {
        return phoneHeight;
    }

    public void setPhoneHeight(int phoneHeight) {
        this.phoneHeight = phoneHeight;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isFirstOpen() {
        return firstOpen;
    }

    public void setFirstOpen(boolean firstOpen) {
        this.firstOpen = firstOpen;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(int dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getJsessionID() {
        return jsessionID;
    }

    public void setJsessionID(String jsessionID) {
        this.jsessionID = jsessionID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSelfmotionLogin() {
        return selfmotionLogin;
    }

    public void setSelfmotionLogin(boolean selfmotionLogin) {
        this.selfmotionLogin = selfmotionLogin;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile;
    }

    public String getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(String equipmentid) {
        this.equipmentid = equipmentid;
    }

    public int getUserLoginType() {
        return userLoginType;
    }

    public void setUserLoginType(int userLoginType) {
        this.userLoginType = userLoginType;
    }

    public int getComfigurationState() {
        return comfigurationState;
    }

    public void setComfigurationState(int comfigurationState) {
        this.comfigurationState = comfigurationState;
    }

    public boolean ismCalloK() {
        return mCalloK;
    }

    public void setmCalloK(boolean mCalloK) {
        this.mCalloK = mCalloK;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getWyImToken() {
        return wyImToken;
    }

    public void setWyImToken(String wyImToken) {
        this.wyImToken = wyImToken;
    }
}
