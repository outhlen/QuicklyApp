package com.escort.carriage.android.ui.adapter.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidybp.basics.fastjson.JsonManager;
import com.androidybp.basics.glide.GlideManager;
import com.androidybp.basics.okhttp3.OkgoUtils;
import com.androidybp.basics.okhttp3.entity.ResponceBean;
import com.androidybp.basics.ui.dialog.UploadAnimDialogUtils;
import com.androidybp.basics.utils.date.ProjectDateUtils;
import com.androidybp.basics.utils.hint.LogUtils;
import com.androidybp.basics.utils.hint.ToastUtil;
import com.androidybp.basics.utils.resources.ResourcesTransformUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.escort.carriage.android.R;
import com.escort.carriage.android.configuration.ProjectUrl;
import com.escort.carriage.android.entity.bean.home.AddrBean;
import com.escort.carriage.android.entity.bean.home.GoodsBean;
import com.escort.carriage.android.entity.bean.home.MyOrderListItemEntity;
import com.escort.carriage.android.entity.bean.home.OrderInfoEntity;
import com.escort.carriage.android.entity.request.RequestEntity;
import com.escort.carriage.android.entity.response.my.ResponseEnterpriseAuthenticationEntity;
import com.escort.carriage.android.http.MyStringCallback;
import com.escort.carriage.android.ui.activity.mes.OrderInfoActivity;
import com.escort.carriage.android.ui.activity.my.AppealActivity;
import com.escort.carriage.android.ui.activity.my.AppraiseActivity;
import com.escort.carriage.android.ui.activity.my.LoadingUnloadListActivity;
import com.escort.carriage.android.ui.activity.my.MyOrderAppraiseActivity;
import com.escort.carriage.android.ui.activity.my.RouteNavigationActivity;
import com.escort.carriage.android.ui.activity.my.TransfeOrderActivity;
import com.escort.carriage.android.ui.activity.play.PlayMesFeesActivity;
import com.escort.carriage.android.utils.AmapUtils;
import com.tripartitelib.android.amap.ProjectLocationEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * 我的订单中心
 */

public class MyOrderListAdapter extends BaseQuickAdapter<OrderInfoEntity, MyOrderListAdapter.OrderHolder> implements View.OnClickListener {

    private int pageType = 0;
    private Fragment fragment;
    private int repeal_order = 0;//撤单标识
    private int see_order_details = 1;//查看详情
    private int path_navigation = 2;//路线导航
    private int go_shipment = 3;//前往装货
    private int start_carriage = 4;//开始运输
    private int shipment_confirm = 5;//装货确认
    private int unload_accomplish = 6;//卸货完成
    private int unload_affirm = 7;//卸货确认
    private int transfer_of_order = 8;//转单
    private int reminder = 9;//我要催单
    private int reply_evaluate = 10;//回复评价
    private int appeal = 11;//申诉
    private int dispose_revoke = 12;//处理撤单
    private int dispose_payment = 13;//线下支付
    private int turn_car = 14;//转车
    private int comeback_car = 15;//回转

    private String text = "MyOrderListActivity_updata_positon = ";

    public MyOrderListAdapter(@Nullable List<OrderInfoEntity> data, Fragment fragment, int pageType) {
        super(R.layout.item_my_order_list_layout, data);
        this.pageType = pageType;
        this.fragment = fragment;
    }

