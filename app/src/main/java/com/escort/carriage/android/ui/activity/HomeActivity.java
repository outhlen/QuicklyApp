package com.escort.carriage.android.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.cache.db.model.DataCacheKeyModel;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.Fileprovider;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectDataConfig;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.AmapCacheEntity;
import com.escort.carriage.android.entity.bean.home.VersionEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.view.dialog.AdvertisingImageDialog;
import com.escort.carriage.android.ui.view.dialog.AuthSuccessDialog;
import com.escort.carriage.android.ui.view.dialog.VersionDialog;
import com.escort.carriage.android.ui.view.holder.HomeLeftHolder;
import com.escort.carriage.android.ui.view.holder.HomeMainHolder;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.OnDownloadListener;
import com.hjq.http.model.DownloadInfo;
import com.hjq.http.model.HttpMethod;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tripartitelib.android.amap.AmapUtils;
import com.tripartitelib.android.iflytek.IflytekUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import okhttp3.Call;

public class HomeActivity extends ProjectBaseActivity {
    private DrawerLayout drawerLayout;
    private FrameLayout homeFrameLayout;
    private FrameLayout leftFrameLayout;
    private HomeLeftHolder homeLeftHoler;
    private HomeMainHolder homeMainHoler;

    /**
     * Apk 文件
     */
    private File mApkFile;
    /**
     * 下载地址
     */
    private String mDownloadUrl;
    /**
     * 文件 MD5
     */
    private String mFileMD5;
    /**
     * 当前是否下载中
     */
    private boolean mDownloading;
    /**
     * 当前是否下载完毕
     */
    private boolean mDownloadComplete;

    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";
    private AdvertisingImageDialog advertisingImageDialog;

