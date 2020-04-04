package com.escort.carriage.android.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.escort.carriage.android.R;

import androidx.annotation.NonNull;

/**
 *
 */
public class AuthSuccessDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView mTvCompanyName;
    private TextView mTvKnow;
    private ImageView mIvGeRen;
    private ImageView ivGeRenImage;
    private ImageView ivCompanyNameImage;
    private int type;//0公司,1个人

    public static AuthSuccessDialog getInstance(Context context, int type) {
        return new AuthSuccessDialog(context, type);
    }

    public AuthSuccessDialog(@NonNull Context context, int type) {
        super(context);
        this.type = type;
        mContext = context;
        initView();
    }

//    public AuthSuccessDialog(@NonNull Context context, int themeResId) {
//        super(context, themeResId);
//    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.dialog_auth_success, null);
        mTvCompanyName = view.findViewById(R.id.tvCompanyName);
        ivCompanyNameImage = view.findViewById(R.id.ivCompanyNameImage);
        mTvKnow = view.findViewById(R.id.tvKnow);
        mIvGeRen = view.findViewById(R.id.ivGeRen);
        ivGeRenImage = view.findViewById(R.id.ivGeRenImage);
        mTvKnow.setOnClickListener(this);
        if (type == 0){
            mTvCompanyName.setVisibility(View.VISIBLE);
            ivCompanyNameImage.setVisibility(View.VISIBLE);
            mIvGeRen.setVisibility(View.GONE);
            ivGeRenImage.setVisibility(View.GONE);
        }else {
            mTvCompanyName.setVisibility(View.GONE);
            ivCompanyNameImage.setVisibility(View.GONE);
            mIvGeRen.setVisibility(View.VISIBLE);
            ivGeRenImage.setVisibility(View.VISIBLE);
        }
        setCancelable(false);
        setContentView(view);

    }

    public AuthSuccessDialog setCompanyName(String value) {
        mTvCompanyName.setText(value);
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvKnow:
                dismiss();
                if (mClickKnowListener != null) {
                    mClickKnowListener.onClickKnow();
                }
                break;
        }
    }

    private OnClickKnowListener mClickKnowListener;

    public AuthSuccessDialog setClickKnowListener(OnClickKnowListener clickKnowListener) {
        mClickKnowListener = clickKnowListener;
        return this;
    }

    public interface OnClickKnowListener {
        void onClickKnow();
    }
}
