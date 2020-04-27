package com.escort.carriage.android.entity.bean.home;

import java.util.List;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-20 16:34
 */
public class OrderInfoEntity {

    public String id;
    public String orderNumber;
    public String startCityCode;
    public String startCityName;
    public String endCityCode;
    public String endCityName;
    public String shipperCompId;
    public String shipperUserLoginId;
    public String userName;
    public String nameCardPicture;
    public String shipperCustomerId;
    public String startComp;
    public String startLinkman;
    public String startTelephone;
    public String startCellphone;
    public String recipientCustomerId;
    public String endComp;
    public String endLinkman;
    public String endTelephone;
    public String endCellphone;
    public double distance;
    public String cargoName;
    public double cargoWeight;
    public double cargoVolume;
    public int cargoCount;
    public String payMode;
    public String unitPrice;
    public double cargoValue;
    public String isOverspeedOvermanTransfinite;
    public String packingManner;
    public String loadNumAndDischargeNum;
    public String loadDate;
    public String dischargeDate;
    public double expectFreightRate;
    public int actualFreight;
    public int pickUpPay;
    public int deliverGoodsPay;
    public int landingChargesPay;
    public int otherPay;
    public int paidAmount;
    public String specificRequirement;
    public String paymentMethod; //是否发起线下支付  0否 1是
    public String deliveryWay;
    public String carpoolingType;
    public String orderType = "0";
    public String insuranceId;
    public String directLineId;
    public String goodsReleasedId;
    public String isOut;
    public String driverCompId;
    public String driverUserLoginId;
    public String driverVehicleId;
    public int payToDriver;
    public int payInCash;
    public int payInBank;
    public int payInOil;
    public String startDate;
    public String endDate;
    public String outLogisticsCarrierId;
    public String outDate;
    public int outPay;
    public int outPickUpPay;
    public int outDeliverGoodsPay;
    public String outLandingChargesPay;
    public String payStatus;
    public long orderPlaceTime;
    public String orderReceiveTime;
    public String orderFinishTime;
    public String cancelTime;
    public String remark;
    public int receipt;
    public String isReceipt;
    public String receiptUrl;
    public String receiptRemarks;
    public String isPickUpGoods;
    public String isDeleted;
    public String orderPlaceDate;
    public String imgUrl1;
    public String imgUrl2;
    public String imgUrl3;
    public String deposit;
    public double payMoney;
    public String isDeposit;
    public String reminder;
    public String trId;
    public String intention;
    public String track;
    public String count;
    public String plateNumber;
    public String driverPhone;
    public String shipperPhone;
    public String driverName;
    public String shipperName;
    public String driverImg;
    public String shipperImg;
    public String longitude;
    public String latitude;
    public String travelNumberPlate;
    public String orderStartType;
    public String orderEndType;
    public String isInvoice;
    public int orderStatus;//订单状态(-1取消 0 待接单 1 已接单 2 前往载货地 3 抵达载货地 4 载货完成 5 出发目的地 6 抵达目的地 7 卸货完成 8 已结单)
    public int isUrge;//是否催款 过  0否 1是
    public int isShow;//是否转单 0不显示1显示
    public int isComplaint;//申诉  isComplaint  = 1  申诉按钮显示  = 2时候 不可点击 显示为已申诉   =0不显示按钮
    public int isAllowTurn;//转单  需要通过状态判断 是否可以点击   isAllowTurn 0 不能转单  1是可以转单
    public boolean repealOrderIsNotClick;//本地临时属性 是否 撤单
    public int isTurn;//是否经历转单(0否1是)
    public String vehicleType;//	车辆类型
    public String isBargain;//	是否已经议价成功
    public int isServiceChange;//	是否付了信息费 0 没有 1 已经付过了
    public String paramValue;//	服务费金额
    public String insuranceComName;//	保险公司名称
    public String taxPay;//	发票金额
    public String insurancePay;//	保险金额
    public String taxType;//	保险类型 s 电子增值税普通发票  p 增值税专用发票  否则 不开发票
    public int orderTab;//	0显示：撤单  1显示：处理撤单

    public String driverEvaluate;// 评价

    public String distances;
    public List<AddrBean> addr;
    public List<String> turn;

    public String getDriverEvaluate() {
        return driverEvaluate;
    }

