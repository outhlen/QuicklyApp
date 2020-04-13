package com.escort.carriage.android.entity.bean.play;

public class WalletMenuEntity {
//    {
//                        "remaining": "100", // 余额
//                                "remainingFrozen": "50", // 冻结余额
//                                "remainingDonation": "100", // 赠送余额
//    }
    public double remaining;
    public double remainingFrozen;
    public double remainingDonation;
    public double integralAllCount;

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getRemainingFrozen() {
        return remainingFrozen;
    }

    public void setRemainingFrozen(double remainingFrozen) {
        this.remainingFrozen = remainingFrozen;
    }

    public double getIntegralAllCount() {
        return integralAllCount;
    }

    public void setIntegralAllCount(double integralAllCount) {
        this.integralAllCount = integralAllCount;
    }

    public double getRemainingDonation() {
        return remainingDonation;
    }

    public void setRemainingDonation(double remainingDonation) {
        this.remainingDonation = remainingDonation;
    }
}
