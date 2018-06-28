package com.shyx.rthc.utils;


import com.shove.gateway.GeneralRestGateway;
import com.shove.security.Encrypt;
import com.shyx.rthc.common.ServiceConfig;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
 
public class EncryptUtil {

    
    /**
	 * 获取加密的url 不会破坏输入的参数
	 * 
	 * @param map
	 *            参数
	 * @return
	 */
	public static String encryptUrl(Map<String, String> map) {
		try {
			if(map==null){
				return null;
			}
			for (String key: map.keySet()) {
				if (map.get(key) == null) map.put(key, ""); //防止GeneralRestGateway.buildUrl方法null异常
			}
			String data = GeneralRestGateway.buildUrl(ServiceConfig.getServicesRootUrl(), ServiceConfig.MD5_KEY, map);
			return data;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/******************************************* app端构造sign ****************************************************/
	/** 加密串有效时间(s) */
	public static final int VALID_TIME = 3600;
	/** 加密action标识:标ID */
	public static final String BID_ID_SIGN = "b";
	/** 加密action标识:债权转让项目ID */
	public static final String DEBT_TRANSFER_ID_SIGN = "debt";
	/** 加密action标识:等级ID */
	public static final String CREDITLEVEL_ID_SIGN = "cl";
	/** 加密action标识:账单ID */
	public static final String BILL_ID_SIGN = "bill";

	/** 加密action标识:投资ID */
	public static final String INVEST_ID_SIGN = "invest";

	/** 加密action标识:产品ID */
	public static final String PRODUCT_ID_SIGN = "p";
	/** 加密action标识:用户ID */
	public static final String USER_ID_SIGN = "user";
	/** 加密action标识:管理员ID */
	public static final String SUPERVISOR_ID_SIGN = "supervisor_id";
	/** 加密action标识:资料ID */
	public static final String ITEM_ID_SIGN = "i";
	/** 加密action标识:用户资料ID */
	public static final String USER_ITEM_ID_SIGN = "ui";
	/** 加密action标识:用户站内信 */
	public static final String MSG_ID_SIGN = "mi";
	/** 加密action标识:用户邮箱 */
	public static final String MSG_EMAIL_SIGN = "email";
	/** 合同模板加密标志 */
	public static final String MSG_PACTTEMP_SIGN = "pactTemp";
	/** 合同模板加密标志 */
	public static final String MSG_PACT_SIGN = "pact";
	/** 加密action标识:资讯ID */
	public static final String INFORMATION_ID_SIGN="infor";
	/** 加密action标识:广告图片ID  */
	public static final String ADS_ID_SIGN="ads";
	/** 加密action标识:理财顾问ID  */
	public static final String CONSULTANT_ID_SIGN="consultant";
	/** 加密action标识:合作伙伴ID  */
	public static final String PARTNER_ID_SIGN="partner";
	/** 加密action标识:帮助中心ID  */
	public static final String HELPCENTER_ID_SIGN="help";
	/** 加密action标识:兑换记录  */
	public static final String CONV_ID_SIGN="conv";
	/** 加密action标识:主题  */
	public static final String THEME_ID_SIGN="theme";
	/** 一些弹框的sign加密value */
	public static final String SHOW_BOX = "show_box";
	/** 加密action标识:通知模板  */
	public static final String NOTEMP_ID_SIGN="notemp";
	/** 加密action标识:cps推广记录  */
	public static final String CPS_ID_SIGN="cps";
	/** 加密action标识:财富圈推广记录  */
	public static final String WEALTHCIRCLE_ID_SIGN="wealCir";

	/**
	 * Id加密
	 * @description
	 *
	 * @author Chenzhipeng
	 * @createDate 2015年12月18日
	 * @param id 需要加密的Id
	 * @param action 加密action标识
	 * @return
	 */
	public static String addSign(long id, String action) {
		/* 将id、action标示、当前时间利用3des加密 */
		String des = Encrypt.encrypt3DES(id + "," + action + "," + TimeUtils.getTimeString("yyyy-MM-dd HH:mm:ss",System.currentTimeMillis()),ServiceConfig.DES_KEY);
		/* 将加密得到的des利用MD5再次加密 */
		String md5 = Encrypt.MD5(des + ServiceConfig.DES_KEY);
		/* 将得到的des和MD5密文拼接处理 */
		String sign= des + md5.substring(0, 8);
		return sign;
	}


	/******************************************** app 端解密sign**************************************************/
	/**
	 * Id解密
	 * @description
	 *
	 * @author Chenzhipeng
	 * @createDate 2015年12月18日
	 * @param sign 密文
	 * @param action 加密action标识
	 * @param encryptKey
	 * @return
	 */
	public static long decodeSign(String sign, String action,String encryptKey) {
		/* 判断密文是否为空 */
		if(StringUtils.isEmpty(sign) || sign.length() < 8) {
			return 0;
		}
		String des = sign.substring(0, sign.length() - 8);
		String key = sign.substring(sign.length() - 8);
		String md5 = Encrypt.MD5(des + encryptKey);
		if(!key.equals(md5.substring(0, 8))) {
			return 0;//无效请求
		}
		String[] decryArray = Encrypt.decrypt3DES(des,encryptKey).split(",");
		Date validTime = TimeUtils.parseTime2Date("yyyy-MM-dd HH:mm:ss",decryArray[2]);

		if(!NumberUtils.isNumber(decryArray[0]) || decryArray.length != 3 || !decryArray[1].equals(action) || validTime == null || (Math.abs(System.currentTimeMillis() - validTime.getTime()) / 1000 > VALID_TIME)) {
			return 0;//无效请求
		}
		return Long.valueOf(decryArray[0]);//解密成功
	}

	public static long decodeSign(String sign, String action) {
		return decodeSign(sign,action,ServiceConfig.DES_KEY);
	}
}