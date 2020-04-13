package com.androidybp.basics.ui.mvp.model;

import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;

/**
 * 通用的model 管理类  这个类可以复用  多个网络请求用一个model
 */
public class CommonMvpModel extends BaseMvpModel<ResponceJsonEntity>{
    private CommonMvpModelInterface mCommonMvpModelInterface;


    @Override
    protected Class<?> setResponClass(int type) {
        if(mCommonMvpModelInterface == null){
            return ResponceBean.class;
        } else {
            return mCommonMvpModelInterface.getResponClass(type);
        }
    }

    public void setInterface(CommonMvpModelInterface mCommonMvpModelInterface){
        this.mCommonMvpModelInterface = mCommonMvpModelInterface;
    }


    @Override
    public void clear() {
        super.clear();
        mCommonMvpModelInterface = null;
    }

    public interface CommonMvpModelInterface {
        Class<?> getResponClass(int type);
    }
}