    @Override
    protected void convert(OrderHolder helper, OrderInfoEntity item) {
        if (!TextUtils.isEmpty(item.imgUrl1)) {
            if (item.imgUrl1.contains(",")) {
                String[] arrays = item.imgUrl1.split(",");
                GlideManager.getGlideManager().loadImage(arrays[0], helper.ivImage, R.drawable.live_placeholder);
            } else {
                GlideManager.getGlideManager().loadImage(item.imgUrl1, helper.ivImage, R.drawable.live_placeholder);
            }
        }

        if (item.addr != null && item.addr.size() > 0) {
            AddrBean addrBean = item.addr.get(0);
            helper.tvStartLocation.setText(addrBean.startCityName);
            helper.tvEndtLocation.setText(addrBean.endCityName);
        }
        helper.tvOrderNum.setText("订单号：***" + item.orderNumber.substring(item.orderNumber.length() - 4, item.orderNumber.length()));
        helper.tvTime.setText("订单时间:" + ProjectDateUtils.getTimeDay("yyyy-MM-dd HH:mm", item.orderPlaceTime));
        helper.tvCargoName.setText(item.cargoName);
        helper.cargoCount.setText(item.cargoCount + "件");
        helper.tvCargoWeightVolume.setText(item.cargoWeight + "吨/" + item.cargoVolume + "方");
        helper.tvPackingManner.setText(item.packingManner);
        setBottomBtn(helper, item);
    }

    //订单状态(-1取消 0 待接单 1 已接单 2 前往载货地 3 抵达载货地 4 载货完成 5 出发目的地 6 抵达目的地 7 卸货完成 8 已结单)
    private void setBottomBtn(OrderHolder helper, OrderInfoEntity item) {
        if (pageType == 1) {
            //待取货
            setClaimGoods(helper, item);
        } else if (pageType == 2) {
            //装货中
            setUnderwayGoods(helper, item);
        } else if (pageType == 5) {
            //运输中
            setCarriageGoods(helper, item);
        } else if (pageType == 7) {
            //待结算
            setAwaitAccount(helper, item);
        } else if (pageType == 8) {
            //待评价
            setAwaitEvaluate(helper, item);
        } else if (pageType == -1) {
            //已撤单
            setCancleView(helper, item);
        }
    }

    private void setCancleView(OrderHolder helper, OrderInfoEntity item) {

        helper.btnThree.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnThree.setText("查看详情");
        helper.btnThree.setTag(see_order_details);
        helper.btnThree.setTag(R.id.tg_json, item);
        helper.btnThree.setOnClickListener(this);
        helper.btnTwo.setVisibility(View.INVISIBLE);
        helper.btnOne.setVisibility(View.INVISIBLE);
        helper.btnFour.setVisibility(View.GONE);
        helper.btnFive.setVisibility(View.GONE);

    }

    /**
     * 待取货 页面
     *
     * @param item
     */
    private void setClaimGoods(OrderHolder helper, OrderInfoEntity item) {

        //如果 已经点击过 撤单了 就把按钮之灰  不可点击   orderStatus == 1时才显示这个控件
        if (item.orderStatus == 1) {
            helper.btnFive.setTag(R.id.tg_json, item);
            if (item.repealOrderIsNotClick) {
                helper.btnFive.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
                helper.btnFour.setOnClickListener(null);
            } else {
                if (item.orderTab == 0) {
                    helper.btnFive.setText("撤单");
                    helper.btnFive.setTag(repeal_order);
                    helper.btnFive.setBackgroundResource(R.drawable.bg_b_f56c6c_bj_3dp);
                    helper.btnFive.setOnClickListener(this);
                } else if (item.orderTab == 1) {
                    helper.btnFive.setText("处理撤单");
                    helper.btnFive.setTag(dispose_revoke);
                    helper.btnFive.setBackgroundResource(R.drawable.bg_b_f56c6c_bj_3dp);
                    helper.btnFive.setOnClickListener(this);
                } else {
                    helper.btnFive.setVisibility(View.GONE);
                }

            }
        } else {
            helper.btnFive.setVisibility(View.GONE);
        }


        helper.btnFour.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnFour.setText("查看详情");
        helper.btnFour.setTag(see_order_details);
        helper.btnFour.setTag(R.id.tg_json, item);
        helper.btnFour.setOnClickListener(this);

        helper.btnThree.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
        helper.btnThree.setText("路线导航");
        helper.btnThree.setTag(path_navigation);
        helper.btnThree.setTag(R.id.tg_json, item);
        helper.btnThree.setOnClickListener(this);

        helper.btnTwo.setBackgroundResource(R.drawable.bg_b_3e9fff_bj_3dp);
        helper.btnTwo.setText("前往装货");
        helper.btnTwo.setTag(go_shipment);
        helper.btnTwo.setTag(R.id.tg_json, item);
        helper.btnTwo.setOnClickListener(this);

        helper.btnOne.setVisibility(View.INVISIBLE);

    }

