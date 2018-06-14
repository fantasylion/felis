package com.serio.felis.xml.parser.translater;

public class ReplyDictionary extends Dictionary {
	
	
	public ReplyDictionary() {
		
		initDictionary();
		
	}
	
	
	/**
	 * 初始化数据字典
	 */
	public void initDictionary() {
		
		dictionary.put("content", "reply");
		dictionary.put("submitTime", "reply_time");
		
	}
}
