package com.serio.felis.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CouponCodeParticipleFactory extends DefaultParticipleFactory {
	
	public CouponCodeParticipleFactory() {
		
		this.baseList	=	Arrays.asList( new String[]{ "coupon", "status" } );
		
	}
	
	
	public void buildRawmaterial( Map<String, String> rawmaterial ) {
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(rawmaterial);
		
		buildRawmaterial(list);
	}
}
