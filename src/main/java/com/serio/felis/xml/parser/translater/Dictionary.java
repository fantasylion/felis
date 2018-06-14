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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serio.felis.tools.SuperAESEncryptor;


/**
 * 对字段和字段值转换成Nebula的字段名和值域
 * @author zl.shi
 *
 */
public abstract class Dictionary {
	
	protected Map<String, String> dictionary			= new HashMap<String, String>();
	protected List<String>        endecryptFields		= new ArrayList<String>();
	
	
	/**
	 * @return the dictionary
	 */
	public Map<String, String> getDictionary() {
		return dictionary;
	}

	/**
	 * @param dictionary the dictionary to set
	 */
	public void setDictionary(Map<String, String> dictionary) {
		this.dictionary = dictionary;
	}

	/**
	 * 根据keyword查询到对应的值
	 * @param keyWord
	 * @return
	 */
	public String searchKey(String keyWord) {
		return this.dictionary.get(keyWord);
	}
	
	
	/**
	 * 对Key进行翻译，如果没有找到直接返回原key
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchKey( String keyWord, String value ) {
		
		String key = dictionary.get(keyWord);
		
		return key == null ? keyWord : key;
		
	}
	
	
	/**
	 * 对值进行翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchValue( String keyWord, String value ) {
		if ( "lifecycle".equalsIgnoreCase(keyWord) ) {
			if ( "true".equalsIgnoreCase(value) ) {
				return "0";
			}
		}
		return value;
		
	}
	
	
	/**
	 * 对 key 和 值 都进行搜索翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public Map<String, String> searchKeyValue( String keyWord, String value ) {
		
		return searchKeyValueDefault( keyWord, value );
		
	}
	
	
	/**
	 * 默认的处理方式
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public Map<String, String> searchKeyValueDefault( String keyWord, String value ) {
		
		String key = searchKey( keyWord, value );
		
		if ( key == null ) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		String valued = searchValue(key, value);
//		map.put( key, searchValue(keyWord, endecryptFields( key, valued )) );
		map.put( key, endecryptFields( key, valued ) );
		
		return map;
		
	}
	
	
	/**
	 * 根据fields中的字段进行加密
	 * @param plainText
	 * @param map
	 * @param fields
	 * @return
	 */
	public String endecryptFields( String key, String value ) {
		
		if ( endecryptFields.contains(key) ) {
			return SuperAESEncryptor.endecrypt(value);
		}
		
		return value;
	}
}
