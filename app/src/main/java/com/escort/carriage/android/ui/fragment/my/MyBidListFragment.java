package com.escort.carriage.android.ui.fragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.MyOrderListItemEntity;
import com.escort.carriage.android.entity.bean.my.MyBidListEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.RequestListBean;
import com.escort.carriage.android.entity.response.home.ResponseMyOrderListItemEntity;
import com.escort.carriage.android.entity.response.my.ResponseMyBidListEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.adapter.home.MyBidListAdapter;
import com.escort.carriage.android.ui.adapter.home.MyOrderListAdapter;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class MyBidListFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rl_refresh)
    BGARefreshLayout rlRefresh;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_empty_refresh)
    TextView tvEmptyRefresh;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.main)
    LinearLayout main;

    private String mId;
    private View view;
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isHandleData;
    private int allPage = Integer.MAX_VALUE;


    public MyBidListAdapter adapter;
    private int page = 1;
    private List<MyBidListEntity.ListBean> newsItemList;
    private int pageType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            pageType = arguments.getInt("type", 0);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_order_list, container, false);
            unbinder = ButterKnife.bind(this, view);
            initRefreshLayout();
            isPrepared = true;
            newsItemList = new ArrayList<>();
            onVisible();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }


    private void onVisible() {
        if (isPrepared && isVisible && !isHandleData) {
            page = 1;
            newsItemList = new ArrayList<>();
            initDatas();
        }
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

    private void initDatas() {
        isHandleData = true;
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page", RequestEntityUtils.getPageBeanOrders(page, 10));
        hashMap.put("isDelete", pageType);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDERVEHICLE_VEHICLELIST, jsonString).execute(new MyStringCallback<ResponseMyBidListEntity>() {
            @Override
            public void onResponse(ResponseMyBidListEntity entity) {
                isHandleData = false;
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                isHandleData = false;
                if (null != rlRefresh) {
                    rlRefresh.endLoadingMore();
                    rlRefresh.endRefreshing();

                    if (entity != null && entity.getData() != null
                            && entity.getData().getList() != null
                            && entity.getData().getList().size() > 0) {//有数据
                        List<MyBidListEntity.ListBean> list = entity.getData().getList();
                        if (entity.data.isLastPage) {
                            allPage = page;
                        }
                        llEmpty.setVisibility(View.GONE);
                        rlRefresh.setVisibility(View.VISIBLE);
                        if (newsItemList.size() == 0) {
                            newsItemList = list;
                            showRv(newsItemList);
                        } else {
                            if (adapter != null) {
                                if (page <= 1) {
                                    newsItemList = list;
                                } else {
                                    newsItemList.addAll(list);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        if (newsItemList.size() != 0) {//第一次加载有数据
                            page--;
                            adapter.notifyDataSetChanged();
                            ToastUtil.showToastString("没有更多了");
                        } else {//第一次加载没有数据
                            rlRefresh.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            ivEmpty.setBackgroundResource(R.drawable.shuaxin);
                            tvEmptyRefresh.setVisibility(View.GONE);
                            tvEmpty.setText("没有任何订单信息");
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
                llEmpty.setVisibility(View.VISIBLE);
                ivEmpty.setBackgroundResource(R.drawable.shuaxin);
                tvEmpty.setVisibility(View.VISIBLE);
                tvEmpty.setText("当前网络不佳，刷新试试！");
                tvEmptyRefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public Class<ResponseMyBidListEntity> getClazz() {
                return ResponseMyBidListEntity.class;
            }
        });


    }


    private void showRv(final List<MyBidListEntity.ListBean> list) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyBidListAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                GoodsBean bean = (GoodsBean) adapter.getData().get(position);
//                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
//                intent.putExtra("id", bean.getOrderNumber());
//                startActivityForResult(intent, 333);
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
        newsItemList = new ArrayList<>();
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
        onVisible();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        newsItemList = new ArrayList<>();
        initDatas();
    }
}
