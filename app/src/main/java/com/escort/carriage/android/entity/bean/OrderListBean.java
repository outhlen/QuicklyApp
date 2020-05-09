package com.escort.carriage.android.entity.bean;

import java.io.Serializable;
import java.util.List;

public class OrderListBean implements Serializable {


    private Long id;//编号
    private String orderNumber;  //订单号          "orderNumber": "1229234272418795520",
    private Long shipperCompId;//货主公司ID  "shipperCompId": 123456,
    private Long shipperUserLoginId;//货主登录ID "shipperUserLoginId": 123456,
    private Long shipperCustomerId;     //发货方客户ID        "shipperCustomerId": null,
    private String startComp;   //发货公司         "startComp": null,
    private String startLinkman;       //发货人     "startLinkman": null,
    private String startTelephone;     //发货联系电话      "startTelephone": null,
    private String startCellphone;      //发货手机号码    "startCellphone": null,
    private Long recipientCustomerId;      //收货方客户ID    "recipientCustomerId": null,
    private String endComp;      //收货公司    "endComp": null,
    private String endLinkman;      //收货人    "endLinkman": null,
    private String endTelephone;      //收货联系电话    "endTelephone": null,
    private String endCellphone;     //收货手机号码   "endCellphone": null,
    private String distance;   //总里程     "distance": 5.00,
    private String vehicleType;      //车型      "vehicleType": "1",
    private String cargoName;     //货物名称 "cargoName": "测试货物",
    private double cargoWeight;   //货物重量 "cargoWeight": 10.00,
    private double cargoVolume;     //货物体积"cargoVolume": 1.00,
    private int cargoCount;    //  "cargoCount": 1, //货物数量
    private String payMode;      // "payMode": "1", //计费方式
    private String unitPrice;       //"unitPrice": 0.00, //单价
    private String cargoValue;     //  "cargoValue": 10.00, //货物价值
    private String isOverspeedOvermanTransfinite; //     "isOverspeedOvermanTransfinite": "0", //是否三超(0 否 1 是)
    private String packingManner;       //  "packingManner": "2", //包装方式
    private String loadNumAndDischargeNum;   //     "loadNumAndDischargeNum": "2装2卸", //几装几卸
    private long loadDate;          // "loadDate": null, //装货时间
    private long dischargeDate;          //  "dischargeDate": null, //卸货时间
    private String expectFreightRate;         //"expectFreightRate": 10.00, //预期运价
    private double actualFreight;     //  "actualFreight": 20.00, //实际运价
    private double pickUpPay;    //  "pickUpPay": 10.00, //提货费
    private double deliverGoodsPay;        //"deliverGoodsPay": 10.00, //送货费
    private double landingChargesPay;     //  "landingChargesPay": 10.00, //卸货费
    private double insurancePay;    //  "insurancePay": 10.00, //保险费用
    private double otherPay;    //  "otherPay": 0.00, //其他费用
    private double paidAmount;    //  "paidAmount": 20.00, //已付运费金额
    private String specificRequirement;    // "specificRequirement": "无", //特殊要求
    private String paymentMethod;       //  "paymentMethod": "1", //付款方式(字典表选择)
    private String deliveryWay;       // "deliveryWay": "2", //配送方式(1 自提 2送货)
    private String carpoolingType;            //"carpoolingType": "1", //拼车类型(1 专车 2拼车)
    private String orderType;         // "orderType": "0", //订单类型(0 发布货源单 1 货源大厅匹配订单 2实时（智能供配） 3专线直达) 4 系统录入)
    private String insuranceId;        // "insuranceId": 0, //保险ID
    private String directLineId;        //   "directLineId": 0, //专线ID
    private String goodsReleasedId;       //  "goodsReleasedId": 0, //货源ID
    private String isOut;        //"isOut": null, //是否外包承运商(0 否 1 是)
    private String driverCompId;      //"driverCompId": null, //车主公司ID
    private String driverUserLoginId;      //"driverUserLoginId": null, //车主登录ID
    private String driverVehicleId;        // "driverVehicleId": null, //车辆ID
    private double payToDriver;         //"payToDriver": 0.00, //应付司机金额
    private double payInCash;         // "payInCash": 0.00, //付现金
    private double payInBank;          //"payInBank": 0.00, //打卡金额
    private double payInOil;            // "payInOil": 0.00, //油卡金额
    private String startDate;         //  "startDate": null, //发车日期
    private String endDate;       //   "endDate": null, //到达日期
    private String outLogisticsCarrierId;         // "outLogisticsCarrierId": null, //外包承运单位ID
    private long outDate;         // "outDate": null, //外包日期
    private double outPay;        //  "outPay": 0.00, //外包费用
    private double outPickUpPay;       // "outPickUpPay": 0.00, //支出提货费
    private double outDeliverGoodsPay;        //"outDeliverGoodsPay": 0.00, //支出送货费
    private String outLandingChargesPay;      //  "outLandingChargesPay": null, //支出卸货费
    private String orderStatus;      //   "orderStatus": "0", //订单状态(-1取消 0 待接单 1 已接单 2 前往载货地 3 抵达载货地 4 载货完成 5 出发目的地 6 抵达目的地 7 卸货完成 8 已结单)
    private String payStatus;       // "payStatus": "0", //支付状态(0 未支付 1 付款中 2 支付成功)
    private long orderPlaceTime;        //"orderPlaceTime": "2020-02-17T02:41:02.000+0000", //下单时间(录单时间)
    private long orderReceiveTime;        //    "orderReceiveTime": null, //接单时间
    private long orderFinishTime;        // "orderFinishTime": null, //结单时间
    private long cancelTime;        // "cancelTime": null, //取消时间
    private String remark;        //"remark": "测试", //备注
    private String receipt;      // "receipt": "0", //是否需要回单(0 否 1 是)
    private String isReceipt;       //是否已经回单(0 否 1 是) "isReceipt": null,
    private String receiptUrl;       //回单图片  "receiptUrl": null,
    private String receiptRemarks;       //回单备注 "receiptRemarks": null,
    private String isPickUpGoods;      //是否需要提货(0 否 1 是) "isPickUpGoods": null,
    private String isDeleted;      //删除状态(0 否 1 是) "isDeleted": "0",
    private String orderPlaceDate;       //下单时间"orderPlaceDate": null,
    private String imgUrl1;     //货物图片1    "imgUrl1": null,
    private String imgUrl2;     //货物图片2    "imgUrl2": null,
    private String imgUrl3;        //货物图片3  "imgUrl3": null,
    private String deposit;     //押金   "deposit": null,
    private double payMoney;//"payMoney": 50000.00,
    private String isDeposit;     //是否需要交押金(0否1是)   "isDeposit": "0",
    private String reminder;       //催单次数(最高3次) "reminder": null,
    private String trId;       //轨迹id  "trId": null
    private String intention;
    private String driverPhone;
    private String travelNumberPlate;
    private double longitude;
    private double latitude;

