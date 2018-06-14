package com.serio.felis.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * 高效率的 sql 写入工具
 * @author zl.shi
 *
 */
public class SqlScriptFileSuperMaker {
//	private static final Logger		logger					= LoggerFactory.getLogger(SqlScriptFileSuperMaker.class);

	// sql buffer
	private List<String> sqls = new ArrayList<String>();
	
	// The max buffer count
	private static final int BUFFER_SIZE = 10000;
	
	
	/**
	 * 通过缓存提高文件写入效率，执行效率最高，需要调用者在函数执行完后，单独调用一次{@link #flushSuperSqlScript(File)}
	 * @param sql
	 * @param file
	 */
	public void createSqlScriptSuper( String sql, File file ) {
		sqls.add(sql);
		if ( sqls.size() >= BUFFER_SIZE ) {
			flushSuperSqlScript( file );
		}
	}
	
	
	/**
	 * 将缓存中的数据写入文件中
	 * @param sql
	 * @param file
	 */
	public void flushSuperSqlScript( File file ) {
		if ( !sqls.isEmpty() ) {
			createSqlScriptSuper( sqls, file );
			sqls.clear();
		}
	}
	
	
	/**
	 * 
	 * 使用缓存生成sql脚本，减少IO量提高效率
	 * @param sql
	 * @param file
	 */
	public void createSqlScriptSuper( List<String> sqls, File file ) {
	
		BufferedWriter out = null;
		try {
			
			out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file, true)) );
			StringBuilder sqlBuffer = new StringBuilder();
			for ( String sql : sqls ) {
				sqlBuffer.append(sql);
			}
			out.write( sqlBuffer.toString() );
			
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
	
}
