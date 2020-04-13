package com.androidybp.basics.ui.mvc.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 这是一个Fragment 基本的类
 */
public class BaseFragment extends Fragment {
    protected static final String TAG = BaseFragment.class.getSimpleName();
    protected long frontClickTime;
    public boolean pageExist = true;//当前页面是否存在
    public boolean pageVisible = true;//当前页面是否可见
    public boolean pageFocus = true;//判断当前页面是否可以获取焦点
    public Activity mActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        pageVisible = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        pageFocus = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        pageFocus = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        pageVisible = false;
    }

    @Override
    public void onDestroyView() {
        pageExist = false;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    /**
     * 判断用户是否是频繁点击，判断条件是在一秒内不能频繁点击
     *
     * @return true -- 是频繁点击    ；  false -- 不是频繁点击
     */
    protected synchronized boolean isFrequentlyClick() {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - frontClickTime) > 800) {
            frontClickTime = currentClickTime;
            return false;
        }
        frontClickTime = currentClickTime;
        return true;
    }

}
