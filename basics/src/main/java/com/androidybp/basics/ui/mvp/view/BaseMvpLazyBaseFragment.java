package com.androidybp.basics.ui.mvp.view;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.mvp.presenter.PresenterFrginterface;


/**
 * 这个类只能用在 和 viewPager共同使用时 否则页面会出现问题
 * 为了使ViewPage中实现懒加载  桥接工具类
 *
 * 继承子 BaseListSmartLayoutFragment
 */
public abstract class BaseMvpLazyBaseFragment<T extends PresenterFrginterface, D> extends BaseMvpFragment<T, D> {
    protected boolean isPrepared;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    protected boolean isFirstResume = true;
    protected boolean isFirstVisible = true;
    protected boolean isFirstInvisible = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initPrepare(view, savedInstanceState);//测试懒加载
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            if (isFirstVisible) {
//                isFirstVisible = false;
                initPrepare(view, null);
//            } else {
//                onUserVisible();
//            }
        } else {
//            if (isFirstInvisible) {
//                isFirstInvisible = false;
//                onFirstUserInvisible();
//            } else {
                onUserInvisible();
//            }
        }
    }

    public synchronized void initPrepare(View view, @Nullable Bundle savedInstanceState) {
        if (isPrepared) {
            onFirstUserVisible(view, savedInstanceState);
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible(View view, @Nullable Bundle savedInstanceState) {
//        LogUtils.showE(TAG, "第一次fragment可见（进行初始化工作）onFirstUserVisible");
        if(pagePresenter == null){
            pagePresenter = createPresenter();

        }
        if(pagePresenter != null){
            pagePresenter.bindingView(this);
            pagePresenter.onViewCreated(view, savedInstanceState);
        }
        if (!isOpen) {
            simpleInitUI(view, savedInstanceState);
            isOpen = true;
        }
        initUI(view, savedInstanceState);
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
    }
}
