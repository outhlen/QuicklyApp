package com.escort.carriage.android.entity.bean.my;

public class DriverInfoEntity {
    /**
     * drivingLicencePicture : https://dongfangbaode.oss-cn-qingdao.aliyuncs.com/xiaoer_xiaochengxu/img/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20200307115454.png
     * carClass : C1
     * driverAddress : 山东省济南市市中区王官庄371号
     * driverUserName : 术宇
     */

    private String drivingLicencePicture;
    private String carClass;
    private String driverAddress;
    private String driverUserName;

    public String getDrivingLicencePicture() {
        return drivingLicencePicture;
    }

    public void setDrivingLicencePicture(String drivingLicencePicture) {
        this.drivingLicencePicture = drivingLicencePicture;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverUserName() {
        return driverUserName;
    }

    public void setDriverUserName(String driverUserName) {
        this.driverUserName = driverUserName;
    }
}
