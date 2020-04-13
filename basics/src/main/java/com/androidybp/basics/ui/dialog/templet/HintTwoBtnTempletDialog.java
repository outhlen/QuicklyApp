package com.androidybp.basics.ui.dialog.templet;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.androidybp.basics.R;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.ui.dialog.templet.listerner.DialogCallbackListener;


/**
 * 一个标准的dialog提示框   样式为  顶部文字标题   中间为提示文字     底部为两个按钮
 * 里面方法除特定的方法外  都支持链条式调用
 *
 * 监听说明：
 *      点击按钮默认调用监听给打开的类，方便做后续操作  DialogCallbackListener
 *      不设置监听  点击按钮 都为关闭当前dialog
 *
 * 方法说明：
 *      获取控件的方法说明：
 *              getTitleView()   -- 获取顶部文字提示展示控件方法
 *              getContextView() -- 获取显示提示内容的控件方法
 *              getLeftBtn()   -- 获取底部左边（取消）按钮控件的方法
 *              getRightBtn()   -- 获取底部右边（确定）按钮控件的方法
 *
 *      设置监听的方法：
 *              setListener() -- 设置监听的方法
 *
 *      设置控件内容的方法：
 *
 *              setDefaultPage()   -- 使用默认的样式 只设置提示内容   默认：标题-- 提示     左边按钮 -- 取消   右边按钮 -- 确定
 *
 *              setTitleText()           --   设置顶部显示标题方法一  字符串
 *              setTitleTextRes()        --   设置顶部显示标题方法二  文字资源内容ID
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

public class HintTwoBtnTempletDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;

    public HintTwoBtnTempletDialog(Context context) {
        this(context, 0);
    }

    public HintTwoBtnTempletDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_white_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hint_two_btn_templet);
        setDialogWidth();
        initView();
    }


    private void initView() {
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_hint_two_btn_templet_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_hint_two_btn_templet_confirm).setOnClickListener(this);
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

    /**
     * 设置 按钮点击回调监听
     * @param listener
     * @return
     */
    public HintTwoBtnTempletDialog setListener(DialogCallbackListener listener){
        this.listener = listener;
        return this;
    }

    /**
     * 获取标题对象
     *
     * @return  当前设置标题的 TextView
     */
    public TextView getTitleView(){
        try {
            return (TextView) findViewById(R.id.dialog_hint_two_btn_templet_title);
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
            return (TextView) findViewById(R.id.dialog_hint_two_btn_templet_content);
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
            return (TextView) findViewById(R.id.dialog_hint_two_btn_templet_cancel);
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
            return (TextView) findViewById(R.id.dialog_hint_two_btn_templet_confirm);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置标题显示内容  方法一
     * @param str 需要显示的文字
     */
    public HintTwoBtnTempletDialog setTitleText(String str){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置标题显示内容 方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public HintTwoBtnTempletDialog setTitleTextRes(int textRes){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 文字设置一
     *
     * @param str 需要设置提示文字
     */
    public HintTwoBtnTempletDialog setContext(String str) {
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
    public HintTwoBtnTempletDialog setContextRes(int strRes) {
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
    public HintTwoBtnTempletDialog setLeftBtnText(String str) {
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
    public HintTwoBtnTempletDialog setLeftBtnTextRes(int strRes) {
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
    public HintTwoBtnTempletDialog setRightBtnText(String str) {
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
    public HintTwoBtnTempletDialog setRightBtnTextRes(int strRes) {
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
    public HintTwoBtnTempletDialog setBtnText(String leftBtnText, String reghtBtnText) {
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
    public HintTwoBtnTempletDialog setBtnText(int leftBtnTextRes, int reghtBtnTextRes) {
        setLeftBtnTextRes(leftBtnTextRes);
        setRightBtnTextRes(reghtBtnTextRes);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param str  显示字符串
     */
    public HintTwoBtnTempletDialog setDefaultPage(String str){
        setTitleTextRes(R.string.prompt);
        setContext(str);
        setBtnText(R.string.cancel, R.string.confirm);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param textRes  显示字符串资源ID
     */
    public HintTwoBtnTempletDialog setDefaultPage(int textRes){
        setTitleTextRes(R.string.prompt);
        setContextRes(textRes);
        setBtnText(R.string.cancel, R.string.confirm);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_hint_two_btn_templet_confirm) {
            if (listener != null)
                listener.clickCallBack(this, 1);
            else
                dismiss();

        } else if (i == R.id.dialog_hint_two_btn_templet_cancel) {
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
