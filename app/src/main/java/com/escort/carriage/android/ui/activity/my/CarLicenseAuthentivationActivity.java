package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.file.photo.SelectPhotoUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.view.imgview.RoundedImagView;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 行驶证 认证
 */
public class CarLicenseAuthentivationActivity extends ProjectBaseActivity {
    @BindView(R.id.main)
    View main;
    @BindView(R.id.ivImg)
    ImageView ivImg;
    @BindView(R.id.imageGroup)
    RelativeLayout imageGroup;
    @BindView(R.id.ivImageView)
    RoundedImagView ivImageView;
    @BindView(R.id.ivFyImg)
    ImageView ivFyImg;
    @BindView(R.id.imageFyGroup)
    RelativeLayout imageFyGroup;
    @BindView(R.id.ivFyImageView)
    RoundedImagView ivFyImageView;
    @BindView(R.id.tvAddCar)
    TextView tvAddCar;
    private SelectPhotoUtils selectPhotoUtils;
    String frontUrl, backUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_license_authentivation);
        ButterKnife.bind(this);
        setPageActionBar();
        setSelectUtils();
    }


    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("行驶证信息");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void setSelectUtils() {
        selectPhotoUtils = new SelectPhotoUtils(this, null);
        selectPhotoUtils.setCallback(new SelectPhotoUtils.SelectPhotoCallback() {
            @Override
            public void selectPhotoCallback(int selectType, int openType, Uri uri) {
                //将图片上传到服务器上
                uploadImage(openType, uri);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        selectPhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectPhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.ivImg, R.id.ivImageView, R.id.ivFyImg, R.id.ivFyImageView, R.id.tvAddCar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivImg:
            case R.id.ivImageView:
                selectPhotoUtils.selectPhoto(1, main);
                break;
            case R.id.ivFyImg:
            case R.id.ivFyImageView:
                selectPhotoUtils.selectPhoto(2, main);
                break;
            case R.id.tvAddCar:
                if(TextUtils.isEmpty(frontUrl)){
                    ToastUtil.showToastString("请上传行驶证信息-主页");
                } else if(TextUtils.isEmpty(backUrl)){
                    ToastUtil.showToastString("请上传行驶证信息-副页");
                } else {
                    commitUrl();
                }
                break;
        }
    }

    private void uploadImage(int openType, Uri uri) {
        RequestEntityUtils.uploadOssImage(this, openType, uri, new RequestEntityUtils.ImageCallback() {
            @Override
            public void imageCallback(int openType, String url) {
                if (openType == 1) {
                    imageGroup.setVisibility(View.GONE);
                    ivImageView.setVisibility(View.VISIBLE);
                    frontUrl = url;
                    GlideManager.getGlideManager().loadImage(url, ivImageView);
                } else if (openType == 2) {
                    imageFyGroup.setVisibility(View.GONE);
                    ivFyImageView.setVisibility(View.VISIBLE);
                    backUrl = url;
                    GlideManager.getGlideManager().loadImage(url, ivFyImageView);
                }
            }
        });
    }

    private void commitUrl() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("frontUrl", frontUrl);
        hashMap.put("backUrl", backUrl);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.VEHICLE_INFO_ADD, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ToastUtil.showToastString("数据提交成功");
                        setResult(666);
                        finish();
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }
}
