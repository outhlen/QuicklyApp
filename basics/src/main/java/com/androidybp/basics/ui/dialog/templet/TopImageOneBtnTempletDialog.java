package com.androidybp.basics.ui.dialog.templet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.androidybp.basics.R;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.ui.dialog.templet.listerner.DialogCallbackListener;


/**
 * 一个标准的dialog提示框   样式为  顶部一个图片   中间为黑色标黑字体     底部为一个按钮
 * 里面方法除特定的方法外  都支持链条式调用
 *
 * 监听说明：
 *      点击按钮默认调用监听给打开的类，方便做后续操作  DialogCallbackListener
 *      不设置监听  点击按钮 都为关闭当前dialog
 *
 * 方法说明：
 *      获取控件的方法说明：
 *              getImageView() -- 获取顶部图片展示控件方法
 *              getContextView() -- 获取显示提示内容的控件方法
 *              getBottomBtn()     -- 获取底部按钮控件的方法
 *
 *      设置监听的方法：
 *              setListener() -- 设置监听的方法
 *
 *      设置控件内容的方法：
 *
 *              setDefaultPage()   -- 使用默认的样式 只设置提示内容   默认：标题-- 提示     按钮 -- 确定
 *
 *              setImageSrc()           --   设置顶部图片的方法一  根据类型显示预定的图片内容  1 -- 成功  2 -- 注意（异常）  3 -- 失败
 *              setImageSrcRes()        --   设置顶部图片的方法二  给控件设置指定的资源图片ID
 *              setImageSrcDrawable()   --   设置顶部图片的方法三  给控件设置指定drawable对象
 *
 *              setContext()    --  设置需要提示内容的方法一   字符串
 *              setContextRes() --  设置需要提示内容的方法二   文字资源内容ID
 *
 *              setBottomBtnText()     --  设置底部按钮显示内容的方法一    字符串
 *              setBottomBtnTextRes()  --  设置底部按钮显示内容的方法二    文字资源内容ID
 *
 *              setPageText()  -- 快速设置页面内容的方法  两种方式  一种是 字符串   一种是  文字资源内容ID
 */

public class TopImageOneBtnTempletDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;

    public TopImageOneBtnTempletDialog(Context context) {
        this(context, 0);
    }

    public TopImageOneBtnTempletDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_white_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_top_image_one_btn_templet);
        setDialogWidth();
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_top_image_one_btn_templet_button).setOnClickListener(this);
    }

    /**
     * 设置宽度
     */
    private void setDialogWidth() {
         /*————将对话框的大小按屏幕大小的百分比设置↓————*/
        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = CacheDBMolder.getInstance().getCacheDataEntity(null).getPhoneWidth() / 10 * 9; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
    }

    public TopImageOneBtnTempletDialog setListener(DialogCallbackListener listener){
        this.listener = listener;
        return this;
    }

    /**
     * 获取ImageVIew 对象
     *
     * @return 当前的图片对象   有可能为null
     */
    public ImageView getImageView() {
        try {
            return (ImageView) findViewById(R.id.dialog_top_image_one_btn_templet_image);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取TextView 对象
     *
     * @return 当前的展示文字对象   有可能为null
     */
    public TextView getContextView() {
        try {
            return (TextView) findViewById(R.id.dialog_top_image_one_btn_templet_context);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取底部按钮控件对象
     *
     * @return  底部按钮控件对象 TextView
     */
    public TextView getBottomBtn(){
        try {
            return (TextView) findViewById(R.id.dialog_top_image_one_btn_templet_button);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置图片 方法 一
     * 根据类型设置 顶部图片
     *
     * @param type 1 -- 成功  2 -- 注意（异常）  3 -- 失败
     */
    public TopImageOneBtnTempletDialog setImageSrc(int type) {

        int resImage = 0;
        switch (type) {
            case 1://成功
                resImage = R.drawable.real_name_succeed_small;
                break;
            case 2://异常
                resImage = R.drawable.real_name_fail_small;
                break;
            case 3://失败
                resImage = R.drawable.zhuyi;
                break;
        }
        if (resImage > 0) {
            ImageView imageView = getImageView();
            if(imageView != null){
                imageView.setImageResource(resImage);
            }
        }
        return this;
    }

    /**
     * 设置图片 方法 二
     * <p>
     * 给定指定的 资源图片地址  展示图片
     *
     * @param res 资源图片地址
     */
    public TopImageOneBtnTempletDialog setImageSrcRes(int res) {
        ImageView imageView = getImageView();
        if(imageView != null){
            imageView.setImageResource(res);
        }
        return this;
    }

    /**
     * 设置图片 方法 三
     * <p>
     * 给定指定的 drawanle  展示图片
     *
     * @param drawable drawanle对象
     */
    public TopImageOneBtnTempletDialog setImageSrcDrawable(Drawable drawable) {
        ImageView imageView = getImageView();
        if(imageView != null){
            imageView.setImageDrawable(drawable);
        }
        return this;
    }

    /**
     * 文字设置一
     *
     * @param str 需要设置提示文字
     */
    public TopImageOneBtnTempletDialog setContext(String str) {
        TextView contextView = getContextView();
        if(contextView != null){
            contextView.setText(str);
        }
        return this;
    }

    /**
     * * 文字设置二
     *
     * @param strRes 需要设置提示文字的资源地址
     */
    public TopImageOneBtnTempletDialog setContextRes(int strRes) {
        TextView contextView = getContextView();
        if(contextView != null){
            contextView.setText(strRes);
        }
        return this;
    }


    /**
     * 设置底部按钮显示内容   方法一
     * @param str  需要显示文字的字符串
     */
    public TopImageOneBtnTempletDialog setBottomBtnText(String str){
        TextView contextView = getBottomBtn();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置底部按钮显示内容   方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public TopImageOneBtnTempletDialog setBottomBtnTextRes(int textRes){
        TextView contextView = getBottomBtn();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 组合方法  设置页面控件的值
     * @param contextStr   提示内容  字符串
     * @param btnStr       底部按钮内容  字符串
     */
    public TopImageOneBtnTempletDialog setPageText(String contextStr, String btnStr){
        setContext(contextStr);
        setBottomBtnText(btnStr);
        return this;
    }
    /**
     * 组合方法  设置页面控件的值
     * @param contextRes   提示内容  资源ID
     * @param btnRes       底部按钮内容  资源ID
     */
    public TopImageOneBtnTempletDialog setPageText(int contextRes, int btnRes){
        setContextRes(contextRes);
        setBottomBtnTextRes(btnRes);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param str  显示字符串
     */
    public TopImageOneBtnTempletDialog setDefaultPage(String str){
        setContext(str);
        setBottomBtnTextRes(R.string.confirm);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param textRes  显示字符串资源ID
     */
    public TopImageOneBtnTempletDialog setDefaultPage(int textRes){
        setContextRes(textRes);
        setBottomBtnTextRes(R.string.confirm);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_top_image_one_btn_templet_button) {
            if (listener != null)
                listener.clickCallBack(this, 1);
            else
                dismiss();

        }
    }
}
