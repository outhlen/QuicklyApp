package com.escort.carriage.android.ui.activity.my;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
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

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申诉 Activity
 */
public class AppealActivity extends ProjectBaseEditActivity {

    @BindView(R.id.et_evalute)
    EditText etEvalute;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.btn_commit_evalute)
    Button btnCommitEvalute;
    @BindView(R.id.main)
    LinearLayout main;

    private SelectPhotoUtils selectPhotoUtils;
    private String imageUrl;
    private String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);
        ButterKnife.bind(this);
        orderNumber = getIntent().getStringExtra("orderNumber");
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
        tvTitle.setText("申诉");
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

    private void uploadImage(int openType, Uri uri) {

        RequestEntityUtils.uploadOssImage(this, openType, uri, new RequestEntityUtils.ImageCallback() {
            @Override
            public void imageCallback(int openType, String url) {
                imageUrl = url;
                GlideManager.getGlideManager().loadImage(url, ivImage, R.drawable.icon_order_ima_bg);
            }

            @Override
            public void imageArrayCallback(int openType, List<String> urls) {

            }
        });
    }

    @OnClick({R.id.ivImage, R.id.btn_commit_evalute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivImage:
                if (selectPhotoUtils != null) {
                    selectPhotoUtils.selectPhoto(1, main);
                }
                break;
            case R.id.btn_commit_evalute:
                String inputText = etEvalute.getText().toString().trim();
                if(TextUtils.isEmpty(inputText)){
                    ToastUtil.showToastString("请输入内容");
                } else {
                    toAppealInfo(inputText);
                }
                break;
        }
    }


    private void finishPage(){
        setResult(456);
        finish();
    }

    /**
     * 请求数据
     * @param trim
     */
    private void toAppealInfo(String trim) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("id", "1");
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("appealContent", trim);
        hashMap.put("appealImg", imageUrl);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.HANDLE_ADDAPPEAL, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        finishPage();
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
