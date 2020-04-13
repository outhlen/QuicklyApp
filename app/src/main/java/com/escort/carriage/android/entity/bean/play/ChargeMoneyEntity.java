package com.escort.carriage.android.entity.bean.play;

public class ChargeMoneyEntity {
    /**
     * id : 1243475685593381451
     * title : 充100送10元
     * icon : null
     * remaining : 100
     * remainingDonation : 10
     * payAmout : 100
     * remark : 无
     * createTime : 1585384914000
     * updateTime : 1585384917000
     * creater : null
     */

    public String id;
    public String title;
    public String icon;
    public double remaining;
    public double remainingDonation;
    public double payAmout;
    public String remark;
    public long createTime;
    public long updateTime;
    public String creater;
    public boolean isInputType;//false 不可输入内容   true 可以输入内容


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getRemainingDonation() {
        return remainingDonation;
    }

    public void setRemainingDonation(double remainingDonation) {
        this.remainingDonation = remainingDonation;
    }

    public double getPayAmout() {
        return payAmout;
    }

    public void setPayAmout(double payAmout) {
        this.payAmout = payAmout;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}
