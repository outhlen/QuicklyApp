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
 * 标准的提示模板   标题   内容    底部按钮
 *
 * 监听说明：
 *      点击按钮默认调用监听给打开的类，方便做后续操作  DialogCallbackListener
 *      不设置监听  点击按钮 都为关闭当前dialog
 *
 * 方法说明：
 *      获取控件的方法说明：
 *              getTitleView()   -- 获取顶部文字提示展示控件方法
 *              getContextView() -- 获取显示提示内容的控件方法
 *              getBottomBtn()     -- 获取底部按钮控件的方法
 *
 *      设置监听的方法：
 *              setListener() -- 设置监听的方法
 *
 *      设置控件内容的方法：
 *
 *              setDefaultPage()   -- 使用默认的样式 只设置提示内容   默认：标题-- 提示     按钮 -- 知道了
 *
 *              setTitleText()           --   设置顶部显示标题方法一  字符串
 *              setTitleTextRes()        --   设置顶部显示标题方法二  文字资源内容ID
 *
 *              setContext()    --  设置需要提示内容的方法一   字符串
 *              setContextRes() --  设置需要提示内容的方法二   文字资源内容ID
 *
 *              setBottomBtnText()     --  设置底部按钮显示内容的方法一    字符串
 *              setBottomBtnTextRes()  --  设置底部按钮显示内容的方法二    文字资源内容ID
 *
 *              setPageText()  -- 快速设置页面内容的方法  两种方式  一种是 字符串   一种是  文字资源内容ID
 */

public class HintOneBtnTempletDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;

    public HintOneBtnTempletDialog(Context context) {
        this(context, 0);
    }

    public HintOneBtnTempletDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_white_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hint_one_btn_templet);
        setDialogWidth();
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);
        findViewById(R.id.dialog_hint_one_btn_templet_button).setOnClickListener(this);
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
    public HintOneBtnTempletDialog setListener(DialogCallbackListener listener){
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
            return (TextView) findViewById(R.id.dialog_hint_one_btn_templet_title);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取提示文字展示控件
     *
     * @return  当前设置提示内容的 TextView
     */
    public TextView getContextView(){
        try {
            return (TextView) findViewById(R.id.dialog_hint_one_btn_templet_content);
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
            return (TextView) findViewById(R.id.dialog_hint_one_btn_templet_button);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置标题显示内容  方法一
     * @param str 需要显示的文字
     */
    public HintOneBtnTempletDialog setTitleText(String str){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置标题显示内容 方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public HintOneBtnTempletDialog setTitleTextRes(int textRes){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 设置需要提示内容  方法一
     * @param str  需要显示文字的字符串
     */
    public HintOneBtnTempletDialog setContext(String str){
        TextView contextView = this.getContextView();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置需要提示内容  方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public HintOneBtnTempletDialog setContextRes(int textRes){
        TextView contextView = this.getContextView();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 设置底部按钮显示内容   方法一
     * @param str  需要显示文字的字符串
     */
    public HintOneBtnTempletDialog setBottomBtnText(String str){
        TextView contextView = getBottomBtn();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置底部按钮显示内容   方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public HintOneBtnTempletDialog setBottomBtnTextRes(int textRes){
        TextView contextView = getBottomBtn();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 组合方法  设置页面控件的值
     * @param titleStr     标题内容  字符串
     * @param contextStr   提示内容  字符串
     * @param btnStr       底部按钮内容  字符串
     */
    public HintOneBtnTempletDialog setPageText(String titleStr, String contextStr, String btnStr){
        setTitleText(titleStr);
        setContext(contextStr);
        setBottomBtnText(btnStr);
        return this;
    }
    /**
     * 组合方法  设置页面控件的值
     * @param titleRes     标题内容  资源ID
     * @param contextRes   提示内容  资源ID
     * @param btnRes       底部按钮内容  资源ID
     */
    public HintOneBtnTempletDialog setPageText(int titleRes, int contextRes, int btnRes){
        setTitleTextRes(titleRes);
        setContextRes(contextRes);
        setBottomBtnTextRes(btnRes);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param str  显示字符串
     */
    public HintOneBtnTempletDialog setDefaultPage(String str){
        setTitleTextRes(R.string.prompt);
        setContext(str);
        setBottomBtnTextRes(R.string.got_it);
        return this;
    }

    /**
     * 使用默认的样式 只设置提示内容
     * @param textRes  显示字符串资源ID
     */
    public HintOneBtnTempletDialog setDefaultPage(int textRes){
        setTitleTextRes(R.string.prompt);
        setContextRes(textRes);
        setBottomBtnTextRes(R.string.got_it);
        return this;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_hint_one_btn_templet_button) {
            if (listener != null)
                listener.clickCallBack(this, 1);
            else
                dismiss();

        }
    }
}
