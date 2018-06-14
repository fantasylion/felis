package com.serio.felis.assembly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 支付日志
 * @author zl.shi
 *
 */
public class PayInfoLogParticipleFactory extends ParticipleFactory {
	
	List<String> t_so_payinfo_log = Arrays.asList( new String[]{ "processor-id", "transaction-id", "amount", "payment-status", "order-no", "method-name" } );
	
	Map<String, String> payInfoLog				=	new HashMap<String, String>();
	
	
	@Override
	public void buildRawmaterial(List<Map<String, String>> rawmaterial) {
		
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( t_so_payinfo_log.contains(entry.getKey()) ) {
					payInfoLog.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
	}

	/**
	 * @return the payinfo
	 */
	public Map<String, String> getPayinfoLog() {
		return payInfoLog;
	}
}
