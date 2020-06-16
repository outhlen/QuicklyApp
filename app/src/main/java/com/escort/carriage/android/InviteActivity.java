package com.escort.carriage.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.utils.hint.LogUtils;
import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteActivity extends ProjectBaseActivity {

    @BindView(R.id.wx_tui)
    LinearLayout tuilayout;
    @BindView(R.id.wx_value)
    LinearLayout wxShare;
    @BindView(R.id.wx_history)
    LinearLayout wxhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_invete);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.wx_tui, R.id.wx_value, R.id.wx_wechar,R.id.wx_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_tui:
                Intent intentInvate = new Intent(this, VueActivity.class);
                intentInvate.putExtra("url", VueUrl.inviteFriends);
                startActivity(intentInvate);
                break;
            case R.id.wx_value://分享
                Intent intentBonus = new Intent(this, VueActivity.class);
                intentBonus.putExtra("url", VueUrl.userRewardRecord);
                startActivity(intentBonus);
                break;
            case R.id.wx_history://朋友圈
                Intent intentPerformance = new Intent(this, VueActivity.class);
                intentPerformance.putExtra("url", VueUrl.userAchivementRecord);
                startActivity(intentPerformance);
                break;

        }
    }
}
