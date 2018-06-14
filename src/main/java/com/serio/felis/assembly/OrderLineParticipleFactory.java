package com.serio.felis.assembly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OrderLineParticipleFactory extends ParticipleFactory {
	
	List<String>  t_so_orderline = Arrays.asList( new String[]{ "gross-price", "base-price", "product-id", "product-name", "quantity", "engravingMessages", "engravingSpecialCharacter" } );//tax-basis
	List<Map<String, String>> orderlines			=	new ArrayList<Map<String, String>>();

	@Override
	public void buildRawmaterial(List<Map<String, String>> rawmaterial) {
		for ( Map<String, String> map :  rawmaterial ) {
			Map<String, String> line = new HashMap<String, String>();
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( t_so_orderline.contains(entry.getKey()) ) {
					line.put(entry.getKey(), entry.getValue());
				}
			}
			orderlines.add(line);
		}
	}

	public List<Map<String, String>> getOrderlines() {
		return orderlines;
	}

	public void setOrderlines(List<Map<String, String>> orderlines) {
		this.orderlines = orderlines;
	}

	
}
