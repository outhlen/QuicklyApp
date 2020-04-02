package com.escort.carriage.android.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;

/**
 *
 */
public class EditInfoDialog extends Dialog {
    private TextView tvYes;
    private TextView tvNo;
    private TextView tvTitle;
    private TextView etContent;

    private String mTitle;


    public EditInfoDialog(Context context) {
        super(context, R.style.DialogTheme);
    }

    public EditInfoDialog(Context context, int style) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_info);

        tvYes = findViewById(R.id.tv_yes);
        tvNo = findViewById(R.id.tv_no);
        tvTitle = findViewById(R.id.tv_dialog_title);
        etContent = findViewById(R.id.et_dialog_message);

        tvTitle.setText(mTitle);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    String msg = etContent.getText().toString();
                    if (TextUtils.isEmpty(msg)){
                        ToastUtil.showToastString("不能为空");
                        return;
                    }
                    dismiss();
                    mCallback.onCallback(msg);
                }
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setTitle(String title){
        mTitle = (title);
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onCallback(String msg);
    }
}
