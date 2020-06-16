package com.escort.carriage.android;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.ListView;

import androidx.drawerlayout.widget.DrawerLayout;

import com.androidybp.basics.ui.base.ProjectBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  抽屉
 */
public class ShowMenuActivity extends ProjectBaseActivity {

    private ListView list_left_drawer;


    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }
}
