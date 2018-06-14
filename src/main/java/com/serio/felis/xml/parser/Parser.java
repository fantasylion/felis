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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ximpleware.VTDNav;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperAutoPilotHuge;
import com.ximpleware.extended.VTDNavHuge;

public abstract class Parser {
//	private static final Logger		logger					= LoggerFactory.getLogger(Parser.class);


	/**
	 * @return the rawmaterial
	 */
	public abstract Map<String, String> getRawmaterial( String xmlPath );
	
	
	/**
	 * 解析子节点的子节点，并将节点数据存储到Map中
	 * @param vnh
	 * @param map
	 */
	public void parseGrandChild( VTDNavHuge vnh, List<Map<String, String>> list ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			Map<String, String> map;
			do{
				map = new HashMap<String, String>();
				attrPutToMap( vnh, map );
				parseChildSimple(vnh, map);
				list.add(map);
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			this.toParentNext(vnh);
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析子节点，并将节点的数据存储到指定Map中，并跳转到下个父节点
	 * @param vnh
	 * @param map
	 */
	public void parseChild( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			do{
				parseToMap(vnh, map);
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			vnh.toElement(VTDNav.PARENT);
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析子节点，并将节点的数据存储到指定Map中，并跳转到父节点
	 * @param vnh
	 * @param map
	 */
	public void parseChildSimple( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			do{
				parseCurrentEle( vnh, map );
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			vnh.toElement( VTDNav.PARENT );
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

			if ( vnh.matchElement("custom-attributes") ) {
				parseCustomChild( vnh, map );
			} else {
				parseToMap(vnh, map);
			}
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
		
	}
	
	
	/**
	 * 解析custom结构的元素，并调到下一个父节点<br>
	 * &lt;custom-attribute attribute-id=&quot;emailValidationExpireTime&quot;&gt;1.513841135705E12&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;isAgreedPrivacyPolicy&quot;&gt;true&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;lastAgreedPrivacyPolicy&quot;&gt;2018-03-14T07:26:57.503+0000&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;luckyDrawProductId&quot;&gt;F5867000U,sampleset048&lt;/custom-attribute&gt;<br>
	 * @param vnh
	 * @param map
	 */
	public void parseCustomChild( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			parseCustomChilds(vnh, map);
			this.toParentNext(vnh);
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析custom结构的元素<br>
	 * &lt;custom-attribute attribute-id=&quot;emailValidationExpireTime&quot;&gt;1.513841135705E12&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;isAgreedPrivacyPolicy&quot;&gt;true&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;lastAgreedPrivacyPolicy&quot;&gt;2018-03-14T07:26:57.503+0000&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;luckyDrawProductId&quot;&gt;F5867000U,sampleset048&lt;/custom-attribute&gt;<br>
	 * @param vnh
	 * @param map
	 */
	public void parseCustomChilds( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			do{
				this.customAttributeToMap(vnh, map);
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析当前所有的子节点，并将节点数据存储到Map中，指针调回当前节点的父节点
	 * @param vnh
	 * @param map
	 */
	public void parseAllChild( VTDNavHuge vnh, Map<String, String> map ) {
		
		try {
			if ( vnh.toElement(VTDNav.FIRST_CHILD) ) {
				parseCurrentEleAndAllChild( vnh, map );
			}
		} catch (NavExceptionHuge e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析当前节点的兄弟节点和所有的子节点，并将节点数据存储到Map中，指针调回当前节点的节点
	 * @param vnh
	 * @param map
	 */
	public void parseCurrentEleAndAllChild( VTDNavHuge vnh, Map<String, String> map ) {
		try {
			do{
				if ( vnh.toElement(VTDNav.FIRST_CHILD) ) {
					this.parseCurrentEleAndAllChild(vnh, map);
				}
				parseCurrentEle(vnh, map);
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			 vnh.toElement(VTDNav.PARENT);
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 解析元素对象和元素的属性值，通过key-value的格式存储到Map中
	 * @param vnh
	 * @param map
	 * @throws NavExceptionHuge
	 */
	public void parseToMap( VTDNavHuge vnh, Map<String, String> map )throws NavExceptionHuge {
		textPutToMap( vnh, map );
		attrPutToMap( vnh, map );
	}
	
	
	/**
	 * 这种结构的取值<br>
	 * <code>
	 * &lt;custom-attribute attribute-id=&quot;emailValidationExpireTime&quot;&gt;1.513841135705E12&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;isAgreedPrivacyPolicy&quot;&gt;true&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;lastAgreedPrivacyPolicy&quot;&gt;2018-03-14T07:26:57.503+0000&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;luckyDrawProductId&quot;&gt;F5867000U,sampleset048&lt;/custom-attribute&gt;<br>
	 * </code>
	 * @param vnh
	 * @param map
	 * @throws NavExceptionHuge
	 */
	public void customAttributeToMap( VTDNavHuge vnh, Map<String, String> map )throws NavExceptionHuge {
		map.put(vnh.toString(vnh.getAttrVal("attribute-id")), vnh.toString(vnh.getText()));
	}
	
	
	/**
	 * 将元素的Text设置到指定的map对象中
	 * @param vnh
	 * @param map
	 * @throws NavExceptionHuge
	 */
	public void textPutToMap( VTDNavHuge vnh, Map<String, String> map ) throws NavExceptionHuge {
		
		if ( vnh.getText() != -1 ) {
			map.put(vnh.toString(vnh.getCurrentIndex()), vnh.toString(vnh.getText()));
		}

	}
	
	
	/**
	 * 将元素的属性值设置到指定的Map对象中
	 * @param vnh
	 * @param map
	 * @throws NavExceptionHuge
	 */
	public void attrPutToMap( VTDNavHuge vnh, Map<String, String> map ) throws NavExceptionHuge {
		SupperAutoPilotHuge saph = new SupperAutoPilotHuge(vnh);
		
		saph.superSelectAttr("*");
		
		int count = vnh.getAttrCount(), indexName = 0;
		String attrName;
		for ( int i = 0; i < count; i++  ) {
			indexName = saph.superIterateAttr();
			if ( indexName != -1 ) {
				attrName = vnh.toString(indexName);
				map.put(attrName, vnh.toString(vnh.getAttrVal(attrName)));
			}
		}
		
	}
	
	
	/**
	 * 到父节点的下一个节点
	 * @param vnh
	 * @throws NavExceptionHuge
	 */
	public void toParentNext( VTDNavHuge vnh ) throws NavExceptionHuge {
		
		vnh.toElement( VTDNav.PARENT );
		vnh.toElement( VTDNav.NEXT_SIBLING );
		
	}
}
