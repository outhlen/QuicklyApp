package com.escort.carriage.android.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.GoodsBean;
import com.escort.carriage.android.entity.bean.home.SelectOrderEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.home.HomeListRequestEnity;
import com.escort.carriage.android.entity.response.home.HomeListEntity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.activity.mes.OrderInfoActivity;
import com.escort.carriage.android.ui.activity.mes.SelectOrderActivity;
import com.escort.carriage.android.ui.adapter.home.OrderAdapter;
import com.escort.carriage.android.ui.view.dialog.SelectpRrovinceCityDialog;
import com.escort.carriage.android.ui.view.text.DrawableTextView;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author Yangbp
 * @description:
 * @date :2020-03-17 15:06
 */
public class HomeListFragment extends BaseFragment implements OnRefreshLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.re_fresh_layout)
    SmartRefreshLayout reFreshLayout;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    @BindView(R.id.iv_empty)
    ImageView iv_empty;
    @BindView(R.id.tv_empty_refresh)
    TextView tv_empty_refresh;
    @BindView(R.id.tvItem01)
    DrawableTextView tvItem01;
    @BindView(R.id.tvItem02)
    DrawableTextView tvItem02;
    @BindView(R.id.tvItem04)
    DrawableTextView tvItem04;

    private String mId;
    private View view;
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isHandleData;
    private int allPage = Integer.MAX_VALUE;
    public OrderAdapter adapter;
    private int page = 1;
    private int allPages = Integer.MAX_VALUE;
    private HomeListRequestEnity homeListRequestEnity;
    private long requestTime;
    private boolean isResumeNotUpdata = false;
    private int pageIndex = 1;
    private boolean hasMore = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_recycler, container, false);
            unbinder = ButterKnife.bind(this, view);
            initRefreshLayout();
            isPrepared = true;
            onVisible();
            getOrderList(page,reFreshLayout);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }





    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
        }
    }





    private void onVisible() {
        if (isPrepared && isVisible && !isHandleData) {
            page = 1;
//            initDatas();
        }
    }

    /**
     * 更新页面数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGameWindowChangedEvent(String e) {
        if (TextUtils.equals("homeAct_updat_data", e)) {
            getOrderList(page,reFreshLayout);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        page = 1;
//        getOrderList(page,reFreshLayout);
    }

//    private void initDatas() {
//        long currentTimeMillis = System.currentTimeMillis();
//        long time = currentTimeMillis - requestTime;
//        if (time < 1500) {
//            return;
//        }
//        requestTime = currentTimeMillis;
//        //  isHandleData = true;
//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
//        RequestEntity requestEntity = new RequestEntity(0);
//        if (homeListRequestEnity == null) {
//            homeListRequestEnity = new HomeListRequestEnity();
//        }
//        homeListRequestEnity.page = RequestEntityUtils.getPageBeanOrders(page, 10);
//        requestEntity.setData(homeListRequestEnity);
//        String jsonString = JsonManager.createJsonString(requestEntity);
//        Log.e("OkgoUtils>>>", "page==>>" + page + "Loadmore=" + jsonString);
//        // 货源大厅推送列表
//        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_LISTMATCHING, jsonString).execute(new MyStringCallback<HomeListEntity>() {
//            @Override
//            public void onResponse(HomeListEntity entity) {
//                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
//                if (null != rlRefresh) {
//                    rlRefresh.setVisibility(View.VISIBLE);
//                    if (entity != null && entity.getData() != null
//                            && entity.getData().getList() != null
//                            && entity.getData().getList().size() > 0) {
//                        rlRefresh.endLoadingMore();
//                        rlRefresh.endRefreshing();
//                        //有数据
//                        List<GoodsBean> list = entity.getData().getList();
//                        if (entity.data.isLastPage) {
//                            allPage = page;
//                        }
//                        allPages = entity.data.pages;
//                        if (page == allPages) {
//                            isHandleData = false;
//                            ToastUtil.showToastString("没有更多了");
//                        }
//                        Log.e("OkgoUtils>>>", "pageSize==" + entity.data.pages + "list==>>" + list.size());
//                        ll_empty.setVisibility(View.GONE);
//                        if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {
//                            showRv(list);
//                        } else {
//                            if (adapter != null) {
//                                if (page <= 1) {
//                                    adapter.replaceData(list);
//                                } else {
//                                    adapter.addData(list);
////                                    newsItemList.addAll(list);
//                                }
////                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    } else {
//                        if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {
//                            //第一次加载有数据
//                            page--;
//                            adapter.notifyDataSetChanged();
//                            ToastUtil.showToastString("没有更多了");
//                        } else {//第一次加载没有数据
//                            rlRefresh.setVisibility(View.GONE);
//                            ll_empty.setVisibility(View.VISIBLE);
//                            iv_empty.setBackgroundResource(R.drawable.shuaxin);
//                            tv_empty_refresh.setVisibility(View.GONE);
//                            tv_empty.setText("没有任何信息");
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onError(Response<String> response) {
//                super.onError(response);
//                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
//                isHandleData = false;
//                if (null != rlRefresh) {
//                    rlRefresh.endLoadingMore();
//                    rlRefresh.endRefreshing();
//                    rlRefresh.setVisibility(View.GONE);
//                }
//                ll_empty.setVisibility(View.VISIBLE);
//                iv_empty.setBackgroundResource(R.drawable.shuaxin);
//                tv_empty.setVisibility(View.VISIBLE);
//                tv_empty.setText("当前网络不佳，刷新试试！");
//                tv_empty_refresh.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public Class<HomeListEntity> getClazz() {
//                return HomeListEntity.class;
//            }
//        });
//
//
//    }


    private void showRv(List<GoodsBean> list) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            // 查看竞价详情 todo:
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsBean bean = (GoodsBean) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                intent.putExtra("id", bean.getOrderNumber());
                startActivityForResult(intent, 333);
            }
        });

        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

            }
        });
    }


    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        // rlRefresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格
        //    rl_refresh.setRefreshViewHolder(meiTuanRefreshViewHolder);
//        rlRefresh.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(), true));
//        // 设置正在加载更多时不显示加载更多控件
//        rlRefresh.setIsShowLoadingMoreView(true);

        reFreshLayout.setOnRefreshLoadMoreListener(this);
        reFreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        reFreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
       // initRefreshLayout(reFreshLayout);
    }


//    @Override
//    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
//        page = 1;
//        initDatas();
//        rlRefresh.endRefreshing();
//    }
//
//    @Override
//    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        if (page <= allPages) { //不到
//            page++;
//            initDatas();
//        } else {
//            rlRefresh.endLoadingMore();
//        }
//        Log.e("onBeginLoadingMore", "allPages==" + allPages + "curr page==" + page);
//        return true;
//
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        unbinder.unbind();
    }

    @OnClick(R.id.tv_empty_refresh)
    public void onViewClicked() {
        onVisible();
    }


    @OnClick({R.id.tvItem01, R.id.tvItem02, R.id.tvItem04})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvItem01:
                //打开地区选择器
                SelectpRrovinceCityDialog.getInstance().setContext(getActivity())
                        .setFlag(tvItem01)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
//                                ((TextView)flag).setText(provinceBean.getProvince() + " " + cityBean.getCity() + "  " + areaBean.getArea());
                                homeListRequestEnity.startCityCode = cityBean.getCityCode();
                                tvItem01.setText(cityBean.getCity());
                                tvItem01.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_bottom_jt));
                               getOrderList(page,reFreshLayout);
                            }
                        })
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());

                break;
            case R.id.tvItem02:
                //打开地区选择器
                SelectpRrovinceCityDialog.getInstance().setContext(getActivity())
                        .setFlag(tvItem01)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
//                                ((TextView)flag).setText(provinceBean.getProvince() + " " + cityBean.getCity() + "  " + areaBean.getArea());
                                homeListRequestEnity.endCityCode = cityBean.getCityCode();
                                tvItem02.setText(cityBean.getCity());
                                tvItem02.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_bottom_jt));
                                getOrderList(page,reFreshLayout);
                            }
                        })
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());

                break;
            case R.id.tvItem04:
                Intent intent = new Intent(getActivity(), SelectOrderActivity.class);
                intent.putExtra("json", JsonManager.createJsonString(getSelectOrderEntity()));
                startActivityForResult(intent, 333);
                break;
        }
    }

    private SelectOrderEntity getSelectOrderEntity() {
        SelectOrderEntity entity = new SelectOrderEntity();
        entity.orderType = homeListRequestEnity.orderType;
        entity.cargoName = homeListRequestEnity.cargoName;
        entity.startWeight = homeListRequestEnity.cargoWeight1;
        entity.endWeight = homeListRequestEnity.cargoWeight2;
        entity.startVolume = homeListRequestEnity.cargoVolume1;
        entity.endVolume = homeListRequestEnity.cargoVolume2;
        entity.orderPlaceTime = homeListRequestEnity.orderPlaceTime1;
        entity.endDate = homeListRequestEnity.orderPlaceTime2;
        return entity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == 666 && data != null) {
            isResumeNotUpdata = true;
            SelectOrderEntity entity = JsonManager.getJsonBean(data.getStringExtra("json"), SelectOrderEntity.class);
            homeListRequestEnity.orderType = entity.orderType;
            homeListRequestEnity.cargoName = entity.cargoName;
            homeListRequestEnity.cargoWeight1 = entity.startWeight;
            homeListRequestEnity.cargoWeight2 = entity.endWeight;
            homeListRequestEnity.cargoVolume1 = entity.startVolume;
            homeListRequestEnity.cargoVolume2 = entity.endVolume;
            homeListRequestEnity.orderPlaceTime1 = entity.orderPlaceTime;
            homeListRequestEnity.orderPlaceTime2 = entity.endDate;

        } else if (requestCode == 333 && resultCode == 665) {
            //重置起始地 目的地 数据
            homeListRequestEnity = new HomeListRequestEnity();
            tvItem01.setText("出发地");
            tvItem02.setText("目的地");
            tvItem01.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_top_jt));
            tvItem02.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_top_jt));
        }
        page = 1;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (hasMore) {
            page++;
            getOrderList(page, refreshLayout);
        } else {
            refreshLayout.finishLoadMore(1000);
        }
    }

    private void getOrderList(int pageNum, RefreshLayout refreshLayout) {
        long currentTimeMillis = System.currentTimeMillis();
        long time = currentTimeMillis - requestTime;
//        if (time < 1500) {
//            return;
//        }
        requestTime = currentTimeMillis;
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        if (homeListRequestEnity == null) {
            homeListRequestEnity = new HomeListRequestEnity();
        }
        homeListRequestEnity.page = RequestEntityUtils.getPageBeanOrders(pageNum, 10);
        requestEntity.setData(homeListRequestEnity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        Log.e("OkgoUtils>>>", "page==>>" + pageNum + "Loadmore=" + jsonString);
        // 货源大厅推送列表
        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_LISTMATCHING, jsonString).execute(new MyStringCallback<HomeListEntity>() {
            @Override
            public void onResponse(HomeListEntity entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (pageNum == 1) {
                    if (refreshLayout != null) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setNoMoreData(false);
                    }
                } else {
                    if (refreshLayout != null) {
                        refreshLayout.finishLoadMore();
                    }
                }
                if (entity.getData() == null) {
                    hasMore = false;
                    refreshLayout.setEnableLoadMore(false);
                    ll_empty.setVisibility(View.VISIBLE);
                    tv_empty.setText("暂无订单信息");
                }else{
                    List<GoodsBean> list  = entity.getData().list;
                    ll_empty.setVisibility(View.GONE);
                    if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {
                        showRv(list);
                    } else {
                        if (adapter != null) {
                            if (page <= 1) {
                                adapter.replaceData(list);
                            } else {
                                adapter.addData(list);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                isHandleData = false;
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.setNoMoreData(false);
                }
                ll_empty.setVisibility(View.VISIBLE);
                iv_empty.setBackgroundResource(R.drawable.shuaxin);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("当前网络不佳，刷新试试！");
                tv_empty_refresh.setVisibility(View.VISIBLE);
            }

            @Override
            public Class<HomeListEntity> getClazz() {
                return HomeListEntity.class;
            }
        });

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        hasMore = true;
        refreshLayout.setEnableLoadMore(true);
        getOrderList(page, refreshLayout);
    }
}