package com.escort.carriage.android.ui.activity.mes;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidybp.basics.entity.TabStripItemEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.adapter.TabStripAdapter;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.fragment.home.HomeListFragment;
import com.escort.carriage.android.ui.fragment.home.MyOrderListFragment;
import com.escort.carriage.android.utils.mes.MesNumUtils;
import com.escort.carriage.android.utils.tab.TabUtil;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderListActivity extends ProjectBaseActivity {

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

    private String[] titles = {"待提货", "装货中", "运输中", "待结算", "待评价","已撤单"};
    private int positioin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        setContentView(R.layout.activity_my_order_list);
        ButterKnife.bind(this);
        setTitleBar();
        positioin = getIntent().getIntExtra("position", 0);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setTabLayout();
    }

    private void setTabLayout() {
        TabStripAdapter tabStripAdapter = new TabStripAdapter(getSupportFragmentManager(), fragHomeInformationLayoutTabstrip, this);
        vpOrder.setAdapter(tabStripAdapter);
        fragHomeInformationLayoutTabstrip.setupWithViewPager(vpOrder);
        ArrayList arr = new ArrayList<TabStripItemEntity>();
        for (int x = 0; x < titles.length; x++) {
            TabStripItemEntity tabStripItemEntity = new TabStripItemEntity(titles[x], MyOrderListFragment.class, getBundle(x));
            arr.add(tabStripItemEntity);
        }
        tabStripAdapter.addTabAll(arr);
        setCurrentItem(positioin);
    }

    private void setCurrentItem(int positioin) {
        if(vpOrder != null && positioin >= 0 && positioin < titles.length && this.positioin != positioin){
            vpOrder.setCurrentItem(positioin);
            this.positioin = positioin;
        }
    }

    /**
     * 更新页面数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updataCurrent(String str) {
        String text = "MyOrderListActivity_updata_positon = ";
        if(!TextUtils.isEmpty(str) && str.contains(text)){
            try {
                String replace = str.replace(text, "");
                int integer = Integer.valueOf(replace);
                setCurrentItem(integer);
            }catch (Exception e){

            }
        }
    }

    private Bundle getBundle(int x) {
        Bundle bundle = new Bundle();
        switch (x) {
            case 0:
                bundle.putInt("type", 1);
                break;
            case 1:
                bundle.putInt("type", 2);
                break;
            case 2:
                bundle.putInt("type", 5);
                break;
            case 3:
                bundle.putInt("type", 7);
                break;
             case 4:
                bundle.putInt("type", 8);
                break;
            case 5:
                bundle.putInt("type", -1);
                break;

        }
        return bundle;
    }

    private void setTitleBar() {
        itemHeadBarTvTitle.setText("我的订单");
//        itemHeadBarIvRight.setVisibility(View.VISIBLE);
//        itemHeadBarIvRight.setImageResource(R.mipmap.img_index_msg);
//        itemHeadBarTvRed.setVisibility(View.VISIBLE);
//        itemHeadBarTvRed.setText("2");

    }

    @OnClick({R.id.item_head_bar_iv_back, R.id.item_head_bar_tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_head_bar_iv_back:
                finish();
                break;
            case R.id.item_head_bar_tv_right:
                MesNumUtils.getMesNumUtils().openMesAct(this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
