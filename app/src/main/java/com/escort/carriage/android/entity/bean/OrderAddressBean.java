package com.escort.carriage.android.entity.bean;

import java.io.Serializable;

public class OrderAddressBean implements Serializable {

    private String id;
    private String orderId;
    private String orderNumber;
    private String installType;
    private String unloadType;
    private String isDetele;
    private long createTime;
    private String shipperCustomerId;
    private String startComp;
    private String recipientCustomerId;
    private String startAreaName;
    private String endAreaName;
    private String startCityName;
    private String endCityName;
    private String startProvinceName;
    private String endProvinceName;
    private String startLongitude;//发货经度
    private String startLatitude;//发货纬度
    private String startProvinceCode;//始发省
    private String startCityCode;//始发市
    private String startAreaCode;//始发区
    private String startAddr;//始发地详细地址
    private String endLongitude;//卸货经度
    private String endLatitude;//卸货纬度
    private String endProvinceCode;//目的地省
    private String endCityCode;//目的地市
    private String endAreaCode;
    private String endAddr;
    private String startLinkman;
    private String startTelephone;
    private String startCellphone;
    private String endComp;
    private String endLinkman;
    private String endTelephone;
    private String endCellphone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getInstallType() {
        return installType;
    }

    public void setInstallType(String installType) {
        this.installType = installType;
    }

    public String getUnloadType() {
        return unloadType;
    }

    public void setUnloadType(String unloadType) {
        this.unloadType = unloadType;
    }

    public String getIsDetele() {
        return isDetele;
    }

    public void setIsDetele(String isDetele) {
        this.isDetele = isDetele;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getShipperCustomerId() {
        return shipperCustomerId;
    }

    public void setShipperCustomerId(String shipperCustomerId) {
        this.shipperCustomerId = shipperCustomerId;
    }

    public String getStartComp() {
        return startComp;
    }

    public void setStartComp(String startComp) {
        this.startComp = startComp;
    }

    public String getRecipientCustomerId() {
        return recipientCustomerId;
    }

    public void setRecipientCustomerId(String recipientCustomerId) {
        this.recipientCustomerId = recipientCustomerId;
    }

    public String getStartAreaName() {
        return startAreaName;
    }

    public void setStartAreaName(String startAreaName) {
        this.startAreaName = startAreaName;
    }

    public String getEndAreaName() {
        return endAreaName;
    }

    public void setEndAreaName(String endAreaName) {
        this.endAreaName = endAreaName;
    }

    public String getStartCityName() {
        return startCityName;
    }

    public void setStartCityName(String startCityName) {
        this.startCityName = startCityName;
    }

    public String getEndCityName() {
        return endCityName;
    }

    public void setEndCityName(String endCityName) {
        this.endCityName = endCityName;
    }

    public String getStartProvinceName() {
        return startProvinceName;
    }

    public void setStartProvinceName(String startProvinceName) {
        this.startProvinceName = startProvinceName;
    }

    public String getEndProvinceName() {
        return endProvinceName;
    }

    public void setEndProvinceName(String endProvinceName) {
        this.endProvinceName = endProvinceName;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartProvinceCode() {
        return startProvinceCode;
    }

    public void setStartProvinceCode(String startProvinceCode) {
        this.startProvinceCode = startProvinceCode;
    }

    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getStartAreaCode() {
        return startAreaCode;
    }

    public void setStartAreaCode(String startAreaCode) {
        this.startAreaCode = startAreaCode;
    }

    public String getStartAddr() {
        return startAddr;
    }

    public void setStartAddr(String startAddr) {
        this.startAddr = startAddr;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndProvinceCode() {
        return endProvinceCode;
    }

    public void setEndProvinceCode(String endProvinceCode) {
        this.endProvinceCode = endProvinceCode;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public String getEndAreaCode() {
        return endAreaCode;
    }

    public void setEndAreaCode(String endAreaCode) {
        this.endAreaCode = endAreaCode;
    }

    public String getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(String endAddr) {
        this.endAddr = endAddr;
    }

    public String getStartLinkman() {
        return startLinkman;
    }

    public void setStartLinkman(String startLinkman) {
        this.startLinkman = startLinkman;
    }

    public String getStartTelephone() {
        return startTelephone;
    }

    public void setStartTelephone(String startTelephone) {
        this.startTelephone = startTelephone;
    }

    public String getStartCellphone() {
        return startCellphone;
    }

    public void setStartCellphone(String startCellphone) {
        this.startCellphone = startCellphone;
    }

    public String getEndComp() {
        return endComp;
    }

    public void setEndComp(String endComp) {
        this.endComp = endComp;
    }

    public String getEndLinkman() {
        return endLinkman;
    }

    public void setEndLinkman(String endLinkman) {
        this.endLinkman = endLinkman;
    }

    public String getEndTelephone() {
        return endTelephone;
    }

    public void setEndTelephone(String endTelephone) {
        this.endTelephone = endTelephone;
    }

    public String getEndCellphone() {
        return endCellphone;
    }

    public void setEndCellphone(String endCellphone) {
        this.endCellphone = endCellphone;
    }
}
