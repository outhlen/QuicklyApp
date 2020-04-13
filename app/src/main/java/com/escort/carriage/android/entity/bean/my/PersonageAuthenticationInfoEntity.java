package com.escort.carriage.android.entity.bean.my;

public class PersonageAuthenticationInfoEntity {
    /**
     * idPictureFront : http://huipu41819bbcc1c564945b0e.jpg
     * headPictureReverse : http://819bbcc1c564945b0e.jpg
     * idNumber : 37241123203263013
     * userName : 王XX
     * authStatus : 2
     */

    private String idPictureFront;
    private String headPictureReverse;
    private String idNumber;
    private String userName;
    private int authStatus;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}
