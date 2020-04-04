package com.escort.carriage.android.entity.request.home;

import com.escort.carriage.android.entity.request.PageBean;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-17 16:06
 */
public class HomeListRequestEnity {

    public String cargoName;//货物名称
    public String startCityCode;//开始城市
    public String endCityCode;//目的城市
    public String cargoWeight1;//货物重量下线
    public String cargoWeight2;//货物重量上线
    public String cargoVolume1;//货物体积下线
    public String cargoVolume2;//货物体积上线
    public String orderPlaceTime1;//下单时间开始时间 yyyy-MM-dd HH:mm:ss
    public String orderPlaceTime2;//下单时间终止时间 yyyy-MM-dd HH:mm:ss
    public String orderType;//货运类型 0 发布货源单 1 货源大厅匹配订单 2实时（智能供配） 3专线直达) 4 系统录入 这个参数，就按照这个传


    public PageBean page;

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getCargoWeight1() {
        return cargoWeight1;
    }

    public void setCargoWeight1(String cargoWeight1) {
        this.cargoWeight1 = cargoWeight1;
    }

    public String getCargoWeight2() {
        return cargoWeight2;
    }

    public void setCargoWeight2(String cargoWeight2) {
        this.cargoWeight2 = cargoWeight2;
    }

    public String getCargoVolume1() {
        return cargoVolume1;
    }

    public void setCargoVolume1(String cargoVolume1) {
        this.cargoVolume1 = cargoVolume1;
    }

    public String getCargoVolume2() {
        return cargoVolume2;
    }

    public void setCargoVolume2(String cargoVolume2) {
        this.cargoVolume2 = cargoVolume2;
    }

    public String getOrderPlaceTime1() {
        return orderPlaceTime1;
    }

    public void setOrderPlaceTime1(String orderPlaceTime1) {
        this.orderPlaceTime1 = orderPlaceTime1;
    }

    public String getOrderPlaceTime2() {
        return orderPlaceTime2;
    }

    public void setOrderPlaceTime2(String orderPlaceTime2) {
        this.orderPlaceTime2 = orderPlaceTime2;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }


    public String getStartCityCode() {
        return startCityCode;
    }

    public void setStartCityCode(String startCityCode) {
        this.startCityCode = startCityCode;
    }

    public String getEndCityCode() {
        return endCityCode;
    }

    public void setEndCityCode(String endCityCode) {
        this.endCityCode = endCityCode;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }
}
