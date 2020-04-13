package com.escort.carriage.android.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidybp.basics.entity.TabStripItemEntity;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.mvc.adapter.TabStripAdapter;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.ui.fragment.home.MyOrderAppraiseLeftFragment;
import com.escort.carriage.android.ui.fragment.home.MyOrderAppraiseRightFragment;
import com.escort.carriage.android.utils.tab.TabUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的订单中的 评价
 */
public class MyOrderAppraiseActivity extends ProjectBaseActivity {

    @BindView(R.id.frag_home_information_layout_tabstrip)
    TabLayout fragHomeInformationLayoutTabstrip;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private String[] titles = {"我的评价", "收到的评价"};
    private int positioin;
    private String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        setContentView(R.layout.activity_appraise_layout);
        ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
        setPageActionBar();
        positioin = getIntent().getIntExtra("position", 0);

        setTabLayout();

    }

    private void setTabLayout() {
        TabStripAdapter tabStripAdapter = new TabStripAdapter(getSupportFragmentManager(), fragHomeInformationLayoutTabstrip, this);
        vpOrder.setAdapter(tabStripAdapter);
        fragHomeInformationLayoutTabstrip.setupWithViewPager(vpOrder);

        ArrayList arr = new ArrayList<TabStripItemEntity>();
        TabStripItemEntity tabStripItemEntity = new TabStripItemEntity(titles[0], MyOrderAppraiseLeftFragment.class, getBundle(0));
        TabStripItemEntity tabStripItemEntityTwo = new TabStripItemEntity(titles[1], MyOrderAppraiseRightFragment.class, getBundle(1));
        arr.add(tabStripItemEntity);
        arr.add(tabStripItemEntityTwo);
        tabStripAdapter.addTabAll(arr);
        vpOrder.setCurrentItem(positioin);
        TabUtil.setIndicator(fragHomeInformationLayoutTabstrip, 8, 8);
    }

    private Bundle getBundle(int x) {

        Bundle bundle = new Bundle();

        switch (x) {
            case 0:
                bundle.putInt("type", 0);
                bundle.putString("orderNumber", orderNumber);
                break;
            case 1:
                bundle.putInt("type", 1);
                bundle.putString("orderNumber", orderNumber);
                break;

        }
        return bundle;
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("评价");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}