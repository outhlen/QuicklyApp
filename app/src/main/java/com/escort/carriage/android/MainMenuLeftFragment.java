package com.escort.carriage.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.escort.carriage.android.configuration.VueUrl;
import com.escort.carriage.android.entity.bean.BannerBean;
import com.escort.carriage.android.ui.WebPageActivity;
import com.escort.carriage.android.ui.activity.login.ForgetPwdActivity;
import com.escort.carriage.android.ui.activity.login.LoginActivity;
import com.escort.carriage.android.ui.activity.login.RegisterPhoneActivity;
import com.escort.carriage.android.ui.activity.my.EnterpriseAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.PersonageAuthenticationActivity;
import com.escort.carriage.android.ui.activity.my.SettingActivity;
import com.escort.carriage.android.ui.activity.my.UserInfoActivity;
import com.escort.carriage.android.ui.activity.web.VueActivity;
import com.escort.carriage.android.ui.adapter.home.ImageAdapter;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by HaiyuKing
 * Used 首页左侧侧边栏碎片界面
 */

public class MainMenuLeftFragment extends Fragment {
	private static final String TAG = "MainMenuLeftFragment";

	@BindView(R.id.code_layout)
	LinearLayout llInfoGroup;
	@BindView(R.id.ll_login)
	LinearLayout ll_login;
	@BindView(R.id.ll_my_money)
	LinearLayout ll_my_money;
	@BindView(R.id.ll_dl_manager)
	LinearLayout llHistoryGroup;
	@BindView(R.id.ll_yq_hy)
	LinearLayout ll_yq_hy;
	@BindView(R.id.ll_fp_manager)
	LinearLayout ll_fp_manager;
	@BindView(R.id.ll_feedback)
	LinearLayout ll_feedback;
	@BindView(R.id.ll_kefu)
	LinearLayout ll_kefu;
	@BindView(R.id.llPersonageAuthentication)
	LinearLayout llPersonageAuthentication;
	@BindView(R.id.llEnterpriseAuthentication)
	LinearLayout llEnterpriseAuthentication;
	@BindView(R.id.banner)
	Banner banner;

	/**View实例*/
	private View myView;
	private TextView tv_show;
	Unbinder unbinder;

	//重写
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(TAG,"onCreateView");
		myView = inflater.inflate(R.layout.fragment_home_left_menu, container, false);
		unbinder = ButterKnife.bind(this, myView);
		return myView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG,"onActivityCreated");
		//初始化控件以及设置
		initView();
	}

	@OnClick({R.id.code_layout, R.id.ll_login, R.id.ll_my_money, R.id.ll_dl_manager,
			R.id.ll_yq_hy,R.id.ll_fp_manager,R.id.ll_feedback,R.id.ll_kefu,R.id.llPersonageAuthentication,R.id.llEnterpriseAuthentication})
	public void onViewClicked(View view) {
		switch (view.getId()) {
			case R.id.code_layout:
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				break;
			case R.id.ll_login:
				startActivity(new Intent(getActivity(), RegisterPhoneActivity.class));
				break;
			case R.id.ll_my_money:
				startActivity(new Intent(getActivity(), WebPageActivity.class));
				break;
			case R.id.ll_dl_manager:
				Intent intentInvate = new Intent(getActivity(), VueActivity.class);
				intentInvate.putExtra("url", VueUrl.inviteFriends);
				startActivity(intentInvate);
				break;
			case R.id.ll_yq_hy:
				Intent intent2 = new Intent(getActivity(), PersonageAuthenticationActivity.class);
				intent2.putExtra("status", 1);
				startActivity(intent2);
				break;
			case R.id.ll_fp_manager:
				Intent intent1 = new Intent(getActivity(), EnterpriseAuthenticationActivity.class);
				intent1.putExtra("status", 1);
				startActivity(intent1);
				break;
			case R.id.ll_feedback:
				//押镖认证
				startActivity(new Intent(getActivity(), UserInfoActivity.class));
				break;
			case R.id.ll_kefu:
				//获取认证状态
				startActivity(new Intent(getActivity(), SettingActivity.class)); //设置
				break;
			case R.id.llPersonageAuthentication://企业认证
				startActivity(new Intent(getActivity(), PersonBounsActvitiy.class));
				break;
			case R.id.llEnterpriseAuthentication:
				startActivity(new Intent(getActivity(), ForgetPwdActivity.class));
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		//初始化监听事件
		initEvent();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	/**初始化控件*/
	private void initView(){
		Log.d(TAG,"initView");
		List<BannerBean.Banner.ListBean> picArray = new ArrayList<>();
		BannerBean.Banner.ListBean listBean = new BannerBean.Banner.ListBean();
		listBean.setBannerUrl(R.mipmap.bg_home_left_header);
		BannerBean.Banner.ListBean listBean1=new BannerBean.Banner.ListBean();
		listBean1.setBannerUrl(R.mipmap.bg_home_main_header);
		picArray.add(listBean);
		picArray.add(listBean1);

		banner.setAdapter(new ImageAdapter(picArray))
				.setIndicator(new CircleIndicator(getContext()))
				.setIndicatorGravity(IndicatorConfig.Direction.CENTER)
				.setBannerRound(10)
				.setIndicatorSelectedColor(getResources().getColor(R.color.white))
				.start();
	}
	public void setDefaultDatas(){
	}

	/**初始化监听事件*/
	private void initEvent(){

	}
}
