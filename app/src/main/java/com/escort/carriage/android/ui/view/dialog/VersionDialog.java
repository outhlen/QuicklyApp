package com.escort.carriage.android.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.escort.carriage.android.R;

import androidx.annotation.NonNull;

/**
 * 版本更新
 */
public class VersionDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView tvNewVersion;
    private TextView tvUpdata;
    private TextView tvOldVersion;

    private int compulsory;

    public static VersionDialog getInstance(Context context, int compulsory) {
        return new VersionDialog(context, compulsory);
    }

    public VersionDialog(@NonNull Context context, int compulsory) {
        super(context);
        mContext = context;
        this.compulsory = compulsory;
        initView();
    }

//    public AuthSuccessDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.dialog_version, null);
        tvNewVersion = view.findViewById(R.id.tvNewVersion);
        tvUpdata = view.findViewById(R.id.tvUpdata);
        tvOldVersion = view.findViewById(R.id.tvOldVersion);
        tvUpdata.setOnClickListener(this);

        tvOldVersion.setText("当前版本：" + ApplicationContext.getInstance().versionName);
        if(compulsory == 1){
            setCancelable(false);
        }

        setContentView(view);

    }

    public VersionDialog setVersionName(String value) {
        tvNewVersion.setText(value);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUpdata:
                if (mClickKnowListener != null) {
                    mClickKnowListener.onClickKnow(this, compulsory);
                }
                break;
        }
    }

    private OnClickKnowListener mClickKnowListener;

    public VersionDialog setClickKnowListener(OnClickKnowListener clickKnowListener) {
        mClickKnowListener = clickKnowListener;
        return this;
    }

    public interface OnClickKnowListener {
        void onClickKnow(Dialog dialog, int compulsory);
    }
}
