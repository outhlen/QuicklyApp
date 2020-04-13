package com.androidybp.basics.ui.mvp.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;

public interface PresenterFrginterface<T extends MvpViewInterface> {

    /**
     * 绑定View的接口
     */
    void bindingView(T view);

    /**
     * 点击事件
     * @param type 类型
     */
    void onClick(int type, Object obj);

    /**
     * 退出销毁数据的方法
     */
    void clear();

    void onCreate(@Nullable Bundle savedInstanceState);

    void onCreateView();

    void onActivityCreated(@Nullable Bundle savedInstanceState);

    void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroyView();

    void onDestroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);


}
