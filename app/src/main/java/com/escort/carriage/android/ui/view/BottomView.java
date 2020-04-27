package com.escort.carriage.android.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.escort.carriage.android.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BottomView extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG_ReleaseSource = "ReleaseSourceFragment";
    private static final String TAG_Direct = "DirectFragment";
    private static final String TAG_CityDelivery = "CityDeliveryFragment";

    private Context mContext;
    private RadioGroup  mRgBox;
    private RadioButton mRbReleaseSource;
    private RadioButton mRbDirect;
    private RadioButton mRbCityDelivery;

    private Fragment currentFragment;

    private ScrollLayout mScrollLayout;

    public BottomView(Context context) {
        this(context, null);
    }

    public BottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = inflate(mContext, R.layout.view_bottom, this);
        mRgBox = view.findViewById(R.id.rg_box);
        mRbReleaseSource = view.findViewById(R.id.rb_release_source);
        mRbDirect = view.findViewById(R.id.rb_direct);
        mRbCityDelivery = view.findViewById(R.id.rb_city_delivery);

        mRgBox.setOnCheckedChangeListener(this);
        mRbReleaseSource.setChecked(true);
        mRbDirect.setChecked(true);
        mRbReleaseSource.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        int position = 0;
        switch (checkedId) {
            case R.id.rb_release_source://发布货源
                position = 0;
                break;
            case R.id.rb_direct://专线直达
                position = 1;
                break;
            case R.id.rb_city_delivery://城市配送
                position = 2;
                break;
        }
    }

    private void showFragment(int index) {
        FragmentManager manager = ((ProjectBaseActivity) mContext).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = null;
        String tag = null;
        switch (index) {
            case 0:
//                tag = TAG_ReleaseSource;// 分类
//                fragment = manager.findFragmentByTag(TAG_ReleaseSource);
//                if (fragment == null) {
//                    fragment = setFragmentArguments(ReleaseSourceFragment.getInstance());
//                }
                break;
            case 1:
//                tag = TAG_Direct;// 分类
//                fragment = manager.findFragmentByTag(TAG_Direct);
//                if (fragment == null) {
//                    fragment = setFragmentArguments(DirectFragment.getInstance());
//                }
                break;
            case 2:
//                tag = TAG_CityDelivery;// 活动
//                fragment = manager.findFragmentByTag(TAG_CityDelivery);
//                if (fragment == null) {
//                    fragment = setFragmentArguments(CityDeliveryFragment.getInstance());
//                }
                break;
            default:
                return;
        }
        if (!fragment.isAdded())
            transaction.add(R.id.fl_main_content, fragment, tag);
        transaction.show(fragment);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        currentFragment = fragment;
        transaction.commit();
    }

    public Fragment setFragmentArguments(Fragment fragment) {
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setScrollLayout(ScrollLayout scrollLayout) {
        mScrollLayout = scrollLayout;
    }
}
