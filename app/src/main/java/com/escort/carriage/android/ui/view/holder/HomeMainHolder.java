package com.escort.carriage.android.ui.view.holder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.home.CircuitListEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.home.NewsListReqBean;
import com.escort.carriage.android.entity.response.home.NewsTitleListBean;
import com.escort.carriage.android.entity.response.home.ResponseCircuitListEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.mes.NewsListActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.fragment.home.HomeListFragment;
import com.escort.carriage.android.ui.fragment.home.HomeOpenPushFragment;
import com.escort.carriage.android.ui.fragment.home.HomePushInfoFragment;
import com.escort.carriage.android.ui.view.TextSwitchView;
import com.escort.carriage.android.ui.view.imgview.RoundImageView;
import com.escort.carriage.android.utils.mes.MesNumUtils;
import com.tripartitelib.android.iflytek.IflytekUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeMainHolder {

    @BindView(R.id.statusBarView)
    View statusBarView;
    @BindView(R.id.iv_head_img1)
    RoundImageView ivHeadImg;
    @BindView(R.id.tsv)
    TextSwitchView mTsv;
    @BindView(R.id.userSettingText)
    TextView userSettingText;


    private View viewGroup;
    private HomeActivity activity;
    private Unbinder bind;

    private HomeListFragment homeListFragment;
    private HomeOpenPushFragment homeOpenPushFragment;
    private HomePushInfoFragment homePushInfoFragment;
    private Fragment currentFragment;

    private int viewShowType = 0;//当前显示的类型
    public  CircuitListEntity cuitListEntity;

    public HomeMainHolder(HomeActivity activity, FrameLayout leftFrameLayout) {
        this.activity = activity;
        viewGroup = View.inflate(activity, R.layout.view_main_menu, null);
        leftFrameLayout.removeAllViews();
        leftFrameLayout.addView(viewGroup);
        bind = ButterKnife.bind(this, viewGroup);

        initView();
    }

    public View getMainGroupView() {
        return viewGroup;
    }

    public View findView(int resId){
        return viewGroup.findViewById(resId);
    }

    public void initView() {
        setStatusBar();
        getTitleList();
        startHomeListFragment();
        //获取配置参数
        getPageData(0);
    }

    private void startHomeListFragment() {
        viewShowType = 0;
        if(homeListFragment == null){
            homeListFragment = new HomeListFragment();
        }
        showFragment(homeListFragment);
    }

     private void startHomePushFragment() {
        viewShowType = 1;
        //未开启推送
         if(cuitListEntity.isOpen == 1){
            openHomePushInfoFragment();
        } else {
            openHomeOpenPushFragment();
        }

    }

    public void openHomeOpenPushFragment() {
        if(homeOpenPushFragment == null){
            homeOpenPushFragment = new HomeOpenPushFragment();
        }
        //将描述改为
        userSettingText.setText("未开启，订单暂不推送");
        homeOpenPushFragment.setMainHolder(this);
        showFragment(homeOpenPushFragment);
    }

    public void openHomePushInfoFragment() {
        if(homePushInfoFragment == null){
            homePushInfoFragment = new HomePushInfoFragment();
        }
        userSettingText.setText("已开启，订单正在推送");
        homePushInfoFragment.setMainHolder(this);
        showFragment(homePushInfoFragment);
    }

    private void setStatusBar() {
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(activity);
        ViewGroup.LayoutParams layoutParams = statusBarView.getLayoutParams();
        layoutParams.height = statusBarHeight;
        statusBarView.setLayoutParams(layoutParams);
    }

    public void updataUserInfo(UserInfoEntity entity) {
        GlideManager.getGlideManager().loadImageRoundFitCrop(entity.getHeadPictureUrl(), ivHeadImg, 100, R.mipmap.img_user_head_img_default);
    }

    //
    public void getTitleList() {

        RequestEntity requestEntity = new RequestEntity(0);
        NewsListReqBean reqBean = new NewsListReqBean();
        reqBean.setNewsType("2");//类别（1 发货方 2 司机端）
        requestEntity.setData(reqBean);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.SYSNEW_GETTITLELIST, jsonString).execute(new MyStringCallback<NewsTitleListBean>() {
            @Override
            public void onResponse(NewsTitleListBean s) {
                if (s != null) {
                    if (s.success) {
                        getTitleListResp(s);
                    }
                }
            }

            @Override
            public Class<NewsTitleListBean> getClazz() {
                return NewsTitleListBean.class;
            }
        });
    }

    private void getTitleListResp(NewsTitleListBean resp) {
        if (resp.isSuccess() && resp.data != null && resp.data.size() > 0) {
            String[] arr = new String[resp.getData().size()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = resp.getData().get(i).getTitle();
            }
            mTsv.setResources(arr);
            mTsv.setTextStillTime(3000);
//            mTsv.setChilClickListener(new TextSwitchView.ChilClickListener() {
//                @Override
//                public void onClick(int index) {
////                    Intent intent = new Intent(activity, NewsListActivity.class);
////                    activity.startActivity(intent);
//                }
//            });
        }
    }

    private void showFragment(Fragment fragment){
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String tag = fragment.getClass().getName();
        transaction.replace(R.id.frgGroupLayout, fragment, tag);
        transaction.show(fragment);
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        currentFragment = fragment;
        transaction.commit();
    }



    @OnClick({R.id.iv_head_img1, R.id.ll_order_setting, R.id.ll_goods_lobby, R.id.ll_news, R.id.ivMess})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_head_img1:
                activity.openLeftView();
                break;
            case R.id.ll_news:
                Intent intentNews = new Intent(activity, VueActivity.class);
                intentNews.putExtra("url", VueUrl.HeadLines);
                activity.startActivity(intentNews);
                break;

            case R.id.ivMess:
                MesNumUtils.getMesNumUtils().openMesAct(activity);
                break;
            case R.id.ll_order_setting:
                if(viewShowType == 0) {
                    if (cuitListEntity == null) {
                        //请求数据
                        getPageData(1);
                    } else {
                        startFragment(cuitListEntity);
                    }
                }

                break;
            case R.id.ll_goods_lobby:
                showHomeListFrag();
                break;
        }
    }

    /**
     * 显示货源大厅
     */
    public void showHomeListFrag() {
        if(viewShowType == 1){
            startHomeListFragment();
        }
    }


    private void startFragment(CircuitListEntity cuitListEntity) {


        startHomePushFragment();
    }

    /**
     * 0 : 只刷新数据 1：带页面切换的
     * @param type
     */
    public void getPageData(int type) {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(activity, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page", RequestEntityUtils.getPageBeanOrders(1, 100));
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVELINE_GETLIST, jsonString).execute(new MyStringCallback<ResponseCircuitListEntity>() {
            @Override
            public void onResponse(ResponseCircuitListEntity entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success && entity.data != null) {
                        cuitListEntity = entity.data;
                        if(cuitListEntity.isOpen == 0){
                            CacheDBMolder.getInstance().setNotification(false);
                            //设置文字提示
                            userSettingText.setText("未开启，订单暂不推送");
                        } else {
                            CacheDBMolder.getInstance().setNotification(true);
                            userSettingText.setText("已开启，订单正在推送");
                        }
                        if(type == 1){
                            startFragment(cuitListEntity);
                        }

                    } else {
                        ToastUtil.showToastString(entity.message);
                    }
                }
            }

            @Override
            public Class<ResponseCircuitListEntity> getClazz() {
                return ResponseCircuitListEntity.class;
            }
        });
    }



    public void clear() {
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
        viewGroup = null;
        activity = null;

    }


}
