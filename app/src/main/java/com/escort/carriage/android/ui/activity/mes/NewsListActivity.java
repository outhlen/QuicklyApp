package com.escort.carriage.android.ui.activity.mes;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.home.NewsListReqBean;
import com.escort.carriage.android.entity.response.home.NewsListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.adapter.mes.NewsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsListActivity extends ProjectBaseActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private NewsListAdapter mAdapter;
    private List<NewsListBean.DataBean.ListBean> mList = new ArrayList<>();

    private int mPageNum = 1;
    private int mPageSize = 100;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_news_list);
        bind = ButterKnife.bind(this);
        setPageActionBar();
        initData(savedInstanceState);
    }

    private void setPageActionBar() {
        //获取顶部状态栏的高度 给对应View设置高度
        View statusBar = findViewById(R.id.statusBarView);
        int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = statusBarHeight;
        statusBar.setLayoutParams(layoutParams);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(android.R.color.black));
        tvTitle.setText("小二头条");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void initData(@Nullable Bundle savedInstanceState) {
        //初始化Rv
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(layoutManager);
        mAdapter = new NewsListAdapter(this, mList);
        mRv.setAdapter(mAdapter);
        getSysNewList();
    }

    /**
     * 列表
     */
    public void getSysNewList() {

        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        NewsListReqBean reqBean = new NewsListReqBean();
        reqBean.setNewsType("1");//类别（1 发货方 2 承接方）
        NewsListReqBean.PageBean page = new NewsListReqBean.PageBean();
        page.setPageNumber(mPageNum);
        page.setPageSize(mPageSize);
        reqBean.setPage(page);
        requestEntity.setData(reqBean);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.SYSNEW_GETSYSNEWLIST, jsonString).execute(new MyStringCallback<NewsListBean>() {
            @Override
            public void onResponse(NewsListBean resp) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if(resp != null ){
                    if(resp.success){
                        if (mPageNum == 1){
                            mList.clear();
                        }

                        mList.addAll(resp.getData().getList());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showToastString(resp.message);
                    }
                }
            }

            @Override
            public Class<NewsListBean> getClazz() {
                return NewsListBean.class;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bind != null){
            bind.unbind();
            bind = null;
        }
    }
}
