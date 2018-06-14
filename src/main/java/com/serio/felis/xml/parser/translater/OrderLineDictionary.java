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

import com.serio.felis.tools.CommonTools;

/**
 * @author zl.shi
 *
 */
public class OrderLineDictionary extends Dictionary {
	/**
	 * Init the dictionary
	 */
	public OrderLineDictionary() {
		
		initDictionary();
		
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		// 这里 少了 sale_price 在 orderSqlFactory里补 
		dictionary.put("base-price", "msrp");
		dictionary.put("gross-price", "subtotal");
		dictionary.put("product-id", "extention_code");
		dictionary.put("product-name", "item_name");
		dictionary.put("quantity", "count");
		dictionary.put("gift", "type");
	}
	
	/**
	 * 对值进行翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchValue( String keyWord, String value ) {
		
		if ( "count".equalsIgnoreCase(keyWord) ) {
			return CommonTools.toInt( value ) + "";
		}
		
		if ( "type".equals("keyWord") ) {
			if ( "true".equalsIgnoreCase(value) ) {
				return "0";
			}
			return "1";
		}
		
		return value;
	}
	
}
