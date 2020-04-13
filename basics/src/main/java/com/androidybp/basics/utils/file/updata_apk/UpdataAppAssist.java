package com.androidybp.basics.utils.file.updata_apk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.androidybp.basics.R;
import com.androidybp.basics.okhttp3.assist.DownFileAssist;
import com.androidybp.basics.ui.dialog.DownloadFileDialog;
import com.androidybp.basics.ui.dialog.templet.HintTwoBtnTempletDialog;
import com.androidybp.basics.ui.dialog.templet.listerner.DialogCallbackListener;
import com.androidybp.basics.utils.equipment.OpenExternalEquipent;
import com.androidybp.basics.utils.file.FileUtil;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;

/**
 * dowloadType : 0-静默下载  1-带进度的下载
 * updataType : 0-非强制升级  1-强制升级
 */
public class UpdataAppAssist {

    private FileDownload downFileAssist;
    private Activity activity;
    private UpdataAPkEntity bean;
    private DownloadFileDialog downloadFileDialog;

    /**
     * @param activity
     * @param bean
     * @param type     0:直接使用浏览器下载  1：使用传统方式自动下载
     */
    public UpdataAppAssist(Activity activity, UpdataAPkEntity bean, int type) {
        this.activity = activity;
        this.bean = bean;
        if (bean != null) {
            if (type == 0) {
                openBrowser();
            } else if (type == 1) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                //当给定的SDK 版本  小于  等于   当前版本号    使用外跳升级方式升级
                if (bean.sdkVersion < currentapiVersion) {
                    downloadApkFile();
                } else {
                    if (bean.updataType == 1) {
                        showDialog(null);
                    }
                }
            }

        }

    }

    private void downloadApkFile() {
        if (bean.dowloadType == 1) {
            //打开进度显示条
            downloadFileDialog = new DownloadFileDialog(activity);
            downloadFileDialog.setUpataType(bean.updataType);
            downloadFileDialog.setListener(new DialogCallbackListener() {
                @Override
                public void clickCallBack(Dialog dialog, int type) {
                    dialog.dismiss();
                    downloadFileDialog = null;
                }
            });
            downloadFileDialog.setCanceledOnTouchOutside(false);
            downloadFileDialog.setCancelable(false);
            downloadFileDialog.show();
        }
        if (downFileAssist == null)
            downFileAssist = new FileDownload(bean.downUrl, bean.filePath);
    }

    private void showDialog(String context) {
        HintTwoBtnTempletDialog hintTwoBtnTempletDialog = new HintTwoBtnTempletDialog(activity);
        hintTwoBtnTempletDialog.setTitleTextRes(R.string.prompt);
        if (TextUtils.isEmpty(context)) {
            context = "您的版本太低，请升级后操作";
        }
        hintTwoBtnTempletDialog.setContext(context);
        hintTwoBtnTempletDialog.setBtnText("打开浏览器", "打开应用商城");
        hintTwoBtnTempletDialog.setListener(new DialogCallbackListener() {
            @Override
            public void clickCallBack(Dialog dialog, int type) {
                switch (type) {
                    case 1://打开应用商城
                        try {
                            launchAppDetail(activity, activity.getPackageName(), "");
                            if (dialog != null)
                                dialog.dismiss();
                        } catch (Exception e) {
                            ToastUtil.showToastString("请是使用浏览器手动下载");
                        }

                        break;
                    case 2://打开浏览器
                        openBrowser();
                        if (dialog != null)
                            dialog.dismiss();
                        break;
                }

            }
        });
        hintTwoBtnTempletDialog.getLeftBtn().setTextColor(ResourcesTransformUtil.getColor(R.color.color_666666));
        hintTwoBtnTempletDialog.setCanceledOnTouchOutside(false);
        if (bean.updataType == 1) {
            hintTwoBtnTempletDialog.setCancelable(false);
        }
        hintTwoBtnTempletDialog.show();
    }

    private void openBrowser() {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(bean.downUrl);
            intent.setData(content_url);
            activity.startActivity(intent);

        } catch (Exception e) {

        }
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public void launchAppDetail(Context context, String appPkg, String marketPkg) {
        if (TextUtils.isEmpty(appPkg)) return;

        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (!TextUtils.isEmpty(marketPkg)) {
            intent.setPackage(marketPkg);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    class FileDownload extends DownFileAssist {
        long fileLength = 0;

        public FileDownload(String url, String downPath) {
            super(url, downPath);
        }

        @Override
        public void downLoser() {
            showDialog("自动升级失败，请选择其它升级方式");
        }

        @Override
        public void downFileLength(long length) {
            //文件大小
            fileLength = length;
        }

        @Override
        public void downNumber(long count) {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    float d = (float) count / fileLength;
                    if (downloadFileDialog != null) {
                        downloadFileDialog.setProgress(d);
                    }
                }
            });

        }

        //这里是主线程
        @Override
        public void downFileAccomplish() {
            OpenExternalEquipent mOpenExternalEquipent = new OpenExternalEquipent();
            mOpenExternalEquipent.openApk(activity, bean.filePath);//打开APK按装页面
            //关闭所有的 展示Dialog
            if (downloadFileDialog != null) {
                downloadFileDialog.dismiss();
            }
        }
    }

    public void clear() {
        if (downloadFileDialog != null) {
            downloadFileDialog.dismiss();
            downloadFileDialog = null;
        }
        downFileAssist = null;
        activity = null;
        bean = null;
    }

}
