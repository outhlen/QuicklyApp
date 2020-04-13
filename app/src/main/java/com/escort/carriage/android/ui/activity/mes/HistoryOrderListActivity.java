package com.escort.carriage.android.ui.activity.mes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidybp.basics.entity.TabStripItemEntity;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.mvc.adapter.TabStripAdapter;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.ui.fragment.home.HistoryOrderListFragment;
import com.escort.carriage.android.ui.fragment.home.HomeListFragment;
import com.escort.carriage.android.ui.fragment.home.MyOrderListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 历史订单
 */
public class HistoryOrderListActivity extends ProjectBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        setContentView(R.layout.activity_history_order_list);
        ButterKnife.bind(this);
        setPageActionBar();
        showFragment();
    }


    private void setPageActionBar() {
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("历史订单");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showFragment(){
        HistoryOrderListFragment fragment = new HistoryOrderListFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String tag = fragment.getClass().getName();
        transaction.replace(R.id.fragmeLayout, fragment, tag);
        transaction.show(fragment);
        transaction.commit();
    }

}

