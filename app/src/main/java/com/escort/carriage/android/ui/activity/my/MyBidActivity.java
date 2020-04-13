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
import com.escort.carriage.android.R;
import com.escort.carriage.android.ui.fragment.home.MyOrderListFragment;
import com.escort.carriage.android.ui.fragment.my.MyBidListFragment;
import com.escort.carriage.android.utils.tab.TabUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的投标
 */
public class MyBidActivity  extends ProjectBaseActivity {

    @BindView(R.id.item_head_bar_iv_back)
    ImageView itemHeadBarIvBack;
    @BindView(R.id.item_head_bar_tv_title)
    TextView itemHeadBarTvTitle;
    @BindView(R.id.item_head_bar_iv_right)
    ImageView itemHeadBarIvRight;
    @BindView(R.id.item_head_bar_tv_red)
    TextView itemHeadBarTvRed;
    @BindView(R.id.item_head_bar_tv_right)
    TextView itemHeadBarTvRight;
    @BindView(R.id.item_head_bar_rl)
    RelativeLayout itemHeadBarRl;
    @BindView(R.id.frag_home_information_layout_tabstrip)
    TabLayout fragHomeInformationLayoutTabstrip;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private String[] titles = {"投标中", "未中标"};
    private int positioin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        setContentView(R.layout.activity_my_bid_list);
        ButterKnife.bind(this);
        setTitleBar();
        positioin = getIntent().getIntExtra("position", 0);

        setTabLayout();

    }

    private void setTabLayout() {
        TabStripAdapter tabStripAdapter = new TabStripAdapter(getSupportFragmentManager(), fragHomeInformationLayoutTabstrip, this);
        vpOrder.setAdapter(tabStripAdapter);
        fragHomeInformationLayoutTabstrip.setupWithViewPager(vpOrder);

        ArrayList arr = new ArrayList<TabStripItemEntity>();
        for (int x = 0; x < titles.length; x++) {
            TabStripItemEntity tabStripItemEntity = new TabStripItemEntity(titles[x], MyBidListFragment.class, getBundle(x));
            arr.add(tabStripItemEntity);

        }
        tabStripAdapter.addTabAll(arr);
        vpOrder.setCurrentItem(positioin);
        TabUtil.setIndicator(fragHomeInformationLayoutTabstrip, 8, 8);
    }

    private Bundle getBundle(int x) {

        Bundle bundle = new Bundle();

        switch (x) {
            case 0:
                bundle.putInt("type", 0);
                break;
            case 1:
                bundle.putInt("type", 1);
                break;

        }
        return bundle;
    }

    private void setTitleBar() {
        itemHeadBarTvTitle.setText("我的投标");
//        itemHeadBarIvRight.setImageResource(R.mipmap.img_index_msg);
        itemHeadBarTvRed.setVisibility(View.GONE);
//        itemHeadBarTvRed.setText("2");

    }

    @OnClick({R.id.item_head_bar_iv_back, R.id.item_head_bar_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_head_bar_iv_back:
                finish();
                break;
            case R.id.item_head_bar_tv_right:
                break;
        }
    }


}