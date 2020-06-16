package com.escort.carriage.android.ui.activity.my;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.okhttp3.entity.ResponceJsonEntity;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.file.photo.ProjectPhotoUtils;
import com.androidybp.basics.utils.file.photo.SelectPhotoUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.CompanyBean;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.my.RequestEnterpriseAuthenticationEntity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.entity.response.my.ResponseEnterpriseAuthenticationEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.view.dialog.AuthSuccessDialog;
import com.escort.carriage.android.ui.view.dialog.SelectAddressDialog;

import java.io.File;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 企业认证
 */
public class EnterpriseAuthenticationActivity extends ProjectBaseActivity implements View.OnClickListener {
    @BindView(R.id.main)
    View main;
    @BindView(R.id.llName)
    LinearLayout llName;
    @BindView(R.id.etCompanyName)
    EditText etCompanyName;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.llAddress)
    LinearLayout llAddress;
    @BindView(R.id.imageGroup)
    LinearLayout imageGroup;
    @BindView(R.id.ivImageView)
    ImageView ivImageView;
    @BindView(R.id.etAddressDetails)
    EditText etAddressDetails;
    @BindView(R.id.cbSaveCommonAddress)
    CheckBox cbSaveCommonAddress;
    @BindView(R.id.ivImg)
    ImageView ivImg;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    private int status;//(0 未认证 1审核中 2 审核通过 3驳回)
    private RequestEnterpriseAuthenticationEntity requestEntity;
    private SelectPhotoUtils selectPhotoUtils;
    private AuthSuccessDialog authSuccessDialog;
    String remoteUrl,areaCode,cityCode,provinceCode ;
    String errorString;
    boolean isPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_auth);
        ButterKnife.bind(this);
        setPageActionBar();
        status = getIntent().getIntExtra("status", 0);
        requestEntity = new RequestEnterpriseAuthenticationEntity();
        initView();
        setSelectUtils();
        if (status == 2) {
            getInfo();
            //弹出对话框
            authSuccessDialog = AuthSuccessDialog.getInstance(this, 0);
            authSuccessDialog.setCompanyName("");
            authSuccessDialog.setClickKnowListener(new AuthSuccessDialog.OnClickKnowListener() {
                @Override
                public void onClickKnow() {
                    finish();
                }
            });
            authSuccessDialog.show();
        } else {
            //啥也不干
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
        tvTitle.setText("企业认证");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        ivImg.setOnClickListener(this);
        tvAddress.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        ivImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivImg:
            case R.id.ivImageView:
                selectPhotoUtils.selectPhoto(1, main);
                break;
             case R.id.tvAddress:
                 SelectAddressDialog.getInstance().setContext(this)
                         .setFlag(tvAddress)
                         .setCallback(new SelectAddressDialog.Callback() {
                             @Override
                             public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
                                 ((TextView)flag).setText(provinceBean.getProvince() + cityBean.getCity() + areaBean.getArea());
                                 areaCode =  areaBean.getAreaCode();
                                 cityCode = cityBean.getCityCode();
                                 provinceCode = provinceBean.getProvinceCode();
                                 requestEntity.setCompanyProvinceCode(provinceBean.getProvinceCode());
                                 requestEntity.setCompanyCityCode(cityBean.getCityCode());
                                 requestEntity.setCompanyAreaCode(areaBean.getAreaCode());
                             }
                         })
                         .setShowBottom(true)
                         .show(getSupportFragmentManager());
                break;
            case R.id.tvSubmit:
                String companyName = etCompanyName.getText().toString();
                String code = etCode.getText().toString();
                String addressDetails = etAddressDetails.getText().toString();
                if(!isPass){
                    ToastUtil.showToastString(errorString);
                    return;
                }
                if(TextUtils.isEmpty(companyName)){
                    ToastUtil.showToastString("请填写公司名称");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    ToastUtil.showToastString("请填写信用代码");
                    return;
                }
                if(TextUtils.isEmpty(addressDetails)){
                    ToastUtil.showToastString("请填写详细地址");
                    return;
                }
                if(TextUtils.isEmpty(requestEntity.getCompanyProvinceCode())){
                    ToastUtil.showToastString("请选择地址");
                    return;
                }
