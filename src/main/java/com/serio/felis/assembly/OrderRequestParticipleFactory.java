package com.serio.felis.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OrderRequestParticipleFactory extends DefaultParticipleFactory {
	

	public OrderRequestParticipleFactory() {
		
		this.baseList	=	Arrays.asList( new String[]{ "create_time", "financial_status", "last_error", "logistics_status", "modify_time", "order_code",   "order_id", "order_status", "order_type", "payment", "result", "warn_count" } );
		
	}
	
	
	public void buildRawmaterial( Map<String, String> rawmaterial ) {
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(rawmaterial);
		
		buildRawmaterial(list);
	}
}
