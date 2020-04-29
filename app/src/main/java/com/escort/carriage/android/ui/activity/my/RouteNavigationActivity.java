package com.escort.carriage.android.ui.activity.my;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.entity.bean.home.AddrBean;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.utils.ChineseNumUtill;
import com.tripartitelib.android.amap.AmapUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 路线导航
 */
public class RouteNavigationActivity extends Activity {

    @BindView(R.id.llLocationGroup)
    LinearLayout llLocationGroup;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.finishView)
    View finishView;
    private OrderInfoEntity infoEntity;
    ScaleAnimation scaleAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_route_navigation);
        ButterKnife.bind(this);
        scaleAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF,
                0.9f);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.setDuration(1200);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setFillAfter(true);
        String json = getIntent().getStringExtra("json");
        infoEntity = JsonManager.getJsonBean(json, OrderInfoEntity.class);
        setLocation();

        finishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setLocation() {
        int truckLoadingNum = getTruckLoadingNum();
        int unloadNum = getUnloadNum();
        if (infoEntity.addr != null && infoEntity.addr.size() > 0) {
            llLocationGroup.removeAllViews();
            ////设置 多条装车数据
            setTruckLoadingView(truckLoadingNum);
            //设置多条卸车数据
            setUnloadNum(unloadNum);
        }

    }

    private void setUnloadNum(int unloadNum) {
        if (unloadNum > infoEntity.addr.size()) {
            unloadNum = infoEntity.addr.size();
        }


        for (int x = 0; x < unloadNum; x++) {
            View inflate = View.inflate(this, R.layout.item_order_route_navigation_layout, null);
            AddrBean addrBean = infoEntity.addr.get(x);
            String addrStr = addrBean.endProvinceName + addrBean.endCityName + addrBean.endAreaName + addrBean.endAddr;
            setTruckLoadingAndUnloadView(1, x, inflate, addrStr, addrBean);
        }
    }

    private void setTruckLoadingView(int truckLoadingNum) {
        if (truckLoadingNum > infoEntity.addr.size()) {
            truckLoadingNum = infoEntity.addr.size();
        }


        for (int x = 0; x < truckLoadingNum; x++) {
            View inflate = View.inflate(this, R.layout.item_order_route_navigation_layout, null);
            AddrBean addrBean = infoEntity.addr.get(x);
            String addrStr = addrBean.startProvinceName + addrBean.startCityName + addrBean.startAreaName + addrBean.startAddr;
            setTruckLoadingAndUnloadView(0, x, inflate, addrStr, addrBean);
        }
    }

    private void setTruckLoadingAndUnloadView(int type, int position, View viewGroup, String addrStr, AddrBean addrBean) {
        ImageView iv = viewGroup.findViewById(R.id.ivItem);
        ImageView ivToGaode = viewGroup.findViewById(R.id.ivToGaode);
        TextView tv = viewGroup.findViewById(R.id.tvItem);
        String str;
        double latitude;
        double longitude;
        if (type == 0) {
            str = "(第" + ChineseNumUtill.numberToChinese(position + 1) + "装货地)";
            latitude = addrBean.startLatitude;
            longitude = addrBean.startLongitude;
            ivToGaode.setImageResource(R.drawable.icon_navi_y);
            if (position < 1) {
                iv.setImageResource(R.drawable.icon_text_zhuang);
            } else {
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.height = ResourcesTransformUtil.dip2px(8);
                layoutParams.width = ResourcesTransformUtil.dip2px(8);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.drawable.bg_b_ffdd29_circular_size);
            }

        } else {
            str = "(第" + ChineseNumUtill.numberToChinese(position + 1) + "卸货地)";
            latitude = addrBean.endLatitude;
            longitude = addrBean.endLongitude;
            ivToGaode.setImageResource(R.drawable.icon_navi_b);
            if (position < 1) {
                iv.setImageResource(R.drawable.icon_text_xie);
            } else {
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                layoutParams.height = ResourcesTransformUtil.dip2px(8);
                layoutParams.width = ResourcesTransformUtil.dip2px(8);
                iv.setLayoutParams(layoutParams);
                iv.setImageResource(R.drawable.bg_b_3699ff_circular_size);
            }
        }
        SpannableString spannableString = new SpannableString(addrStr + str);
        //设置字体前景色
        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), addrStr.length(), spannableString.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色

        tv.setText(spannableString);
        ivToGaode.startAnimation(scaleAnim);
        ivToGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmapUtils.getAmapUtils().openGaoDe(RouteNavigationActivity.this, latitude, longitude);
            }
        });
        llLocationGroup.addView(viewGroup);
    }

    /**
     * 装车地点数
     *
     * @return
     */
    private int getTruckLoadingNum() {
        int num = 1;
        try {
            String substring = infoEntity.loadNumAndDischargeNum.substring(0, 1);
            num = Integer.valueOf(substring);
        } catch (Exception e) {

        }
        return num;
    }

    /**
     * 卸车地点数
     *
     * @return
     */
    private int getUnloadNum() {

        int num = 1;
        try {
            String substring = infoEntity.loadNumAndDischargeNum.substring(2, 3);
            num = Integer.valueOf(substring);
        } catch (Exception e) {

        }
        return num;
    }


}
