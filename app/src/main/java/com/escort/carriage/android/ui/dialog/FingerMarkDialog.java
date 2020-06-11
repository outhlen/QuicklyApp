package com.escort.carriage.android.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.escort.carriage.android.R;

import javax.crypto.Cipher;


public class FingerMarkDialog extends Dialog {
    Context mContext;
    private onPassListen onPassListener;
    private boolean isPass;
    ImageView ivZhiwen;
    View line;
    TextView tvContent;
    TextView tvCancle;

    public FingerMarkDialog(@NonNull Context context) {
        super(context, R.style.frameworkLoadingDialogStyle);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_mark_dialog);
        fingerprintManager = getContext().getSystemService(FingerprintManager.class);

        ivZhiwen = findViewById(R.id.iv_zhiwen);

        line = findViewById(R.id.line);
        tvContent = findViewById(R.id.tv_hint);
        tvCancle = findViewById(R.id.tv_cancel);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    private void setStateGone() {
        line.setVisibility(View.GONE);
        tvCancle.setVisibility(View.GONE);
        tvContent.setText("验证成功");
        tvContent.setTextColor(Color.GREEN);
        ivZhiwen.setBackgroundResource(R.drawable.zhiwen_success);
    }

    private void setStateFail() {
        tvContent.setText("验证失败请重新验证");
        tvContent.setTextColor(Color.RED);
    }

    private FingerprintManager fingerprintManager;
    private CancellationSignal mCancellationSignal;
    private Cipher mCipher;
    /**
     * 标识是否是用户主动取消的认证。
     */
    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    public interface onPassListen {
        void pass(boolean isPass);
    }

    public void setPassListen(onPassListen onPassListener) {
        this.onPassListener = onPassListener;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void start() {
        startListening(mCipher);

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher), mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                if (!isSelfCancelled) {
                    if (errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                        Toast.makeText(mContext, errString, Toast.LENGTH_SHORT).show();
                        dismiss();
                        isPass = false;
                        onPassListener.pass(isPass);
                        setStateFail();
                    }
                }
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                Toast.makeText(mContext, helpString, Toast.LENGTH_SHORT).show();
                isPass = false;
                onPassListener.pass(isPass);
                setStateFail();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                Toast.makeText(mContext, "指纹认证成功", Toast.LENGTH_SHORT).show();
                isPass = true;
                onPassListener.pass(isPass);
                setStateGone();
                dismiss();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(mContext, "指纹认证失败，请再试一次", Toast.LENGTH_SHORT).show();
                setStateFail();
                isPass = false;
                onPassListener.pass(isPass);
            }
        }, null);
    }

    private void stopListening() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopListening();
    }
}