    public List<OrderAddressBean> getAddr() {
        return addr;
    }

    public String getTravelNumberPlate() {
        return travelNumberPlate;
    }

    public void setTravelNumberPlate(String travelNumberPlate) {
        this.travelNumberPlate = travelNumberPlate;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    private List<OrderAddressBean> addr;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public double getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public double getCargoVolume() {
        return cargoVolume;
    }

    public void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }

    public int getCargoCount() {
        return cargoCount;
    }

    public void setCargoCount(int cargoCount) {
        this.cargoCount = cargoCount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCargoValue() {
        return cargoValue;
    }

    public void setCargoValue(String cargoValue) {
        this.cargoValue = cargoValue;
    }

    public String getIsOverspeedOvermanTransfinite() {
        return isOverspeedOvermanTransfinite;
    }

    public void setIsOverspeedOvermanTransfinite(String isOverspeedOvermanTransfinite) {
        this.isOverspeedOvermanTransfinite = isOverspeedOvermanTransfinite;
    }

    public String getPackingManner() {
        return packingManner;
    }

    public void setPackingManner(String packingManner) {
        this.packingManner = packingManner;
    }

    public String getLoadNumAndDischargeNum() {
        return loadNumAndDischargeNum;
    }

    public void setLoadNumAndDischargeNum(String loadNumAndDischargeNum) {
        this.loadNumAndDischargeNum = loadNumAndDischargeNum;
    }

    public long getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(long loadDate) {
        this.loadDate = loadDate;
    }

    public long getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(long dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getExpectFreightRate() {
        return expectFreightRate;
    }

    public void setExpectFreightRate(String expectFreightRate) {
        this.expectFreightRate = expectFreightRate;
    }

    public double getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(double actualFreight) {
        this.actualFreight = actualFreight;
    }

    public double getPickUpPay() {
        return pickUpPay;
    }

    public void setPickUpPay(double pickUpPay) {
        this.pickUpPay = pickUpPay;
    }

    public double getDeliverGoodsPay() {
        return deliverGoodsPay;
    }

    public void setDeliverGoodsPay(double deliverGoodsPay) {
        this.deliverGoodsPay = deliverGoodsPay;
    }

    public double getLandingChargesPay() {
        return landingChargesPay;
    }

    public void setLandingChargesPay(double landingChargesPay) {
        this.landingChargesPay = landingChargesPay;
    }

    public double getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(double insurancePay) {
        this.insurancePay = insurancePay;
    }

    public double getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(double otherPay) {
        this.otherPay = otherPay;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getSpecificRequirement() {
        return specificRequirement;
    }

    public void setSpecificRequirement(String specificRequirement) {
        this.specificRequirement = specificRequirement;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryWay() {
        return deliveryWay;
    }

    public void setDeliveryWay(String deliveryWay) {
        this.deliveryWay = deliveryWay;
    }

    public String getCarpoolingType() {
        return carpoolingType;
    }

    public void setCarpoolingType(String carpoolingType) {
        this.carpoolingType = carpoolingType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getDirectLineId() {
        return directLineId;
    }

    public void setDirectLineId(String directLineId) {
        this.directLineId = directLineId;
    }

    public String getGoodsReleasedId() {
        return goodsReleasedId;
    }

    public void setGoodsReleasedId(String goodsReleasedId) {
        this.goodsReleasedId = goodsReleasedId;
    }

    public String getIsOut() {
        return isOut;
    }

    public void setIsOut(String isOut) {
        this.isOut = isOut;
    }

    public String getDriverCompId() {
        return driverCompId;
    }

    public void setDriverCompId(String driverCompId) {
        this.driverCompId = driverCompId;
    }

    public String getDriverUserLoginId() {
        return driverUserLoginId;
    }

    public void setDriverUserLoginId(String driverUserLoginId) {
        this.driverUserLoginId = driverUserLoginId;
    }

    public String getDriverVehicleId() {
        return driverVehicleId;
    }

    public void setDriverVehicleId(String driverVehicleId) {
        this.driverVehicleId = driverVehicleId;
    }

    public double getPayToDriver() {
        return payToDriver;
    }

    public void setPayToDriver(double payToDriver) {
        this.payToDriver = payToDriver;
    }

    public double getPayInCash() {
        return payInCash;
    }

    public void setPayInCash(double payInCash) {
        this.payInCash = payInCash;
    }

    public double getPayInBank() {
        return payInBank;
    }

    public void setPayInBank(double payInBank) {
        this.payInBank = payInBank;
    }

    public double getPayInOil() {
        return payInOil;
    }

    public void setPayInOil(double payInOil) {
        this.payInOil = payInOil;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOutLogisticsCarrierId() {
        return outLogisticsCarrierId;
    }

    public void setOutLogisticsCarrierId(String outLogisticsCarrierId) {
        this.outLogisticsCarrierId = outLogisticsCarrierId;
    }

    public long getOutDate() {
        return outDate;
    }

    public void setOutDate(long outDate) {
        this.outDate = outDate;
    }

    public double getOutPay() {
        return outPay;
    }

    public void setOutPay(double outPay) {
        this.outPay = outPay;
    }

    public double getOutPickUpPay() {
        return outPickUpPay;
    }

    public void setOutPickUpPay(double outPickUpPay) {
        this.outPickUpPay = outPickUpPay;
    }

    public double getOutDeliverGoodsPay() {
        return outDeliverGoodsPay;
    }

    public void setOutDeliverGoodsPay(double outDeliverGoodsPay) {
        this.outDeliverGoodsPay = outDeliverGoodsPay;
    }

    public String getOutLandingChargesPay() {
        return outLandingChargesPay;
    }

    public void setOutLandingChargesPay(String outLandingChargesPay) {
        this.outLandingChargesPay = outLandingChargesPay;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public long getOrderPlaceTime() {
        return orderPlaceTime;
    }

    public void setOrderPlaceTime(long orderPlaceTime) {
        this.orderPlaceTime = orderPlaceTime;
    }

    public long getOrderReceiveTime() {
        return orderReceiveTime;
    }

    public void setOrderReceiveTime(long orderReceiveTime) {
        this.orderReceiveTime = orderReceiveTime;
    }

    public long getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(long orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(String isReceipt) {
        this.isReceipt = isReceipt;
    }

    public String getReceiptUrl() {
        return receiptUrl;
    }

    public void setReceiptUrl(String receiptUrl) {
        this.receiptUrl = receiptUrl;
    }

    public String getReceiptRemarks() {
        return receiptRemarks;
    }

    public void setReceiptRemarks(String receiptRemarks) {
        this.receiptRemarks = receiptRemarks;
    }

    public String getIsPickUpGoods() {
        return isPickUpGoods;
    }

    public void setIsPickUpGoods(String isPickUpGoods) {
        this.isPickUpGoods = isPickUpGoods;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getOrderPlaceDate() {
        return orderPlaceDate;
    }

    public void setOrderPlaceDate(String orderPlaceDate) {
        this.orderPlaceDate = orderPlaceDate;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public String getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(String isDeposit) {
        this.isDeposit = isDeposit;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getTrId() {
        return trId;
    }

    public void setTrId(String trId) {
        this.trId = trId;
    }

    public String getIntention() {
        return intention;
    }

    public void setIntention(String intention) {
        this.intention = intention;
    }


    public void setAddr(List<OrderAddressBean> addr) {
        this.addr = addr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getShipperCompId() {
        return shipperCompId;
    }

    public void setShipperCompId(Long shipperCompId) {
        this.shipperCompId = shipperCompId;
    }

    public Long getShipperUserLoginId() {
        return shipperUserLoginId;
    }

    public void setShipperUserLoginId(Long shipperUserLoginId) {
        this.shipperUserLoginId = shipperUserLoginId;
    }

    public Long getShipperCustomerId() {
        return shipperCustomerId;
    }

    public void setShipperCustomerId(Long shipperCustomerId) {
        this.shipperCustomerId = shipperCustomerId;
    }

    public String getStartComp() {
        return startComp;
    }

    public void setStartComp(String startComp) {
        this.startComp = startComp;
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

    public Long getRecipientCustomerId() {
        return recipientCustomerId;
    }

    public void setRecipientCustomerId(Long recipientCustomerId) {
        this.recipientCustomerId = recipientCustomerId;
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
