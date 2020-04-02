package com.escort.carriage.android.ui.activity.mes;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.action_bar.StatusBarUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.NewsDetailsBean;
import com.escort.carriage.android.entity.response.home.NewsListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.base.BaseWebViewActivity;
import com.escort.carriage.android.ui.view.ProgressView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsDetailsActivity extends BaseWebViewActivity {

    @BindView(R.id.progressView)
    ProgressView mProgressView;
    @BindView(R.id.webView)
    WebView mWebView;

    private Unbinder bind;

    private NewsListBean.DataBean.ListBean mBean;

    protected void onCreate(Bundle savedInstanceState) {
        StatusBarCompatManager.fullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        bind = ButterKnife.bind(this);
        mBean = (NewsListBean.DataBean.ListBean) getIntent().getSerializableExtra("bean");
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
        if (mBean != null) {
            tvTitle.setText(mBean.getTitle());
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutResId() {
        return 0;
    }

    public void initData(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initWebView(mWebView, mProgressView);
        mBean = (NewsListBean.DataBean.ListBean) getIntent().getSerializableExtra("bean");
        if (mBean != null) {
            getSysNewInfo(mBean.getId());
        }

    }


    /**
     * 详情
     */
    public void getSysNewInfo(String id) {


        RequestEntity requestEntity = new RequestEntity(0);
        ReqBen reqBean = new ReqBen();
        reqBean.id = id;
        requestEntity.setData(reqBean);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.SYSNEW_GETSYSNEWINFO, jsonString).execute(new MyStringCallback<NewsDetailsBean>() {
            @Override
            public void onResponse(NewsDetailsBean resp) {
                if (resp != null) {
                    if (resp.success) {
                        loadHtmlData(resp.getData().getContent());
                    } else {
                        ToastUtil.showToastString(resp.message);
                    }
                }
            }

            @Override
            public Class<NewsDetailsBean> getClazz() {
                return NewsDetailsBean.class;
            }
        });

    }

    class ReqBen {
        String id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
    }
}
