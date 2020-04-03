package com.escort.carriage.android.ui.view.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.home.QuListBean;
import com.escort.carriage.android.entity.response.home.ShengListBean;
import com.escort.carriage.android.entity.response.home.ShiListBean;
import com.escort.carriage.android.http.MyStringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 级联选择地址
 */
public class SelectAddressDialog extends BaseDialogFragment {
    private Context mContext;
    private LinearLayout llBoxProvince;
    private LinearLayout llBoxCity;
    private LinearLayout llBoxArea;

    private List<ShengListBean.DataBean> mProvinceList = new ArrayList<>();
    private List<ShiListBean.DataBean> mCityList = new ArrayList<>();
    private List<QuListBean.DataBean> mAreaList = new ArrayList<>();

    private List<TextView> mProvinceTvList = new ArrayList<>();
    private List<TextView> mCityTvList = new ArrayList<>();
    private List<TextView> mAreaTvList = new ArrayList<>();
    private ShengListBean.DataBean mCurrentProvinceBean;
    private ShiListBean.DataBean mCurrentCityBean;
    private QuListBean.DataBean mCurrentAreaBean;
    private View mFlag;
    public static SelectAddressDialog getInstance() {
        return new SelectAddressDialog();
    }
    public SelectAddressDialog setContext(Context context){
        mContext = context;
        return this;
    }
    public SelectAddressDialog setFlag(View object){
        mFlag = object;
        return this;
    }


    @Override
    public int intLayoutId() {
        return R.layout.dialog_select_address;
    }
    @Override
    public void convertView(BaseDialogViewHolder holder, BaseDialogFragment dialog) {
        setHeight(300);
        llBoxProvince = holder.getView(R.id.llBoxProvince);
        llBoxCity = holder.getView(R.id.llBoxCity);
        llBoxArea = holder.getView(R.id.llBoxArea);
        holder.getView(R.id.tvClose).setOnClickListener((v)->dismiss());
        holder.getView(R.id.tvOk).setOnClickListener((v)->{
            if (mCallback != null && mCurrentCityBean != null && mCurrentAreaBean != null){
                if (mFlag == null){
                    mFlag = new View(getActivity());
                }
                mCallback.onCallback(mFlag, mCurrentProvinceBean, mCurrentCityBean,
                        mCurrentAreaBean);
                dismiss();
            }
        });

        getProvince();
    }



