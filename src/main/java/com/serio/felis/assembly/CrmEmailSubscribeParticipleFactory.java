package com.serio.felis.assembly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 隐私条款
 * @author zl.shi
 *
 */
public class CrmEmailSubscribeParticipleFactory extends ParticipleFactory {
	
	List<String> t_crm_email_subscribe_log = Arrays.asList( new String[]{"agreedPrivacyPolicyURL", "isAgreedPrivacyPolicy", "lastAgreedPrivacyPolicy"} );

	Map<String, String> email_subscribe_log 			= new HashMap<String, String>();

	@Override
	public void buildRawmaterial(List<Map<String, String>> rawmaterial) {
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				
				if ( t_crm_email_subscribe_log.contains(entry.getKey()) ) {
					email_subscribe_log.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
	}

	public Map<String, String> getEmailSubscribeLog() {
		return email_subscribe_log;
	}

	
	

}
