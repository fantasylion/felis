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

import com.serio.felis.db.MemberMapping;
import com.serio.felis.tools.CommonTools;

/**
 * @author zl.shi
 *
 */
public class RateAndReViewDictionary extends Dictionary {
	/**
	 * Init the dictionary
	 */
	public RateAndReViewDictionary() {
		
		initDictionary();
		
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		dictionary.put("content", "content");
		dictionary.put("pid", "style");
		
		dictionary.put("status", "lifecycle");
		dictionary.put("submitTime", "create_time");
		dictionary.put("submitterId", "member_id");
		dictionary.put("tags", "tags");
		dictionary.put("rating", "score");
		dictionary.put("channel", "source");
		
		dictionary.put("ID", "original_id");
		
	}
	
	
	/**
	 * 对值进行翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchValue( String keyWord, String value ) {
		
		if ( "member_id".equals(keyWord) ) {
			return MemberMapping.getMemberMapping().getValue(value);
		}
		
		if ( "lifecycle".equals(keyWord) ) {
			// 5 待审核、1 审核通过
			return "1";
		}
		
		if ( "score".equals(keyWord) ) {
			return CommonTools.toInt(value) + "";
		}
		
		return value;
		
	}
}
