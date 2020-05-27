package com.escort.carriage.android.ui.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.ui.base.ProjectBaseActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.adapter.ArrayListAdapter;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.my.CarLicenseInfoEntity;
import com.escort.carriage.android.entity.bean.my.DriverInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.login.ResponseUserInfoEntity;
import com.escort.carriage.android.entity.response.my.ResponceCarLicenseInfoEntity;
import com.escort.carriage.android.entity.response.my.ResponseDriverInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.http.RequestEntityUtils;
import com.escort.carriage.android.ui.activity.HomeActivity;
import com.escort.carriage.android.ui.fragment.home.HomePushInfoFragment;
import com.escort.carriage.android.ui.view.imgview.RoundedImagView;
import com.escort.carriage.android.ui.view.list.FillListView;
import com.escort.carriage.android.ui.view.popu.AddCircuitPopupWindow;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 押镖认证
 */
public class EscortAuthenticationActivity extends ProjectBaseActivity {

    @BindView(R.id.ivDrivingImage)
    RoundedImagView ivDrivingImage;
    @BindView(R.id.tvDrivingAuthentication)
    TextView tvDrivingAuthentication;
    @BindView(R.id.list)
    FillListView filllist;
    @BindView(R.id.tvAddCar)
    TextView tvAddCar;
//    @BindView(R.id.zige_et)
//    EditText zgEdit;
//    @BindView(R.id.hege_et)
//    EditText hgEdit;

