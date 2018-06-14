/**
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.serio.felis.xml.parser.translater;

import java.util.HashMap;
import java.util.Map;

import com.serio.felis.hamal.common.SalesOrder;


/**
 * PayInfo跟PayInfoLog 公用这个类
 * @author zl.shi
 *
 */
public class PayInfoDictionary extends Dictionary {
	
	Map<String, String> payInfo = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("6","支付宝支付");
			put("4","微信支付");
			put("1","货到付款");
			put("10","小程序卡支付");
			put("300","财付通支付");
			put("320","银联在线支付");
		}
	};
	
	/**
     * <li>支付宝支付.payType=1</li>
     * <li>银联在线支付.payType=161</li>
     * <li>微信支付.payType=4</li> 
	 */
	public static Map<String, String> thirdPayTypeDic = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put( "6", "1" );
			put( "320", "161" );
			put( "4", "4" );
			
		}
	};

	
	/**
	 * Init the dictionary
	 */
	public PayInfoDictionary() {
		
		initDictionary();
		
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		// t_so_salesorder
		dictionary.put("amount", "pay_money");
		dictionary.put("processor-id", "pay_type");
		dictionary.put("method-name", "pay_type");
		dictionary.put("transaction-id", "third_pay_no");
		dictionary.put("completedTime", "pay_end_time");
		dictionary.put("payment-status", "pay_success_status");
		dictionary.put("order-no", "sub_ordinate");
	}
	
	
	/**
	 * 对 key 和 值 都进行搜索翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public Map<String, String> searchKeyValue( String keyWord, String value ) {
		
		Map<String, String> map = searchKeyValueDefault( keyWord, value );
		
		if ( "method-name".equals(keyWord) || "processor-id".equalsIgnoreCase(keyWord) ) {
			String payType = map.get("pay_type");
			String payInfoStr = payInfo.get(payType);
			if ( payInfoStr != null && !payInfoStr.isEmpty() ) {
				map.put("pay_info", payInfoStr);
			}
		}
		
		return map;
		
	}
	
	
	/**
	 * 对订单表某些字段值切换到 nebula 指定值
	 */
	public String searchValue( String keyWord, String value ) {
		
		value = tanslatePaymentType( keyWord, value );
		value = translatePaymentStatus( keyWord, value );
		
		return value;
		
	}
	

	/**
	 * 
	 * 支付状态码转换成 nebula需要的状态码
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String tanslatePaymentType( String keyWord, String value ) {
		
		if ( "pay_type".equalsIgnoreCase(keyWord) || "payment".equalsIgnoreCase(keyWord) ) {

			if ( "Alipay".equalsIgnoreCase(value) ) {
				return SalesOrder.SO_PAYMENT_TYPE_ALIPAY;
			} else if ( "COD".equalsIgnoreCase(value) ) {
				return SalesOrder.COD_TYPE_CASH;
			} else if ( "WECHAT".equalsIgnoreCase(value) ) {
				return SalesOrder.SO_PAYMENT_TYPE_WECHAT;
			} else if ( "WechatGiftCard".equalsIgnoreCase(value) ) {
				return SalesOrder.SO_PAYMENT_TYPE_PREPAID_CARD;
			} else if ( "Tenpay".equalsIgnoreCase(value) ) {
				return SalesOrder.SO_PAYMENT_TYPE_TENPAY;
			} else if ( "Unionpay".equalsIgnoreCase(value) ) {
				return SalesOrder.SO_PAYMENT_TYPE_UNIONPAY;
			}
		}
		return value;
	}
	
	
	/**
	 * 支付状态码转换成 nebula需要的状态码
	 * |display: NOTPAID value: 0 |display: PARTPAID value: 1 |display: PAID value: 2
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String translatePaymentStatus( String keyWord, String value ) {
		
		if ( "pay_success_status".equals(keyWord) ) {
			if ( "NOTPAID".equalsIgnoreCase(value) ) {
				return "f";
			} else if ( "PARTPAID".equalsIgnoreCase(value) ) {
				return "f";
			} else if ( "PAID".equalsIgnoreCase(value) ) {
				return "t";
			}
			return "f";
		}
		
		return value;
	}
}
