package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;

import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 驾驶员 认证
 */
public class DriverAuthentivationActivity extends ProjectBaseActivity {
    @BindView(R.id.ivImg)
    ImageView ivImg;
    @BindView(R.id.imageGroup)
    LinearLayout imageGroup;
    @BindView(R.id.ivImageView)
    RoundedImagView ivImageView;
    @BindView(R.id.tvAddCar)
    TextView tvAddCar;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.zige_et)
    EditText zgEdit;
    @BindView(R.id.hege_et)
    EditText hgEdit;

    private SelectPhotoUtils selectPhotoUtils;
    String imageUrl;
    String zige,hege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_authentivation);
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
        tvTitle.setText("驾驶证信息");
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

    private void uploadImage(int openType, Uri uri) {
        RequestEntityUtils.uploadOssImage(this, openType, uri, new RequestEntityUtils.ImageCallback() {
            @Override
            public void imageCallback(int openType, String url) {
                imageGroup.setVisibility(View.GONE);
                ivImageView.setVisibility(View.VISIBLE);
                imageUrl = url;
                GlideManager.getGlideManager().loadImage(url, ivImageView);
            }

            @Override
            public void imageArrayCallback(int openType, List<String> urls) {

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

    @OnClick({R.id.ivImg, R.id.ivImageView, R.id.tvAddCar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivImg:
            case R.id.ivImageView:
                selectPhotoUtils.selectPhoto(1, main);
                break;
            case R.id.tvAddCar:
                 zige = zgEdit.getText().toString().trim();
                 hege = hgEdit.getText().toString().trim();
                if (TextUtils.isEmpty(imageUrl)) {
                    ToastUtil.showToastString("请上传驾驶证信息");
                } else {
                    commitUrl();
                }
                break;
        }
    }

    private void commitUrl() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("drivingLicencePicture", imageUrl);
        hashMap.put("employeeQualificationCode", zige);
        hashMap.put("employeeQualificationType", hege);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USER_USERAUTHDRIVER_DRIVERAUTHENTICATION, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ToastUtil.showToastString("认证成功,请上传车辆信息完成押镖认证");
                        setResult(345);
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
