package com.escort.carriage.android.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.androidybp.basics.fastjson.JsonManager;
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
import com.escort.carriage.android.ui.adapter.home.GridViewImgsAdapter;
import com.escort.carriage.android.ui.view.gridview.MyGridView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ComplaintActivity extends ProjectBaseEditActivity {

    @BindView(R.id.et_evalute)
    EditText etEvalute;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.btn_commit_evalute)
    Button btnCommitEvalute;
    @BindView(R.id.main)
    LinearLayout main;
    @BindView(R.id.gridView)
    MyGridView gridView;
    @BindView(R.id.et_phone)
    EditText editPhone;

    private SelectPhotoUtils selectPhotoUtils;
    private String imageUrl;
    private String orderNumber;
    private List<LocalMedia> mPaths = new ArrayList<>();//ImageItem
    private GridViewImgsAdapter gridViewImgsAdapter = null;
    private String urlArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
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
        tvTitle.setText("投诉");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setSelectUtils() {
//        selectPhotoUtils = new SelectPhotoUtils(this, null);
//        selectPhotoUtils.setCallback(new SelectPhotoUtils.SelectPhotoCallback() {
//            @Override
//            public void selectPhotoCallback(int selectType, int openType, Uri uri) {
//                //将图片上传到服务器上
//                uploadImage(openType, uri);
//            }
//        });
        gridViewImgsAdapter = new GridViewImgsAdapter(this, mPaths);
        gridView.setAdapter(gridViewImgsAdapter);
        gridViewImgsAdapter.setImgChangeCallback(new GridViewImgsAdapter.ImgChangeCallback() {
            @Override
            public void onImgChange(List<LocalMedia> paths) {
                List<LocalMedia> tempList = new ArrayList<>();
                tempList.addAll(paths);
                mPaths.clear();
                mPaths.addAll(tempList);
                gridViewImgsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onImgDelete(LocalMedia media) {
                if (mPaths.size() > 0) {
                    mPaths.remove(media);
                    gridViewImgsAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            if (PictureSelector.obtainMultipleResult(data) == null || PictureSelector
                    .obtainMultipleResult(data).size() == 0)
                return;
            mPaths.clear();
            // 图片、视频、音频选择结果回调
            mPaths.addAll(PictureSelector.obtainMultipleResult(data));
            gridViewImgsAdapter.notifyDataSetChanged();
        }
    }


    private void uploadImage(int openType, List<LocalMedia> mediaLista,String msg,String phone) {
        ArrayList<String> urls = new ArrayList<>();
        if (mediaLista.size() > 0) {
            for (LocalMedia local : mediaLista) {
                urls.add(local.getPath());
            }
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    RequestEntityUtils.uploadOssImageArray(ComplaintActivity.this, openType, urls, new RequestEntityUtils.ImageCallback() {
                        @Override
                        public void imageCallback(int openType, String url) {
                        }
                        @Override
                        public void imageArrayCallback(int openType, List<String> urls) {
                            StringBuilder stringBuilder = new StringBuilder();
                            if(urls.size()==1){
                                imageUrl  =  urls.get(0);
                            }else{
                                for(String url  :urls){
                                    stringBuilder.append(url).append(",");
                                    imageUrl  =  stringBuilder.substring(0,stringBuilder.length()-1);
                                }
                            }
                            uploadInfo(phone,msg);
                        }
                    });
                }
            }.start();
        }else{
            uploadInfo(phone,msg);
        }

    }

    private void uploadInfo(String phone,String msg) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("phone", phone);
        hashMap.put("msg", msg);
        hashMap.put("code", "");
        hashMap.put("type", "1");
        hashMap.put("img", imageUrl);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.TOUSUINFO_URL, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        ToastUtil.showToastString("投诉成功");
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
                String phone  =   editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    ToastUtil.showToastString("请输入内容");
                } else if(TextUtils.isEmpty(phone)){
                    ToastUtil.showToastString("请输入投诉手机号");
                }
                else {
                    uploadImage(0,mPaths,inputText,phone);
                }
                break;
        }
    }

    private void finishPage() {
        finish();
    }


}
