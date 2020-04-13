package com.androidybp.basics.entity;

import android.os.Bundle;

/**
 * TabStrip  item 信息封装类
 */

public class TabStripItemEntity {
    public String titleStr;
    public Class<?> fragment;
    public Bundle bundle;

    public TabStripItemEntity() {
    }

    public TabStripItemEntity(String titleStr, Class<?> fragment, Bundle bundle) {
        this.titleStr = titleStr;
        this.fragment = fragment;
        this.bundle = bundle;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public Class<?> getFragment() {
        return fragment;
    }

    public void setFragment(Class<?> fragment) {
        this.fragment = fragment;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
