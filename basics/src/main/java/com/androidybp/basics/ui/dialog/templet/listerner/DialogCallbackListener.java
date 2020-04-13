package com.androidybp.basics.ui.dialog.templet.listerner;

import android.app.Dialog;

public interface DialogCallbackListener {
    /**

     * @param dialog  当前Dialog对象
     * *
     * @param type    点击触发的类型
     */
    void clickCallBack(Dialog dialog, int type);
}
