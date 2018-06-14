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
 * 将指定Map中的数据划分到PayInfo 表中
 * @author zl.shi
 *
 */
public class PayInfoParticipleFactory extends ParticipleFactory {
	
	
	List<String> t_so_payinfo;
	Map<String, String> payinfo				=	new HashMap<String, String>();
	String[] so_payinfo			= { "processor-id", "transaction-id", "completedTime", "amount", "payment-status", "method-name" };

	public PayInfoParticipleFactory() {
		t_so_payinfo			=	Arrays.asList( so_payinfo );
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see com.baozun.assembly.ParticipleFactory#buildRawmaterial(java.util.List)
	 */
	@Override
	public void buildRawmaterial(List<Map<String, String>> rawmaterial) {
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( t_so_payinfo.contains(entry.getKey()) ) {
					payinfo.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
	}

	/**
	 * @return the payinfo
	 */
	public Map<String, String> getPayinfo() {
		return payinfo;
	}

	

}
