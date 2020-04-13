package com.androidybp.basics.ui.mvc.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.R;
import com.androidybp.basics.okhttp3.OkHttpManager;
import com.androidybp.basics.okhttp3.listenerIm.OKResponseCallBack;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.encryption.Base64Utils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.SystemCodeUtils;
import okhttp3.Call;

import java.io.IOException;

/**
 * 基于  OKhttp   网络请求框架    抽取的
 */

public abstract class MvcRequestFragment extends BaseFragment implements PackagingFragmentLintener {
    public boolean isRefreshing = false;//是否正在刷新信息
    public String newDate = "";

    protected View view;
    //参照 setUserVisibleHint  和 getUserVisibleHint() 方法
    protected Unbinder unbinder;
    public static final int INIT = 0;
    private IndentBaseCallBack indentBaseCallBack;
    protected boolean isOpen = false;
    protected boolean isTransmit = false;//这个标识  是  当前Activity不存在了 是否还需要继续执行网络响应的数据 默认为不执行

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pageExist = true;
        if (view == null) {
            view = setInflaterView(inflater);
            initModelView(view);
            unbinder = ButterKnife.bind(this, view);
        } else {
            if(unbinder == null)
                unbinder = ButterKnife.bind(this, view);
            initModelView(view);
        }

        return view;
    }

    /**
     * 这个方法 当 全局的View 为null 的时候  创建View 的时候执行这个方法
     *
     * @param view
     */
    protected void simpleSetView(View view) {
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isOpen) {
            simpleInitView(view);
            simpleInitView(view, savedInstanceState);
            isOpen = true;
        }
        initView(view, savedInstanceState);
        initData(view, savedInstanceState);
        toPostJsonService(getUrl(SystemCodeUtils.INITPAGE), getJson(SystemCodeUtils.INITPAGE), SystemCodeUtils.INITPAGE);
    }

    /**
     * 为了防止重复初始化控件  这个方法只会在第一次打开的时候调用 之后不会再被调用到
     *
     * @param view
     */
    protected void simpleInitView(View view) {

    }

    /**
     * 为了防止重复初始化控件  这个方法只会在第一次打开的时候调用 之后不会再被调用到
     *
     * @param view
     */
    protected void simpleInitView(View view, @Nullable Bundle savedInstanceState) {

    }

    /**
     * 请求网络获取数据
     * @param url        请求的url
     * @param json       请求的Json
     * @param initpage   本次请求的标识
     * sessionId - 默认 登录状态  已登录上传sessionId  未登录 不显示 sessionId
     */
    public void toPostJsonService(String url, String json, int initpage) {
        toPostJsonService(url, json, initpage, ApplicationContext.getInstance().isRegister);

    }
    /**
     * 请求网络数据
     * @param url        请求的url
     * @param json       请求的Json
     * @param initpage   本次请求的标识
     * @param flag       是否上传sessionId
     */
    public void toPostJsonService(String url, String json, int initpage, boolean flag) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            LogUtils.showE("IndentBaseFragment", "url = " + url + "\n请求带的Json =" + Base64Utils.decode(json));
            isRefreshing = true;
            setRequestAnim(initpage);
            if (indentBaseCallBack == null)
                indentBaseCallBack = new IndentBaseCallBack();
            OkHttpManager.getManager().post(url, json, flag, indentBaseCallBack, this.hashCode(), initpage);
        } catch (Exception e) {
            showLoserPage(initpage);
//            e.printStackTrace();
        }

    }

    /**
     * 请求网络报错情况下执行这个方法
     *
     * @param initpage
     */
    protected abstract void showLoserPage(int initpage);

    /**
     * 请求超时时调用   拦截器拦截后也会调用
     *
     * @param initpage
     */
    protected abstract void requestTimeOut(int initpage);

    /**
     * 解析请求的结果
     * <p>
     * 注意：这个方法都是在子线程中调用的
     *
     * @param mResponceBean 最外层的json 对象
     * @param type          网络请求的类型
     */
    protected abstract void analysisResponseData(Object mResponceBean, int type);

    /**
     * 显示网络加载动画  初始化页面请求参数类型是 TypeModel.INITPAGE
     *
     * @param initpage 当前请求加载的状态
     */
    protected void setRequestAnim(int initpage) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), getResources().getString(R.string.at_full_split_loading));
    }

    /**
     * 初始化控件的时候 调用
     * 因为目前在model 中无法使用 ButterKnife 只能手动findViewById 了
     * @param view
     */
    protected void initModelView(View view){

    }

    protected void initData(View view, Bundle savedInstanceState) {
    }


    protected void initView(View view, Bundle savedInstanceState) {

    }

    /**
     * 是否需要睡眠上1秒  为了响应太快导致 主线程堵塞 导致的页面卡顿问题
     *
     * @return true 需要延迟1秒
     * false  不需要延迟1秒
     * <p>
     * 默认是不需要延迟的
     */
    protected boolean sleepOneSecond() {
        return false;
    }

    /**
     * 子类选择性重写   是否需要走拦截的机制
     * @param type  当前请求的类型
     * @return
     */
    protected boolean urlIsUpdateCache(int type){
        if(ApplicationContext.getInstance().isRegister)
            return true;
        else
            return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isRefreshing = false;
        indentBaseCallBack = null;
        OkHttpManager.getManager().cancelByTag(this.hashCode());
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        view = null;
    }

    class IndentBaseCallBack extends OKResponseCallBack {

        @Override
        protected boolean isUpdateCache(int finalRequestType) {
            return urlIsUpdateCache(finalRequestType);
        }

        @Override
        public Class<?> setResponClass(int type) {
            return getResponseClass(type);
        }


        @Override
        public void interceptor(Object object, int interceptorCode, int type) {
            MvcRequestFragment.this.interceptor(object, type);
        }

        public void requestTimeOutMainThread(int type) {
            if (pageExist || isTransmit)
                requestTimeOut(type);
        }

        @Override
        public void responseBeanMainThread(Object mResponceBean, int type) {
            if (pageExist || isTransmit)
                analysisResponseData(mResponceBean, type);
        }

        @Override
        protected void responseBeanChildThread(Object jsonBean, int finalRequestType) {
            if (pageExist || isTransmit)
                analysisResponseChildThreadData(jsonBean, finalRequestType);
        }

        @Override
        public boolean getSleepOneSecond() {
            return sleepOneSecond();
        }

        @Override
        protected void noNetworkConnected(int requestType, Call call, IOException e) {
            showLoserPage(requestType);
        }
    }

    /**
     * 子类选择性重写   子线程响应请求结果
     *
     * @param jsonBean
     * @param type
     */
    protected void analysisResponseChildThreadData(Object jsonBean, int type) {

    }

    /**
     * 要解析的  json对象  从最外层开始    必须是继承 ResponceJsonBean  对象的子类
     *
     * @param type
     * @return
     */
    protected abstract Class<?> getResponseClass(int type);

    /**
     * 拦截器拦截了
     *
     * @param object
     * @param type
     */
    protected abstract void interceptor(Object object, int type);
}