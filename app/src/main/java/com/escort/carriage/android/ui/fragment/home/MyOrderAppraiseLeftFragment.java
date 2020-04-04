package com.escort.carriage.android.ui.fragment.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.ui.mvc.fragment.BaseFragment;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.my.AppraiseInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.my.ResponseAppraiseInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyOrderAppraiseLeftFragment extends BaseFragment {
    @BindView(R.id.evaluation_ratingbar)
    RatingBar evaluationRatingbar;
    @BindView(R.id.tvPj)
    TextView tvPj;
    @BindView(R.id.et_evalute)
    EditText etEvalute;
    @BindView(R.id.tv_text_totalnum)
    TextView tvTextTotalnum;
    @BindView(R.id.btn_commit_evalute)
    Button btnCommitEvalute;
    private String orderNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            orderNumber = arguments.getString("orderNumber");
        }
        View inflate = inflater.inflate(R.layout.fragment_my_order_appraise_left, null);

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
        getInfo();
    }

    private void initView() {
        evaluationRatingbar.setIsIndicator(false);
        evaluationRatingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) { // 用户点击才会响应，否则会死循环
                    int r = (int) (rating + 0.5f); // 四舍五入
                    ratingBar.setRating(rating);

                    setPjText(rating);
                }
            }
        });


    }

    private void setPjText(float rating) {
        if (rating == 1) {
            tvPj.setText("非常差");
        } else if (rating == 2) {
            tvPj.setText("差");
        } else if (rating == 3) {
            tvPj.setText("一般");
        } else if (rating == 4) {
            tvPj.setText("满意");
        } else if (rating == 5) {
            tvPj.setText("非常满意");
        }
    }

    //1代表1个星 5 非常满意 4 满意 3 一般 2差 1非常差
    private void setPageInfo(AppraiseInfoEntity entity) {
        etEvalute.setText(entity.evaluationContent);
        etEvalute.setEnabled(false);

        evaluationRatingbar.setRating(entity.score);
        evaluationRatingbar.setOnRatingBarChangeListener(null);
        evaluationRatingbar.setIsIndicator(true);

        setPjText(entity.score);

        btnCommitEvalute.setVisibility(View.INVISIBLE);
    }


    @OnClick(R.id.btn_commit_evalute)
    public void onViewClicked() {
        int rating = (int) evaluationRatingbar.getRating();
        String trim = etEvalute.getText().toString().trim();
        if (rating == 0) {
            ToastUtil.showToastString("请选择评价星级！");
        } else if (TextUtils.isEmpty(trim)) {
            ToastUtil.showToastString("请输入评价内容");
        } else {
            toAppraiseInfo(rating, trim);
        }

    }

    private void getInfo() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("type", "0");
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.EVALUATION_GETINFO, jsonString).execute(new MyStringCallback<ResponseAppraiseInfoEntity>() {
            @Override
            public void onResponse(ResponseAppraiseInfoEntity s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        setPageInfo(s.data);
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponseAppraiseInfoEntity> getClazz() {
                return ResponseAppraiseInfoEntity.class;
            }
        });
    }

    /**
     * 请求数据
     * @param rating
     * @param trim
     */
    private void toAppraiseInfo(int rating, String trim) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(getActivity(), "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("type", "0");
        hashMap.put("score", String.valueOf(rating));
        hashMap.put("evaluationContent", trim);
        requestEntity.setData(hashMap);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(ProjectUrl.EVALUATION_ADD, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        finishPage();
                    } else {
                        ToastUtil.showToastString(s.message);
                    }
                }
            }

            @Override
            public Class<ResponceBean> getClazz() {
                return ResponceBean.class;
            }
        });
    }

    private void finishPage(){
        getActivity().setResult(456);
        getActivity().finish();
    }
}