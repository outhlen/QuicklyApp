package com.escort.carriage.android.ui.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.view.holder.HomeMainHolder;
import com.escort.carriage.android.utils.NotificationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class HomeOpenPushFragment extends BaseFragment implements View.OnClickListener {
    private HomeMainHolder homeMainHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_open_push, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.tvExitPush).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvExitPush:
                //调用接口获取数据
                setNotifi(1);

                break;
        }
    }

    public void setMainHolder(HomeMainHolder homeMainHolder) {
        this.homeMainHolder = homeMainHolder;
    }

    public void setNotifi(int isOpen){
        //调用接口获取数据
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, Object> data = new HashMap<>();
        data.put("isOpen", isOpen);
        data.put("isDeposit", homeMainHolder.cuitListEntity.isDeposit);
        data.put("cityDistribute", homeMainHolder.cuitListEntity.cityDistribute);
        requestEntity.setData(data);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.DRIVELINE_SETTING, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean entity) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (entity != null) {
                    if (entity.success) {
                        homeMainHolder.cuitListEntity.isOpen = isOpen;
                        CacheDBMolder.getInstance().setNotification(true);
                        if (homeMainHolder != null) {
                            homeMainHolder.openHomePushInfoFragment();
                        }
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
    public void onDestroy() {
        super.onDestroy();
        homeMainHolder = null;
    }
}
