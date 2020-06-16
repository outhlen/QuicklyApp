package com.escort.carriage.android.entity.request;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.escort.carriage.android.ProjectApplication;
import com.escort.carriage.android.utils.DeviceUtils;
import com.escort.carriage.android.utils.Md5Utils;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-16 11:28
 */
public class RequestEntity {
    private HashMap<String, Object> header;
    private Object data;
    private Object param;

    public RequestEntity(){}

    public RequestEntity(int type){
        header = new HashMap<>();
        header.put("deviceType", "ANDROID");
        String timestamp = String.valueOf(new Date().getTime()).substring(0,11);
        header.put("timestamp", timestamp);
        header.put("sign", getSign(timestamp));
        if(type == -1){
            //说明没有token
        } else {
            header.put("token", getToken());
        }
    }

    private String getSign(String time){
        String imei = DeviceUtils.getIMEI(ApplicationContext.getInstance().application);
        String token = getToken();
        return Md5Utils.getMD5(imei + "_" + token + "_" + time);
    }

    private String getToken(){
        String userToken = CacheDBMolder.getInstance().getUserToken();
        return userToken;
    }

    public HashMap<String, Object> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, Object> header) {
        this.header = header;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }
}
