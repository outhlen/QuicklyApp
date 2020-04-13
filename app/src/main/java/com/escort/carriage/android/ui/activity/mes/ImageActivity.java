package com.escort.carriage.android.ui.activity.mes;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.bumptech.glide.Glide;
import com.escort.carriage.android.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： Lance
 * 日期:  2018/5/7 17:27
 */

public class ImageActivity extends ProjectBaseActivity {
    @BindView(R.id.iv_image)
    ImageView iv_image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isClickIntercept = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aimge);
        ButterKnife.bind(this);
        setPageActionBar();
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        File file = (File) intent.getSerializableExtra("file");
        Uri uri = (Uri) intent.getParcelableExtra("Uri");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(iv_image);
        } else if(file != null) {
            Glide.with(ImageActivity.this).load(file).into(iv_image);
        } else if(uri != null){
            Glide.with(ImageActivity.this).load(uri).into(iv_image);
        }
    }
    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("图片详情");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
