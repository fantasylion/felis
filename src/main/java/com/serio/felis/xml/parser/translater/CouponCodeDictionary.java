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

/**
 * @author zl.shi
 *
 */
public class CouponCodeDictionary extends Dictionary {
	
	public static final String USED_YES 	= "1";
	public static final String USED_NO		= "0";
	public static final String ACTIVE_YES	= "1";
	public static final String ACTIVE_NO	= "0";
	
	/**
	 * Init the dictionary
	 */
	public CouponCodeDictionary() {
		
		initDictionary();
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		dictionary.put("coupon", "coupon_code");
		dictionary.put("status", "isused");
		
	}
	
	
	/**
	 * 对 key 和 值 都进行搜索翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public Map<String, String> searchKeyValue( String keyWord, String value ) {

		if ( "status".equalsIgnoreCase(keyWord) ) {
			Map<String, String> map = new HashMap<String, String>();
			
			if ( "USED".equalsIgnoreCase(value) ) {				// USED：	     已使用、已激活
				map.put("isused", USED_YES);
				map.put("active_mark", ACTIVE_YES);
			} else if ( "AVAILABLE".equalsIgnoreCase(value) ) { // AVAILABLE：未使用、已激活
				map.put("isused", USED_NO);
				map.put("active_mark", ACTIVE_YES);
			} else if ( "CREATED".equalsIgnoreCase(value) ) {   // CREATED：     未使用、未激活
				map.put("isused", USED_NO);
				map.put("active_mark", ACTIVE_NO);
			}
			return map;
		}
		
		return searchKeyValueDefault( keyWord, value );
		
	}
	
}
