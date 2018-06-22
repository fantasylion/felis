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
package com.serio.felis.sql.maker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.tools.SqlScriptFileMaker;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.tools.TimeTool;


/**
 * @author zl.shi
 *
 */
public abstract class SqlFactory {
	

	public static String INSERT_PREFIX		= "INSERT INTO";
	
	public static String UPDATE_PREFIX		= "UPDATE";
	
	
	public static String SPACING 			= " ";
	
	public static String LEFT_HALF_BRACKET	= "(";
	public static String RIGHT_HALF_BRACKET = ")";
	public static String SEMICOLON			= ";";
	public static String COMMA				= ",";
	public static String LINE_FEED			= "\r\n";
	
	public static String QUOTATION_MARK		= "'";
	
	public static String VALUE_GRAMMAR		= "VALUES";
	
	/** 是否需要打标签*/
	public static boolean MAKETAG = "true".equalsIgnoreCase(System.getProperty("damal.maketag"));
	/** 标签*/
	public static String TAG = System.getProperty("damal.tag");
	
	
	public abstract void run( String inputFilePath );
	
	
	/**
	 * 生成sql 语句并写到文件中
	 * @param sqlPath
	 * @param sql
	 */
	public void createSqlFile( String sqlPath, String sql ) {
		
		File file = new File(sqlPath);
		
		if ( !file.exists() ) {
			try {
				file.createNewFile();
			} catch (IOException e) {
//				logger.error("Has exception",e);
			}
		}
		
		SqlScriptFileMaker.getSqlScriptFileMaker().createSqlScript( sql, file);
	}
	
	
	/**
	 * 根据 rawmaterial 生成sql 语句并写到文件中 
	 * @param rawmaterial
	 * @param tableName
	 * @param sqlPath
	 */
	public void createSqlFile( Map<String, String> rawmaterial, String tableName, String sqlPath ) {
		
		createSqlFile( sqlPath, this.createInsertSql( rawmaterial, tableName )  + LINE_FEED );
		
	}
	
	
	/**
	 * 创建插入语句，并将插入语句写入到文件中（效率低）
	 * @param rawmaterial
	 * @param tableName
	 * @param file
	 */
	public void createInsertSqlFile( Map<String, String> rawmaterial, String tableName, File file ) {
		SqlScriptFileMaker.getSqlScriptFileMaker().createSqlScript(this.createInsertSql( rawmaterial, tableName )  + LINE_FEED, file); // 性能不高
	}
	
	
	/**
	 * 创建插入语句，并将插入语句写入到文件中（效率高），执行完成后需要调用{@link #flushSqlFile(SqlScriptFileSuperMaker, File)}
	 * @param maker
	 * @param rawmaterial
	 * @param tableName
	 * @param file
	 */
	public void createInsertSqlFile( SqlScriptFileSuperMaker maker, Map<String, String> rawmaterial, String tableName, File file ) {
		maker.createSqlScriptSuper(this.createInsertSql( rawmaterial, tableName )  + LINE_FEED, file);
	}
	
	
	/**
	 * 创建更新语句，并将语句写入到文件中（效率低）
	 * @param rawmaterial
	 * @param tableName
	 * @param file
	 */
	public void createUpdateSqlFile( Map<String, String> rawmaterial, String conditionKey, String conditionValue, String tableName, File file ) {
		SqlScriptFileMaker.getSqlScriptFileMaker().createSqlScript(this.createUpdateSql( rawmaterial, conditionKey, conditionValue, tableName ) + LINE_FEED, file); // 性能不高
	}
	
	
	/**
	 * 创建更新语句，并将语句写入到文件中（效率高），执行完成后需要调用{@link #flushSqlFile(SqlScriptFileSuperMaker, File)}
	 * @param maker
	 * @param rawmaterial
	 * @param conditionKey
	 * @param conditionValue
	 * @param tableName
	 * @param file
	 */
	public void createUpdateSqlFile( SqlScriptFileSuperMaker maker, Map<String, String> rawmaterial, String conditionKey, String conditionValue, String tableName, File file ) {
		maker.createSqlScriptSuper(this.createUpdateSql( rawmaterial, conditionKey, conditionValue, tableName ) + LINE_FEED, file);
	}
	
	
	/**
	 * 将最后一次缓存的数据写入到文件中
	 * @param file
	 */
	public void flushSqlFile( SqlScriptFileSuperMaker maker, File file ) {
		maker.flushSuperSqlScript(file);
	}
	
	
	
	/**
	 * 构建insert语法，rawmaterial 结构必须为 key=字段名，value=字段值
	 * @param rawmaterial
	 * @param tableName
	 * @return
	 */
	public String createInsertSql( Map<String, String> rawmaterial, String tableName ) {
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		Set<Entry<String, String>> entrys = rawmaterial.entrySet();
		for ( Entry<String, String> entry : entrys ) {
			keys.add(entry.getKey());
			values.add(entry.getValue());
		}
		
		return createInsertSql( keys, values, tableName );
	}
	
	
	
