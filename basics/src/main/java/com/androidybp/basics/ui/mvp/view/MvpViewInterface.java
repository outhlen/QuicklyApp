package com.androidybp.basics.ui.mvp.view;

public interface MvpViewInterface<T> {

    void setPageView(T t, int type);

    void showUplodingAnim(int type, String str);

    void hintUplodingAnim(int type, boolean isFlag);

    void clear();

}
