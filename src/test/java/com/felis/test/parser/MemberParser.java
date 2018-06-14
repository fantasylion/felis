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
package com.felis.test.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.serio.felis.xml.parser.Parser;
import com.ximpleware.VTDNav;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperVTDGenHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import com.ximpleware.extended.XPathParseExceptionHuge;

/**
 * @author serio.shi
 *
 */
public class MemberParser extends Parser {

	
	
	/* (non-Javadoc)
	 * @see com.serio.felis.xml.parser.Parser#getRawmaterial(java.lang.String)
	 */
	@Override
	public Map<String, String> getRawmaterial(String xmlPath) {
		return null;
	}
	
	
//	public  void parseChildEle( VTDNavHuge vnh ) {
//		try {
//			if ( !vnh.toElement(VTDNav.FIRST_CHILD) ) {
//				this.parseToMap(vnh, profile);
//				return;
//			}
//			
//			// 在customer子元素中循环
//			do{
//				
//				if ( vnh.matchElement("credentials") ) {
//					this.parseChild(vnh, credentials);
//				}
//				if ( vnh.matchElement("profile") ) {
//					this.parseChild(vnh, profile);
//				}
//				if ( vnh.matchElement("addresses") ) {
//					this.parseGrandChild(vnh, addresses);
//				}
//				if ( vnh.matchElement("external-profiles") ) {
//					this.parseGrandChild(vnh, externaProfile);
//				}
//				
//				this.textPutToMap(vnh, profile);
//				
//			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
//				
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public void parseLoop( VTDNavHuge vnh, String selecter ) {
		try {
			AutoPilotHuge aph = new AutoPilotHuge(vnh);
			
			aph.selectXPath(selecter);
			vnh.toElement(VTDNav.FIRST_CHILD);

			// 在customer层元素 循环
			do{
				Map<String, String> profile = new HashMap<String, String>();
				this.attrPutToMap(vnh, profile);
				this.parseAllChild(vnh, profile);
				System.out.println(JSON.toJSONString(profile));
				System.out.println("-----------------------------");
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			
		} catch (NavExceptionHuge e) {
			e.printStackTrace();
		} catch (XPathParseExceptionHuge e) {
			e.printStackTrace();
		}
		
	}
	
	public void run(String inputFilePath) {
		
		String xpath = "/customers/customer";
		System.out.println("Start processing members...");
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile(inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
			System.out.println("Start loop members...");
	        VTDNavHuge vnh = vgh.getNav();
	        this.parseLoop( vnh, xpath );
		}
	}
	
	public static void main(String[] args) {
		
		MemberParser mp = new MemberParser();
		mp.run("D:/DATA/loreal/数据迁移/sampledata/stg_lan_customer_20180327.xml");
		
	}

}
