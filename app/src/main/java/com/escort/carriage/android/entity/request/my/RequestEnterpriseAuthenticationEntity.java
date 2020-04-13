package com.escort.carriage.android.entity.request.my;

public class RequestEnterpriseAuthenticationEntity {

    /**
     * companyName : 海口棕海项目建设投资有限公司
     * creditCode : 91460100MA5RD6X910
     * companyProvinceCode : 370000
     * companyCityCode : 370300
     * companyAreaCode : 370306
     * companyAddress : 湖南省海口市龙华区滨海大道85号天邑国际大厦15楼1502室
     * licencePicture : http://huipushenghuo.oss-cn/c01b684faf.jpg
     */

    private String companyName;
    private String creditCode;
    private String companyProvinceCode;
    private String companyCityCode;
    private String companyAreaCode;
    private String companyAddress;
    private String licencePicture;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getCompanyProvinceCode() {
        return companyProvinceCode;
    }

    public void setCompanyProvinceCode(String companyProvinceCode) {
        this.companyProvinceCode = companyProvinceCode;
    }

    public String getCompanyCityCode() {
        return companyCityCode;
    }

    public void setCompanyCityCode(String companyCityCode) {
        this.companyCityCode = companyCityCode;
    }

    public String getCompanyAreaCode() {
        return companyAreaCode;
    }

    public void setCompanyAreaCode(String companyAreaCode) {
        this.companyAreaCode = companyAreaCode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getLicencePicture() {
        return licencePicture;
    }

    public void setLicencePicture(String licencePicture) {
        this.licencePicture = licencePicture;
    }
}
