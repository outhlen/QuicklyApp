package com.escort.carriage.android.entity.bean.play;

public class ChargeMoneyPlayEntity {
    /**
     * id : 1244169540781256705
     * userLoginId : 1238684860705439745
     * money : 0.01
     * donationMoney : 0
     * payMoney : 0.01
     * rechargePreferentialId : null
     * orderNo : 1244169540779249664
     * thirdPartyOrderNo : null
     * payWay : 2
     * payStatus : 0
     * creatTime : 1585468108731
     * payParam : {"sign":"2A2EACF29183B21F792B72D72A5352A2","prepayId":"wx2915482952464096197342671515754000","partnerId":"1545351531","appId":"wxa9a0fa1eb7bc4b85","packageValue":"Sign=WXPay","timeStamp":"1585468109","nonceStr":"1585468109560"}
     */

    public String id;
    public String userLoginId;
    public double money;
    public int donationMoney;
    public double payMoney;
    public String rechargePreferentialId;
    public String orderNo;
    public String thirdPartyOrderNo;
    public String payWay;
    public String payStatus;
    public long creatTime;
    public String payParam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getDonationMoney() {
        return donationMoney;
    }

    public void setDonationMoney(int donationMoney) {
        this.donationMoney = donationMoney;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public String getRechargePreferentialId() {
        return rechargePreferentialId;
    }

    public void setRechargePreferentialId(String rechargePreferentialId) {
        this.rechargePreferentialId = rechargePreferentialId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getThirdPartyOrderNo() {
        return thirdPartyOrderNo;
    }

    public void setThirdPartyOrderNo(String thirdPartyOrderNo) {
        this.thirdPartyOrderNo = thirdPartyOrderNo;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public String getPayParam() {
        return payParam;
    }

    public void setPayParam(String payParam) {
        this.payParam = payParam;
    }
}
