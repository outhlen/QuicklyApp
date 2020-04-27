package com.escort.carriage.android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.escort.carriage.android.R;
import com.escort.carriage.android.utils.DisplayUtil;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

public class AutoBottomUIDialog extends Dialog {

    private Context mContext;
    private float height;

    public AutoBottomUIDialog(Context context){
        super(context);
        this.mContext  = context;
    }


    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setVisibility(View.VISIBLE);
    }

    public void show() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        if(height == 0f){
            getWindow().getAttributes().height = DisplayUtil.dip2px(mContext, 300f);
        }else {
            getWindow().getAttributes().height = DisplayUtil.dip2px(mContext, height/2);
        }
        getWindow().getAttributes().width = DisplayUtil.dip2px(mContext, 300f);
        getWindow().setBackgroundDrawable(mContext.getResources().getDrawable(R.color.transparent));
        getWindow().setGravity(Gravity.CENTER);
        super.show();
    }

    public void setDialogHeight(float height){
        this.height = height;
    }

}