    public void setDriverEvaluate(String driverEvaluate) {
        this.driverEvaluate = driverEvaluate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistances() {
        return distances;
    }

    public void setDistances(String distances) {
        this.distances = distances;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShipperCompId() {
        return shipperCompId;
    }

    public void setShipperCompId(String shipperCompId) {
        this.shipperCompId = shipperCompId;
    }

    public String getShipperUserLoginId() {
        return shipperUserLoginId;
    }

    public void setShipperUserLoginId(String shipperUserLoginId) {
        this.shipperUserLoginId = shipperUserLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNameCardPicture() {
        return nameCardPicture;
    }

    public void setNameCardPicture(String nameCardPicture) {
        this.nameCardPicture = nameCardPicture;
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

    public String getRecipientCustomerId() {
        return recipientCustomerId;
    }

    public void setRecipientCustomerId(String recipientCustomerId) {
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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

    public String getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public int getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(int actualFreight) {
        this.actualFreight = actualFreight;
    }

    public int getPickUpPay() {
        return pickUpPay;
    }

    public void setPickUpPay(int pickUpPay) {
        this.pickUpPay = pickUpPay;
    }

    public int getDeliverGoodsPay() {
        return deliverGoodsPay;
    }

    public void setDeliverGoodsPay(int deliverGoodsPay) {
        this.deliverGoodsPay = deliverGoodsPay;
    }

    public int getLandingChargesPay() {
        return landingChargesPay;
    }

    public void setLandingChargesPay(int landingChargesPay) {
        this.landingChargesPay = landingChargesPay;
    }


    public int getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(int otherPay) {
        this.otherPay = otherPay;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
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

    public int getPayToDriver() {
        return payToDriver;
    }

    public void setPayToDriver(int payToDriver) {
        this.payToDriver = payToDriver;
    }

    public int getPayInCash() {
        return payInCash;
    }

    public void setPayInCash(int payInCash) {
        this.payInCash = payInCash;
    }

    public int getPayInBank() {
        return payInBank;
    }

    public void setPayInBank(int payInBank) {
        this.payInBank = payInBank;
    }

    public int getPayInOil() {
        return payInOil;
    }

    public void setPayInOil(int payInOil) {
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

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public int getOutPay() {
        return outPay;
    }

    public void setOutPay(int outPay) {
        this.outPay = outPay;
    }

    public int getOutPickUpPay() {
        return outPickUpPay;
    }

    public void setOutPickUpPay(int outPickUpPay) {
        this.outPickUpPay = outPickUpPay;
    }

    public int getOutDeliverGoodsPay() {
        return outDeliverGoodsPay;
    }

    public void setOutDeliverGoodsPay(int outDeliverGoodsPay) {
        this.outDeliverGoodsPay = outDeliverGoodsPay;
    }

    public String getOutLandingChargesPay() {
        return outLandingChargesPay;
    }

    public void setOutLandingChargesPay(String outLandingChargesPay) {
        this.outLandingChargesPay = outLandingChargesPay;
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

    public String getOrderReceiveTime() {
        return orderReceiveTime;
    }

    public void setOrderReceiveTime(String orderReceiveTime) {
        this.orderReceiveTime = orderReceiveTime;
    }

    public String getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(String orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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


    public void setPayMoney(int payMoney) {
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

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getDriverImg() {
        return driverImg;
    }

    public void setDriverImg(String driverImg) {
        this.driverImg = driverImg;
    }

    public String getShipperImg() {
        return shipperImg;
    }

    public void setShipperImg(String shipperImg) {
        this.shipperImg = shipperImg;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getTravelNumberPlate() {
        return travelNumberPlate;
    }

    public void setTravelNumberPlate(String travelNumberPlate) {
        this.travelNumberPlate = travelNumberPlate;
    }

    public String getOrderStartType() {
        return orderStartType;
    }

    public void setOrderStartType(String orderStartType) {
        this.orderStartType = orderStartType;
    }

    public String getOrderEndType() {
        return orderEndType;
    }

    public void setOrderEndType(String orderEndType) {
        this.orderEndType = orderEndType;
    }

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public List<AddrBean> getAddr() {
        return addr;
    }

    public void setAddr(List<AddrBean> addr) {
        this.addr = addr;
    }

    public List<String> getTurn() {
        return turn;
    }

    public void setTurn(List<String> turn) {
        this.turn = turn;
    }


    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getDunningStatus() {
        return isUrge;
    }

    public void setDunningStatus(int isUrge) {
        this.isUrge = isUrge;
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

    public int getOrderStatus() {
        return orderStatus;
    }

    public int getIsUrge() {
        return isUrge;
    }

    public void setIsUrge(int isUrge) {
        this.isUrge = isUrge;
    }

    public int getIsComplaint() {
        return isComplaint;
    }

    public void setIsComplaint(int isComplaint) {
        this.isComplaint = isComplaint;
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

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsAllowTurn() {
        return isAllowTurn;
    }

    public void setIsAllowTurn(int isAllowTurn) {
        this.isAllowTurn = isAllowTurn;
    }

    public boolean isRepealOrderIsNotClick() {
        return repealOrderIsNotClick;
    }

    public void setRepealOrderIsNotClick(boolean repealOrderIsNotClick) {
        this.repealOrderIsNotClick = repealOrderIsNotClick;
    }

    public int getIsTurn() {
        return isTurn;
    }

    public void setIsTurn(int isTurn) {
        this.isTurn = isTurn;
    }

    public String getIsBargain() {
        return isBargain;
    }

    public void setIsBargain(String isBargain) {
        this.isBargain = isBargain;
    }


    public String getInsuranceComName() {
        return insuranceComName;
    }

    public void setInsuranceComName(String insuranceComName) {
        this.insuranceComName = insuranceComName;
    }

    public String getTaxPay() {
        return taxPay;
    }

    public void setTaxPay(String taxPay) {
        this.taxPay = taxPay;
    }

    public String getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(String insurancePay) {
        this.insurancePay = insurancePay;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public int getIsServiceChange() {
        return isServiceChange;
    }

    public void setIsServiceChange(int isServiceChange) {
        this.isServiceChange = isServiceChange;
    }

    public double getExpectFreightRate() {
        return expectFreightRate;
    }

    public void setExpectFreightRate(double expectFreightRate) {
        this.expectFreightRate = expectFreightRate;
    }

    public double getCargoValue() {
        return cargoValue;
    }

    public void setCargoValue(double cargoValue) {
        this.cargoValue = cargoValue;
    }

    public int getOrderTab() {
        return orderTab;
    }

    public void setOrderTab(int orderTab) {
        this.orderTab = orderTab;
    }

    public int getReceipt() {
        return receipt;
    }

    public void setReceipt(int receipt) {
        this.receipt = receipt;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }


}
