package com.escort.carriage.android.entity.request.my;

public class RequestPersonageAuthenticationEntity {
    /**
     * {
     *           "faceImage":"http://huipu741819bbcc1c564945b0e.jpg", // 脸照
     *           "idPictureFront":"http://huipu741819bbcc1c564945b0e.jpg", // 身份证正面
     *           "headPictureReverse":"http://hui741819bbcc1c564945b0e.jpg",// 身份证反面
     *           "urgentUserName":"wangrui",// 紧急联系人
     *           "urgentPhone":"17362162267",// 紧急联系人电话
     *           "provinceCode":"100000",// 省份Code
     *           "cityCode":"100100",// 所属市code
     *           "areaCode":"100110",// 所属区域code
     *   }
     */

    private String faceImage;
    private String idPictureFront;
    private String headPictureReverse;
    private String urgentUserName;
    private String urgentPhone;
//    private String provinceCode;
//    private String cityCode;
//    private String areaCode;

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getIdPictureFront() {
        return idPictureFront;
    }

    public void setIdPictureFront(String idPictureFront) {
        this.idPictureFront = idPictureFront;
    }

    public String getHeadPictureReverse() {
        return headPictureReverse;
    }

    public void setHeadPictureReverse(String headPictureReverse) {
        this.headPictureReverse = headPictureReverse;
    }

    public String getUrgentUserName() {
        return urgentUserName;
    }

    public void setUrgentUserName(String urgentUserName) {
        this.urgentUserName = urgentUserName;
    }

    public String getUrgentPhone() {
        return urgentPhone;
    }

    public void setUrgentPhone(String urgentPhone) {
        this.urgentPhone = urgentPhone;
    }

}
