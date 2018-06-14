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

/**
 * @author zl.shi
 *
 */
public class AddressDictionary extends Dictionary {

	
	/**
	 * Init the dictionary
	 */
	public AddressDictionary() {
		
		initDictionary();
		initEndecryptFields();
	}
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		// t_mem_contact
		dictionary.put("postal-code", "post_code");
		dictionary.put("city", "city");
		dictionary.put("state-code",  "province");
		dictionary.put("district", "area");//town
		dictionary.put("country-code", "country");
		dictionary.put("phone", "telphone");
		dictionary.put("mobilePhone", "mobile");
		dictionary.put("last-name", "name");
		dictionary.put("address1", "address");
		dictionary.put("preferred", "is_default");

	}
	
	/**
	 * 初始化需要加密的字段,(翻译过后的字段)
	 */
	public void initEndecryptFields() {
		endecryptFields.add("address");
		endecryptFields.add("province");
		endecryptFields.add("town");
		endecryptFields.add("area");
		endecryptFields.add("city");
		endecryptFields.add("mobile");
		endecryptFields.add("telphone");
		endecryptFields.add("name");
		endecryptFields.add("post_code");
	}
	
	@Override
	public String searchValue(String keyWord, String value) {
		
		if ( "is_default".equalsIgnoreCase(keyWord) ) {
			if ( "true".equalsIgnoreCase(value) ) {
				return "t";
			} else {
				return "f";
			}
		}
		return super.searchValue(keyWord, value);
	}
	
}
