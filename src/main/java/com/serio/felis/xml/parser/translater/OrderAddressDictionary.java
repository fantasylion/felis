package com.serio.felis.xml.parser.translater;

public class OrderAddressDictionary extends AddressDictionary {
	/**
	 * Init the dictionary
	 */
	public OrderAddressDictionary() {
		
		initDictionary();
		dictionary.put("postal-code", "postcode");
		
		initEndecryptFields();
		endecryptFields.add("buyerName");
		endecryptFields.add("country");
		endecryptFields.add("postcode");
		endecryptFields.add("tel");
		endecryptFields.add("buyerTel");
		endecryptFields.add("email");
		
	}
}
