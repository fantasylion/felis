package com.serio.felis.assembly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OrderAddressParticipleFactory extends ParticipleFactory {
	
	
	List<String> order_address;
	
	Map<String, String> orderAddress		=	new HashMap<String, String>();
	


	
	public OrderAddressParticipleFactory() {
		
		String[] orderAddress		=	{ "last-name", "address1", "city", "postal-code", "state-code", "country-code", "district", "mobilePhone", "city_id", "area_id", "town_id", "province_id" };
		
		order_address				=	Arrays.asList( orderAddress );
		
	}
	
	
	@Override
	public void buildRawmaterial(List<Map<String, String>> rawmaterial) {
		for ( Map<String, String> map : rawmaterial ) {
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( order_address.contains(entry.getKey()) ) {
					orderAddress.put(entry.getKey(), entry.getValue());
				}
			}
		}
	}


	public Map<String, String> getOrderAddress() {
		return orderAddress;
	}


	
	
	
}