	/**
	 * 构建insert语法，rawmaterial 结构必须为 key=字段名，value=字段值
	 * @param rawmaterial
	 * @param tableName
	 * @return
	 */
	public String createUpdateSql( Map<String, String> rawmaterial, String conditionKey, String conditionValue, String tableName ) {
		
		List<String> keys = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		Set<Entry<String, String>> entrys = rawmaterial.entrySet();
		for ( Entry<String, String> entry : entrys ) {
			keys.add(entry.getKey());
			values.add(entry.getValue());
		}
		
		List<String> conditionKeys = new ArrayList<String>();
		List<String> conditionValues = new ArrayList<String>();
		conditionKeys.add(conditionKey);
		conditionValues.add(conditionValue);
		
		return this.createUpdateSql(keys, values, conditionKeys, conditionValues, tableName);
	}
	
	
	
	/**
	 * 构建insert语法,通过字段名集合与字段值集合去构建语法。
	 * @param keys
	 * @param values
	 * @param tableName
	 * @return
	 */
	public String createInsertSql( List<String> keys, List<String> values, String tableName ) {
		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append(INSERT_PREFIX);
		sqlStatement.append(SPACING);
		sqlStatement.append(tableName);
		sqlStatement.append(SPACING);
		
		syntaxFragment( sqlStatement, keys, "" );
		
		sqlStatement.append(SPACING);
		sqlStatement.append(VALUE_GRAMMAR);
		sqlStatement.append(SPACING);
		
		syntaxFragment( sqlStatement, values, QUOTATION_MARK );
		sqlStatement.append(SEMICOLON);
		
		return sqlStatement.toString();
	}
	
	
	/**
	 * 构建update语法,通过字段名集合与字段值集合去构建语法。
	 * @param keys
	 * @param values
	 * @param tableName
	 * @return
	 */
	public String createUpdateSql( List<String> keys, List<String> values, List<String> conditionKeys, List<String> conditionValues, String tableName ) {
		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append(UPDATE_PREFIX);
		sqlStatement.append(SPACING);
		sqlStatement.append(tableName);
		sqlStatement.append(SPACING);
		
		sqlStatement.append("SET");
		sqlStatement.append(SPACING);
		updateSyntaxFragment( sqlStatement, keys, values, ",");
		
		sqlStatement.append(SPACING);
		sqlStatement.append("WHERE");
		sqlStatement.append(SPACING);
		
		updateSyntaxFragment( sqlStatement, conditionKeys, conditionValues, "AND");
		
		sqlStatement.append(SEMICOLON);
		
		return sqlStatement.toString();
	}
	
	
	/**
	 * 构建一个格式为 "('test1', 'test2', 'test3')"的字符串，用于设置申明字段名或者字段值
	 * @param sqlStatement
	 * @param list	字段名或者字段值
	 */
	public void syntaxFragment( StringBuilder sqlStatement, List<String> list, String bothSides ) {
		
		sqlStatement.append(LEFT_HALF_BRACKET);
		
		for ( int i = 0; i < list.size(); i++ ) {
			
			addbothSides( handleSpecialStr(list.get(i)), sqlStatement, bothSides );
			
			if ( i+1 != list.size() ) {
				sqlStatement.append(",");
			}
		}
		
		sqlStatement.append(RIGHT_HALF_BRACKET);
		
	}
	
	
	/**
	 * 给值得两边加符号，成为 'test1'
	 * @param str				需要追加的值
	 * @param sqlStatement		原语句，后面的值会追加到这个值之后
	 * @param bothSides			两边需要添加的符号
	 */
	public void addbothSides( String str, StringBuilder sqlStatement, String bothSides ) {
		if ( null == str ) {
			sqlStatement.append(str);
		} else {
			sqlStatement.append(bothSides);
			sqlStatement.append(str);
			sqlStatement.append(bothSides);
		}
	}
	
	
	/**
	 * 构建一个格式为 key1 = 'test1', key2 = 'test2', key3 = 'test3'的字符串，用于设置申明字段名或者字段值
	 * @param sqlStatement
	 * @param list	字段名或者字段值
	 */
	public void updateSyntaxFragment( StringBuilder sqlStatement, List<String> keys, List<String> values, String bothSides ) {
		
		for ( int i = 0; i < keys.size(); i++ ) {
			sqlStatement.append(keys.get(i));
			sqlStatement.append("=");
			sqlStatement.append("'");
			sqlStatement.append(values.get(i));
			sqlStatement.append("'");
			if ( i < keys.size()-1 ) {
				sqlStatement.append(bothSides);
			}
		}
		
	}
	
	
	/**
	 * 处理特殊字符
	 * @param value
	 * @return
	 */
	public String handleSpecialStr( String value ) {
		
		if ( value == null ) {
			return value;
		}
		
		value = value.replace("'", "");
		
		return value.replace("\\", "");
	}
	
	
	/**
	 * 
	 * @param map
	 * @param index
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> rebuildIndex( Map<String, String> map, int index ) throws IndexException {
		if ( index <= 0 ) {
			throw new IndexException("ID can not less than 0.");
		}
		
		map.put("ID", index+"");
		
		return map;
	}
	
	
	
	/**
	 * 设置版本号
	 * @param map
	 */
	public void setVersion( Map<String, String> map ) {
		
		map.put("version", TimeTool.currentTime());
		
	}
	
	
	/**
	 * 给手机号、邮箱等字段加上 Test 标签，使其失效
	 * @param map
	 * @param mobileKey
	 */
	public void setFieldTestTag( Map<String, String> map, String key ) {

		String value = map.get(key);
		if ( value != null && !value.isEmpty() && MAKETAG ) {
			map.put( key, "test-"+map.get(key)+"-test" );
		}
	}
	
}
