/**
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.serio.felis.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zl.shi
 *
 */
public class CommonTools {
	
	
	/**
	 * 验证邮箱
	 * @param string
	 * @return
	 */
	public static boolean isEmail( String string ) {
        
		if (string == null)
            return false;
        
        String regEx1 = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(string);
        
        return m.matches();
    }
	
	
	/**
	 * 验证手机号
	 * @param phone
	 * @return
	 */
    public static boolean isPhone( String phone ) {
    	
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phone.length() != 11){
            return false;
        }
        
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
    
    
    /**
     * 整型数字，小数点后可以带零
     * @param s
     * @return
     */
    public final static boolean isNumeric(String s) {

    	if ( s == null || "".equals(s.trim()) ) {
    		return false;
        }
    	
    	return s.matches("^[0-9]+\\.?[0]*$");
    	
    }
    
    
    /**
     * 将整形数字，小数点后带零，转成int <br>
     * e.g. (1.0 -> 1) ( 1.2 -> 1)
     * @param value
     * @return
     */
    public final static int toInt( String value ) {
    	
		if ( isNumeric( value ) ) {
			Number num = Float.parseFloat(value);
			return num.intValue();
		}
		
		throw new RuntimeException(value + " not number.");
    }
    
    
    /**
     * 将数字字符转成正数，-15 to 15, 原理：直接去掉“-”符号
     * @param value
     * @return
     */
    public final static String toPositiveNumberStr( String value ) {
    	if ( value == null ) {
    		return value;
    	}
    	
    	 return value.replace("-", ""); 
    }
    
    /**
     * 判断字符是否为零的数字<br>
     * 0.0  return true<br>
     * 0.00 return true<br>
     * 00.0 return true<br>
     * @param value
     * @return
     */
    public final static boolean isZero( String value ) {
    	if ( value == null ) {
    		return false;
    	}
    	
    	 return value.matches("^[0]+.[0]*$");
    }
    
    
    /**
     * 获取斜线 windows 环境使用\\ linux 环境使用 /
     * @return
     */
	public final static  String getSlant() {
		String os = System.getProperty("os.name");  
		return os.toLowerCase().startsWith("win") ? "\\" : "/";
	}
	
}
