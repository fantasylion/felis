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
import java.util.Map.Entry;

/**
 * @author serio.shi
 *
 */
public abstract class Translater {
	
	
	 Dictionary dic;
	
	/**
	 * 
	 */
	public Translater( Dictionary dic ) {
		this.dic = dic;
	}
	
	
	abstract Map<String, String> buildBucket();
	
	/**
	 * 
	 * Translate the key and value with the init dictionary. 
	 * @param map
	 * @return
	 */
	public Map<String, String> translate( Map<String, String> map ) {
		return translate( dic, map );
	}
	
	
	/**
	 * 
	 * Translate the key and value with the specified dictionary. 
	 * @param dic
	 * @param map
	 * @return
	 */
	public Map<String, String> translate( Dictionary dic,  Map<String, String> map ) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		Map<String, String> mapTemp;
		for ( Entry<String, String> entry : map.entrySet() ) {
			mapTemp = dic.searchKeyValue( entry.getKey(), entry.getValue() );
			if ( mapTemp != null )
				result.putAll(mapTemp);
		}
		
		return result;
	}
	
	
	/**
	 * Translate the single word 
	 * @param keyword
	 * @return
	 */
	public String translate( String keyword ) {
		return this.dic.searchKey(keyword);
	}
	
}
