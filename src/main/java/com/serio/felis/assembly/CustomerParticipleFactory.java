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
 * 用于指定那些字段是属于nebula的那些表的
 * @author zl.shi
 *
 */
public class CustomerParticipleFactory extends ParticipleFactory {
	
	
	List<String> t_mem_member, t_mem_personal_data, t_mem_cryptoguard, t_mem_conduct;
	
	Map<String, String> member 			= new HashMap<String, String>();
	Map<String, String> personalData	= new HashMap<String, String>();
	Map<String, String> cryptoguard		= new HashMap<String, String>();
//	List<Map<String, String>> contact	= new ArrayList<Map<String, String>>();
	Map<String, String> conduct			= new HashMap<String, String>();
	
	
	public CustomerParticipleFactory() {
		
		String[] mem_member				= { "login", "password", "enabled-flag", "external-id", "customer-no", "provider-id", "newsLetterSubscribed" };
		String[] mem_personal_data		= { "salutation", "last-name", "company-name", "email", "birthday", "companyName", "phone-mobile", "sex", "externalMobilePhone" };//"gender",
		String[] mem_cryptoguard		= { "password-question", "password-answer" };
		String[] mem_conduct			= { "creation-date", "last-login-time" };
		
		t_mem_member		=	Arrays.asList( mem_member );
		t_mem_personal_data	=	Arrays.asList( mem_personal_data );
		t_mem_cryptoguard	=	Arrays.asList( mem_cryptoguard );
//		t_mem_contact		=	Arrays.asList( mem_contact );
		t_mem_conduct		=	Arrays.asList( mem_conduct );
		
	}
	
	
	/**
	 * Build Tables
	 * @param rawmaterial
	 */
	public void buildRawmaterial( List<Map<String, String>> rawmaterial ) {
		
		
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( t_mem_member.contains(entry.getKey()) ) {
					member.put(entry.getKey(), entry.getValue());
				}
				
				if ( t_mem_personal_data.contains(entry.getKey()) ) {
					personalData.put(entry.getKey(), entry.getValue());
				}
				
				if ( t_mem_cryptoguard.contains(entry.getKey()) ) {
					cryptoguard.put(entry.getKey(), entry.getValue());
				}
				
				if ( t_mem_conduct.contains(entry.getKey()) ) {
					conduct.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
		
	}
	
	/**
	 * 构建地址数据
	 * @param rawmaterial
	 */
//	public void buildAddress( List<Map<String, String>> rawmaterial ) {
//		
//		for ( Map<String, String> map :  rawmaterial ) {
//			Map<String, String> addressMap = new HashMap<String, String>();
//			for ( Entry<String,String> entry : map.entrySet() ) {
//				if ( t_mem_contact.contains(entry.getKey()) ) {
//					addressMap.put(entry.getKey(), entry.getValue());
//				}
//			}
//			this.contact.add(addressMap);
//		}
//		
//	}
	
	// 筛选地址字段
//	public Map<String, String> buildAddres( Map<String, String> map ) {
//		
//		Map<String, String> addressMap = new HashMap<String, String>();
//		
//		for ( Entry<String,String> entry : map.entrySet() ) {
//			if ( t_mem_contact.contains(entry.getKey()) ) {
//				addressMap.put(entry.getKey(), entry.getValue());
//			}
//		}
//		
//		return addressMap;
//		
//	}


	/**
	 * @return the member
	 */
	public Map<String, String> getMember() {
		return member;
	}

	/**
	 * @param member the member to set
	 */
	public void setMember(Map<String, String> member) {
		this.member = member;
	}

	/**
	 * @return the personalData
	 */
	public Map<String, String> getPersonalData() {
		return personalData;
	}

	/**
	 * @param personalData the personalData to set
	 */
	public void setPersonalData(Map<String, String> personalData) {
		this.personalData = personalData;
	}

	/**
	 * @return the cryptoguard
	 */
	public Map<String, String> getCryptoguard() {
		return cryptoguard;
	}

	/**
	 * @param cryptoguard the cryptoguard to set
	 */
	public void setCryptoguard(Map<String, String> cryptoguard) {
		this.cryptoguard = cryptoguard;
	}
	
	
//	/**
//	 * @return the contact
//	 */
//	public List<Map<String, String>> getContact() {
//		return contact;
//	}
//
//
//	/**
//	 * @param contact the contact to set
//	 */
//	public void setContact(List<Map<String, String>> contact) {
//		this.contact = contact;
//	}


	/**
	 * @return the conduct
	 */
	public Map<String, String> getConduct() {
		return conduct;
	}

	/**
	 * @param conduct the conduct to set
	 */
	public void setConduct(Map<String, String> conduct) {
		this.conduct = conduct;
	}
	
	
	
	
}