   private EscorAuthenticationListAdapter escorAuthenticationListAdapter;
    private DriverInfoEntity driverInfoEntity;//驾驶人信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escort_authentication);
        ButterKnife.bind(this);
        setPageActionBar();
        //获取车辆列表数据
        getListCar();
        //获取驾驶证信息
        getDrivingInfo();
    }

    /**
     * 获取驾驶证信息
     */
    private void getDrivingInfo() {
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.USER_USERAUTHDRIVER_GETDRIVERAUTHENTICATIONINFO, jsonString).execute(new MyStringCallback<ResponseDriverInfoEntity>() {
            @Override
            public void onResponse(ResponseDriverInfoEntity s) {
                if (s != null) {
                    if (s.success && s.data != null && !TextUtils.isEmpty(s.data.getDrivingLicencePicture())) {
                        driverInfoEntity = s.data;
                        //设置驾驶人信息
                        GlideManager.getGlideManager().loadImage(s.data.getDrivingLicencePicture(), ivDrivingImage);
                        tvDrivingAuthentication.setText("重新认证");
                    } else {
                        tvDrivingAuthentication.setText("驾驶证认证");
                    }
                }
            }

            @Override
            public Class<ResponseDriverInfoEntity> getClazz() {
                return ResponseDriverInfoEntity.class;
            }
        });
    }

    /**
     * 设置车辆状态
     * @param entity
     * @param driverType
     * @param switchView
     */
    private void setDriverType(CarLicenseInfoEntity.ListBean entity, int driverType, Switch switchView){
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "设置数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", entity.id);
        hashMap.put("vehicleUsage", driverType);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.VEHICLE_INFO_UPDATEUSAGE, jsonString).execute(new MyStringCallback<ResponseDriverInfoEntity>() {
            @Override
            public void onResponse(ResponseDriverInfoEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        entity.vehicleUsage = driverType;
                    } else {
                        switchView.setChecked(false);
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseDriverInfoEntity> getClazz() {
                return ResponseDriverInfoEntity.class;
            }
        });
    }

    private void setList(List<CarLicenseInfoEntity.ListBean> list) {
        if(escorAuthenticationListAdapter == null){
            escorAuthenticationListAdapter = new EscorAuthenticationListAdapter(this);
        }
        escorAuthenticationListAdapter.setList(list);

        //默认设置微信支付
        filllist.setAdapter(escorAuthenticationListAdapter);
    }

    /**
     * 获取车辆信息
     */
    private void getListCar() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = RequestEntityUtils.getRequestListEntity(1, 100);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.VEHICLE_INFO_GETLIST, jsonString).execute(new MyStringCallback<ResponceCarLicenseInfoEntity>() {
            @Override
            public void onResponse(ResponceCarLicenseInfoEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if(s.data != null && s.data.list != null && s.data.list.size() > 0){
                            //设置列表数据
                            setList(s.data.list);
                        }
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponceCarLicenseInfoEntity> getClazz() {
                return ResponceCarLicenseInfoEntity.class;
            }
        });
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("押镖认证");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.inflateMenu(R.menu.item_menu);
        Menu menu = toolbar.getMenu();
        MenuItem item = menu.findItem(R.id.item_menu_view);
        if (item != null) {
            item.setIcon(R.drawable.icon_ybrz_right);
        }

    }

    @OnClick({R.id.tvDrivingAuthentication, R.id.tvAddCar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvDrivingAuthentication:
                Intent intent = new Intent(this, DriverAuthentivationActivity.class);
                startActivityForResult(intent, 123);
                break;
            case R.id.tvAddCar:

                Intent intentCar = new Intent(this, CarLicenseAuthentivationActivity.class);
                startActivityForResult(intentCar, 123);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123){
            //获取车辆列表数据
            getListCar();
            //获取驾驶证信息
            getDrivingInfo();
        }
    }

    public class EscorAuthenticationListAdapter extends ArrayListAdapter<CarLicenseInfoEntity.ListBean> {

        public EscorAuthenticationListAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            //利用缓存
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_escort_authentication_cl_layout, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            CarLicenseInfoEntity.ListBean entity = mList.get(position);

            holder.defaultSwitch.setOnCheckedChangeListener(null);
            if(entity.vehicleUsage == 1){
                holder.defaultSwitch.setChecked(true);
            } else {
                holder.defaultSwitch.setChecked(false);
            }

            holder.defaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        setDriverType(entity, 1, holder.defaultSwitch);
                    } else {
                        setDriverType(entity, 0, holder.defaultSwitch);
                    }
                }
            });

            if(!TextUtils.isEmpty(entity.vehicleLicenseFront)){
                GlideManager.getGlideManager().loadImage(entity.vehicleLicenseFront, holder.itemImageView);
            }

            holder.tvCarId.setText("车牌号码：" + entity.licencePlate);
            holder.tvNature.setText("使用性质：" + entity.useNature);
            holder.tvCarType.setText("车辆类型：" + entity.vehicleType);
            holder.tvBrand.setText("品牌型号：" + entity.vehicleTypeInfo);


            holder.tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentCar = new Intent(EscortAuthenticationActivity.this, CarLicenseAuthentivationActivity.class);
                    intentCar.putExtra("json", JsonManager.createJsonString(entity));
                    startActivityForResult(intentCar, 123);
                }
            });

            return convertView;
        }


        class Holder{
            View topView;
            LinearLayout llDefaultViewGroup;
            TextView defaultText;
            Switch defaultSwitch;
            RoundedImagView itemImageView;
            TextView tvCarId;
            TextView tvNature;
            TextView tvCarType;
            TextView tvBrand;
            TextView tvNext;
            public Holder(View view){
                topView =  view.findViewById(R.id.topView);
                llDefaultViewGroup =  view.findViewById(R.id.llDefaultViewGroup);
                defaultText =  view.findViewById(R.id.defaultText);
                defaultSwitch =  view.findViewById(R.id.defaultSwitch);
                itemImageView =  view.findViewById(R.id.itemImageView);
                tvCarId =  view.findViewById(R.id.tvCarId);
                tvNature =  view.findViewById(R.id.tvNature);
                tvCarType =  view.findViewById(R.id.tvCarType);
                tvBrand =  view.findViewById(R.id.tvBrand);
                tvNext =  view.findViewById(R.id.tvNext);
            }
        }
    }


}
