package com.escort.carriage.android.utils.tab;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * Created by yangbp
 * <p>
 * 适配 androidx com.google.android.material.tabs.TabLayout 的工具类
 */

public class TabUtil {
    /**
     * 设置下划线的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("slidingTabIndicator");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static void setTabTextView(TabLayout tabLayout, int leftDip, int rightDip) {
        try {

            for (int x = 0; x < tabLayout.getTabCount(); x++) {
                TabLayout.Tab tab = tabLayout.getTabAt(x);
                Field mTabStripField = tab.getClass().getDeclaredField("view");
                mTabStripField.setAccessible(true);

                View tabView = (View) mTabStripField.get(tab);

                Field mTextViewField = tabView.getClass().getDeclaredField("textView");
                mTextViewField.setAccessible(true);

                TextView textView = (TextView) mTextViewField.get(tabView);

                int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
                int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
                tabView.setPadding(0, 0, 0, 0);
                //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                int width = 0;
                width = textView.getWidth();
                if (width == 0) {
                    textView.measure(0, 0);
                    width = textView.getMeasuredWidth();
                }

                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                params.width = width;
                params.leftMargin = left;
                params.rightMargin = right;
                tabView.setLayoutParams(params);

                tabView.invalidate();


            }
//            //拿到tabLayout的mTabStrip属性
//            Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
//            mTabStripField.setAccessible(true);
//
//            LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);
//
//            int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
//            int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
//
//            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
//                View tabView = mTabStrip.getChildAt(i);
//
//                //拿到tabView的mTextView属性
//                Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
//                mTextViewField.setAccessible(true);
//
//                TextView mTextView = (TextView) mTextViewField.get(tabView);
//
//                tabView.setPadding(0, 0, 0, 0);
//
//                //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
//                int width = 0;
//                width = mTextView.getWidth();
//                if (width == 0) {
//                    mTextView.measure(0, 0);
//                    width = mTextView.getMeasuredWidth();
//                }
//
//                //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
//                params.width = width ;
//                params.leftMargin = left;
//                params.rightMargin = right;
//                tabView.setLayoutParams(params);
//
//                tabView.invalidate();
//            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
