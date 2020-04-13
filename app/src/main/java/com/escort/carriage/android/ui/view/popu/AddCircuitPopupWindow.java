package com.escort.carriage.android.ui.view.popu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.CircuitListEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.request.home.AddCircuitEntity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.fragment.home.HomePushInfoFragment;
import com.escort.carriage.android.ui.view.dialog.SelectpRrovinceCityDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 新增线路
 */
public class AddCircuitPopupWindow extends PopupWindow {


    @BindView(R.id.itemTitle)
    TextView itemTitle;
    @BindView(R.id.tvStartSite)
    TextView tvStartSite;
    @BindView(R.id.tvAddBtn)
    TextView tvAddBtn;
    @BindView(R.id.tvEndSite)
    TextView tvEndSite;
    @BindView(R.id.ivPush)
    Switch ivPush;
    @BindView(R.id.cityGroup)
    LinearLayout cityGroup;

    private View mMenuView;
    private Context context;
    private Fragment fragment;
    private Unbinder bind;
    private int pageType = 0;//0:新加 1：修改

    private AddCircuitEntity addCircuitEntity;

    public AddCircuitPopupWindow(Activity context, Fragment fragment, int pageType) {
        super(context);
        this.fragment = fragment;
        this.context = context;
        this.pageType = pageType;
        addCircuitEntity = new AddCircuitEntity();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_add_circuit, null);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
//        CacheDataEntity cacheDataEntity = CacheDBMolder.getInstance().getCacheDataEntity(null);
//        int phoneHeight = cacheDataEntity.getPhoneHeight();
//        int statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
//        int height = phoneHeight + statusBarHeight;
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimRight);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000); //半透明
        ColorDrawable dw = new ColorDrawable(0x60000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
        setView();

    }

    private void setView() {
        bind = ButterKnife.bind(this, mMenuView);
        if(pageType == 0){
            setAddCircuitView();
        }


    }

    public void setUpdataCircuitView(CircuitListEntity.CircuitItemEntity entity) {

        setRequestData(entity);

        showPageViewData(addCircuitEntity, entity);

    }

    private void showPageViewData(AddCircuitEntity addCircuitEntity, CircuitListEntity.CircuitItemEntity entity) {
        itemTitle.setText("修改线路");
        if(addCircuitEntity.getStstus() == 0){
            ivPush.setChecked(false);
        } else {
            ivPush.setChecked(true);
        }
        tvStartSite.setText(entity.getStartProvinceName() + " " + entity.getStartCitName());
        tvEndSite.setText(entity.getEndProvinceName() + " " + entity.getEndCitName());

        ivPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ivPush.setChecked(isChecked);
                if(isChecked){
                    addCircuitEntity.setStstus(1);
                } else {
                    addCircuitEntity.setStstus(0);
                }
            }
        });
        cityGroup.removeAllViews();
        if(addCircuitEntity.getList() != null && addCircuitEntity.getList().size() > 0){
            for (int x = 0; x < addCircuitEntity.getList().size(); x++){
                addCircuitViewData(addCircuitEntity.getList().get(x));
            }
        }
        setCityGroupView();
    }



    private void setRequestData(CircuitListEntity.CircuitItemEntity entity) {
        addCircuitEntity.setStartCityCode(entity.getStartCityCode());
        addCircuitEntity.setStartProvinceCode(entity.getStartProvinceCode());
        addCircuitEntity.setEndProvinceCode(entity.getEndProvinceCode());
        addCircuitEntity.setEndCityCode(entity.getEndCityCode());
        addCircuitEntity.setStstus(entity.getStstus());
        addCircuitEntity.setId(entity.getId());

        if(entity.getList() != null && entity.getList().size() > 0){
            List<CircuitListEntity.CircuitItemEntity.ListBean> list = entity.getList();
            ArrayList<AddCircuitEntity.ListBean> arrayList = new ArrayList<>();
            for (int x = 0; x < list.size(); x++){
                AddCircuitEntity.ListBean listBean = new AddCircuitEntity.ListBean();
                CircuitListEntity.CircuitItemEntity.ListBean oldBean = list.get(x);
                listBean.setCityCode(oldBean.getCityCode());
                listBean.setCityName(oldBean.getCityName());
                arrayList.add(listBean);
            }
            addCircuitEntity.setList(arrayList);
        }

    }

    private void setAddCircuitView() {
        ivPush.setChecked(true);
        addCircuitEntity.setStstus(1);
        ivPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ivPush.setChecked(isChecked);
                if(isChecked){
                    addCircuitEntity.setStstus(1);
                } else {
                    addCircuitEntity.setStstus(0);
                }
            }
        });

        setCityGroupView();
    }

    private void setCityGroupView() {
        cityGroup.removeAllViews();

        cityGroupAddView();

    }

    private void removeView(View flag) {
        cityGroup.removeView(flag);
    }

    private void addCircuitViewData(AddCircuitEntity.ListBean cityBean) {
        View inflate = View.inflate(context, R.layout.item_add_circuit_city, null);
        TextView tvWayCity = inflate.findViewById(R.id.tvWayCity);
        ImageView imView = inflate.findViewById(R.id.itemImage);
        tvWayCity.setText(cityBean.getCityName());
        imView.setImageResource(R.drawable.item_red_jh);
        imView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(inflate);
            }
        });
        inflate.setTag(cityBean);
        cityGroup.addView(inflate);
    }

    private void cityGroupAddView() {
        View inflate = View.inflate(context, R.layout.item_add_circuit_city, null);
        TextView tvWayCity = inflate.findViewById(R.id.tvWayCity);
        tvWayCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectpRrovinceCityDialog.getInstance().setContext(context)
                        .setFlag(inflate)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
                                ImageView imView = flag.findViewById(R.id.itemImage);
                                TextView tv = inflate.findViewById(R.id.tvWayCity);
                                tv.setText(cityBean.getCity());
                                imView.setImageResource(R.drawable.item_red_jh);
                                imView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        removeView(flag);
                                    }
                                });
                                if(flag.getTag() == null){
                                    cityGroupAddView();
                                }
                                AddCircuitEntity.ListBean listBean = new AddCircuitEntity.ListBean();
                                listBean.setCityCode(cityBean.getCityCode());
                                listBean.setCityName(cityBean.getCity());
                                flag.setTag(listBean);
                            }
                        })
                        .setShowBottom(true)
                        .show(fragment.getChildFragmentManager());
            }
        });


        cityGroup.addView(inflate);
    }

    @OnClick({R.id.ivClose, R.id.tvStartSite, R.id.tvAddBtn, R.id.tvEndSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivClose:
                dismiss();
                break;
            case R.id.tvStartSite:
                SelectpRrovinceCityDialog.getInstance().setContext(context)
                        .setFlag(tvStartSite)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
                                tvStartSite.setText(provinceBean.getProvince() + " " + cityBean.getCity());
                                addCircuitEntity.setStartProvinceCode(provinceBean.getProvinceCode());
                                addCircuitEntity.setStartCityCode(cityBean.getCityCode());
//                                addCircuitEntity.setStartAreaCode(areaBean.getAreaCode());
                            }
                        })
                        .setShowBottom(true)
                    .show(fragment.getChildFragmentManager());
                break;
            case R.id.tvAddBtn:
                if(TextUtils.isEmpty(addCircuitEntity.getStartProvinceCode())){
                    ToastUtil.showToastString("请选择出发地");
                } else if(TextUtils.isEmpty(addCircuitEntity.getEndProvinceCode())){
                    ToastUtil.showToastString("请选择目的地");
                } else {
                    addCircuitEntity.setList(getItemListEntity());
                    requestEntity(addCircuitEntity);
                }

                break;
            case R.id.tvEndSite:
                SelectpRrovinceCityDialog.getInstance().setContext(context)
                        .setFlag(tvEndSite)
                        .setCallback(new SelectpRrovinceCityDialog.Callback() {
                            @Override
                            public void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean) {
                                tvEndSite.setText(provinceBean.getProvince() + " " + cityBean.getCity());
                                addCircuitEntity.setEndProvinceCode(provinceBean.getProvinceCode());
                                addCircuitEntity.setEndCityCode(cityBean.getCityCode());
//                                addCircuitEntity.setEndAreaCode(areaBean.getAreaCode());
                            }
                        })
                        .setShowBottom(true)
                        .show(fragment.getChildFragmentManager());
                break;
        }
    }

    private List<AddCircuitEntity.ListBean> getItemListEntity() {
        ArrayList<AddCircuitEntity.ListBean> arrayList = new ArrayList<>();
        int childCount = cityGroup.getChildCount();
        if(childCount > 1){
            for (int x = 0; x < childCount - 1; x++){
                View childAt = cityGroup.getChildAt(x);
                AddCircuitEntity.ListBean tag = (AddCircuitEntity.ListBean) childAt.getTag();
                arrayList.add(tag);
            }
        }
        return arrayList;
    }

    private void requestEntity(AddCircuitEntity addCircuitEntity) {
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(context, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(addCircuitEntity);
        String jsonString = JsonManager.createJsonString(requestEntity);
        String url = null;
        if(pageType == 0){
            url = ProjectUrl.DRIVELINE_ADD;
        } else {
            url = ProjectUrl.DRIVELINE_UPDATE;
        }
        OkgoUtils.post(url, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success) {
                        HomePushInfoFragment fragment = (HomePushInfoFragment) AddCircuitPopupWindow.this.fragment;
                        fragment.getPageData();
                        dismiss();
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

    @Override
    public void dismiss() {
        super.dismiss();
        if (bind != null) {
            bind.unbind();
            bind = null;
        }
    }

}