package com.escort.carriage.android.entity.bean.push;

public class PushEntity {
    /**
     * isVoice : 0
     * serviceApi : http:xiaoer.jinanluke.com:8182/api/undertake/orderVehicle/add/foldFalconLocation
     * serviceApiParams : {orderNumber=1243782616302358528}
     */

    private int isVoice;//0:不播报 1：播报
    private String serviceApi;
    private String serviceApiParams;
    public int page;

    public String getServiceApi() {
        return serviceApi;
    }

    public void setServiceApi(String serviceApi) {
        this.serviceApi = serviceApi;
    }

    public String getServiceApiParams() {
        return serviceApiParams;
    }

    public void setServiceApiParams(String serviceApiParams) {
        this.serviceApiParams = serviceApiParams;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getIsVoice() {
        return isVoice;
    }

    public void setIsVoice(int isVoice) {
        this.isVoice = isVoice;
    }
}
