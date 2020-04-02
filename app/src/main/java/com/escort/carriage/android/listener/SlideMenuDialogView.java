package com.escort.carriage.android.listener;

import com.escort.carriage.android.entity.bean.CheckUserAuthResp;

public interface SlideMenuDialogView /*extends BaseView*/ {

    /**
     * 校验是否认证 回调
     */
    void checkUserAuthenticationResp(CheckUserAuthResp resp, String type);
}
