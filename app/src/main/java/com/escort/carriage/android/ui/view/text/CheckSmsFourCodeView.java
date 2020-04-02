package com.escort.carriage.android.ui.view.text;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.escort.carriage.android.R;

public class CheckSmsFourCodeView extends RelativeLayout {

    private Context mContext;
    private EditText etCode;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    public CheckSmsFourCodeView(Context context) {
        this(context, null);
    }

    public CheckSmsFourCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {

        View view = inflate(mContext, R.layout.view_check_sms_four_code, this);

        etCode = view.findViewById(R.id.etCode);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        tv4 = view.findViewById(R.id.tv4);

        etCode.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() == 0) {
                    tv1.setText("");
                    tv2.setText("");
                    tv3.setText("");
                    tv4.setText("");

                }else if (str.length() == 1){
                    tv1.setText(str);
                    tv2.setText("");
                    tv3.setText("");
                    tv4.setText("");

                }else if (str.length() == 2){
                    tv1.setText(str.substring(0,1));
                    tv2.setText(str.substring(1,2));
                    tv3.setText("");
                    tv4.setText("");

                }else if (str.length() == 3){
                    tv1.setText(str.substring(0,1));
                    tv2.setText(str.substring(1,2));
                    tv3.setText(str.substring(2,3));
                    tv4.setText("");

                } else if (str.length() == 4){
                    tv1.setText(str.substring(0,1));
                    tv2.setText(str.substring(1,2));
                    tv3.setText(str.substring(2,3));
                    tv4.setText(str.substring(3,str.length()));
                    if(mOnInputListener != null){
                        mOnInputListener.onInput(str);
                    }
                }

            }
        });
    }

    private OnInputListener mOnInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        mOnInputListener = onInputListener;
    }

    public interface OnInputListener{
        void onInput(String str);
    }
}
