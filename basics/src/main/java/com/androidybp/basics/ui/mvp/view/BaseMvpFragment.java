package com.androidybp.basics.ui.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.ui.mvp.presenter.PresenterFrginterface;

public abstract class BaseMvpFragment<T extends PresenterFrginterface, D> extends BaseFragment implements MvpViewInterface<D> {

    protected T pagePresenter;
    protected boolean isOpen = false;
    protected View view;


    protected abstract View getPageView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void findView(View view);

    protected abstract void simpleInitUI(View view, Bundle savedInstanceState);

    /**
     * 创建当前页面强绑定的
     *
     * @return
     */
    public abstract T createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagePresenter = createPresenter();
        if (pagePresenter != null) {
            pagePresenter.onCreate(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pageExist = true;
        if (view == null) {
            view = getPageView(inflater, container, savedInstanceState);
            findView(view);
        } else {
            findView(view);
        }
        if (pagePresenter != null) {
            pagePresenter.onCreateView();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (pagePresenter != null) {
            pagePresenter.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (!isOpen) {
            if (pagePresenter != null) {
                pagePresenter.bindingView(this);
                pagePresenter.onViewCreated(view, savedInstanceState);
            }
            simpleInitUI(view, savedInstanceState);
            isOpen = true;
        } else {
            if (pagePresenter != null) {
                pagePresenter.onViewCreated(view, savedInstanceState);
            }
        }


        initUI(view, savedInstanceState);
    }

    protected void initUI(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (pagePresenter != null) {
            pagePresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pagePresenter != null) {
            pagePresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pagePresenter != null) {
            pagePresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (pagePresenter != null) {
            pagePresenter.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
        if (pagePresenter != null) {
            pagePresenter.onDestroyView();
        }
    }

    public <T extends View> T findView(int idRes){
        return view.findViewById(idRes);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pagePresenter != null) {
            pagePresenter.onDestroy();
            pagePresenter = null;
        }
        isOpen = false;
        view = null;
        clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (pagePresenter != null) {
            pagePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (pagePresenter != null) {
            pagePresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showUplodingAnim(int type, String str) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(mActivity, str);
    }

    @Override
    public void hintUplodingAnim(int type, boolean isFlag) {
        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
    }

}
