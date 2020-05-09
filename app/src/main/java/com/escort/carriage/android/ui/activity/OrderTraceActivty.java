package com.escort.carriage.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.ResponseMyOrderListItemEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.activity.adapter.MyTraceOrderAdapter;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class OrderTraceActivty extends ProjectBaseActivity {

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
    @BindView(R.id.item_head_bar_tv_title)
    TextView headTitleTv;
    @BindView(R.id.item_head_bar_iv_back)
    ImageView backBtn;

    private int allPage = Integer.MAX_VALUE;
    private int page = 1;
    private MyTraceOrderAdapter myTraceOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order_list);
        ButterKnife.bind(this);
        headTitleTv.setText("转单追踪");
        initDatas();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initDatas() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("page", RequestEntityUtils.getPageBeanOrders(page, 10));
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.ORDER_TARCE_LIST, jsonString).execute(new MyStringCallback<ResponseMyOrderListItemEntity>() {
            @Override
            public void onResponse(ResponseMyOrderListItemEntity entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
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
                        llEmpty.setVisibility(View.GONE);
                        rlRefresh.setVisibility(View.VISIBLE);
                        if (myTraceOrderAdapter == null || myTraceOrderAdapter.getData() == null || myTraceOrderAdapter.getData().size() == 0) {
                            showRv(list);
                        } else {
                            if (myTraceOrderAdapter != null) {
                                if (page <= 1) {
                                    myTraceOrderAdapter.replaceData(list);
                                } else {
                                    myTraceOrderAdapter.addData(list);
                                }
                            }
                        }
                    } else {
                        if (myTraceOrderAdapter == null || myTraceOrderAdapter.getData() == null || myTraceOrderAdapter.getData().size() == 0) {//第一次加载有数据
                            page--;
                            myTraceOrderAdapter.notifyDataSetChanged();
                            ToastUtil.showToastString("没有更多了");
                        } else {//第一次加载没有数据
                            rlRefresh.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            ivEmpty.setBackgroundResource(R.drawable.shuaxin);
                            tvEmptyRefresh.setVisibility(View.GONE);
                            tvEmpty.setText("没有任何信息");
                        }
                    }
                }

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (null != rlRefresh) {
                    rlRefresh.endLoadingMore();
                    rlRefresh.endRefreshing();
                    rlRefresh.setVisibility(View.GONE);
                }
                if (llEmpty != null) {
                    llEmpty.setVisibility(View.VISIBLE);
                    ivEmpty.setBackgroundResource(R.drawable.shuaxin);
                    tvEmpty.setVisibility(View.VISIBLE);
                    tvEmpty.setText("当前网络不佳，刷新试试！");
                }
                if (tvEmptyRefresh != null) {
                    tvEmptyRefresh.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public Class<ResponseMyOrderListItemEntity> getClazz() {
                return ResponseMyOrderListItemEntity.class;
            }
        });
    }

    private void showRv(final List<OrderInfoEntity> list) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        myTraceOrderAdapter = new MyTraceOrderAdapter(list, this);
        rv.setAdapter(myTraceOrderAdapter);
        myTraceOrderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                GoodsBean bean = (GoodsBean) adapter.getData().get(position);
//                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
//                intent.putExtra("id", bean.getOrderNumber());
//                startActivityForResult(intent, 333);
            }
        });

        myTraceOrderAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

            }
        });
    }
}
