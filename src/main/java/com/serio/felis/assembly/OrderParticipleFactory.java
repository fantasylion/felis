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
package com.serio.felis.assembly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 确认xml订单中的哪些字段是属于nebula的订单表的,哪些是属于订单行表的，哪些是属于活动表的
 * @author zl.shi
 *
 */
public class OrderParticipleFactory extends ParticipleFactory {
	
	
	List<String> t_so_salesorder, t_so_orderpromotion;
	
	Map<String, String> salesorder			=	new HashMap<String, String>();
	Map<String, String> orderpromotion		=	new HashMap<String, String>();
	
	
	public OrderParticipleFactory() {
		
		t_so_salesorder			=	Arrays.asList( new String[]{ "order-no", "customer-locale", "customer-no", "order-status", "payment-status", "method-name", "invoiceData", "omsOrderNumber", "remoteHost", "shipping-total", "adjusted-shipping-total", "invoiceData", "discount", "order-date", "omsOrderDevice", "order-total" } );
		
	}
	
	
	/** 
	 * 将数据按照nebula表进行划分
	 * @see com.baozun.assembly.ParticipleFactory#buildRawmaterial(java.util.List)
	 */
	@Override
	public void buildRawmaterial( List<Map<String, String>> rawmaterial ) {
		
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( t_so_salesorder.contains(entry.getKey()) ) {
					salesorder.put(entry.getKey(), entry.getValue());
				}
				
				if ( t_so_orderpromotion.contains(entry.getKey()) ) {
					orderpromotion.put(entry.getKey(), entry.getValue());
				}
				
				
			}
		}
	}


	/**
	 * @return the salesorder
	 */
	public Map<String, String> getSalesorder() {
		return salesorder;
	}


	/**
	 * @param salesorder the salesorder to set
	 */
	public void setSalesorder(Map<String, String> salesorder) {
		this.salesorder = salesorder;
	}


	/**
	 * @return the orderpromotion
	 */
	public Map<String, String> getOrderpromotion() {
		return orderpromotion;
	}


	/**
	 * @param orderpromotion the orderpromotion to set
	 */
	public void setOrderpromotion(Map<String, String> orderpromotion) {
		this.orderpromotion = orderpromotion;
	}

}
