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
 * 一个标准的dialog提示框   样式为  顶部一个图片   中间为黑色标黑字体     底部为两个按钮
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
 *              getLeftBtn()   -- 获取底部左边（取消）按钮控件的方法
 *              getRightBtn()   -- 获取底部右边（确定）按钮控件的方法
 *
 *      设置监听的方法：
 *              setListener() -- 设置监听的方法
 *
 *      设置控件内容的方法：
 *              setImageSrc()           --   设置顶部图片的方法一  根据类型显示预定的图片内容  1 -- 成功  2 -- 注意（异常）  3 -- 失败
 *              setImageSrcRes()        --   设置顶部图片的方法二  给控件设置指定的资源图片ID
 *              setImageSrcDrawable()   --   设置顶部图片的方法三  给控件设置指定drawable对象
 *
 *              setContext()    --  设置需要提示内容的方法一   字符串
 *              setContextRes() --  设置需要提示内容的方法二   文字资源内容ID
 *
 *              setLeftBtnText()     --  设置左边按钮显示内容的方法一    字符串
 *              setLeftBtnTextRes()  --  设置左边按钮显示内容的方法二    文字资源内容ID
 *
 *              setRightBtnText()     --  设置右边按钮显示内容的方法一    字符串
 *              setRightBtnTextRes()  --  设置右边按钮显示内容的方法二    文字资源内容ID
 *
 *              setBtnText()  -- 快速设置底部按钮提示内容的方法  两种方式  一种是 字符串   一种是  文字资源内容ID
 */

public class TopImageTwoBtnTempletDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;

    public TopImageTwoBtnTempletDialog(Context context) {
        this(context, 0);
    }

    public TopImageTwoBtnTempletDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_white_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_top_image_two_btn_templet);
        setDialogWidth();
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_top_image_two_btn_templet_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_top_image_two_btn_templet_confirm).setOnClickListener(this);
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

    public TopImageTwoBtnTempletDialog setListener(DialogCallbackListener listener){
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
            return (ImageView) findViewById(R.id.dialog_top_image_two_btn_templet_image);
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
            return (TextView) findViewById(R.id.dialog_top_image_two_btn_templet_context);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 左边 取消按钮的 对象
     *
     * @return 底部 左边按钮的对象
     */
    public TextView getLeftBtn() {
        try {
            return (TextView) findViewById(R.id.dialog_top_image_two_btn_templet_cancel);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 右边 确定按钮的 对象
     *
     * @return 底部 右边按钮的对象
     */
    public TextView getRightBtn() {
        try {
            return (TextView) findViewById(R.id.dialog_top_image_two_btn_templet_confirm);
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
    public TopImageTwoBtnTempletDialog setImageSrc(int type) {

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
    public TopImageTwoBtnTempletDialog setImageSrcRes(int res) {
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
    public TopImageTwoBtnTempletDialog setImageSrcDrawable(Drawable drawable) {
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
    public TopImageTwoBtnTempletDialog setContext(String str) {
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
    public TopImageTwoBtnTempletDialog setContextRes(int strRes) {
        TextView contextView = getContextView();
        if(contextView != null){
            contextView.setText(strRes);
        }
        return this;
    }

    /**
     * 左边button 设置显示内容 方法一
     *
     * @param str 需要设置显示文字
     */
    public TopImageTwoBtnTempletDialog setLeftBtnText(String str) {
        TextView leftBtn = getLeftBtn();
        if(leftBtn != null){
            leftBtn.setText(str);
        }
        return this;
    }

    /**
     * 左边button 设置显示内容 方法二
     *
     * @param strRes 需要设置显示文字的资源地址
     */
    public TopImageTwoBtnTempletDialog setLeftBtnTextRes(int strRes) {
        TextView leftBtn = getLeftBtn();
        if(leftBtn != null){
            leftBtn.setText(strRes);
        }
        return this;
    }

    /**
     * 右边button 设置显示内容 方法一
     *
     * @param str 需要设置显示文字
     */
    public TopImageTwoBtnTempletDialog setRightBtnText(String str) {
        TextView rightBtn = getRightBtn();
        if(rightBtn != null){
            rightBtn.setText(str);
        }
        return this;
    }

    /**
     * 右边button 设置显示内容 方法二
     *
     * @param strRes 需要设置显示文字的资源地址
     */
    public TopImageTwoBtnTempletDialog setRightBtnTextRes(int strRes) {
        TextView rightBtn = getRightBtn();
        if(rightBtn != null){
            rightBtn.setText(strRes);
        }
        return this;
    }

    /**
     *  设置底部按钮的方法
     * @param leftBtnText   左边按钮显示的文字
     * @param reghtBtnText  右边按钮显示的文字
     * @return
     */
    public TopImageTwoBtnTempletDialog setBtnText(String leftBtnText, String reghtBtnText) {
        setLeftBtnText(leftBtnText);
        setRightBtnText(reghtBtnText);
        return this;
    }

    /**
     * 设置底部按钮的方法
     *
     * @param leftBtnTextRes     左边按钮显示的文字资源地址
     * @param reghtBtnTextRes    右边按钮显示的文字资源地址
     * @return
     */
    public TopImageTwoBtnTempletDialog setBtnText(int leftBtnTextRes, int reghtBtnTextRes) {
        setLeftBtnTextRes(leftBtnTextRes);
        setRightBtnTextRes(reghtBtnTextRes);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_top_image_two_btn_templet_confirm) {
            if (listener != null)
                listener.clickCallBack(this, 1);
            else
                dismiss();

        } else if (i == R.id.dialog_top_image_two_btn_templet_cancel) {
            if (listener != null)
                listener.clickCallBack(this, 2);
            else
                dismiss();

        }
    }

    public void clear(){
        listener = null;
    }
}
