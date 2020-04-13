package com.escort.carriage.android.entity.bean.play;

public class PlayMesFeesEntity {
    /**
     * appId : wxee530addca9e3872
     * timeStamp : 1584008483
     * nonceStr : 1584008483290
     * packageValue : prepay_id=wx121821171476417e0674aa971517514400
     * sign : D2C0F0C7B9005785DF6A131888F4FC1F
     * prepayId : wx281627208522640eafd9a5141841579200
     * partnerId : 1545351531
     */

    public String appId;
    public String timeStamp;
    public String nonceStr;
    public String packageValue;
    public String sign;
    public String prepayId;
    public String partnerId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}
