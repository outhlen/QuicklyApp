package com.escort.carriage.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.MenuBean;
import com.escort.carriage.android.entity.bean.home.VersionEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.ResponseIntegerBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.WebPageActivity;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.activity.login.ForgetPwdActivity;
import com.escort.carriage.android.ui.activity.login.LoginActivity;
import com.escort.carriage.android.ui.activity.login.RegisterPhoneActivity;
import com.escort.carriage.android.ui.activity.my.EnterpriseAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.PersonageAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.SettingActivity;
import com.escort.carriage.android.ui.activity.my.UserInfoActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.view.dialog.AdvertisingImageDialog;
import com.escort.carriage.android.ui.view.dialog.VersionDialog;
import com.escort.carriage.android.utils.AppUpdateUtils;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnDownloadListener;
import com.hjq.http.model.DownloadInfo;
import com.hjq.http.model.HttpMethod;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class MainFrgament extends BaseFragment implements GridRecyclerAdapter.ClickCallBack {

    View view = null;
    Unbinder unbinder;
    private static final String ARG_SHOW_TEXT = "text";
    private boolean mDownloadComplete;
    private boolean mDownloading;
    private File mApkFile;
    private String mFileMD5;

    private RecyclerView recyclerView;
    List<MenuBean> menuBeans = null;
    GridRecyclerAdapter adapter;
    private AdvertisingImageDialog advertisingImageDialog;
    private String mDownloadUrl;

    public static MainFrgament newInstance(String param1) {
        MainFrgament fragment = new MainFrgament();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main, container, false);
            unbinder = ButterKnife.bind(this, view);
        } else {
            unbinder = ButterKnife.bind(this, view);
        }
        initView();
        initDate();
        return view;
    }

    private void initDate() {
        menuBeans = new ArrayList<>();
        MenuBean menuBean1 = new MenuBean();
        menuBean1.setName("登录模块");
        menuBean1.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean2 = new MenuBean();
        menuBean2.setName("注册模块");
        menuBean2.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean3 = new MenuBean();
        menuBean3.setName("分享模块");
        menuBean3.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean4 = new MenuBean();
        menuBean4.setName("邀请模块");
        menuBean4.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean5 = new MenuBean();
        menuBean5.setName("个人认证模块");
        menuBean5.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean6 = new MenuBean();
        menuBean6.setName("企业认证模块");
        menuBean6.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean7 = new MenuBean();
        menuBean7.setName("个人信息模块");
        menuBean7.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean8 = new MenuBean();
        menuBean8.setName("会员设置模块");
        menuBean8.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean9 = new MenuBean();
        menuBean9.setName("会员资产模块");
        menuBean9.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean22 = new MenuBean();
        menuBean22.setName("找回密码模块");
        menuBean22.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean23 = new MenuBean();
        menuBean23.setName("系统升级");
        menuBean23.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean24 = new MenuBean();
        menuBean24.setName("手写板");
        menuBean24.setImage(R.mipmap.ic_launcher);
        MenuBean menuBean25 = new MenuBean();
        menuBean25.setName("地图");
        menuBean25.setImage(R.mipmap.ic_launcher);

        menuBeans.add(menuBean1);
        menuBeans.add(menuBean2);
        menuBeans.add(menuBean3);
        menuBeans.add(menuBean4);
        menuBeans.add(menuBean5);
        menuBeans.add(menuBean6);
        menuBeans.add(menuBean7);
        menuBeans.add(menuBean8);
        menuBeans.add(menuBean9);
        menuBeans.add(menuBean22);
        menuBeans.add(menuBean23);
        menuBeans.add(menuBean24);
        menuBeans.add(menuBean25);

        adapter = new GridRecyclerAdapter(getActivity(), menuBeans, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(4, 10, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
    }


    @Override
    public void onClick(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(getActivity(), RegisterPhoneActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), WebPageActivity.class));
                break;
            case 3:
                Intent intentInvate = new Intent(getActivity(), VueActivity.class);
                intentInvate.putExtra("url", VueUrl.inviteFriends);
                startActivity(intentInvate);
                break;
//            case 4://邀请模块
//               // getPersonageAuthentication(1);//个人认证
//                Intent ii = new Intent(getActivity(), VueActivity.class);
//                ii.putExtra("url", VueUrl.inviteFriends);
//                startActivity(ii);
//
//                break;
            case 4:
                //getPersonageAuthentication(2);// 企业认证
                Intent intent2 = new Intent(getActivity(), PersonageAuthenticationActivity.class);
                intent2.putExtra("status", 1);
                startActivity(intent2);

                break;
            case 5:
                Intent intent1 = new Intent(getActivity(), EnterpriseAuthenticationActivity.class);
                intent1.putExtra("status", 1);
                startActivity(intent1);


                break;
            case 6: //个人信息
                startActivity(new Intent(getActivity(), UserInfoActivity.class));

                break;
            case 7:
                startActivity(new Intent(getActivity(), SettingActivity.class)); //设置

                break;
            case 8:
                startActivity(new Intent(getActivity(), PersonBounsActvitiy.class));
                break;
            case 9:
                startActivity(new Intent(getActivity(), ForgetPwdActivity.class));
                break;
            case 10:
                getVersion();
                break;
            case 11:
                startActivity(new Intent(getActivity(), SignatureActivity.class));
                break;

            case 12:
                startActivity(new Intent(getActivity(), MapMangerActivity.class));
                break;
        }
    }


    private void getVersion() {
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("terminalId", "1");
        hashMap.put("groupId", "1");
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.CONFIG_GETVERSION, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                if (s != null) {
                    if (s.success) {
                        VersionEntity jsonBean = JsonManager.getJsonBean(s.data, VersionEntity.class);
                        String versionName = ApplicationContext.getInstance().versionName;
                        int versionCode = ApplicationContext.getInstance().versionCode;
                        if (jsonBean.terminalId == 1 && jsonBean.groupId == 1 && versionCode < jsonBean.versionCode) {
                            if (advertisingImageDialog != null) {
                                advertisingImageDialog.dismiss();
                            }
                            showVersionDialog(jsonBean);
                        }
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    private void showVersionDialog(VersionEntity jsonBean) {
        //弹出对话框
        VersionDialog authSuccessDialog = VersionDialog.getInstance(getActivity(), jsonBean.compulsory);
        authSuccessDialog.setVersionName(jsonBean.versionName);
        authSuccessDialog.setClickKnowListener(new VersionDialog.OnClickKnowListener() {
            @Override
            public void onClickKnow(Dialog dialog, int compulsory) {
                dialog.dismiss();
                //跳转浏览器
                String downLoadUrl = jsonBean.updateUrl;
                mDownloadUrl = downLoadUrl;
                setEmpower();
            }
        });
        authSuccessDialog.show();
    }

    //授权
    private void setEmpower() {
        XXPermissions.with(getActivity())
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            if (mDownloadComplete) {
                                // 下载完毕，安装 Apk
                                // installApk();
                                AppUpdateUtils.installApk(getActivity(),mApkFile.getPath());
                            } else if (!mDownloading) {
                                // 没有下载，开启下载
                                downloadApk();
                            }
                        }
                    }
                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtil.showToastString("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            ToastUtil.showToastString("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        }
                    }
                });
    }

    private void downloadApk() {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIcon(null);
        progressDialog.setTitle("下载");
        progressDialog.setMessage("正在下载中");
        progressDialog.setMax(100);
        //ProgressDialog.STYLE_SPINNER  默认进度条是转圈
        //ProgressDialog.STYLE_HORIZONTAL  横向进度条
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();
        // 创建要下载的文件对象
        mApkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), getString(R.string.app_name) + ".apk");
        EasyHttp.download(getActivity())
                .method(HttpMethod.GET)
                .file(mApkFile)
                .url(mDownloadUrl)
                .md5(mFileMD5)
                .listener(new OnDownloadListener() {
                    @Override
                    public void onStart(Call call) {
                        // 标记为下载中
                        mDownloading = true;
                        // 标记成未下载完成
                        mDownloadComplete = false;
                    }

                    @Override
                    public void onProgress(DownloadInfo info) {
                        int downloadProgress = info.getDownloadProgress();
                        progressDialog.setProgress(downloadProgress);
                    }

                    @Override
                    public void onComplete(DownloadInfo info) {
                        progressDialog.dismiss();
                        ToastUtil.showToastString("下载完成");
                        // 标记成下载完成
                        mDownloadComplete = true;
                        // 安装 Apk
                        AppUpdateUtils.installApk(getActivity(),mApkFile.getPath());
                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void onError(DownloadInfo info, Exception e) {
                        // 删除下载的文件
                        info.getFile().delete();
                        ToastUtil.showToastString("下载失败");
                    }

                    @Override
                    public void onEnd(Call call) {
                        // 标记当前不是下载
                        mDownloading = false;
                        ToastUtil.showToastString("下载成功");
                    }
                }).start();
    }


    private void getPersonageAuthentication(int type) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
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
                        if (status == 1) {
                            ToastUtil.showToastString("认证正在审核中");
                        } else {
                            if (type == 1) {
                                Intent intent = new Intent(getActivity(), PersonageAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                startActivity(intent);
                            } else if (type == 2) {
                                Intent intent = new Intent(getActivity(), EnterpriseAuthenticationActivity.class);
                                intent.putExtra("status", status);
                                startActivity(intent);
                            }
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
}
