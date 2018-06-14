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
package com.serio.felis.xml.parser;

import java.util.Map;

import com.ximpleware.VTDNav;
import com.ximpleware.extended.VTDNavHuge;

/**
 * @author zl.shi
 *
 */
public class RatingsAndReviewParser extends Parser {

//	private static final Logger		logger					= LoggerFactory.getLogger(RatingsAndReviewParser.class);

	
	/* (non-Javadoc)
	 * @see com.baozun.xml.parser.Parser#getRawmaterial(java.lang.String)
	 */
	@Override
	public Map<String, String> getRawmaterial(String xmlPath) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 解析子节点，并将节点的数据存储到指定Map中，并跳转到父节点
	 * @param vnh
	 * @param map
	 */
	public void parseChildSimple( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			
			vnh.toElement(VTDNav.FIRST_CHILD);
			parseCurrentEle( vnh, map );
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 
	 * @param vnh
	 * @param map
	 */
	public void parseCurrentEle( VTDNavHuge vnh, Map<String, String> map ) {
		
		try {
			
//			logger.debug(vnh.toString(vnh.getCurrentIndex()));
			if ( vnh.matchElement("object-attribute") ) {
				parseCustomChilds( vnh, map );
			} else {
				parseToMap(vnh, map);
			}
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
		
	}
	
}
