package com.escort.carriage.android.utils;

import android.content.Context;
import android.text.TextUtils;

import com.androidybp.basics.cache.CacheDBMolder;
import com.androidybp.basics.entity.UserInfoEntity;
import com.escort.carriage.android.R;
import com.hyphenate.helpdesk.model.AgentIdentityInfo;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.OrderInfo;
import com.hyphenate.helpdesk.model.QueueIdentityInfo;
import com.hyphenate.helpdesk.model.VisitorInfo;
import com.hyphenate.helpdesk.model.VisitorTrack;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

/**
 * 对轨迹跟踪的消息操作 此类不是必须，只是为了演示和初始化一些数据
 */
public class DemoMessageHelper {

	public static final String IMAGE_URL_1 = "http://o8ugkv090.bkt.clouddn.com/hd_one.png";
	public static final String IMAGE_URL_2 = "http://o8ugkv090.bkt.clouddn.com/hd_two.png";
	public static final String IMAGE_URL_3 = "http://o8ugkv090.bkt.clouddn.com/hd_three.png";
	public static final String IMAGE_URL_4 = "http://o8ugkv090.bkt.clouddn.com/hd_four.png";


	public static VisitorInfo createVisitorInfo() {
		VisitorInfo info = ContentFactory.createVisitorInfo(null);
		UserInfoEntity userInfoEntity = CacheDBMolder.getInstance().getUserInfoEntity(null);
		info.nickName(userInfoEntity.getNickName())
				.name(userInfoEntity.getUserName())
				.qq("10000")
				.phone(userInfoEntity.getPhoneNumber())
				.companyName(userInfoEntity.getCompanyName())
				.description("")
				.email("abc@123.com");
		return info;
	}




	public static VisitorTrack createVisitorTrack(Context context, int index) {
		VisitorTrack track = ContentFactory.createVisitorTrack(null);
		switch(index) {
		case 3:
			track.title("会话历史")
                 .price("￥5400")
                 .desc("测试")
                 .imageUrl(IMAGE_URL_3)
                 .itemUrl("http://www.baidu.com");
			break;
		case 4:
			track.title("会话测试1")
					.price("￥3915000")
					.desc("测试1")
            .     imageUrl(IMAGE_URL_4)
                 .itemUrl("http://www.baidu.com");
			break;
			default:
				break;
		}
		return track;
	}
	
	public static OrderInfo createOrderInfo(Context context, int index) {
		OrderInfo info = ContentFactory.createOrderInfo(null);
		switch(index) {
		case 1:
			info.title("订单1")
			    .orderTitle(String.format("%s：7890",context.getString(R.string.order_number)))
			    .price("￥8000")
			    .desc("订单测试")
			    .imageUrl(IMAGE_URL_1)
			    .itemUrl("http://www.baidu.com");
			break;
		case 2:
			info.title("订单2")
				.orderTitle(String.format("%s：7890",context.getString(R.string.order_number)))
		        .price("￥158000")
		        .desc("订单测试2")
		        .imageUrl(IMAGE_URL_2)
		        .itemUrl("http://www.baidu.com");
			break;
			default:
				break;
		}
		return info;
		
	}
	
	public static AgentIdentityInfo createAgentIdentity(String agentName) {
		if (TextUtils.isEmpty(agentName)){
			return null;
		}
		AgentIdentityInfo info = ContentFactory.createAgentIdentityInfo(null);
		info.agentName(agentName);
		return info;
	}
	
	public static QueueIdentityInfo createQueueIdentity(String queueName) {
		if (TextUtils.isEmpty(queueName)){
			return null;
		}
		QueueIdentityInfo info = ContentFactory.createQueueIdentityInfo(null);
		info.queueName(queueName);
		return info;
	}

}
