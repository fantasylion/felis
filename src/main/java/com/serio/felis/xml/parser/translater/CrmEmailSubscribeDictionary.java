package com.serio.felis.xml.parser.translater;

public class CrmEmailSubscribeDictionary extends Dictionary {
	
	/**
	 * Init the dictionary
	 */
	public CrmEmailSubscribeDictionary() {
		
		initDictionary();
		
	}
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		dictionary.put( "agreedPrivacyPolicyURL", "link" );
		dictionary.put( "isAgreedPrivacyPolicy", "agreed" );
		dictionary.put( "lastAgreedPrivacyPolicy", "create_time" );

	}
	
	/**
	 * 对值进行翻译
	 * @param keyWord
	 * @param value
	 * @return
	 */
	public String searchValue( String keyWord, String value ) {
		
		if ( "agreed".equalsIgnoreCase(keyWord) ) {
			if ("true".equalsIgnoreCase(value)) {
				return "t";
			} else {
				return "f";
			}
		}
		
		
		return value;
	}
}
