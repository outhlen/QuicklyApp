package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.file.photo.ProjectPhotoUtils;
import com.androidybp.basics.utils.file.photo.SelectPhotoUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.bumptech.glide.Glide;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseIntegerBean;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.entity.response.my.ResponsePersonageAuthenticationInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.view.dialog.EditInfoDialog;
import com.escort.carriage.android.ui.view.imgview.RoundImageView;

import java.io.File;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends ProjectBaseActivity implements View.OnClickListener {

    @BindView(R.id.main)
    View main;
    @BindView(R.id.toolbar_centre_title_right_button_title)
    TextView toolbarCentreTitleRightButtonTitle;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.ivImg)
    RoundImageView ivImg;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.mTvUseDay)
    TextView mTvUseDay;
    @BindView(R.id.llAuth)
    LinearLayout llAuth;
    @BindView(R.id.tvEditUserName)
    TextView tvEditUserName;
    @BindView(R.id.tvChangeNickName)
    TextView tvChangeNickName;
    @BindView(R.id.tvBindPhone)
    TextView tvBindPhone;
    @BindView(R.id.tvChangeBindPhone)
    TextView tvChangeBindPhone;
    @BindView(R.id.tvIndustry)
    TextView tvIndustry;
    @BindView(R.id.tvCompany)
    TextView tvCompany;
    @BindView(R.id.tvAddress)
    TextView tvAddress;

    private SelectPhotoUtils selectPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setPageActionBar();
        setListener();
        setSelectUtils();
        getUserInfo();
        getPersonageAuthentication(1);
    }

    private void setListener() {
        ivImg.setOnClickListener(this);
        llAuth.setOnClickListener(this);
        tvChangeBindPhone.setOnClickListener(this);
        tvChangeNickName.setOnClickListener(this);
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("个人信息");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivImg: //头像
                selectPhotoUtils.selectPhoto(1, main);
                break;
            case R.id.llAuth ://去认证
                startActivity(new Intent(this, PersonageAuthenticationActivity.class));
                break;
            case R.id.tvChangeBindPhone://修改绑定手机号
                startActivity(new Intent(this, ChangePhoneActivity.class));
                break;
            case R.id.tvChangeNickName://修改名字
                EditInfoDialog dialog = new EditInfoDialog(this);
                dialog.setTitle("修改");
                dialog.setCallback(new EditInfoDialog.Callback() {
                    @Override
                    public void onCallback(String msg) {
                        updateNickName(msg);
                    }
                });

                dialog.show();
            break;
        }
    }

    private void getUserInfo() {

        UserInfoEntity userInfoEntity = CacheDBMolder.getInstance().getUserInfoEntity(null);
        if (userInfoEntity != null) {
            updataUserInfo(userInfoEntity);
        }

//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_GETUSERINFO, jsonString).execute(new MyStringCallback<ResponseUserInfoEntity>() {
            @Override
            public void onResponse(ResponseUserInfoEntity s) {
                if (s != null) {
                    if (s.success) {
                        CacheDBMolder.getInstance().setUserInfoEntity(s.data, null, null);
                        updataUserInfo(s.data);
                    }
                }
            }

            @Override
            public Class<ResponseUserInfoEntity> getClazz() {
                return ResponseUserInfoEntity.class;
            }
        });

    }

    private void updataUserInfo(UserInfoEntity userInfoEntity) {
        tvUserName.setText(userInfoEntity.getUserName());
        tvEditUserName.setText(userInfoEntity.nickName);
        tvBindPhone.setText(userInfoEntity.phoneNumber);
        mTvUseDay.setText("使用小二押镖" + userInfoEntity.days + "天");
        Glide.with(this).load(userInfoEntity.headPictureUrl)
                .into(ivImg);
        // 公司信息
        tvCompany.setText(userInfoEntity.companyName);
        // 地址
        tvAddress.setText(userInfoEntity.companyAddress);
    }

    /**
     * 获取个人认证状态   1 个人认证 2 企业认证 3 司机认证 4 推广员
     */
    private void getPersonageAuthentication(int type) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(type);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.UNDERTAKE_USERAUTH_JUDGEAUTH, jsonString).execute(new MyStringCallback<ResponseIntegerBean>() {
            @Override
            public void onResponse(ResponseIntegerBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    int status = 0;
                    if (s.data != null) {
                        status = s.data;
                    }
                    if (s.success) {
                        if (status == 0) { //认证类型 1 个人认证 2 企业认证 3 司机认证 4 推广员
                            llAuth.setVisibility(View.VISIBLE);
                        } else {
                            llAuth.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseIntegerBean> getClazz() {
                return ResponseIntegerBean.class;
            }
        });
    }

/**
     * 更新用户昵称
     */
    private void updateNickName(String name) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(name);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_UPDATENICKNAME, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        tvEditUserName.setText(name);
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
        selectPhotoUtils.onRequestPermissionsResult(requestCode, permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectPhotoUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(int openType, Uri uri) {

        RequestEntityUtils.uploadOssImage(this, openType, uri, new RequestEntityUtils.ImageCallback() {
            @Override
            public void imageCallback(int openType, String url) {
                if (openType == 1) {
                    updateUserImage(url);
                }
            }
        });
    }

    /**
     * 更新用户头像
     */
    private void updateUserImage(String url) {
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(url);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_UPDATEHEADPORTRAIT, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {

                        Glide.with(UserInfoActivity.this).load(url)
                                .into(ivImg);
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
