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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.serio.felis.tools.CommonTools;

/**
 * 将xml中的订单信息转成nebula中的字段
 * @author zl.shi
 *
 */
public class OrderDictionary extends PayInfoDictionary {
	
	
	/**
	 * Init the dictionary
	 */
	public OrderDictionary() {
		
		initDictionary();
		
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		// t_so_salesorder
		dictionary.put("order-no", "code");
		dictionary.put("customer-locale", "lang");
//		dictionary.put("customer-name", "member_name");
		dictionary.put("last-name", "name");
		dictionary.put("customer-no", "member_id");
		dictionary.put("order-status", "logistics_status");
		dictionary.put("payment-status", "financial_status");
		dictionary.put("method-name", "payment");
		dictionary.put("order-date", "create_time");
		dictionary.put("omsOrderDevice", "source");
		
		dictionary.put("last-name", "receipt_consignee");
		dictionary.put("address1", "receipt_address");
		dictionary.put("postal-code", "receipt_code");
		dictionary.put("mobilePhone", "receipt_telphone");
		dictionary.put("custom-method", "payment");
		dictionary.put("invoiceData", "invoice");
		dictionary.put("omsOrderNumber", "oms_code");
		dictionary.put("remoteHost", "ip");
		
		
		// TODO 待确定
		dictionary.put("shipping-total", "actual_freight");
		dictionary.put("adjusted-shipping-total", "payable_freight");
//		dictionary.put("order-total", "");
		dictionary.put("order-total", "total");
//		dictionary.put("adjusted-merchandize-total", "");
		
	}
	
	
	/**
	 * 对 key 和 值 都进行搜索翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public Map<String, String> searchKeyValue( String keyWord, String value ) {
		
		if ( "invoiceData".equalsIgnoreCase(keyWord) ) {
			return processInvoiceData( value );
		}
		
		return searchKeyValueDefault(keyWord, value);
		
	}
	
	
	
	/**
	 * 处理发票
	 * @param data
	 * @return
	 */
	public Map<String, String> processInvoiceData( String data ) {
		
		JSONObject json = JSON.parseObject(data);
		if ( json.get("invoiceNeeded").equals(false) ) {
			return null;
		}
		
		String invoiceTitle = (String) json.get("invoiceTitle");

		Map<String, String> map = new HashMap<String, String>();

		if ( "Personal".equalsIgnoreCase(invoiceTitle) ) {
			map.put("receipt_title", "个人");
			map.put("invoice_type", "1");
			map.put("receipt_type", "1");
			
		} else if ( "Company".equalsIgnoreCase(invoiceTitle) ) {
			String company = (String)json.get("invoiceCompany");
			String taxpayerId = (String)json.get("invoiceIdentificationNumber");
			map.put("receipt_title", company);
			if ( taxpayerId != null && !taxpayerId.isEmpty() && CommonTools.isNumeric(taxpayerId) ) {
				map.put("taxpayer_id", taxpayerId);
			}
			map.put("invoice_type", "1");
			map.put("receipt_type", "2");
		}
		
		return map;
		
	}
	
	
	/**
	 * 对订单表某些字段值切换到 nebula 指定值
	 */
	public String searchValue( String keyWord, String value ) {
		
		value = translatePaymentStatus( keyWord, value );
		
		value = translateOrderStatus( keyWord, value );
		
		value = tanslatePaymentType( keyWord, value );
		
		value = translateDiscount( keyWord, value );
		
		value = omsOrderDevice( keyWord, value );
		
		return value;
	}
	
	
	/**
	 * 设置order 表的source 字段，4 5 6 表示迁移订单
	 * <li>4 表示pc下单</li>
	 * <li>5 表示手机下单</li>
	 * <li>6 表示平板下单</li>
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String omsOrderDevice( String keyWord, String value ) {
		if ( "source".equalsIgnoreCase(keyWord) ) {
			if ( "desktop".equalsIgnoreCase(value) ) {
				return "4";
			} else if ( "mobile".equalsIgnoreCase(value) ) {
				return "5";
			} else if ( "tablet".equalsIgnoreCase(value) ) {
				return "6";
			}
		}
		return value;
	}
	
	
	/**
	 * 处理下订单折扣，目前艾森哲的数据是有负号的，需要处理下。
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String translateDiscount( String keyWord, String value ) {
		
		if ( "discount".equalsIgnoreCase(keyWord) ) {
			return CommonTools.toPositiveNumberStr(value);
		}
		
		return value;
	}
	
	
	/**
	 * 订单状态码转换成 nebula需要的状态码
	 * |display: CREATED value: 0 |display: NEW value: 3 |display: OPEN value: 4 |display: COMPLETED value: 5 |display: CANCELLED value: 6 |display: REPLACED value: 7 |display: FAILED value: 8<br>
	 * <br>
     *  1 新建<br>
     *  3 已同步oms<br>
     *  4 库存已确认<br>
     *  5 库房准备中<br>
     *  6 在途<br>
     *  9 会员取消<br>
     *  10 商城取消<br>
     *  15 交易完成<br>
     *  <br><br>
     *  create    -> 新建<br>
     *  new       -> 库存已确认(过单)<br>
     *  open      -> 交易完成.<br>
     *  complete  -> 交易完成.<br>
     *  cancelled -> 商城取消<br>
     *  replaced  -> 交易完成.<br>
     *  failed    -> 商城取消<br>
     * <br><br>
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String translateOrderStatus( String keyWord, String value ) {
		
		if ( "logistics_status".equalsIgnoreCase(keyWord) ) {
			if ( "CREATED".equalsIgnoreCase(value) ) {
				return "1";
			} else if ( "NEW".equalsIgnoreCase(value) ) {
				return "4";
			} else if ( "OPEN".equalsIgnoreCase(value) ) {
				return "15";
			} else if ( "COMPLETED".equalsIgnoreCase(value) ) {
				return "15";
			} else if ( "CANCELLED".equalsIgnoreCase(value) ) {
				return "10";
			} else if ( "REPLACED".equalsIgnoreCase(value) ) {
				return "15";
			} else if ( "FAILED".equalsIgnoreCase(value) ) {
				return "10";
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
		
		if ( "financial_status".equalsIgnoreCase(keyWord) ) {
			if ( "NOTPAID".equalsIgnoreCase(value) ) {
				return "1";
			} if ( "PARTPAID".equalsIgnoreCase(value) ) {
				return "4";
			} if ( "PAID".equalsIgnoreCase(value) ) {
				return "3";
			} else {
				return "1";
			}
		}
		
		return value;
	}
}

