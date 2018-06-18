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


/**
 * Used to parse to xml files. This class file contain base method. 
 * @author serio.shi
 *
 */
public abstract class Parser {


	/**
	 * @return the rawmaterial
	 */
	public abstract Map<String, String> getRawmaterial( String xmlPath );
	
	
	/**
	 * Parse child nodes of child nodes and store node data in Map.
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
	 * Parse the child node, store the data of the node into the specified Map, and jump to the original parent node.
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
	 * Parse the child node, store the data of the node into the specified Map, and jump to the parent node.
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
				parseCustomChild( vnh, map, "attribute-id" );
			} else {
				parseToMap(vnh, map);
			}
		} catch (Exception e) {

		}
		
	}
	
	
	/**
	 * Resolve the elements of the custom structure and transfer to the next parent<br>
	 * &lt;custom-attribute attribute-id=&quot;emailValidationExpireTime&quot;&gt;1.513841135705E12&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;isAgreedPrivacyPolicy&quot;&gt;true&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;lastAgreedPrivacyPolicy&quot;&gt;2018-03-14T07:26:57.503+0000&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;luckyDrawProductId&quot;&gt;F5867000U,sampleset048&lt;/custom-attribute&gt;<br>
	 * @param vnh
	 * @param map
	 */
	public void parseCustomChild( VTDNavHuge vnh, Map<String, String> map, String attrName ) {
		try {
			parseCustomChilds(vnh, map, attrName);
			this.toParentNext(vnh);
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * Resolve the elements of the custom structure<br>
	 * &lt;custom-attribute attribute-id=&quot;emailValidationExpireTime&quot;&gt;1.513841135705E12&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;isAgreedPrivacyPolicy&quot;&gt;true&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;lastAgreedPrivacyPolicy&quot;&gt;2018-03-14T07:26:57.503+0000&lt;/custom-attribute&gt;<br>
	 * &lt;custom-attribute attribute-id=&quot;luckyDrawProductId&quot;&gt;F5867000U,sampleset048&lt;/custom-attribute&gt;<br>
	 * @param vnh
	 * @param map
	 */
	public void parseCustomChilds( VTDNavHuge vnh, Map<String, String> map, String attrName ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			do{
				this.customAttributeToMap(vnh, map, attrName);
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * Parse all current child nodes, store the node data in the Map, and redirect the pointer to the current node's parent node.
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
	 * Parse the current node's sibling node and all child nodes, store the node data in the Map, and redirect the pointer to the node of the current node.
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
	 * Parse element object and element attribute values and store them in the format of key-value to the Map.
	 * @param vnh
	 * @param map
	 * @throws NavExceptionHuge
	 */
	public void parseToMap( VTDNavHuge vnh, Map<String, String> map )throws NavExceptionHuge {
		textPutToMap( vnh, map );
		attrPutToMap( vnh, map );
	}
	
	
	/**
	 * The value of structure like this<br>
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
	public void customAttributeToMap( VTDNavHuge vnh, Map<String, String> map, String attrName )throws NavExceptionHuge {
		map.put(vnh.toString(vnh.getAttrVal(attrName)), vnh.toString(vnh.getText()));
	}
	
	
	/**
	 * Set the element's Text to the specified map object
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
	 * Set value of the element to the specified Map object.
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
	 * 
	 * Moves the current pointer to the next node of the parent node.
	 * @param vnh
	 * @throws NavExceptionHuge
	 */
	public void toParentNext( VTDNavHuge vnh ) throws NavExceptionHuge {
		
		vnh.toElement( VTDNav.PARENT );
		vnh.toElement( VTDNav.NEXT_SIBLING );
		
	}
}
