package com.escort.carriage.android;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserInfoEntity;
import com.androidybp.basics.glide.RoundFitCropTransform;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.escort.carriage.android.ui.view.BottomView;
import com.escort.carriage.android.ui.view.ScrollLayout;
import com.escort.carriage.android.ui.view.TextSwitchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    ScaleAnimation scaleAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData(savedInstanceState);
    }

    public void initData(Bundle savedInstanceState) {
        ButterKnife.bind(this);
         scaleAnim = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF,
                0.9f);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.setDuration(1200);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setFillAfter(true);
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
            mIvImg.startAnimation(scaleAnim);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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


}
