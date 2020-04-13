package com.escort.carriage.android;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.androidybp.basics.glide.RoundFitCropTransform;
import com.escort.carriage.android.ui.view.BottomView;
import com.escort.carriage.android.ui.view.ScrollLayout;
import com.escort.carriage.android.ui.view.TextSwitchView;
import com.tripartitelib.android.amap.AmapUtils;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.scroll_layout)
    ScrollLayout mScrollLayout;
    @BindView(R.id.bottom_view)
    BottomView mBottomView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_head_img1)
    ImageView mIvImg;
    @BindView(R.id.tsv)
    TextSwitchView mTsv;
    @BindView(R.id.ll_news)
    LinearLayout mLlNews;

//    private MainPresenter mPresenter = new MainPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData(savedInstanceState);
        AmapUtils.getAmapUtils().initTrace(this);
    }

    String TAG = getClass().getSimpleName();

    public void initData(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mScrollLayout.setOnScrollCallback(new ScrollLayout.OnScrollCallback() {
            @Override
            public void callback(boolean isOpen) {
                if (isOpen){
//                    mLlNews.setVisibility(View.VISIBLE);
//                    findViewById(R.id.ll_head).setVisibility(View.VISIBLE);
                }else {
//                    mLlNews.setVisibility(View.GONE);
//                    findViewById(R.id.ll_head).setVisibility(View.GONE);
                }
            }

            @Override
            public void callback(int state) {
                int i = state / 1000;

                mLlNews.setAlpha(i);
                findViewById(R.id.ll_head).setAlpha(i);
            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.3){
                    findViewById(R.id.iv_head_img1).setVisibility(View.GONE);
                    findViewById(R.id.ll_news).setVisibility(View.GONE);
                }else if (slideOffset < 0.7){
                    findViewById(R.id.iv_head_img1).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_news).setVisibility(View.VISIBLE);
                }
            }

        });
        mBottomView.setScrollLayout(mScrollLayout);
    }


    @Override
    protected void onStart() {
        super.onStart();
        UserInfoEntity userInfoBean = CacheDBMolder.getInstance().getUserInfoEntity(null);
        if (userInfoBean != null){
            RequestOptions options = new RequestOptions()
                    .transform(new RoundFitCropTransform(76));
            Glide.with(this).load(userInfoBean.getHeadPictureUrl()).apply(options)
                    .into(mIvImg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AmapUtils.getAmapUtils().startTrack(createNotification(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 两次返回退出
     */
    private long mTimes;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击返回键触发
            long times = System.currentTimeMillis();
            if (times - mTimes > 2000) {
                ToastUtil.showToastString("再按一次退出程序");
                mTimes = System.currentTimeMillis();
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.ivSetting, R.id.iv_head_img1,R.id.ll_news})
    public void onViewClicked(View view){
//        if (!UserUtils.isLogin()) {
//            startActivity(new Intent(this, LoginActivity.class));
//            return;
//        }
//        switch (view.getId()){
//            case R.id.ivSetting://设置
//                startActivity(new Intent(this, SettingActivity.class));
//                break;
//            case R.id.ll_news://头条
//                startActivity(new Intent(this, NewsListActivity.class));
//                break;
//            case R.id.iv_head_img1:
////                startActivity(new Intent(getThisContext(), SearchMapActivity.class));
////                SlideMenuDialog.getInstance().setShowBottom(true).show(getSupportFragmentManager());
//                drawerLayout.openDrawer(GravityCompat.START);
//                break;
//        }
    }

//    public void getTitleListResp(NewsTitleListBean resp){
//        if (resp.isSuccess()){
//            String[] arr = new String[resp.getData().size()];
//            for (int i = 0; i < arr.length; i++) {
//                arr[i] = resp.getData().get(i).getTitle();
//            }
//            mTsv.setResources(arr);
//            mTsv.setTextStillTime(3000);
//            mTsv.setChilClickListener(new TextSwitchView.ChilClickListener() {
//                @Override
//                public void onClick(int index) {
//                    Intent intent = new Intent(getThisContext(), NewsListActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }
//    }


        /**
     * 在8.0以上手机，如果app切到后台，系统会限制定位相关接口调用频率
     * 可以在启动轨迹上报服务时提供一个通知，这样Service启动时会使用该通知成为前台Service，可以避免此限制
     */
        private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification createNotification(Context context) {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_SERVICE_RUNNING, "app service", NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(channel);
            builder = new Notification.Builder(context.getApplicationContext(), CHANNEL_ID_SERVICE_RUNNING);
        } else {
            builder = new Notification.Builder(context.getApplicationContext());
        }
        Intent nfIntent = new Intent(context, MainActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, nfIntent, 0))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("猎鹰sdk运行中")
                .setContentText("猎鹰sdk运行中");
        Notification notification = builder.build();
        return notification;
    }
}
