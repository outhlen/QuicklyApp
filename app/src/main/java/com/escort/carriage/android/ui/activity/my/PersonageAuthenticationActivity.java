package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

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
import com.androidybp.basics.utils.verification.VerificationUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.my.RequestPersonageAuthenticationEntity;
import com.escort.carriage.android.entity.response.my.ResponsePersonageAuthenticationInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.view.dialog.AuthSuccessDialog;
import com.escort.carriage.android.utils.HumanFaceAuthenticationUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人认证
 */
public class PersonageAuthenticationActivity extends ProjectBaseActivity implements View.OnClickListener {
    @BindView(R.id.main)
    View main;
    @BindView(R.id.toolbar_centre_title_right_button_title)
    TextView toolbarCentreTitleRightButtonTitle;
    @BindView(R.id.my_toolbar)
    Toolbar myToolbar;
    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.ivMeId)
    ImageView ivMeId;
    @BindView(R.id.tvMeId)
    TextView tvMeId;
    @BindView(R.id.ivIdZhengMian)
    ImageView ivIdZhengMian;
    @BindView(R.id.tvZhengMian)
    TextView tvZhengMian;
    @BindView(R.id.ivIdGuoHui)
    ImageView ivIdGuoHui;
    @BindView(R.id.tvGuoHui)
    TextView tvGuoHui;
    @BindView(R.id.tvSelectAddress)
    TextView tvSelectAddress;
    @BindView(R.id.etUrgentPerson)
    EditText etUrgentPerson;
    @BindView(R.id.etUrgentPhone)
    EditText etUrgentPhone;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    private int status;//(0 未认证 1审核中 2 审核通过 3驳回)
    private RequestPersonageAuthenticationEntity requestPersonageAuthenticationEntity;
    private SelectPhotoUtils selectPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_identification);
        ButterKnife.bind(this);
        setPageActionBar();
        status = getIntent().getIntExtra("status", 0);
        requestPersonageAuthenticationEntity = new RequestPersonageAuthenticationEntity();
        initView();
        setSelectUtils();
        if (status == 2) {
//            getInfo();
            //弹出对话框
            showAuthSuccessDialog();
        } else {
            //啥也不干
        }

    }

    private void showAuthSuccessDialog() {
        AuthSuccessDialog authSuccessDialog = AuthSuccessDialog.getInstance(this, 1);
        authSuccessDialog.setCompanyName("");
        authSuccessDialog.setClickKnowListener(new AuthSuccessDialog.OnClickKnowListener() {
            @Override
            public void onClickKnow() {
                finish();
            }
        });
        authSuccessDialog.show();
    }

    private void initView() {
        ivMeId.setOnClickListener(this);
        ivIdZhengMian.setOnClickListener(this);
        ivIdGuoHui.setOnClickListener(this);
        tvSelectAddress.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("个人身份认证");
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
                //将图片设置到控件上
                switch (openType) {
                    case 1:
                        GlideManager.getGlideManager().loadImageUriCenterCrop(PersonageAuthenticationActivity.this, uri, ivMeId);
                        tvMeId.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        GlideManager.getGlideManager().loadImageUriCenterCrop(PersonageAuthenticationActivity.this, uri, ivIdZhengMian);
                        tvZhengMian.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        tvGuoHui.setVisibility(View.INVISIBLE);
                        GlideManager.getGlideManager().loadImageUriCenterCrop(PersonageAuthenticationActivity.this, uri, ivIdGuoHui);
                        break;
                }
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
                if(openType == 1){
                    requestPersonageAuthenticationEntity.setFaceImage(url);
                } else if(openType == 2){
                    requestPersonageAuthenticationEntity.setIdPictureFront(url);
                } else if(openType == 3){
                    requestPersonageAuthenticationEntity.setHeadPictureReverse(url);
                }
            }

            @Override
            public void imageArrayCallback(int openType, List<String> urls) {

            }
        });
    }

    /**
     * 获取个人认证状态   1 个人认证 2 企业认证 3 服务认证 4 推广员
     */
    private void getInfo() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_GETPERSONALAUTHENTICATION, jsonString).execute(new MyStringCallback<ResponsePersonageAuthenticationInfoEntity>() {
            @Override
            public void onResponse(ResponsePersonageAuthenticationInfoEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {

                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponsePersonageAuthenticationInfoEntity> getClazz() {
                return ResponsePersonageAuthenticationInfoEntity.class;
            }
        });
    }

    /**
     * 提交认证信息
     */
    private void toPersonageAuthentication() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(requestPersonageAuthenticationEntity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USERINFO_PERSONALAUTHENTICATION, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        showAuthSuccessDialog();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivMeId://人脸照片
//                selectPhotoUtils.selectPhoto(1, main);
                HumanFaceAuthenticationUtils.start(this, new HumanFaceAuthenticationUtils.Callback() {
                    @Override
                    public void callback(String url) {
                        GlideManager.getGlideManager().loadImage(url, ivMeId);
                        requestPersonageAuthenticationEntity.setFaceImage(url);
                    }
                });
                break;
            case R.id.ivIdZhengMian://身份证正面照
                selectPhotoUtils.selectPhoto(2, main);
                break;
            case R.id.ivIdGuoHui://反面照
                selectPhotoUtils.selectPhoto(3, main);
                break;
            case R.id.tvSelectAddress://地区选择
//                SelectAddressDialog.getInstance().setContext(this)
//                        .setFlag(tvSelectAddress)
//                        .setCallback(new SelectAddressDialog.Callback() {
//                            @Override
//                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
//                                ((TextView)flag).setText(provinceBean.getProvince() + " " + cityBean.getCity() + "  " + areaBean.getArea());
//                                requestPersonageAuthenticationEntity.setProvinceCode(provinceBean.getProvinceCode());
//                                requestPersonageAuthenticationEntity.setCityCode(cityBean.getCityCode());
//                                requestPersonageAuthenticationEntity.setAreaCode(areaBean.getAreaCode());
//                            }
//                        })
//                        .setShowBottom(true)
//                        .show(getSupportFragmentManager());
                break;
            case R.id.tvSubmit://提交按钮
                String userName = etUrgentPerson.getText().toString();
                String phone = etUrgentPhone.getText().toString();
                if(TextUtils.isEmpty(userName)){
                    ToastUtil.showToastString("请输入联系人姓名");
                } else if(TextUtils.isEmpty(phone)){
                    ToastUtil.showToastString("请输入手机号");
                } else if(!VerificationUtil.verificationText(phone, VerificationUtil.MOBILE_NUM)){
                    ToastUtil.showToastString("请输入正确的手机号");
                } else {
                    requestPersonageAuthenticationEntity.setUrgentUserName(userName);
                    requestPersonageAuthenticationEntity.setUrgentPhone(phone);
                    toPersonageAuthentication();
                }
                break;

        }
    }
}
