package com.androidybp.basics.ui.mvc.fragment;

import android.view.LayoutInflater;
import android.view.View;

public interface PackagingFragmentLintener {

    /**

     * @return 子类要显示的View对象
     */
    View setInflaterView(LayoutInflater inflater);

    //子类必须实现  跟类型返回对应的json
    String getJson(int type);

    //初始化 访问的地址
    String getUrl(int type);
}