    //省
    private SelectAddressDialog setProvinceList(List<ShengListBean.DataBean> provinceList) {
        mProvinceList = provinceList;

        llBoxProvince.removeAllViews();
        for (int i = 0; i < mProvinceList.size(); i++) {
            View view = View.inflate(mContext, R.layout.item_select_address, null);
            TextView tv = view.findViewById(R.id.item_select_address_tv);
            tv.setText(mProvinceList.get(i).getProvince());
            mProvinceTvList.add(tv);
            int finalI = i;
            tv.setOnClickListener(v -> {
                for (int j = 0; j < mProvinceTvList.size(); j++) {
                    mProvinceTvList.get(j).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                mProvinceTvList.get(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                if (mProvinceCallback != null){
                    mProvinceCallback.onProvince(finalI);
                }
                mCurrentProvinceBean = mProvinceList.get(finalI);
                llBoxCity.removeAllViews();
                llBoxArea.removeAllViews();
                mCityList.clear();
                mAreaList.clear();
                mCityTvList.clear();
                mAreaTvList.clear();
            });
            llBoxProvince.addView(view);
        }
        return this;
    }

    //市
    private SelectAddressDialog setCityList(List<ShiListBean.DataBean> cityList) {
        mCityList = cityList;
//        if (mCityList == null){
//            ToastUtil.showShort("空的");
//        }
        llBoxCity.removeAllViews();
        for (int i = 0; i < mCityList.size(); i++) {
            View view = View.inflate(mContext, R.layout.item_select_address, null);
            TextView tv = view.findViewById(R.id.item_select_address_tv);
            tv.setText(mCityList.get(i).getCity());
            mCityTvList.add(tv);
            int finalI = i;
            tv.setOnClickListener(v -> {
                for (int j = 0; j < mCityTvList.size(); j++) {
                    mCityTvList.get(j).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                mCityTvList.get(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                if (mCityCallback != null){
                    mCityCallback.onProvince(finalI);
                }
                mCurrentCityBean = mCityList.get(finalI);

                mAreaList.clear();
                mAreaTvList.clear();
            });
            llBoxCity.addView(view);
        }
        return this;
    }

    //县
    private SelectAddressDialog setAreaList(List<QuListBean.DataBean> areaList) {
        mAreaList = areaList;

        llBoxArea.removeAllViews();
        for (int i = 0; i < mAreaList.size(); i++) {
            View view = View.inflate(mContext, R.layout.item_select_address, null);
            TextView tv = view.findViewById(R.id.item_select_address_tv);
            tv.setText(mAreaList.get(i).getArea());
            mAreaTvList.add(tv);
            int finalI = i;
            tv.setOnClickListener(v -> {
                for (int j = 0; j < mAreaTvList.size(); j++) {
                    mAreaTvList.get(j).setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                mAreaTvList.get(finalI).setBackgroundColor(Color.parseColor("#EEEEEE"));
                mCurrentAreaBean = mAreaList.get(finalI);
//                if (mAreaCallback != null){
//                    mCurrentAreaBean = mAreaList.get(finalI);
//                    mAreaCallback.onProvince(finalI);
//
//                }
                //TODO:
//                if (mCallback != null){
//                    mCallback.onCallback(mFlag, mCurrentCityBean, mAreaList.get(finalI));
//                }
            });
            llBoxArea.addView(view);
        }
        return this;
    }


    /**
     * 获取省
     */
    private void getProvince() {

        //调用接口获取数据
//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(new Object());
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.REGION_GETPROVINCE, jsonString).execute(new MyStringCallback<ShengListBean>() {
            @Override
            public void onResponse(ShengListBean resp) {
//                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (resp.isSuccess()) {
                    setProvinceList(resp.getData());
                    setProvinceCallback(position -> {
                        getCity(resp.getData().get(position).getProvinceCode());
                    });
                }
            }

            @Override
            public Class<ShengListBean> getClazz() {
                return ShengListBean.class;
            }
        });

    }

    /**
     * 获取市
     */
    private void getCity(String provinceCode) {


        //调用接口获取数据
//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("provinceCode", provinceCode);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.REGION_GETCITY, jsonString).execute(new MyStringCallback<ShiListBean>() {
            @Override
            public void onResponse(ShiListBean resp) {
//                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (resp.isSuccess()){
                    setCityList(resp.getData());
                    setCityCallback(position -> {
                        getArea(resp.getData().get(position).getCityCode());
                    });
                }
            }

            @Override
            public Class<ShiListBean> getClazz() {
                return ShiListBean.class;
            }
        });
    }

    /**
     * 获取区
     */
    private void getArea(String cityCode) {


        //调用接口获取数据
//        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> data = new HashMap<>();
        data.put("cityCode", cityCode);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.REGION_GETAREA, jsonString).execute(new MyStringCallback<QuListBean>() {
            @Override
            public void onResponse(QuListBean resp) {
//                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (resp.isSuccess()){
                    setAreaList(resp.getData());
                }
            }

            @Override
            public Class<QuListBean> getClazz() {
                return QuListBean.class;
            }
        });
    }

    ProvinceCallback  mProvinceCallback;
    public interface ProvinceCallback{
        void onProvince(int potion);
    }
    public SelectAddressDialog setProvinceCallback(ProvinceCallback callback){
        mProvinceCallback = callback;
        return this;
    }


    CityCallback  mCityCallback;
    public interface CityCallback{
        void onProvince(int position);
    }
    public SelectAddressDialog setCityCallback(CityCallback callback){
        mCityCallback = callback;
        return this;
    }


    AreaCallback  mAreaCallback;
    public interface AreaCallback{
        void onProvince(int position);
    }
    public void setAreaCallback(AreaCallback callback){
        mAreaCallback = callback;
    }


    Callback  mCallback;
    public interface Callback{
        void onCallback(View flag, ShengListBean.DataBean provinceBean, ShiListBean.DataBean cityBean, QuListBean.DataBean areaBean);
    }
    public SelectAddressDialog setCallback(Callback callback){
        mCallback = callback;
        return this;
    }

}
