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
package com.serio.felis.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PostgreSQLJDBC {
	
//	private static final Logger		logger					= LoggerFactory.getLogger(PostgreSQLJDBC.class);

	public static String dbname;
	public static String url;
	public static String username;
	public static String password;
	public static String driverName;
	public static String slant;
	
	static {
		try {
			String os = System.getProperty("os.name");  
			slant = os.toLowerCase().startsWith("win") ? "\\" : "/";
			
			InputStream input =  new BufferedInputStream(new FileInputStream(System.getProperty("user.dir") + slant + "config" + slant + "db.properties"));
			Properties pro = new Properties();
			pro.load(input);
			
			dbname     =  pro.getProperty("dbname");
			url        =  pro.getProperty("url") + dbname;
			username   =  pro.getProperty("username");
			password   =  pro.getProperty("password");
			driverName =  pro.getProperty("driverName");
			System.out.println("DataBase name: " + dbname);
		} catch (IOException e) {
		}
	}
	
	private Connection connection;
	private static PostgreSQLJDBC  postgrel;
	
	public PostgreSQLJDBC() {
		connection = this.initConnection();
	}
	
	
	public static PostgreSQLJDBC getPostgreSQLJDBC() {
		
		if ( postgrel == null ) {
			postgrel = new PostgreSQLJDBC();
		}
		
		return postgrel;
	}
	
	
    
    /**
     * 执行插入语句
     * @param sql
     */
    public void insert( String sql ) {
    	
    	try {
    		Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    
    /**
     * 执行查询语句
     * @param querySql
     * @return
     */
    public List<String[]> query( String querySql ) {
    	try {
			List<String[]> result = new ArrayList<String[]>();

        	PreparedStatement pstmt = (PreparedStatement)connection.prepareStatement(querySql);  
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();  
            String[] line = null;
            while (rs.next()) {
            	line = new String[col];
                for (int i = 1; i <= col; i++) {
                	line[i-1] = rs.getString(i);
                }
                result.add(line);
            }
            
            return result;
        } catch (SQLException e) {
        	System.out.println(querySql);
        	e.printStackTrace();
        }  
        return null; 
    }
    
    
    /**
     * 获取数据库链接，关闭自动提交
     * @return
     */
    public Connection initConnection() {
    	
    	try {
    		
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection( url, username, password );  
            connection.setAutoCommit(true); // 把自动提交  
            
//            logger.info("Database connection success.");
            
            return connection;
            
		} catch (Exception e) {
			System.exit(0);
		}
    	
        return null;
    }
    
    
    /**
     * 事物提交
     * @param c
     */
    public void commit( Connection c ){
    	try {
			c.commit();
		} catch (SQLException e) {
//			logger.error("Has exception",e);
		}  
    }
    
    
    /**
     * 关闭数据库连接
     * @param c
     */
    public void closeConnection( Connection c ) {
    	try {
			c.close();
		} catch (SQLException e) {
//			logger.error("Has exception",e);
		}
    }
    
    
    public void closeConnection() {
    	closeConnection( this.connection );
    }
    
}
