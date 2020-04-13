package com.escort.carriage.android.entity.bean.home;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-24 14:19
 */
public class SelectOrderEntity {
    public String cargoName;//货物名称
    public String endWeight;//货物最大重量
    public String startWeight;//货物最小重量
    public String endVolume;//货物最大体积
    public String startVolume;//货物最小体积
    public String orderPlaceTime;//下单时间开始时间 yyyy-MM-dd HH:mm:ss
    public String endDate;//结束时间 yyyy-MM-dd HH:mm:ss
    public String orderType = "0";//货运类型 订单类型(0 快速货运 1专线直达 2城市配送)

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getEndWeight() {
        return endWeight;
    }

    public void setEndWeight(String endWeight) {
        this.endWeight = endWeight;
    }

    public String getStartWeight() {
        return startWeight;
    }

    public void setStartWeight(String startWeight) {
        this.startWeight = startWeight;
    }

    public String getEndVolume() {
        return endVolume;
    }

    public void setEndVolume(String endVolume) {
        this.endVolume = endVolume;
    }

    public String getStartVolume() {
        return startVolume;
    }

    public void setStartVolume(String startVolume) {
        this.startVolume = startVolume;
    }

    public String getOrderPlaceTime() {
        return orderPlaceTime;
    }

    public void setOrderPlaceTime(String orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
