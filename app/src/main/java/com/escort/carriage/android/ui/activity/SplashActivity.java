package com.escort.carriage.android.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.ImageView;

import com.androidybp.basics.ApplicationContext;
import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserEntity;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.ui.mvp.view.BaseMvpActivity;
import com.androidybp.basics.ui.mvp.view.MvpViewInterface;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.file.SharedPrefUtil;
import com.escort.carriage.android.MainActivity;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ResponseInterceptorInterfaceIm;
import com.escort.carriage.android.entity.bean.SplashImvEntity;
import com.escort.carriage.android.mvp.persenter.SplashActPersenter;
import com.escort.carriage.android.ui.activity.login.LoginActivity;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

public class SplashActivity extends BaseMvpActivity<SplashActPersenter, SplashImvEntity> implements View.OnClickListener {


    private ImageView ivBg;
    private Button btnTime;

    private SplashImvEntity splashImvEntity;
    private SplashCountDownTimer splashCountDownTimer;
    //是否需要打开登陆页面 false 打开登陆页面  true 直接进入业务流程业务
    private Boolean isLogin;

    @Override
    public SplashActPersenter createPresenter() {
        return new SplashActPersenter();
    }

    /**
     * 初始化View布局
     */
    @Override
    public int getContentView() {
//        setActionBar();
        StatusBarCompatManager.fullScreen(this);
        return R.layout.activity_splash;
    }

    //适配全屏 状态栏
    private void setActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decorView = window.getDecorView();
            decorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {

                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    WindowInsets windowInsets = v.onApplyWindowInsets(insets);
                    windowInsets.replaceSystemWindowInsets(
                            windowInsets.getSystemWindowInsetLeft(),
                            0,
                            windowInsets.getStableInsetRight(),
                            windowInsets.getStableInsetBottom());
                    return windowInsets;
                }
            });
            ViewCompat.requestApplyInsets(decorView);
            //将状态栏设成透明，如不想透明可设置其他颜色
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    /**
     * 初始化页面数据
     */
    @Override
    public void initUI(Bundle savedInstanceState) {
        //初始化Toast组件
        ApplicationContext.getInstance().examineNotifications(this);

        ivBg = findViewById(R.id.sp_bg);
        btnTime = findViewById(R.id.sp_jump_btn);
        ivBg.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        ApplicationContext.getInstance().setResponseInterceptorInterface(new ResponseInterceptorInterfaceIm());
//        GlideManager.getGlideManager().loadActImageIsNoCache(this,
//            "https://gss0.bdstatic.com/7LsWdDW5_xN3otebn9fN2DJv/doc/pic/item/9f510fb30f2442a729ff2492dd43ad4bd1130210.jpg",
//            ivBg, R.drawable.bg_splash)
    }

    /**
     * 开启倒计时
     */
    private void openTimer() {
        btnTime.setVisibility(View.VISIBLE);
        splashCountDownTimer = new SplashCountDownTimer(3400, 1000);
        splashCountDownTimer.start();
    }

    private void inspectUserData() {
        //判断是否需要自动登陆进去  1、先判断token  2、在判断用户数据
        String userToken = CacheDBMolder.getInstance().getUserToken();
        if (userToken != null && !TextUtils.isEmpty(userToken)) {
            //在判断userInfo是否为空
            UserEntity userInfo = CacheDBMolder.getInstance().getUserInfo(null);
            isLogin = userInfo != null;
        } else {
            isLogin = false;
        }
    }

    /**
     * 这个方法是 persenter 回调使用
     */
    @Override
    public void setPageView(SplashImvEntity model, int type) {
        switch (type) {
            case 3:
                //开启倒计时
                openTimer();
                //检索是否需要打开登陆页面
                inspectUserData();
                break;
            default:
                if (ivBg != null) {
                    splashImvEntity = model;
                    if (model != null) {
                        //更新图片
                        GlideManager.getGlideManager().loadActImageIsNoCache(this, model.ivUrl, ivBg, R.drawable.bg_splash);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v != null && !isFrequentlyClick()) {
            switch (v.getId()) {
                case R.id.sp_bg:
                    if (splashImvEntity != null) {
                        if (!TextUtils.isEmpty(splashImvEntity.webUrl)) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.setData(Uri.parse(splashImvEntity.webUrl));
                            startActivity(intent);
                        }
                    }

                    break;

                case R.id.sp_jump_btn:
                    //取消倒计时计数器
                    cancelTimer();
                    //打开下一个页面
                    openNextAct();
                    break;
            }
        }

    }


    private void openNextAct() {
//        isLogin = true
        if (isLogin == true) {
            openMainAct();
//            val intent1 = Intent(this, ArrayFragmentActivity::class.java)
//            val bundle = Bundle()
//            bundle.putInt("type", 1)
//            intent1.putExtra("bundle", bundle)
//            intent1.putExtra("fragment", "GuessingMyLaunchListFragment")
//            startActivity(intent1)
        } else {
            openLoginAct();
        }
    }

    private void openMainAct() {
//        startActivity(new Intent(this, MainActivity.class));
        HomeActivity.startHomeActivity(this);
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        finish();
    }

    private void openLoginAct() {
        if (isOneOpenApp()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private boolean isOneOpenApp() {
        return SharedPrefUtil.getSharedPrefUtil().getBoolean("isOpen", true);
//        return true
    }

    /**
     * 用于清空数据使用
     */
    @Override
    public void clear() {
        cancelTimer();
        splashImvEntity = null;
    }

    /**
     * 取消倒计时
     */
    private void cancelTimer() {
        if(splashCountDownTimer != null){
            splashCountDownTimer.cancel();
            splashCountDownTimer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        pagePresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class SplashCountDownTimer extends CountDownTimer {

        public SplashCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long l = (millisUntilFinished / 1000) + 1;
            btnTime.setText("跳过(" + l + "s)");
        }

        @Override
        public void onFinish() {
            btnTime.setText("跳过(" + 1 + "s)");
            openNextAct();
        }

    }

}
