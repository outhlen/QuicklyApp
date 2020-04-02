package com.escort.carriage.android.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.ui.dialog.templet.listerner.DialogCallbackListener;
import com.escort.carriage.android.R;


/**
 * 初次打开APP 用户协议 显示弹框
 */

public class UserAgreementDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;

    public UserAgreementDialog(Context context) {
        this(context, 0);
    }

    public UserAgreementDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_white_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_user_agreement);
        setDialogWidth();
        initView();
        setContext();
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
    public UserAgreementDialog setListener(DialogCallbackListener listener){
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
    public UserAgreementDialog setTitleText(String str){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(str);
        return this;
    }

    /**
     * 设置标题显示内容 方法二
     * @param textRes  需要显示文字的资源地址（id）
     */
    public UserAgreementDialog setTitleTextRes(int textRes){
        TextView contextView = getTitleView();
        if(contextView != null)
            contextView.setText(textRes);
        return this;
    }

    /**
     * 文字设置一
     *
     */
    public UserAgreementDialog setContext() {
        TextView contextView = getContextView();
        if(contextView != null){
            String str1 = "\u2000\u2000\u2000\u2000为了向您提供车辆路线服务、语音、订单记录等服务，我们需要获取您的设备信息和操作记录等个人信息。请您仔细阅读并充分理解";
            String str2 = "《用户协议》";
            String str3 = "和";
            String str4 = "《隐私政策》";
            String str5 = "的各项条款。如您同，请点击同意开始接受我们的服务。";
            contextView.append(str1);
            SpannableString spannableString2 = new SpannableString(str2);
            MyClickableSpan clickableSpan = new MyClickableSpan();
            spannableString2.setSpan(clickableSpan, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            SpannableString spannableString4 = new SpannableString(str4);
            MyClickableSpan4 clickableSpan4 = new MyClickableSpan4();
            spannableString4.setSpan(clickableSpan4, 0, spannableString4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            contextView.setMovementMethod(LinkMovementMethod.getInstance()); //点击事件才能起效
            contextView.setHighlightColor(Color.parseColor("#ffffff"));  //点击背景色，默认淡蓝色
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#108de9"));
            spannableString2.setSpan(colorSpan, 0, spannableString2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan colorSpan4 = new ForegroundColorSpan(Color.parseColor("#108de9"));
            spannableString4.setSpan(colorSpan4, 0, spannableString4.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            contextView.append(spannableString2);
            contextView.append(str3);
            contextView.append(spannableString4);
            contextView.append(str5);
        }
        return this;
    }


    /**
     * 左边button 设置显示内容 方法一
     *
     * @param str 需要设置显示文字
     */
    public UserAgreementDialog setLeftBtnText(String str) {
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
    public UserAgreementDialog setLeftBtnTextRes(int strRes) {
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
    public UserAgreementDialog setRightBtnText(String str) {
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
    public UserAgreementDialog setRightBtnTextRes(int strRes) {
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
    public UserAgreementDialog setBtnText(String leftBtnText, String reghtBtnText) {
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
    public UserAgreementDialog setBtnText(int leftBtnTextRes, int reghtBtnTextRes) {
        setLeftBtnTextRes(leftBtnTextRes);
        setRightBtnTextRes(reghtBtnTextRes);
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

    class MyClickableSpan extends ClickableSpan {


        public MyClickableSpan() {
        }

        @Override
        public void updateDrawState(TextPaint ds) { //设置显示样式
            ds.setUnderlineText(false);  //不要默认下划线
        }

        @Override
        public void onClick(View widget) { //点击事件的响应方法
            if (listener != null)
                listener.clickCallBack(UserAgreementDialog.this, 3);
        }
    }

    class MyClickableSpan4 extends ClickableSpan {


        public MyClickableSpan4() {
        }

        @Override
        public void updateDrawState(TextPaint ds) { //设置显示样式
            ds.setUnderlineText(false);  //不要默认下划线
        }

        @Override
        public void onClick(View widget) { //点击事件的响应方法
            if (listener != null)
                listener.clickCallBack(UserAgreementDialog.this, 4);
        }
    }



    public void clear(){
        listener = null;
    }
}
