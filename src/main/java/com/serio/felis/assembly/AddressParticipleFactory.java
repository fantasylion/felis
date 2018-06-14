package com.serio.felis.assembly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AddressParticipleFactory extends DefaultParticipleFactory {

	
	public AddressParticipleFactory() {
		baseList	=	Arrays.asList( new String[]{ "preferred", "salutation", "first-name", "last-name", "second-name", "address1", "city", "postal-code", "state-code", "phone", "district", "mobilePhone" } );
	}
	
	
	public Map<String, String> buildRawmaterial( Map<String, String> rawmaterial ) {
		
		Map<String, String> map = new HashMap<String, String>();
		
		for ( Entry<String,String> entry : rawmaterial.entrySet() ) {
			if ( baseList.contains(entry.getKey()) ) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		
		return map;
	}
}