    public static void startHomeActivity(Activity activity) {
        activity.startActivity(new Intent(activity, HomeActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isClickIntercept = false;
        StatusBarCompatManager.fullScreen(this);
        setContentView(R.layout.activity_home);
        findPageView();
        homeLeftHoler = new HomeLeftHolder(this, leftFrameLayout);
        homeMainHoler = new HomeMainHolder(this, homeFrameLayout);
        getUserInfo();
        IflytekUtils.getIflytekUtils().initTts(ApplicationContext.getInstance().application);
        //获取更新
        getVersion();
        //开启轨迹
        if (ProjectDataConfig.isOpenAmap) {
            AmapUtils.getAmapUtils().initTrace(this);
        }
        ThreadUtils.openSonThread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://xeryb.oss-cn-qingdao.aliyuncs.com/jiaozheng/orderInfo/33.png";
//                        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1585449481161&di=986f50d8be7bca44aeb3004fc525721f&imgtype=0&src=http%3A%2F%2Fbbs.jooyoo.net%2Fattachment%2FMon_0905%2F24_65548_2835f8eaa933ff6.jpg";
                        showPageImageDialog(url);
                    }
                });
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean showListInfo = intent.getBooleanExtra("showListInfo", false);
            if (showListInfo) {
                //隐藏抽屉
                if (drawerLayout != null) {
                    boolean drawerOpen = drawerLayout.isDrawerOpen(GravityCompat.START);
                    if (drawerOpen) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                //显示货源大厅
                if (homeMainHoler != null) {
                    homeMainHoler.showHomeListFrag();
                }

            }
        }
    }

    private void showPageImageDialog(String url) {
        advertisingImageDialog = new AdvertisingImageDialog(this, url, new AdvertisingImageDialog.Callback() {
            @Override
            public void callback(boolean type) {

            }
        });
        advertisingImageDialog.showAtLocation(
                findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0,
                0
        );
    }

    private void findPageView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        homeFrameLayout = findViewById(R.id.homeFrameLayout);
        leftFrameLayout = findViewById(R.id.leftFrameLayout);

    }

    public HomeMainHolder getHomeMainHoler() {
        return homeMainHoler;
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

    private void updataUserInfo(UserInfoEntity entity) {
        if (homeMainHoler != null) {
            homeMainHoler.updataUserInfo(entity);
        }

        if (homeLeftHoler != null) {
            homeLeftHoler.updataUserInfo(entity);
        }
    }

    public void openLeftView() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ProjectDataConfig.isOpenAmap) {
            getAmapInfo();
            //获取  ORDER_GETGOLDFALCON
//
        }
    }

    //--------- 轨迹 使用 配置参数

    /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification(Context context) {
        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(context.getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }
        Intent nfIntent = new Intent(ApplicationContext.getInstance().context, context.getClass());
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(ApplicationContext.getInstance().context, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(ResourcesTransformUtil.getString(R.string.app_name) + "运行中")
                .setContentText(ResourcesTransformUtil.getString(R.string.app_name) + " 点击查看详情");
        Notification notification = builder.build();
        return notification;
    }

    private void getAmapInfo() {

        AmapCacheEntity projectDataEntity = CacheDBMolder.getInstance().getProjectDataEntity(DataCacheKeyModel.AMAP_DATA, AmapCacheEntity.class);
        if (projectDataEntity != null && projectDataEntity.sid != 0 && projectDataEntity.tid != 0 && projectDataEntity.trid != 0) {
            //获取
            AmapUtils.getAmapUtils().startTrack(createNotification(this), projectDataEntity.sid, projectDataEntity.tid, projectDataEntity.trid);
        } else {
            RequestEntity requestEntity = new RequestEntity(0);
            requestEntity.setData("");
            String jsonString = JsonManager.createJsonString(requestEntity);
            OkgoUtils.post(ProjectUrl.ORDER_GETGOLDFALCON, jsonString).execute(new MyStringCallback<ResponceBean>() {
                @Override
                public void onResponse(ResponceBean s) {
                    if (s != null) {
                        if (s.success) {
                            AmapCacheEntity endLocationJson = JsonManager.getJsonBean(s.data, AmapCacheEntity.class);
                            String json = JsonManager.createJsonString(s.data);
                            CacheDBMolder.getInstance().setProjectDataEntity(DataCacheKeyModel.AMAP_DATA, json, 1, -1);
                            AmapUtils.getAmapUtils().startTrack(createNotification(HomeActivity.this), endLocationJson.sid, endLocationJson.tid, endLocationJson.trid);
                        }
                    }
                }

                @Override
                public Class<ResponceBean> getClazz() {
                    return ResponceBean.class;
                }
            });
        }

//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");


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
                        if(jsonBean.terminalId == 1 && jsonBean.groupId == 1 && versionCode > jsonBean.versionCode){
                            if(advertisingImageDialog != null){
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
        VersionDialog authSuccessDialog = VersionDialog.getInstance(HomeActivity.this, jsonBean.compulsory);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            getUserInfo();
        }
    }

    //授权
    private void setEmpower() {
        XXPermissions.with(HomeActivity.this)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .permission(Permission.Group.STORAGE) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            if (mDownloadComplete) {
                                // 下载完毕，安装 Apk
                                installApk();
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
                            XXPermissions.gotoPermissionSettings(HomeActivity.this);
                        } else {
                            ToastUtil.showToastString("获取权限失败");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(HomeActivity.this);
                        }
                    }
                });
    }

    /**
     * 下载 Apk
     */
    private void downloadApk() {
        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
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
        EasyHttp.download(HomeActivity.this)
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
                        installApk();
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

    /**
     * 安装 Apk
     */

    private void installApk() {

        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            Fileprovider fileprovider = new Fileprovider();
            Uri uri = fileprovider.getUri(this, mApkFile, intent);
            intent.setDataAndType(uri, type);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }


//        XXPermissions.with(HomeActivity.this)
//                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
//                .permission(Permission.REQUEST_INSTALL_PACKAGES) //不指定权限则自动获取清单中的危险权限
//                .request(new OnPermission() {
//                    @Override
//                    public void hasPermission(List<String> granted, boolean isAll) {
//                        if (isAll) {
//                            if (mDownloadComplete) {
//                                // 下载完毕，安装 Apk
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_VIEW);
//                                Uri uri;
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    uri = FileProvider.getUriForFile(HomeActivity.this, KeyConstant.provider, mApkFile);
//                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                                } else {
//                                    uri = Uri.fromFile(mApkFile);
//                                }
//
//                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            } else if (!mDownloading) {
//                                // 没有下载，开启下载
//                                downloadApk();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void noPermission(List<String> denied, boolean quick) {
//                        if (quick) {
//                            ToastUtil.showToastString("被永久拒绝授权，请手动授予权限");
//                            //如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.gotoPermissionSettings(HomeActivity.this);
//                        } else {
//                            ToastUtil.showToastString("获取权限失败");
//                            //如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.gotoPermissionSettings(HomeActivity.this);
//                        }
//                    }
//                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (homeMainHoler != null) {
            homeMainHoler.clear();
        }

        if (homeLeftHoler != null) {
            homeLeftHoler.clear();
        }
    }
}
