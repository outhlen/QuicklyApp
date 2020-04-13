package com.androidybp.basics.ui.view.toast;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.R;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.androidybp.basics.utils.thread.ThreadUtils;

/**
 * 使用原生方式显示Toast
 */
public class ProjectAndroidToastUtils {
    private Toast toast;
    private Toast centerToast;

    /****************************************************************************************************
     原生Toast模块
     ****************************************************************************************************/

    /**
     * 显示吐司  可以在子线程中调用
     *
     * @param message 吐司要显示的内容
     */
    public void showToastString(final String message) {
        if (TextUtils.isEmpty(message))
            return;
        if (ThreadUtils.isMainThread()) {
            showToast(message);
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    showToast(message);
                }
            });
        }
    }

    /**
     * 显示吐司   可以在子线程中调用
     *
     * @param message 要显示的资源文件id
     */
    public void showToastResources(int message) {
        String str = ResourcesTransformUtil.getString(message);
        showToastString(str);
    }

    /**
     * 显示吐司
     *
     * @param message 吐司要显示的内容
     */
    public void showToastInt(int message) {
        String str = String.valueOf(message);
        showToastString(str);
    }

    /**
     * 显示吐司
     *
     * @param message 吐司要显示的内容
     */
    public void showToastBoolean(boolean message) {
        String str = String.valueOf(message);
        showToastString(str);
    }

    public void showToast(String str) {
        if (toast == null) {
            toast = Toast.makeText(ApplicationContext.getInstance().getContext(), str, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(ApplicationContext.getInstance().getContext(), str, Toast.LENGTH_SHORT);
        }
//        toast.setText(str);
        toast.show();
    }
    /*************************************************************************************************
     自定义Toast
     *************************************************************************************************/
/*
                                使用说明
     1、直接调用 showCustomToast 方法就可以使用， 参数第一个是要展示图片的id  第二个是要展示的文字
     2、在Activity或者Fragment中使用  要在生命周期关闭的时候调用
                        if(ToastUtil.centerIsShow()){
                            ToastUtil.cancelCenterToast();
                        }
     3、在点击返回按钮的时候也要调用关闭方法
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && ToastUtil.centerIsShow()) {
               ToastUtil.cancelCenterToast();
            }
            return super.onKeyDown(keyCode, event);
        }
 */

    /**
     * 显示自定义吐司  可以在子线程中调用
     *
     * @param resources 要显示的图片资源
     * @param string    要显示的文字
     */
    public void showCustomToast(final int resources, final String string) {
        if (ThreadUtils.isMainThread()) {
            showCenterToast(resources, string);
        } else {
            ThreadUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    showCenterToast(resources, string);
                }
            });
        }
    }

    /**
     * 关掉自定义的Toast
     */
    public void cancelCenterToast() {
        if (centerToast != null) {
            centerToast.cancel();
            centerToast = null;
        }
    }

    /**
     * 判断自定义的View是否显示
     *
     * @return
     */
    public boolean centerIsShow() {
        if (centerToast != null) {
            return centerToast.getView().isShown();
        } else {
            return false;
        }
    }

    /**
     * 显示自定义 Toast
     *
     * @param resources
     * @param string
     */
    private void showCenterToast(int resources, String string) {
        if (centerToast == null) {
            centerToast = new Toast(ApplicationContext.getInstance().getContext());
            View toastRoot = LayoutInflater.from(ApplicationContext.getInstance().getContext()).inflate(R.layout.view_mytoast, null);
            centerToast.setGravity(Gravity.FILL, 0, 0);
            centerToast.setView(toastRoot);
        }
        centerToast.setDuration(Toast.LENGTH_SHORT);
        View view = centerToast.getView();
        boolean visibility = view.isShown();
        ImageView image = (ImageView) view.findViewById(R.id.mytoast_image);
        TextView text = (TextView) view.findViewById(R.id.mytoast_text);
        image.setImageResource(resources);
        text.setText(string);
        centerToast.show();
    }
}