    /**
     * 装货中
     *
     * @param helper
     * @param item
     */
    private void setUnderwayGoods(OrderHolder helper, OrderInfoEntity item) {

        helper.btnFive.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnFive.setText("查看详情");
        helper.btnFive.setTag(see_order_details);
        helper.btnFive.setTag(R.id.tg_json, item);
        helper.btnFive.setOnClickListener(this);
        helper.btnFour.setText("开始运输");
        if (item.orderStatus == 4) {
            helper.btnFour.setBackgroundResource(R.drawable.bg_b_3e9fff_bj_3dp);
            helper.btnFour.setTag(start_carriage);
            helper.btnFour.setTag(R.id.tg_json, item);
            helper.btnFour.setOnClickListener(this);
        } else {
            helper.btnFour.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
            helper.btnFour.setTag(start_carriage);
            helper.btnFour.setTag(R.id.tg_json, item);
            helper.btnFour.setOnClickListener(null);
        }

        helper.btnThree.setText("装货确认");
        if (item.orderStatus == 4) {
            helper.btnThree.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
            helper.btnThree.setTag(shipment_confirm);
            helper.btnThree.setTag(R.id.tg_json, item);
            helper.btnThree.setOnClickListener(null);
        } else {
            helper.btnThree.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
            helper.btnThree.setTag(shipment_confirm);
            helper.btnThree.setTag(R.id.tg_json, item);
            helper.btnThree.setOnClickListener(this);
        }

        helper.btnTwo.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
        helper.btnTwo.setText("路线导航");
        helper.btnTwo.setTag(path_navigation);
        helper.btnTwo.setTag(R.id.tg_json, item);
        helper.btnTwo.setOnClickListener(this);

        helper.btnOne.setVisibility(View.VISIBLE);
        if (item.installTurn == 0) {
            helper.btnOne.setBackgroundResource(R.drawable.bg_b_3699ff_bj_3dp);
            helper.btnOne.setText("我要转车");
            helper.btnOne.setTag(turn_car);
            helper.btnOne.setTag(R.id.tg_json, item);
            helper.btnOne.setOnClickListener(this);
        } else if (item.installTurn == 2) {
            helper.btnOne.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
            helper.btnOne.setText("我要回转");
            helper.btnOne.setTag(comeback_car);
            helper.btnOne.setTag(R.id.tg_json, item);
            helper.btnOne.setOnClickListener(this);
        } else {
            helper.btnOne.setOnClickListener(null);
            helper.btnOne.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 运输中
     *
     * @param helper
     * @param item
     */
    private void setCarriageGoods(OrderHolder helper, OrderInfoEntity item) {

        helper.btnFive.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnFive.setText("查看详情");
        helper.btnFive.setTag(see_order_details);
        helper.btnFive.setTag(R.id.tg_json, item);
        helper.btnFive.setOnClickListener(this);
        helper.btnFour.setBackgroundResource(R.drawable.bg_b_3e9fff_bj_3dp);
        helper.btnFour.setText("卸货完成");
        helper.btnFour.setTag(unload_accomplish);
        helper.btnFour.setTag(R.id.tg_json, item);
        helper.btnFour.setOnClickListener(this);
        helper.btnThree.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
        helper.btnThree.setText("卸货确认");
        helper.btnThree.setTag(unload_affirm);
        helper.btnThree.setTag(R.id.tg_json, item);
        helper.btnThree.setOnClickListener(this);


        if (item.isAllowTurn == 1) {
            LogUtils.showE("订单类型：", item.orderType);
            if (item.orderType.equals("1")) {
                helper.btnTwo.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
                helper.btnTwo.setText("路线导航");
                helper.btnTwo.setTag(path_navigation);
                helper.btnTwo.setTag(R.id.tg_json, item);
                helper.btnTwo.setOnClickListener(this);
                helper.btnOne.setVisibility(View.INVISIBLE);
            } else {
                helper.btnTwo.setText("分拨中转");
                helper.btnTwo.setBackgroundResource(R.drawable.bg_b_f56c6c_bj_3dp);
                helper.btnTwo.setTag(transfer_of_order);
                helper.btnTwo.setTag(R.id.tg_json, item);
                helper.btnTwo.setOnClickListener(this);
                helper.btnOne.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
                helper.btnOne.setText("路线导航");
                helper.btnOne.setTag(path_navigation);
                helper.btnOne.setTag(R.id.tg_json, item);
                helper.btnOne.setOnClickListener(this);
            }

        } else {
            helper.btnTwo.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
            helper.btnTwo.setText("路线导航");
            helper.btnTwo.setTag(path_navigation);
            helper.btnTwo.setTag(R.id.tg_json, item);
            helper.btnTwo.setOnClickListener(this);
            helper.btnOne.setVisibility(View.INVISIBLE);
        }


    }


    /**
     * 待结算
     *
     * @param helper
     * @param item
     */
    private void setAwaitAccount(OrderHolder helper, OrderInfoEntity item) {
        helper.btnFive.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnFive.setText("查看详情");
        helper.btnFive.setTag(see_order_details);
        helper.btnFive.setTag(R.id.tg_json, item);
        helper.btnFive.setOnClickListener(this);

        helper.btnFour.setText("我要催款");
        if (item.isUrge == 0) {
            helper.btnFour.setBackgroundResource(R.drawable.bg_b_f56c6c_bj_3dp);
            helper.btnFour.setTag(reminder);
            helper.btnFour.setTag(R.id.tg_json, item);
            helper.btnFour.setOnClickListener(this);
        } else {
            helper.btnFour.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
            helper.btnFour.setOnClickListener(null);
        }

        if ("1".equals(item.paymentMethod)) {
            helper.btnThree.setBackgroundResource(R.drawable.bg_b_3e9fff_bj_3dp);
            helper.btnThree.setText("线下付款");
            helper.btnThree.setTag(dispose_payment);
            helper.btnThree.setTag(R.id.tg_json, item);
            helper.btnThree.setOnClickListener(this);
        } else {
            helper.btnThree.setVisibility(View.GONE);
        }

        helper.btnTwo.setVisibility(View.INVISIBLE);
        helper.btnOne.setVisibility(View.INVISIBLE);
    }

    /**
     * 待评价
     *
     * @param helper
     * @param item
     */
    private void setAwaitEvaluate(OrderHolder helper, OrderInfoEntity item) {
        helper.btnFive.setBackgroundResource(R.drawable.bg_b_e7a339_bj_3dp);
        helper.btnFive.setText("查看详情");
        helper.btnFive.setTag(see_order_details);
        helper.btnFive.setTag(R.id.tg_json, item);
        helper.btnFive.setOnClickListener(this);
        String pingjia = item.getDriverEvaluate();
        helper.btnFour.setBackgroundResource(R.drawable.bg_b_67c337_bj_3dp);
        if (!TextUtils.isEmpty(pingjia)) {
            if (pingjia.equals("0")) {
                helper.btnFour.setText("给予评价");
            } else {
                helper.btnFour.setText("评价信息");
            }
        } else {
            helper.btnFour.setText("给予评价");
        }
        helper.btnFour.setTag(reply_evaluate);
        helper.btnFour.setTag(R.id.tg_json, item);
        helper.btnFour.setOnClickListener(this);
        //申诉  isComplaint  = 1  申诉按钮显示  = 2时候 不可点击 显示为已申诉   =0不显示按钮
        if (item.isComplaint == 1) {
            helper.btnThree.setVisibility(View.VISIBLE);
            helper.btnThree.setBackgroundResource(R.drawable.bg_b_f56c6c_bj_3dp);
            helper.btnThree.setText("申诉");
            helper.btnThree.setTag(appeal);
            helper.btnThree.setTag(R.id.tg_json, item);
            helper.btnThree.setOnClickListener(this);
        } else if (item.isComplaint == 2) {
            helper.btnThree.setVisibility(View.VISIBLE);
            helper.btnThree.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
            helper.btnThree.setText("已申诉");
            helper.btnThree.setOnClickListener(null);
        } else {
            helper.btnThree.setVisibility(View.INVISIBLE);
        }


        helper.btnTwo.setVisibility(View.INVISIBLE);
        helper.btnOne.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        Object entity = v.getTag(R.id.tg_json);
        OrderInfoEntity item = (OrderInfoEntity) entity;
        if (tag != null && entity != null) {
            int tag1 = (int) tag;
            if (tag1 == repeal_order) {
                //撤单
                new AlertDialog.Builder(fragment.getContext()).setMessage("您是否要进行撤单操作？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //开始运输
                                setRepealOrderMethod(tag1, item, v);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            } else if (tag1 == dispose_revoke) {
                //处理撤单
                //撤单
                new AlertDialog.Builder(fragment.getContext()).setMessage("您是否要进行处理撤单？")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //同意
                                setRepealOrderMethod(tag1, item, "1", v);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不同意
                        setRepealOrderMethod(tag1, item, "0", v);
                        dialog.dismiss();
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }).show();
            } else if (tag1 == see_order_details) {
                //查看详情
                Intent intent = new Intent(fragment.getActivity(), OrderInfoActivity.class);
                intent.putExtra("id", item.orderNumber);
                intent.putExtra("openType", 2);
                fragment.startActivityForResult(intent, 333);
            } else if (tag1 == path_navigation) {
                //路线导航
                String jsonString = JsonManager.createJsonString(item);
                Intent intent = new Intent(fragment.getActivity(), RouteNavigationActivity.class);
                intent.putExtra("json", jsonString);
                fragment.startActivity(intent);
            } else if (tag1 == go_shipment) {

                //前往装货
                goShipmentMethod(tag1, item);

            } else if (tag1 == start_carriage) {
                new AlertDialog.Builder(fragment.getContext()).setMessage("您确定要开始运输吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //开始运输
                                orderUpdataOrderDetailStatus(tag1, item, 2);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            } else if (tag1 == shipment_confirm) {
                //装货确认
                Intent intentOne = new Intent(fragment.getActivity(), LoadingUnloadListActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                intentOne.putExtra("orderStatus", item.orderStatus);
                intentOne.putExtra("pageType", 0);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == unload_accomplish) {
                new AlertDialog.Builder(fragment.getContext()).setMessage("您是否要确定卸货完成？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //卸货完成
                                orderUpdataOrderDetailStatus(tag1, item, 3);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            } else if (tag1 == unload_affirm) {
                //卸货确认
                Intent intentOne = new Intent(fragment.getActivity(), LoadingUnloadListActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                intentOne.putExtra("orderStatus", item.orderStatus);
                intentOne.putExtra("pageType", 1);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == transfer_of_order) {
                //转单
                Intent intentOne = new Intent(fragment.getActivity(), TransfeOrderActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                intentOne.putExtra("license", item.travelNumberPlate);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == turn_car) { //装货转单
                //转单
                Intent intentOne = new Intent(fragment.getActivity(), TransfeOrderActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                intentOne.putExtra("license", item.travelNumberPlate);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == comeback_car) { //回转
                ProjectLocationEntity projectLocationEntity = AmapUtils.getAmapUtils().getLocation();
                returnBackOrder(item.orderNumber, projectLocationEntity.latitude, projectLocationEntity.longitude);
            } else if (tag1 == reminder) {
                //我要催单
                reminderToService(tag1, item);
            } else if (tag1 == reply_evaluate) {
                //回复评价
                Intent intentOne = new Intent(fragment.getActivity(), MyOrderAppraiseActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == appeal) {
                //申诉
                Intent intentOne = new Intent(fragment.getActivity(), AppealActivity.class);
                intentOne.putExtra("orderNumber", item.orderNumber);
                fragment.startActivityForResult(intentOne, 123);
            } else if (tag1 == dispose_payment) {
                //线下付款
                new AlertDialog.Builder(fragment.getContext()).setMessage("您是否同意货主线下付款？")
                        .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //同意
                                setRepealPaymentMethod(tag1, item, "1", v, 4);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //不同意
                        setRepealPaymentMethod(tag1, item, "0", v, -1);
                        dialog.dismiss();
                    }
                }).show();
            }
        }
    }

    /*
     * 回转订单
     */
    private void returnBackOrder(String orderNumber, Double latitude, Double longitude) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", orderNumber);
        hashMap.put("latitude", String.valueOf(latitude));
        hashMap.put("longitude", String.valueOf(longitude));
        hashMap.put("requsetType", "1");
        toService(ProjectUrl.ORDER_RETURNBACK, 0, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                ToastUtil.showToastString("操作成功");
                fragment.onActivityResult(123, 456, null);
            }
        });
    }

    /**
     * 催单
     */
    private void reminderToService(int type, OrderInfoEntity item) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", item.orderNumber);
        toService(ProjectUrl.ORDERVEHICLE_URGE_PAYMENT, type, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                //让列表刷新数据
                fragment.onActivityResult(123, 456, null);
            }
        });
    }

    /**
     * 点击 撤单 申请
     *
     * @param item "requsetType":0//用户类型(0货主1车主)
     */
    private void setRepealOrderMethod(int type, OrderInfoEntity item, View view) {
        //已经交付
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", item.orderNumber);
        hashMap.put("requsetType", "1");
        toService(ProjectUrl.ORDER_GETORDERDELETE, type, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                //让控件不可点击
                item.repealOrderIsNotClick = true;
                view.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
                view.setOnClickListener(null);
            }
        });
    }

    /**
     * 点击 线下付款 处理
     *
     * @param item "requsetType":0//用户类型(0货主1车主)
     */
    private void setRepealPaymentMethod(int type, OrderInfoEntity item, String repealType, View view, int toPage) {
        //已经交付
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", item.orderNumber);
        hashMap.put("requsetType", repealType);
        toService(ProjectUrl.ORDER_UPDATEOFFPAY, type, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                if (toPage != -1) {
                    //跳转至 装货中
                    EventBus.getDefault().post(text + toPage);
                } else {
                    //让列表刷新数据
                    fragment.onActivityResult(123, 456, null);
                }
            }
        });
    }

    /**
     * 点击 撤单 申请
     *
     * @param orderTabType 是否同意撤单0不同意1同意
     */
    private void setRepealOrderMethod(int type, OrderInfoEntity item, String orderTabType, View view) {
        //已经交付
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", item.orderNumber);
        hashMap.put("orderTabType", orderTabType);
        toService(ProjectUrl.ORDER_UPDATEORDERDELETE, type, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                //让控件不可点击
                item.repealOrderIsNotClick = true;
                view.setBackgroundResource(R.drawable.bg_b_999999_bj_3dp);
                view.setOnClickListener(null);
                fragment.onActivityResult(123, 456, null);
            }
        });
    }

    /**
     * 点击 前往装车 按钮
     *
     * @param item
     */
    private void goShipmentMethod(int type, OrderInfoEntity item) {
        if (item.isServiceChange == 0) {
            //未交付
            Intent intent = new Intent(fragment.getActivity(), PlayMesFeesActivity.class);
            intent.putExtra("money", item.paramValue);
            intent.putExtra("orderNumber", item.orderNumber);
            intent.putExtra("deposit", item.deposit);
            intent.putExtra("innsuranceFee", item.insurancePay);
            intent.putExtra("invoiceFee", item.taxPay);
            fragment.startActivityForResult(intent, 123);
        } else if (item.isServiceChange == 1) {
            //已经交付
            orderUpdataOrderDetailStatus(type, item, 1);
        }
    }

    /**
     * 修改状态的万能接口
     *
     * @param type
     * @param item
     */
    private void orderUpdataOrderDetailStatus(int type, OrderInfoEntity item, int toPage) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderNumber", item.orderNumber);
        toService(ProjectUrl.ORDER_UPDATAORDERDETAILSTATUS, type, hashMap, new ServiceCallback() {
            @Override
            public void callback(int type) {
                if (toPage != -1) {
                    //跳转至 装货中
                    EventBus.getDefault().post(text + toPage);
                } else {
                    //让列表刷新数据
                    fragment.onActivityResult(123, 456, null);
                }

            }
        });
    }


    private void toService(String url, int type, Object object, ServiceCallback callback) {
        UploadAnimDialogUtils.singletonDialogUtils().showCustomProgressDialog(fragment.getContext(), "获取数据");
        RequestEntity requestEntity = new RequestEntity(0);
        requestEntity.setData(object);
        String jsonString = JsonManager.createJsonString(requestEntity);
        OkgoUtils.post(url, jsonString).execute(new MyStringCallback<ResponceBean>() {
            @Override
            public void onResponse(ResponceBean s) {
                UploadAnimDialogUtils.singletonDialogUtils().deleteCustomProgressDialog();
                if (s != null) {
                    if (s.success) {
                        if (callback != null) {
                            callback.callback(type);
                        }
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

    interface ServiceCallback {
        void callback(int type);
    }

    public class OrderHolder extends BaseViewHolder {
        ImageView ivImage;
        TextView tvStartLocation;
        TextView tvEndtLocation;
        TextView tvTime;
        TextView tvOrderNum;
        TextView tvCargoName;
        TextView cargoCount;
        TextView tvCargoWeightVolume;
        TextView tvPackingManner;
        TextView btnOne;
        TextView btnTwo;
        TextView btnThree;
        TextView btnFour;
        TextView btnFive;

        public OrderHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            tvStartLocation = view.findViewById(R.id.tvStartLocation);
            tvEndtLocation = view.findViewById(R.id.tvEndtLocation);
            tvTime = view.findViewById(R.id.tvTime);
            tvOrderNum = view.findViewById(R.id.order_num_tv);
            tvCargoName = view.findViewById(R.id.tvCargoName);
            cargoCount = view.findViewById(R.id.cargoCount);
            tvCargoWeightVolume = view.findViewById(R.id.tvCargoWeightVolume);
            tvPackingManner = view.findViewById(R.id.tvPackingManner);
            btnOne = view.findViewById(R.id.btnOne);
            btnTwo = view.findViewById(R.id.btnTwo);
            btnThree = view.findViewById(R.id.btnThree);
            btnFour = view.findViewById(R.id.btnFour);
            btnFive = view.findViewById(R.id.btnFive);
        }

        /**
         * Will set the text of a TextView.
         *
         * @param viewId The view id.
         * @param value  The text to put in the text view.
         * @return The BaseViewHolder for chaining.
         */
        @Override
        public BaseViewHolder setText(int viewId, CharSequence value) {
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            return super.setText(viewId, value);
        }
    }


}
