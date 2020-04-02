package com.escort.carriage.android.utils;

import android.app.Activity;
import android.util.Log;

import com.alibaba.security.cloud.CloudRealIdentityTrigger;
import com.alibaba.security.realidentity.ALRealIdentityCallback;
import com.alibaba.security.realidentity.ALRealIdentityResult;
import com.alibaba.security.rp.RPSDK;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.MainActivity;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;

import java.util.HashMap;
import java.util.UUID;

public class HumanFaceAuthenticationUtils {

    public static void start(Activity activity, Callback callback){
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        hashMap.put("bizId", uuid);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.CLOUDAUTH_GETAUTHKEY, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                if (s != null) {
                    if (s.success) {
                        //开启人脸识别
                        CloudRealIdentityTrigger.start(activity, s.data, new ALRealIdentityCallback() {
                            @Override
                            public void onAuditResult(ALRealIdentityResult alRealIdentityResult, String s) {
                                if (alRealIdentityResult == ALRealIdentityResult.AUDIT_PASS) {
                                    ToastUtil.showToastString(s);
                                    //认证通过 调用接口 获取新的数据
                                    getImageUrl(uuid, callback);
                                } else if(alRealIdentityResult == ALRealIdentityResult.AUDIT_FAIL) {
                                    // 认证不通过。建议接入方调用实人认证服务端接口DescribeVerifyResult来获取最终的认证状态，并以此为准进行业务上的判断和处理
                                    // do something
                                } else if(alRealIdentityResult == ALRealIdentityResult.AUDIT_NOT) {
                                    // 未认证，具体原因可通过code来区分（code取值参见下方表格），通常是用户主动退出或者姓名身份证号实名校验不匹配等原因，导致未完成认证流程
                                    // do something
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    private static void getImageUrl(String uuid, Callback callback) {


        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("bizId", uuid);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.CLOUDAUTH_GETFACEIMG, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                if (s != null) {
                    if (s.success) {
                        if(callback != null){
                            callback.callback(s.data);
                        }
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    public interface Callback{
        void callback(String url);
    }

}