//                else {
//                    requestEntity.setCompanyName(companyName);
//                    requestEntity.setCreditCode(code);
//                    requestEntity.setCompanyAddress(tvAddress.getText() + addressDetails);
//                }
                addAuthenticationInfo();
                    break;

        }
    }


    private void setSelectUtils() {
        selectPhotoUtils = new SelectPhotoUtils(this, null);
        selectPhotoUtils.setCallback(new SelectPhotoUtils.SelectPhotoCallback() {
            @Override
            public void selectPhotoCallback(int selectType, int openType, Uri uri) {
                //将图片设置到控件上
                switch (openType) {
                    case 1:
                        ivImageView.setVisibility(View.VISIBLE);
                        imageGroup.setVisibility(View.INVISIBLE);
                        GlideManager.getGlideManager().loadImageUriCenterCrop(EnterpriseAuthenticationActivity.this, uri, ivImageView);
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
        selectPhotoUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    //requestEntity.setLicencePicture(url);
                    remoteUrl  = url;
                    requestEntity.setImage(url);
                    toEnterpriseAuthentication(); //开始认证
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
        OkgoUtils.post(ProjectUrl.COMPANY_COMPANY_INFO, jsonString).execute(new MyStringCallback<ResponseEnterpriseAuthenticationEntity>() {
            @Override
            public void onResponse(ResponseEnterpriseAuthenticationEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if(authSuccessDialog != null){
                            String companyName = s.data.getCompanyName();
                            authSuccessDialog.setCompanyName(companyName);
                        }
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseEnterpriseAuthenticationEntity> getClazz() {
                return ResponseEnterpriseAuthenticationEntity.class;
            }
        });
    }

    private void toEnterpriseAuthentication() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(this.requestEntity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.CLOUDAUTH_COMMPANY_AUTHOR, jsonString).execute(new MyStringCallback<CompanyBean>() {
            @Override
            public void onResponse(CompanyBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        //ToastUtil.showToastString("数据提交成功");
                        etCompanyName.setText(s.data.getCompanyName());
                        etCode.setText(s.data.getCreditCode());
                        etAddressDetails.setText(s.data.getCompanyAddress());
                        isPass=true;
                    } else {
                        isPass=false;
                        ToastUtil.showToastString(s.message);
                        errorString=s.message;
                    }
                }
            }

            @Override
            public Class<CompanyBean> getClazz() {
                return CompanyBean.class;
            }
        });
    }

    private void addAuthenticationInfo() {
        requestEntity  = new RequestEnterpriseAuthenticationEntity();
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestBody = new RequestEntity(0);
        String name  = etCompanyName.getText().toString();
        String code  = etCode.getText().toString();
        String addr  = etAddressDetails.getText().toString();
        requestEntity.setCompanyName(name);
        requestEntity.setCreditCode(code);
        requestEntity.setCompanyAddress(addr);
        requestEntity.setLicencePicture(remoteUrl);
        requestEntity.setCompanyAreaCode(areaCode);
        requestEntity.setCompanyCityCode(cityCode);
        requestEntity.setCompanyProvinceCode(provinceCode);
        requestBody.setData(this.requestEntity);
        String jsonString = JsonManager.createJsonString(requestBody);
        OkgoUtils.post(ProjectUrl.COMPANY_AUTH_AUTHUNDERTAKE, jsonString).execute(new MyStringCallback<CompanyBean>() {
            @Override
            public void onResponse(CompanyBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        //ToastUtil.showToastString("数据提交成功");
                        etCompanyName.setText(s.data.getCompanyName());
                        etCode.setText(s.data.getCreditCode());
                        etAddressDetails.setText(s.data.getCompanyAddress());
                        finish();
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<CompanyBean> getClazz() {
                return CompanyBean.class;
            }
        });
    }

}
