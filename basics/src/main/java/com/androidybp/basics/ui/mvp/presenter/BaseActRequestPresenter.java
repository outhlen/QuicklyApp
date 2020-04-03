package com.androidybp.basics.ui.mvp.presenter;

import android.content.Intent;
import androidx.annotation.Nullable;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.okhttp3.listener.RequestCallback;
import com.androidybp.basics.ui.mvp.model.ModelInterface;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;
import com.androidybp.basics.utils.hint.ToastUtil;
import okhttp3.Call;

public abstract class BaseActRequestPresenter<T extends MvpViewInterface, D extends ResponceJsonEntity> extends BaseActPresenter<T> implements RequestCallback<D> {
    @Override
    public void interceptor(D jsonBean, int interceptor, int type) {
        if(view != null){
            view.hintUplodingAnim(type, false);
        }
//        ToastUtil.showToastString("数据解析异常");
    }

    @Override
    public void noNetworkConnected(int type, Call call, Exception e) {
        if(view != null){
            view.hintUplodingAnim(type, false);
        }
//        ToastUtil.showToastString("数据解析异常");
    }

    @Override
    public void requestTimeOutMainThread(int type) {
        if(view != null){
            view.hintUplodingAnim(type, false);
        }
//        ToastUtil.showToastString("数据解析异常");
    }

    @Override
    public void analysisEntityError(int type) {
        if(view != null){
            view.hintUplodingAnim(type, false);
        }
//        ToastUtil.showToastString("数据解析异常");
    }

}
