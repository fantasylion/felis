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
package com.serio.felis.hamal;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.serio.felis.sql.maker.CouponSqlFactory;
import com.serio.felis.sql.maker.MemberSqlFactory;
import com.serio.felis.sql.maker.OrderSqlFactory;
import com.serio.felis.sql.maker.SqlFactory;
import com.serio.felis.sql.maker.review.RateAndReViewSqlFactory;
import com.serio.felis.sql.maker.review.ReviewVoteSqlFactory;
import com.serio.felis.sql.maker.review.TxtRateAndReViewSqlFactory;
import com.serio.felis.tools.TimeTool;


/**	
 * 启动入口
 * @author zl.shi
 *
 */
public class Stevedore {
	
	
	public static final String VM_UNICODE_KEY		= "file.encoding";
	
	public static final String VM_UNICODE_CUSTOM	= "file.encoding.custom";
	
	
	public static Map<String, Class> command = new HashMap<String, Class>(){
		private static final long serialVersionUID = 1L;
		{
	       put("order", OrderSqlFactory.class);
	       put("member", MemberSqlFactory.class);
	       put("review", RateAndReViewSqlFactory.class);
	       put("vote", ReviewVoteSqlFactory.class);
	       put("coupon", CouponSqlFactory.class);
	       put("review-txt", TxtRateAndReViewSqlFactory.class);
	    }
	};
	
	
	
	/**
	 * <li>arg[0] command</li>
	 * <li>arg[1] inputFilePath</li>
	 * <li>arg[2] index</li>
	 * @param args
	 */
	public static void main(String[] args) {
		
		if ( args.length < 3 ) {
			throw new RuntimeException("Please set command, inputFilePath and startID");
		}
		
		try {
			System.out.println("Process file: "+ args[1]);
			System.out.println("Index start with: " + args[2]);
			System.out.println("Start from: " + TimeTool.currentTime());
			
			Class<SqlFactory> commandObj = command.get(args[0]);
			Constructor<?> cons = commandObj.getConstructor(int.class);
			SqlFactory sqlFactory = (SqlFactory) cons.newInstance(Integer.parseInt(args[2]));
			
			sqlFactory.run(args[1]);
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace().toString());
		}
		System.out.println("End time: " + TimeTool.currentTime());
		System.out.println("processed.");
		
	}
	
	

}
