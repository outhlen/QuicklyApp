package com.escort.carriage.android.entity.bean.my;

public class AppraiseInfoEntity {

    public long id;
    public String driverUserLoginId;
    public long shipperUserLoginId;
    public String orderNumber;
    public int score;
    public String evaluationContent;
    public String isAnonymous;
    public String createTime;
    public String type;
    public String evalName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getDriverUserLoginId() {
        return driverUserLoginId;
    }



    public long getShipperUserLoginId() {
        return shipperUserLoginId;
    }

    public void setShipperUserLoginId(long shipperUserLoginId) {
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

    public Object getCreateTime() {
        return createTime;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvalName() {
        return evalName;
    }

    public void setEvalName(String evalName) {
        this.evalName = evalName;
    }

    public void setDriverUserLoginId(String driverUserLoginId) {
        this.driverUserLoginId = driverUserLoginId;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
