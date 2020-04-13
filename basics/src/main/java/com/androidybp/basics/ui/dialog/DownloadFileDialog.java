package com.androidybp.basics.ui.dialog;

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
import com.androidybp.basics.ui.view.loading.LoadingView;

public class DownloadFileDialog extends Dialog implements View.OnClickListener{

    private DialogCallbackListener listener;
    private LoadingView loading;

    public DownloadFileDialog(Context context) {
        this(context, 0);
    }

    public DownloadFileDialog(Context context, int themeResId) {
        super(context, themeResId);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_rectangle_b_fffcd03c_j_8);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_download_file);
        setDialogWidth();
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);
        loading = findViewById(R.id.dialog_download_file_loading);
    }

    public void setUpataType(int type){
        if(type == 0){
            findViewById(R.id.dialog_download_file_button).setVisibility(View.VISIBLE);
            findViewById(R.id.dialog_download_file_button).setOnClickListener(this);
        }
    }

    public void setProgress(float num){
        if(isShowing()){
            loading.setNumber(num);
        }
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
    public DownloadFileDialog setListener(DialogCallbackListener listener){
        this.listener = listener;
        return this;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_download_file_button) {
            if (listener != null)
                listener.clickCallBack(this, 1);
            else
                dismiss();

        }
    }
}