package com.escort.carriage.android.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.base.ProjectBaseEditActivity;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.action_bar.StatusBarCompatManager;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.my.AppraiseInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.my.ResponseAppraiseInfoEntity;
import com.escort.carriage.android.http.MyStringCallback;

import java.util.HashMap;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评价 Activity
 */
public class AppraiseActivity extends ProjectBaseEditActivity {

    @BindView(R.id.evaluation_ratingbar)
    RatingBar evaluationRatingbar;
    @BindView(R.id.leftImage)
    ImageView leftImage;
    @BindView(R.id.et_evalute)
    EditText etEvalute;
    @BindView(R.id.tv_text_totalnum)
    TextView tvTextTotalnum;
    @BindView(R.id.btn_commit_evalute)
    Button btnCommitEvalute;
    @BindView(R.id.tvPj)
    TextView tvPj;

    private String orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order_appraise_left);
        orderNumber = getIntent().getStringExtra("orderNumber");
        ButterKnife.bind(this);
        setPageActionBar();
        initView();
        getInfo();
    }

    private void setPageActionBar() {
        StatusBarCompatManager.setStatusBar(Color.parseColor("#FFFFFF"), this);
        //获取顶部状态栏的高度 给对应View设置高度
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setBackgroundResource(android.R.color.white);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        TextView tvTitle = findViewById(R.id.toolbar_centre_title_right_button_title);
        tvTitle.setTextColor(ResourcesTransformUtil.getColor(R.color.color_000000));
        tvTitle.setText("评价");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        } else if(rating == 2){
            tvPj.setText("差");
        } else if(rating == 3){
            tvPj.setText("一般");
        } else if(rating == 4){
            tvPj.setText("满意");
        } else if(rating == 5){
            tvPj.setText("非常满意");
        }
    }

//1代表1个星 5 非常满意 4 满意 3 一般 2差 1非常差
    private void setPageInfo(AppraiseInfoEntity entity) {
        etEvalute.setText(entity.evaluationContent);
        etEvalute.setClickable(false);

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
        }else if(TextUtils.isEmpty(trim)){
            ToastUtil.showToastString("请输入评价类型");
        } else {
            toAppraiseInfo(rating, trim);
        }

    }



    private void finishPage(){
        setResult(456);
        finish();
    }

    private void getInfo() {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("type", "1");
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
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(this, "提交数据");
        RequestEntity requestEntity = new RequestEntity(0);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("type", "1");
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

}
