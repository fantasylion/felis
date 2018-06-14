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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.serio.felis.sql.maker.SqlFactory;


/**
 * @author zl.shi
 *
 */
public class SqlScriptFileMaker {
//	private static final Logger		logger					= LoggerFactory.getLogger(SqlScriptFileMaker.class);

	
	private static SqlScriptFileMaker sqlScriptFileMaker;
	
	private SqlScriptFileMaker() {}
	
	// writer default
	private BufferedWriter out = null;
	
	
	public static SqlScriptFileMaker getSqlScriptFileMaker() {
		
		if ( null != sqlScriptFileMaker ) {
			return sqlScriptFileMaker;
		}
		
		sqlScriptFileMaker = new SqlScriptFileMaker();
		return sqlScriptFileMaker;
		
	}
	
	
	/**
	 * 创建sql脚本，如果只是少量的IO操作可以使用这个函数
	 * @param sql
	 * @param file
	 */
	public void createSqlScript( String sql, File file ) {
		
		BufferedWriter out = null;
		try {
			
			out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file, true)) );
			out.write( sql );
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
//				logger.error("Has exception",e);
			}
		}
	}
	
	
	/**
	 * 
	 * 创建sql脚本，需要重新调用{@link #closeWriter()}去关闭流，执行效率比较{@link #createSqlScript(String, File)}高一点，效果不明显
	 * @param sql
	 * @param file
	 */
	public void createSqlScriptEfficient( String sql, File file ) {
		
		try {
			
			if ( out == null ) {
				out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file, true)) );
			}
			
			out.write( sql );
			out.flush();
			
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 关闭写入
	 */
	public void closeWriter() {
		try {
			if ( out != null ) {
				out.close();
			}
		} catch (IOException e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	public static void main(String[] args) {
		String sql = "INSERT INTO testTable ('LOGIC_DELETE','IS_RECEIVE_EMAIL','UPDATE_TIME','EMAIL','MOBILE_VERIFICATION','TENANT_CODE','USER_CODE','ACCOUNT','STATUS','COUNT_PASSWORD_ERROR','PASSWORD','LAST_CHANGE_PWD','LAST_LOGIN_TIME','LASTEST_PASSWORD','USER_TYPE','UPDATER_USER','SOURCE','LAST_LOGIN_IP','ID','CREATE_TIME','IS_RECEIVE_MSG','EMAIL_VERIFICATION','MOBILE','CREATER_USER') VALUES ('LETE_value','MAIL_value','TIME_value','MAIL_value','TION_value','CODE_value','CODE_value','OUNT_value','ATUS_value','RROR_value','WORD_value','_PWD_value','TIME_value','WORD_value','TYPE_value','USER_value','URCE_value','N_IP_value','ID_value','TIME_value','_MSG_value','TION_value','BILE_value','USER_value');";
		File file = new File("C:/Users/zhengliang.shi/Documents/test.sql");
		
		for ( int i = 0; i < 10; i++ ) {
			SqlScriptFileMaker.getSqlScriptFileMaker().createSqlScript(sql  + SqlFactory.LINE_FEED, file);
		}
	}
}
