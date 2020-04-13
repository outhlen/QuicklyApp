package com.androidybp.basics.ui.mvc.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.androidybp.basics.entity.TabStripItemEntity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Tablayout和Viewpage结合使用的管理类
 */

public class TabStripAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private final TabLayout tabStrip;

    // 数据写到Adapter
    List<Class<?>> fragments;
    List<Bundle> bundles;
    List<String> tabText;

    public TabStripAdapter(FragmentManager fm, TabLayout tabStrip,
                           Context context) {
        super(fm);
        this.context = context;
        this.tabStrip = tabStrip;
    }

    /**
     * 设置全部数据
     * @param tabText
     * @param fragments
     * @param bundles
     */
    public void setData(List<String> tabText, List<Class<?>> fragments, List<Bundle> bundles){
        this.tabText = tabText;
        this.fragments = fragments;
        this.bundles = bundles;
        notifyDataSetChanged();
    }

    public void addTab(String tab, Class<?> fragment, Bundle bundles){
        if(tabText == null)
            tabText = new ArrayList<>();
        if(fragments == null)
            fragments = new ArrayList<>();
        if(this.bundles == null)
            this.bundles = new ArrayList<>();
        addTabStripData(tab, fragment, bundles);
        notifyDataSetChanged();

    }

    private void addTabStripData(String tab, Class<?> fragment, Bundle bundles) {
        tabText.add(tab);
        fragments.add(fragment);
        this.bundles.add(bundles);
    }

    /**
     * 给Adapter 设置数值
     *
     * @param list
     */
    public void setTab(List<TabStripItemEntity> list) {
        if (list == null || list.size() < 1) {
            return;
        }
        if(tabText == null)
            tabText = new ArrayList<>();
        if(fragments == null)
            fragments = new ArrayList<>();
        if(this.bundles == null)
            this.bundles = new ArrayList<>();

        tabStrip.removeAllViews();
        bundles.clear();
        fragments.clear();
        for (TabStripItemEntity hism : list) {
            addTabStripData(hism.titleStr, hism.fragment, hism.bundle);
        }
        notifyDataSetChanged();
    }

    public void addTabAll(List<TabStripItemEntity> list){
        if (list == null || list.size() < 1) {
            return;
        }
        if(tabText == null)
            tabText = new ArrayList<>();
        if(fragments == null)
            fragments = new ArrayList<>();
        if(this.bundles == null)
            this.bundles = new ArrayList<>();
        for (TabStripItemEntity hism : list) {
            addTabStripData(hism.titleStr, hism.fragment, hism.bundle);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(fragments == null)
            return 0;
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(context, fragments.get(position).getName(), bundles.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(tabText == null || tabText.size() < 1 || tabText.size() < position)
            return null;
        return tabText.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;   // 返回发生改变，让系统重新加载
        // 系统默认返回的是     POSITION_UNCHANGED，未改变
        // return super.getItemPosition(object);
    }

    public void clearData() {
        tabStrip.removeAllViews();
        fragments.clear();
        bundles.clear();
        this.notifyDataSetChanged();
    }

}