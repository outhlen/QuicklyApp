package com.escort.carriage.android.ui.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.entity.bean.home.SelectOrderEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.home.HistoryOrderListRequestEnity;
import com.escort.carriage.android.entity.request.home.HomeListRequestEnity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ResponseMyOrderListItemEntity;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.mes.OrderInfoActivity;
import com.escort.carriage.android.ui.activity.mes.SelectOrderActivity;
import com.escort.carriage.android.ui.adapter.home.HistoryOrderListAdapter;
import com.escort.carriage.android.ui.view.dialog.SelectpRrovinceCityDialog;
import com.escort.carriage.android.ui.view.text.DrawableTextView;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author Yangbp
 * @description: 历史订单
 * @date :2020-03-17 15:06
 */
public class HistoryOrderListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rl_refresh)
    BGARefreshLayout rlRefresh;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
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


    public HistoryOrderListAdapter adapter;
    private int page = 1;
    private HistoryOrderListRequestEnity historyOrderListRequestEnity;

    private boolean isResumeNotUpdata = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            initRefreshLayout();
            isPrepared = true;
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatas();
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        initDatas();
//    }

    private void initDatas() {
        isHandleData = true;

        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        if (historyOrderListRequestEnity == null) {
            historyOrderListRequestEnity = new HistoryOrderListRequestEnity();
        }

        HistoryOrderListRequestEnity.PageEntity pageEntity = new HistoryOrderListRequestEnity.PageEntity();
        pageEntity.setPageNumber(page);
        pageEntity.setPageSize(10);
        historyOrderListRequestEnity.page = pageEntity;

        requestEntity.setData(historyOrderListRequestEnity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_GETORDERLIST, jsonString).execute(new MyStringCallback<ResponseMyOrderListItemEntity>() {
            @Override
            public void onResponse(ResponseMyOrderListItemEntity entity) {
                isHandleData = false;
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                isHandleData = false;
                if (null != rlRefresh) {
                    rlRefresh.endLoadingMore();
                    rlRefresh.endRefreshing();

                    if (entity != null && entity.getData() != null
                            && entity.getData().getList() != null
                            && entity.getData().getList().size() > 0) {//有数据
                        List<OrderInfoEntity> list = entity.getData().getList();
                        if (entity.data.isLastPage) {
                            allPage = page;
                        }
                        ll_empty.setVisibility(View.GONE);
                        rlRefresh.setVisibility(View.VISIBLE);
                        if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {
                            showRv(list);
                        } else {
                            if (adapter != null) {
                                if (page <= 1) {
                                    adapter.replaceData(list);
                                } else {
                                    adapter.addData(list);
//                                    newsItemList.addAll(list);
                                }
//                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {//第一次加载有数据
                            page--;
                            adapter.notifyDataSetChanged();
                            ToastUtil.showToastString("没有更多了");
                        } else {//第一次加载没有数据
                            rlRefresh.setVisibility(View.GONE);
                            ll_empty.setVisibility(View.VISIBLE);
                            iv_empty.setBackgroundResource(R.drawable.shuaxin);
                            tv_empty_refresh.setVisibility(View.GONE);
                            tv_empty.setText("没有任何信息");
                        }
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                isHandleData = false;
                if (null != rlRefresh) {
                    rlRefresh.endLoadingMore();
                    rlRefresh.endRefreshing();
                    rlRefresh.setVisibility(View.GONE);
                }
                ll_empty.setVisibility(View.VISIBLE);
                iv_empty.setBackgroundResource(R.drawable.shuaxin);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("当前网络不佳，刷新试试！");
                tv_empty_refresh.setVisibility(View.VISIBLE);
            }

            @Override
            public Class<ResponseMyOrderListItemEntity> getClazz() {
                return ResponseMyOrderListItemEntity.class;
            }
        });


    }


    private void showRv(final List<OrderInfoEntity> list) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryOrderListAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfoEntity bean = (OrderInfoEntity) adapter.getData().get(position);
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                intent.putExtra("id", bean.getOrderNumber());
                intent.putExtra("openType", 3);
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
        rlRefresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格
        //    rl_refresh.setRefreshViewHolder(meiTuanRefreshViewHolder);
        rlRefresh.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(), true));
        // 设置正在加载更多时不显示加载更多控件
        rlRefresh.setIsShowLoadingMoreView(true);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        initDatas();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (allPage <= page) {
            return false;
        } else {
            page++;
            initDatas();
        }
        rlRefresh.endLoadingMore();
        return true;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        unbinder.unbind();
    }

    @OnClick(R.id.tv_empty_refresh)
    public void onViewClicked() {
        initDatas();
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
                                historyOrderListRequestEnity.startCity = cityBean.getCityCode();
                                tvItem01.setText(cityBean.getCity());
                                tvItem01.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_bottom_jt));
                                initDatas();
                            }
                        })
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());
                break;
            case R.id.tvItem02:
                SelectpRrovinceCityDialog.getInstance().setContext(getActivity())
                        .setFlag(tvItem02)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
//                                ((TextView)flag).setText(provinceBean.getProvince() + " " + cityBean.getCity() + "  " + areaBean.getArea());
                                historyOrderListRequestEnity.endCity = cityBean.getCityCode();
                                tvItem02.setText(cityBean.getCity());
                                tvItem02.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_bottom_jt));
                                initDatas();
                            }
                        })
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());
                break;
            case R.id.tvItem04:
                Intent intent = new Intent(getActivity(), SelectOrderActivity.class);
                intent.putExtra("openType", 2);
                intent.putExtra("json", JsonManager.createJsonString(getSelectOrderEntity()));
                startActivityForResult(intent, 333);
                break;
        }
    }

    private SelectOrderEntity getSelectOrderEntity() {
        SelectOrderEntity entity = new SelectOrderEntity();
        entity.orderType = historyOrderListRequestEnity.orderType;
        entity.cargoName = historyOrderListRequestEnity.cargoName;
        entity.startWeight = historyOrderListRequestEnity.startWeight;
        entity.endWeight = historyOrderListRequestEnity.endWeight;
        entity.startVolume = historyOrderListRequestEnity.startVolume;
        entity.endVolume = historyOrderListRequestEnity.endVolume;
        entity.orderPlaceTime = historyOrderListRequestEnity.orderPlaceTime;
        entity.endDate = historyOrderListRequestEnity.endDate;
        return entity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == 666 && data != null) {
            isResumeNotUpdata = true;
            SelectOrderEntity entity = JsonManager.getJsonBean(data.getStringExtra("json"), SelectOrderEntity.class);
            historyOrderListRequestEnity.orderType = entity.orderType;
            historyOrderListRequestEnity.cargoName = entity.cargoName;
            historyOrderListRequestEnity.startWeight = entity.startWeight;
            historyOrderListRequestEnity.endWeight = entity.endWeight;
            historyOrderListRequestEnity.startVolume = entity.startVolume;
            historyOrderListRequestEnity.endVolume = entity.endVolume;
            historyOrderListRequestEnity.orderPlaceTime = entity.orderPlaceTime;
            historyOrderListRequestEnity.endDate = entity.endDate;
            page = 1;
            initDatas();
        } else if(requestCode == 333 && resultCode == 665){
            //重置起始地 目的地 数据
            historyOrderListRequestEnity = new HistoryOrderListRequestEnity();
            tvItem01.setText("出发地");
            tvItem02.setText("目的地");
            tvItem01.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_top_jt));
            tvItem02.setRightDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.home_top_jt));
            initDatas();
        }

    }
}