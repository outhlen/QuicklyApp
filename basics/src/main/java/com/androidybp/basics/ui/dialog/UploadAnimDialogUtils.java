package com.androidybp.basics.ui.dialog;

import android.content.Context;

import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.thread.ThreadUtils;


/**
 * 全局加载
 */
public class UploadAnimDialogUtils {
    private CustomProgressDialog mProgressDialog;//进度条
    private static volatile UploadAnimDialogUtils mDialogUtils;

    /**
     * 创建对话框
     */
    public static UploadAnimDialogUtils singletonDialogUtils() {
        if (mDialogUtils == null) {
            mDialogUtils = new UploadAnimDialogUtils();
        }
        return mDialogUtils;
    }

    /**
     * 显示数据加载的动画
     */

    public synchronized void showCustomProgressDialog(final Context context, final String text) {
        if (ThreadUtils.isMainThread()) {
            createDialog(context);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.setMessage(text);
            } else {
                try {
                    mProgressDialog.setMessage(text).show();
                } catch (Exception e) {
                    LogUtils.showE("mProgressDialog", "setMessage(text).show() 错误！");
                }
            }
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (context != null) {
                        createDialog(context);
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.setMessage(text);
                        } else {
                            mProgressDialog.setMessage(text).show();
                        }
                    }
                }
            });
        }

    }

    private void createDialog(Context context) {
        if (mProgressDialog == null){
            synchronized (UploadAnimDialogUtils.class){
                if(mProgressDialog == null){
                    mProgressDialog = new CustomProgressDialog(context);
                }
            }
        }
    }

    /**
     * 显示加载成功的 图片
     */
    public void loadingSuccess(){
        if(ThreadUtils.isMainThread()){
            loadingSuccessMainThread();
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    loadingSuccessMainThread();
                }
            });
        }
    }

    /**
     * 显示加载成功的 图片
     */
    public void loadingFail(){
        if(ThreadUtils.isMainThread()){
            loadingFailMainThread();
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    loadingFailMainThread();
                }
            });
        }
    }


    private void loadingSuccessMainThread(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.showSuccess();
        }
        mProgressDialog = null;
    }

    /**
     * 显示加载失败的 图片
     */
    public void loadingFailMainThread(){
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.showFail();
        }
        mProgressDialog = null;
    }



    /**
     * 销毁对话框  强制硬性关闭
     */
    public void deleteCustomProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismissDialog();
        }
        mProgressDialog = null;
    }

}
