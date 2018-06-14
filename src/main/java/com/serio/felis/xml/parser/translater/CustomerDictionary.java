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

import com.serio.felis.tools.CommonTools;


/**
 * @author zl.shi
 *
 */
public class CustomerDictionary extends Dictionary {
	
	
	public static Map<String, String> source = new HashMap<String, String>(){
		private static final long serialVersionUID = 1L;
		{
			put(null,"1");
			put("QQ","2");
			put("SinaWeiboLancome", "3");
			put("Alipay","4");
			put("Wechat","5");
			put("RoseBeauty","6");
		}
	};
	
	/**
	 * Init the dictionary
	 */
	public CustomerDictionary() {
		
		initDictionary();
		initEndecryptFields();
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		// member
		dictionary.put("password", "oldpassword");
		dictionary.put("enabled-flag", "lifecycle");
		dictionary.put("external-id", "third_party_identify");
		dictionary.put("customer-no",  "customer_no");
		dictionary.put("provider-id",  "source");
		dictionary.put("newsLetterSubscribed","receive_mail");
		
		
		// person_data
		dictionary.put("salutation",   "local_real_name");
		dictionary.put("last-name",    "nickname");
		dictionary.put("company-name", "company");
		dictionary.put("email",        "email");
		dictionary.put("phone-mobile", "mobile");
		dictionary.put("sex", "sex");
//		dictionary.put("gender", "sex");
		
		// cryptoguard
		dictionary.put("password-question", "question");
		dictionary.put("password-answer", "answer");
		
		// t_mem_conduct
		dictionary.put("creation-date",	  "register_time");
		dictionary.put("last-login-time", "login_time");
		
	}
	
	/**
	 * 初始化需要加密的字段
	 */
	public void initEndecryptFields() {
		
		endecryptFields.add("email");
		endecryptFields.add("mobile");
		endecryptFields.add("nickname");
		
	}
	
	/**
	 * 对Key进行翻译，如果没有找到直接返回原key
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchKey( String keyWord, String value ) {
		
		if ( "login".equals(keyWord) ) {
			if ( CommonTools.isEmail(value) ) {
				return "login_email";
				
			// 非邮箱暂时写到mobile里面如果是第三方登录有externalMobilePhone的会被externalMobilePhone覆盖
			} else {
				return "login_mobile";
			}
		}
		
		return super.searchKey(keyWord, value);
	}
	
	@Override
	public String searchValue(String keyWord, String value) {
		if ( "source".equalsIgnoreCase(keyWord) ) {
			return source.get(value);
		}
		if ( "receive_mail".equalsIgnoreCase(keyWord) ) {
			if ( "true".equalsIgnoreCase(value) ) {
				return "1";
			} else {
				return "0";
			}
		}
		if ( "sex".equalsIgnoreCase(keyWord) ) {
			if ( "female".equalsIgnoreCase(value) ) {
				return "2";
			} else {
				return "1";
			}
		}
		return super.searchValue(keyWord, value);
	}
	
	
}
