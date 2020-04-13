package com.escort.carriage.android.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.escort.carriage.android.entity.bean.my.AppraiseInfoEntity;
import com.escort.carriage.android.entity.bean.my.OrderAppraiseListItemEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.my.ResponseAppraiseInfoEntity;
import com.escort.carriage.android.entity.response.my.ResponseOrderAppraiseListItemEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.adapter.my.MyOrderAppraiseRightListAdapter;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyOrderAppraiseRightFragment  extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv_empty)
    TextView tv_empty;
    @BindView(R.id.ll_empty)
    LinearLayout ll_empty;
    @BindView(R.id.iv_empty)
    ImageView iv_empty;
    @BindView(R.id.tv_empty_refresh)
    TextView tv_empty_refresh;
    private String mId;
    private View view;

    public MyOrderAppraiseRightListAdapter adapter;
    private int page = 1;
    private String orderNumber;
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isHandleData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            orderNumber = arguments.getString("orderNumber");
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_my_order_appraise_right_layout, container, false);
            unbinder = ButterKnife.bind(this, view);
            isPrepared = true;
            onVisible();
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        return view;
    }

    private void onVisible() {
        if (isPrepared && isVisible) {
            page = 1;
            getInfo();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
HashMap<String, String> hashMap = new HashMap<>();
hashMap.put("orderNumber", orderNumber);
hashMap.put("type", "0");

        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.EVALUATION_GETEVALUATIONINFOBYORDERNUMBER, jsonString).execute(new MyStringCallback<ResponseOrderAppraiseListItemEntity>() {
            @Override
            public void onResponse(ResponseOrderAppraiseListItemEntity entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
//                setResponseData(entity);

            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                ll_empty.setVisibility(View.VISIBLE);
                iv_empty.setBackgroundResource(R.drawable.shuaxin);
                tv_empty.setVisibility(View.VISIBLE);
                tv_empty.setText("当前网络不佳，刷新试试！");
                tv_empty_refresh.setVisibility(View.VISIBLE);
            }

            @Override
            public Class<ResponseOrderAppraiseListItemEntity> getClazz() {
                return ResponseOrderAppraiseListItemEntity.class;
            }
        });


    }

    private void getInfo() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("type", "1");
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.EVALUATION_GETINFO, jsonString).execute(new MyStringCallback<ResponseAppraiseInfoEntity>() {
            @Override
            public void onResponse(ResponseAppraiseInfoEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success && s.data != null) {
                        ArrayList<AppraiseInfoEntity> arrayList = new ArrayList<>();
                        arrayList.add(s.data);
                        setResponseData(arrayList);
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseAppraiseInfoEntity> getClazz() {
                return ResponseAppraiseInfoEntity.class;
            }
        });
    }

    private void setResponseData(List<AppraiseInfoEntity> responseList) {
        if (responseList != null
                && responseList.size() > 0) {//有数据
            ll_empty.setVisibility(View.GONE);
            if (adapter == null || adapter.getData() == null || adapter.getData().size() == 0) {
                showRv(responseList);
            } else {
                if (adapter != null) {
                    if (page <= 1) {
                        adapter.replaceData(responseList);
                    } else {
                        adapter.addData(responseList);
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
                ll_empty.setVisibility(View.VISIBLE);
                iv_empty.setBackgroundResource(R.drawable.shuaxin);
                tv_empty_refresh.setVisibility(View.GONE);
                tv_empty.setText("没有任何信息");
            }
        }
    }


    private void showRv(final List<AppraiseInfoEntity> list) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyOrderAppraiseRightListAdapter(list);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                OrderInfoEntity bean = (OrderInfoEntity) adapter.getData().get(position);
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


}