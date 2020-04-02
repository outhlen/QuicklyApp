package com.escort.carriage.android.entity.bean.my;

public class OrderAppraiseListItemEntity {
    /**
     {
     "id": "1238074252779474945",// 评论Id
     "driverUserLoginId": "1236902496740331521", // 车主登录ID
     "shipperUserLoginId": "1237946111662407682", // 货主登录ID
     "orderNumber": "1236889931943645184", // 订单编号
     "score": 5, // 评分(5 非常满意 4满意 3 一般 2差 1非常差)
     "evaluationContent": "发货端评价666", // 评价内容
     "isAnonymous": "1", // 是否匿名评价(0 否 1是)
     "createTime": 1584014869000, // 创建时间
     "type": "1", // 类型 0承接方 1发货方
     "appealType": null, // 申诉类型 0承接方 1发货方
     "appealId": null, // 申诉id
     "appealResault": null, // 申诉结果
     "isAppeal": "0" // 是否申诉(0 否 1是)
     }
     */

    public String id;
    public String driverUserLoginId;
    public String shipperUserLoginId;
    public String orderNumber;
    public int score;
    public String evaluationContent;
    public String isAnonymous;
    public long createTime;
    public String type;
    public String appealType;
    public String appealId;
    public String appealResault;
    public String isAppeal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverUserLoginId() {
        return driverUserLoginId;
    }

    public void setDriverUserLoginId(String driverUserLoginId) {
        this.driverUserLoginId = driverUserLoginId;
    }

    public String getShipperUserLoginId() {
        return shipperUserLoginId;
    }

    public void setShipperUserLoginId(String shipperUserLoginId) {
        this.shipperUserLoginId = shipperUserLoginId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEvaluationContent() {
        return evaluationContent;
    }

    public void setEvaluationContent(String evaluationContent) {
        this.evaluationContent = evaluationContent;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppealType() {
        return appealType;
    }

    public void setAppealType(String appealType) {
        this.appealType = appealType;
    }

    public String getAppealId() {
        return appealId;
    }

    public void setAppealId(String appealId) {
        this.appealId = appealId;
    }

    public String getAppealResault() {
        return appealResault;
    }

    public void setAppealResault(String appealResault) {
        this.appealResault = appealResault;
    }

    public String getIsAppeal() {
        return isAppeal;
    }

    public void setIsAppeal(String isAppeal) {
        this.isAppeal = isAppeal;
    }
}
