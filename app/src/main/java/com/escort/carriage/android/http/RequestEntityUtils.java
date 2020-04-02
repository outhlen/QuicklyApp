package com.escort.carriage.android.http;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.adapter.ArrayListAdapter;
import com.androidybp.basics.utils.file.photo.ProjectPhotoUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.my.CloudauthGetassumeroleEntity;
import com.escort.carriage.android.entity.request.PageBean;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.RequestListBean;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.entity.response.my.ResponseCloudauthGetassumeroleEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestEntityUtils {


    public static RequestEntity getRequestListEntity(int pageNum, int pageSize) {
        RequestEntity requestEntity = new RequestEntity(0);
        RequestListBean page = new RequestListBean();
        page.setPage(getPageBean(pageNum, pageSize));
        requestEntity.setData(page);
        return requestEntity;
    }

    public static PageBean getPageBean(int pageNum, int pageSize){
        PageBean pageBean = new PageBean();
        pageBean.setPageNumber(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

      public static PageBean getPageBeanOrders(int pageNum, int pageSize){
        PageBean pageBean = new PageBean();
        pageBean.setPageNumber(pageNum);
        pageBean.setPageSize(pageSize);
          PageBean.OrdersBean ordersBean = new PageBean.OrdersBean();
          ordersBean.setField("id");
          ordersBean.setDirection("DESC");
          ArrayList<PageBean.OrdersBean> arrayList = new ArrayList<>();
          arrayList.add(ordersBean);
          pageBean.setOrders(arrayList);
          return pageBean;
    }



    /**
     * 专门上传图片用的方法  使用 OSS方式
     * @param openType
     * @param uri
     * @param imageCallback
     */
    public static void uploadOssImage(Activity activity, int openType, Uri uri, ImageCallback imageCallback) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(activity, "提交数据");
        ThreadUtils.openSonThread(new Runnable() {
            @Override
            public void run() {
                File requestFile = ProjectPhotoUtils.compressImage(uri);
                RequestEntity requestEntity = new RequestEntity(0);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("param", "undertakeAndriod");
                requestEntity.setData(hashMap);
                String jsonString = JsonManager.createJsonString(requestEntity);
                OkgoUtils.post(ProjectUrl.CLOUDAUTH_GETASSUMEROLE, jsonString).execute(new MyStringCallback<ResponseCloudauthGetassumeroleEntity>() {
                    @Override
                    public void onResponse(ResponseCloudauthGetassumeroleEntity s) {
                        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                        if (s != null) {
                            if (s.success && s.data != null) {
                                CloudauthGetassumeroleEntity entity = s.data;
                                UploadHelper uploadHelper = new UploadHelper(entity.url, entity.accessKeyId, entity.accessKeySecret, entity.securityToken, entity.bucketName);
                                String uploaduel = uploadHelper.uploadPortrait(requestFile.getPath());
                                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                                if(!TextUtils.isEmpty(uploaduel)){
                                    if(imageCallback != null ){
                                        imageCallback.imageCallback(openType, uploaduel);
                                    }
                                } else {
                                    ToastUtil.showToastString("图片上传失败");
                                }
                            } else {
                                ToastUtil.showToastString(s.message);
                            }
                        }
                    }

                    @Override
                    public Class<ResponseCloudauthGetassumeroleEntity> getClazz() {
                        return ResponseCloudauthGetassumeroleEntity.class;
                    }
                });

            }
        });

    }
  /**
     * 专门上传图片用的方法
     * @param openType
     * @param uri
     * @param imageCallback
     */
    public static void uploadImage(Activity activity, int openType, Uri uri, ImageCallback imageCallback) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(activity, "提交数据");
        ThreadUtils.openSonThread(new Runnable() {
            @Override
            public void run() {
                File requestFile = ProjectPhotoUtils.compressImage(uri);
                OkgoUtils.post(ProjectUrl.OSS_UPLOAD_FILE, requestFile).execute(new MyStringCallback<ResponceBean>() {
                    @Override
                    public void onResponse(ResponceBean s) {
                        UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                        if (s != null) {
                            if (s.success) {
                                if(imageCallback != null){
                                    imageCallback.imageCallback(openType, s.data);
                                }
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
        });

    }

    public interface ImageCallback{
       void imageCallback(int openType, String url);
    }
}
