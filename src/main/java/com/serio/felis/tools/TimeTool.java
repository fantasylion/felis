package com.serio.felis.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTool {
	
	public static final String FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	/**
	 * yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static String currentTime() {
		
		SimpleDateFormat dateFormater = new SimpleDateFormat(FORMAT);
		Date date=new Date();
		
		return dateFormater.format(date);
	}

}
