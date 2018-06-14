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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 用于存储customer-no 和 nebula 对象中ID的关联关系
 * @author zl.shi
 *
 */
public class MemberStore {
//	private static final Logger		logger					= LoggerFactory.getLogger(MemberStore.class);

	static final String symbol = "\\";
	static final String path   = System.getProperty("user.home") + symbol + ".data-hamal";
	static final String file   = "member.properties";
	
    Properties pro = new Properties();
    private static MemberStore memberStore;
    
    
	private MemberStore() {
		loadPropertie();
	}


	public static MemberStore getMemberStore() {
    	
    	if ( memberStore == null ) {
    		memberStore = new MemberStore();
    	}
    	
    	return memberStore;
    }
    
    
	static {
		File temp = new File(path);
		if ( !temp.exists() ) {
			temp.mkdirs();
		}
	}
	
	/**
	 * 设置关联关系
	 * @param key
	 * @param value
	 */
	public void put( String key, String value ) {
        // 存储  
        pro.setProperty( key, value );

        try {
			pro.store(new FileOutputStream( path + symbol + file ), "用户账号跟Id的关联");
		} catch (FileNotFoundException e) {
//			logger.error("Has exception",e);
		} catch (IOException e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 获取关联关系
	 * @param key
	 */
	public String get( String key ) {
		if ( key == null ) {
			return null;
		}
		return pro.getProperty(key); 
	}
	
	
	/**
	 * 加载文件
	 */
	public void loadPropertie() {
		try {
			
			File tempFile = new File( path + symbol + file );

			if ( tempFile.exists() ) {
				InputStream input = new FileInputStream(tempFile);
				pro.load(input);
			}
			
		} catch (IOException e) {
//			logger.error("Has exception",e);
		}
	}
	
    public static void main(String[] args) {
    	for ( int i = 0; i < 30000; i++ ) {
    		MemberStore.getMemberStore().put("LCCUST"+i, i+"");
    	}
	}

}
