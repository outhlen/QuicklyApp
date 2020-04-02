package com.escort.carriage.android.ui.fragment.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.adapter.ArrayListAdapter;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.CircuitListEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ResponseCircuitListEntity;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.adapter.home.MyFlowAdapter;
import com.escort.carriage.android.ui.view.dialog.SelectAddressDialog;
import com.escort.carriage.android.ui.view.dialog.SelectpRrovinceCityDialog;
import com.escort.carriage.android.ui.view.flowlayout.FlowLayout;
import com.escort.carriage.android.ui.view.holder.HomeMainHolder;
import com.escort.carriage.android.ui.view.list.FillListView;
import com.escort.carriage.android.ui.view.popu.AddCircuitPopupWindow;
import com.escort.carriage.android.ui.view.text.DrawableTextView;
import com.escort.carriage.android.utils.NotificationUtils;

import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomePushInfoFragment extends BaseFragment {
    @BindView(R.id.tvExitPush)
    TextView tvExitPush;
    @BindView(R.id.tvSelectProvince)
    TextView tvSelectProvince;
    @BindView(R.id.tvSelectCity)
    TextView tvSelectCity;
    @BindView(R.id.ivCsps)
    Switch ivCsps;
    @BindView(R.id.ivYjInfo)
    Switch ivYjInfo;
    @BindView(R.id.list)
    FillListView filllist;
    @BindView(R.id.tvAddCircuit)
    TextView tvAddCircuit;
    private HomeMainHolder homeMainHolder;
    private Unbinder bind;
    HomePushInfoListAdapter homePushInfoListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_push_info, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);


    }

    @Override
    public void onResume() {
        super.onResume();
        getPageData();
    }

    public void getPageData() {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVELINE_GETLIST, jsonString).execute(new MyStringCallback<ResponseCircuitListEntity>() {
            @Override
            public void onResponse(ResponseCircuitListEntity entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success && entity.data != null) {
                        homeMainHolder.cuitListEntity = entity.data;
                        setNotificationView(entity.data.getCityDistribute(), entity.data.getIsDeposit());
                        setCity(entity.data.provinceName, entity.data.cityName);
                        setList(entity.data.getList());
                    } else {
                        ToastUtil.showToastString(entity.message);
                    }
                }
            }

            @Override
            public Class<ResponseCircuitListEntity> getClazz() {
                return ResponseCircuitListEntity.class;
            }
        });
    }

    /**
     * 设置 城市
     *
     * @param provinceName
     * @param cityName
     */
    private void setCity(String provinceName, String cityName) {
        if (TextUtils.isEmpty(provinceName)) {
            tvSelectProvince.setText("请选择配送省级地区");
        } else {
            tvSelectProvince.setText(provinceName);
        }

        if(TextUtils.isEmpty(cityName)){
            tvSelectCity.setText("请选择配送市级地区");
        } else {
            tvSelectCity.setText(cityName);
        }
    }

    private void setNotificationView(int cityDistribute, int isDeposit) {
        //设置时候先 将点击回调监听屏蔽掉 否则会重复调用接口
        ivCsps.setOnCheckedChangeListener(null);
        ivYjInfo.setOnCheckedChangeListener(null);

        if (cityDistribute == 0) {
            ivCsps.setChecked(false);
        } else {
            ivCsps.setChecked(true);
        }

        ivCsps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setNotifi(homeMainHolder.cuitListEntity.getIsOpen(), 1, homeMainHolder.cuitListEntity.getIsDeposit());
                } else {
                    setNotifi(homeMainHolder.cuitListEntity.getIsOpen(), 0, homeMainHolder.cuitListEntity.getIsDeposit());
                }
            }
        });

        if (isDeposit == 0) {
            ivYjInfo.setChecked(false);
        } else {
            ivYjInfo.setChecked(true);
        }
        ivYjInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setNotifi(homeMainHolder.cuitListEntity.getIsOpen(), homeMainHolder.cuitListEntity.getCityDistribute(), 1);
                } else {
                    setNotifi(homeMainHolder.cuitListEntity.getIsOpen(), homeMainHolder.cuitListEntity.getCityDistribute(), 0);
                }
            }
        });

    }


    public void setMainHolder(HomeMainHolder homeMainHolder) {
        this.homeMainHolder = homeMainHolder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeMainHolder = null;
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
    }

    @OnClick({R.id.tvExitPush, R.id.tvAddCircuit, R.id.tvSelectProvince, R.id.tvSelectCity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvExitPush:
                setNotifi(0, homeMainHolder.cuitListEntity.cityDistribute, homeMainHolder.cuitListEntity.isDeposit);
                break;
            case R.id.tvAddCircuit:
                AddCircuitPopupWindow addCircuitPopupWindow = new AddCircuitPopupWindow(getActivity(), this, 0);
                HomeActivity activity = (HomeActivity) getActivity();
                View view1 = activity.getHomeMainHoler().findView(R.id.statusBarView);
                addCircuitPopupWindow.showAsDropDown(view1);

                break;
            case R.id.tvSelectProvince: //选择省份
            case R.id.tvSelectCity:
                //选择城市
                //打开地区选择器
                SelectpRrovinceCityDialog.getInstance().setContext(getActivity())
                        .setFlag(tvExitPush)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
                                setProvinceCity(provinceBean.getProvinceCode(), provinceBean.getProvince(), cityBean.getCityCode(), cityBean.getCity());
                            }
                        })
                        .setShowBottom(true)
                        .show(getActivity().getSupportFragmentManager());
                break;

        }
    }

    public void setNotifi(int isOpen, int cityDistribute, int isDeposit) {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("isOpen", isOpen);
        data.put("cityDistribute", cityDistribute);
        data.put("isDeposit", isDeposit);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVELINE_SETTING, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success) {
                        homeMainHolder.cuitListEntity.isOpen = isOpen;
                        homeMainHolder.cuitListEntity.cityDistribute = cityDistribute;
                        homeMainHolder.cuitListEntity.isDeposit = isDeposit;
                        if (isOpen == 0) {
                            if (homeMainHolder != null) {
                                homeMainHolder.openHomeOpenPushFragment();
                            }
                            CacheDBMolder.getInstance().setNotification(false);
                        } else {
                            setNotificationView(cityDistribute, isDeposit);
                        }
                    } else {
                        setNotificationView(homeMainHolder.cuitListEntity.cityDistribute, homeMainHolder.cuitListEntity.isDeposit);
                        ToastUtil.showToastString(entity.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    private void setList(List<CircuitListEntity.CircuitItemEntity> list) {
        if (homePushInfoListAdapter == null) {
            homePushInfoListAdapter = new HomePushInfoListAdapter(this.getActivity());
        }
        homePushInfoListAdapter.setList(list);

        //默认设置微信支付
        filllist.setAdapter(homePushInfoListAdapter);
//        setListViewHeightBasedOnChildren(filllist);
    }

    /**
     * 解决ScrollView嵌套ListView只显示一条的问题
     *
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.invalidate();
    }


    public class HomePushInfoListAdapter extends ArrayListAdapter<CircuitListEntity.CircuitItemEntity> {

        public HomePushInfoListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HomePushInfoListAdapterHolder holder;
            //利用缓存
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_push_circuit_layout, null);
                holder = new HomePushInfoListAdapterHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (HomePushInfoListAdapterHolder) convertView.getTag();
            }
            CircuitListEntity.CircuitItemEntity circuitItemEntity = mList.get(position);

            holder.tvStartLocation.setText(circuitItemEntity.getStartProvinceName() + circuitItemEntity.getStartCitName());
            holder.tvEndtLocation.setText(circuitItemEntity.getEndProvinceName() + circuitItemEntity.getEndCitName());
            holder.ivPush.setOnCheckedChangeListener(null);
            if (circuitItemEntity.getStstus() == 0) {
                //关闭
                holder.ivPush.setChecked(false);
            } else {
                //开启
                holder.ivPush.setChecked(true);
            }
            setItemListView(holder.flowLayout, circuitItemEntity.getList());

            holder.ivPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        setListItemNotifi(circuitItemEntity.getId(), "1");
                    } else {
                        setListItemNotifi(circuitItemEntity.getId(), "0");
                    }
                }
            });

            holder.tvItemEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddCircuitPopupWindow addCircuitPopupWindow = new AddCircuitPopupWindow(getActivity(), HomePushInfoFragment.this, 1);
                    HomeActivity activity = (HomeActivity) getActivity();
                    View view1 = activity.getHomeMainHoler().findView(R.id.statusBarView);
                    addCircuitPopupWindow.setUpdataCircuitView(circuitItemEntity);
                    addCircuitPopupWindow.showAsDropDown(view1);
                }
            });

            return convertView;
        }

        private void setItemListView(FlowLayout flowLayout, List<CircuitListEntity.CircuitItemEntity.ListBean> list) {
            flowLayout.removeAllViews();
            MyFlowAdapter adapter = new MyFlowAdapter(getActivity(), list);
            flowLayout.setAdapter(adapter);
//            if(list != null && list.size() > 0){
//                for (CircuitListEntity.CircuitItemEntity.ListBean bean: list){
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    DrawableTextView inflate = (DrawableTextView) View.inflate(mContext, R.layout.item_drawable_text_view, null);
//                    inflate.setText(bean.getCityName());
//                    flowLayout.addView(inflate, layoutParams);
//                }
//
//            }
        }


        class HomePushInfoListAdapterHolder {
            TextView tvStartLocation;
            TextView tvEndtLocation;
            TextView tvItemEdit;
            FlowLayout flowLayout;
            Switch ivPush;

            public HomePushInfoListAdapterHolder(View view) {
                tvStartLocation = view.findViewById(R.id.tvStartLocation);
                tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
                tvItemEdit = view.findViewById(R.id.tvItemEdit);
                flowLayout = view.findViewById(R.id.flowLayout);
                ivPush = view.findViewById(R.id.ivPush);
            }
        }
    }

    public void setListItemNotifi(String id, String ststus) {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("ststus", ststus);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVELINE_UPDATESTATUS, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success) {
                        getPageData();
                    } else {
                        ToastUtil.showToastString(entity.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    public void setProvinceCity(String proCode, String proName, String cityCode, String cityName) {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("provinceCode", proCode);
        data.put("provinceName", proName);
        data.put("cityCode", cityCode);
        data.put("cityName", cityName);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.VEHICLE_INFO_SAVECITYDISTRIBUTE, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success) {
                        setCity(proName, cityName);
                    } else {
                        ToastUtil.showToastString(entity.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

}
