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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.serio.felis.assembly.CouponCodeParticipleFactory;
import com.serio.felis.db.CouponMapping;
import com.serio.felis.db.MemberMapping;
import com.serio.felis.db.RecordCoupon;
import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.sql.maker.sequence.IndexCounter;
import com.serio.felis.tools.FilePool;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.xml.parser.CustomerParser;
import com.serio.felis.xml.parser.translater.CouponCodeDictionary;
import com.ximpleware.VTDNav;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperVTDGenHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import com.ximpleware.extended.XPathParseExceptionHuge;

/**
 * <p>生成SQL脚本文件，更新优惠券</p>
 * @author zl.shi
 *
 */
public class CouponSqlFactory extends AddressSqlFactory {
	
	
	private static final int START_INDEX = 1;
	
	// 解析器
	CustomerParser customer = new CustomerParser();
	
	// 将解析出来的数据放到这些集合中
	private Map<String, String> profile				 = new HashMap<String, String>();
	
	// 序列用于生成ID
	IndexCounter couponLogIndexCounter			=	new IndexCounter(START_INDEX);
	IndexCounter couponCodeIndexCounter			=	new IndexCounter(START_INDEX);

	
	// 文件池，可以自动拆分文件
	FilePool couponLogFile			= new FilePool("couponLog");
	FilePool couponCodeFile			= new FilePool("couponCode");


	// 高速sql文件创建
	SqlScriptFileSuperMaker couponLogMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker couponCodeMaker			=	new SqlScriptFileSuperMaker();
	

	public CouponSqlFactory() {}
	
	
	public CouponSqlFactory( int index ) {
		couponLogIndexCounter			=	new IndexCounter(index);
		couponCodeIndexCounter			=	new IndexCounter(index);
	}
	
	
	/**
	 * 循环解析文件
	 * @param vnh
	 * @param selecter
	 */
	public void parseLoop( VTDNavHuge vnh, String selecter ) {
		try {
			AutoPilotHuge aph = new AutoPilotHuge(vnh);
			
			aph.selectXPath(selecter);
			vnh.toElement(VTDNav.FIRST_CHILD);
			// 在customer层元素 循环
			do{
				customer.attrPutToMap(vnh, profile);
				parseChildEle(vnh);
				vnh.toElement(VTDNav.PARENT);
				
				createCouponLogSqlFile();
				createCouponcodeSqlFile();
				recordCouponLog();
				
				createNewCollection();
				
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			flushAllFile();
			
		} catch (NavExceptionHuge e) {
			e.printStackTrace();
		} catch (XPathParseExceptionHuge e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 刷新所有文件，保证所有数据都写入文件中，文件写入完毕后可能会有剩余数据没有写入，需要重新刷新下
	 */
	public void flushAllFile() {
		this.flushSqlFile( couponLogMaker, couponLogFile.getOneFile());
		this.flushSqlFile( couponCodeMaker, this.couponCodeFile.getOneFile());
	}
	
	
	
	/**
	 * 
	 * @param vnh
	 */
	public  void parseChildEle( VTDNavHuge vnh ) {
		try {
			if ( !vnh.toElement(VTDNav.FIRST_CHILD) ) {
				customer.parseToMap(vnh, profile);
				return;
			}
			
			// 在customer子元素中循环
			do{
				
				if ( vnh.matchElement("profile") ) {
					customer.parseChild(vnh, profile);
				}
				
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 构建优惠券
	 * @return
	 */
	public List<Map<String, String>> buildCouponRawmaterialData() {
		
		String coupon = profile.get("PreSaleCouponJSON");
		if ( coupon == null || coupon.isEmpty() ) {
			return null;
		}
		
		JSONObject json = JSON.parseObject(coupon);
		JSONArray obj = (JSONArray)json.get("couponList");
		ListIterator<Object> iterator = obj.listIterator();
		
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		while( iterator.hasNext() ) {
			JSONObject itera = (JSONObject)iterator.next();
			list.add(itera.toJavaObject(Map.class));
		}
		
		return list;
	}
	
	
	
	/**
	 * 创建优惠券和用户关联表
	 */
	public void createCouponLogSqlFile() {
		
		List<Map<String, String>> list = buildCouponRawmaterialData();
		if ( list == null ) {
			return;
		}
		
		for ( Map<String, String> map : list ){
			try {
				Map<String, String> couponCodeMap = rebuildCouponLogMap( map, couponLogIndexCounter.getIndexAndautoIncrement() );
				if ( couponCodeMap == null || couponCodeMap.isEmpty() ) {
					return;
				}
				createInsertSqlFile(this.couponLogMaker, couponCodeMap, "t_sys_coupon_log", couponLogFile.getOneFile() );
			} catch (IndexException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 记录关联关系
	 */
	public void recordCouponLog() {
		
		List<Map<String, String>> list = buildCouponRawmaterialData();
		if ( list == null ) {
			return;
		}
		
		String customerNo = profile.get("customer-no");
		for ( Map<String, String> map : list ){
			
			map.put( "customer_no",customerNo );
			map.put("order_code", map.get("order"));
			map.remove("order");
			try {
				String sql = createInsertSql( map, "t_so_coupon_record" );
				RecordCoupon.getRecordCoupon().doInsert(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	/**
	 * 创建优惠券SQL 主要用于更新优惠券状态
	 */
	public void createCouponcodeSqlFile() {
		
		List<Map<String, String>> list = buildCouponRawmaterialData();
		if ( list == null ) {
			return;
		}
		
		
		for ( Map<String, String> map : list ){
			CouponCodeParticipleFactory couponParticiple = new CouponCodeParticipleFactory();
			couponParticiple.buildRawmaterial(map);
			try {
				Map<String, String> couponCodeMap = rebuildCouponcodeMap( translaterMap(new CouponCodeDictionary(), couponParticiple.getBaseMap() ), couponCodeIndexCounter.getIndexAndautoIncrement() );
				
				if ( couponCodeMap != null ) {
					String couponCode = couponCodeMap.get("coupon_code");
					if ( couponCode != null && !couponCode.isEmpty() ) {
						couponCodeMap.remove("coupon_code");
						createUpdateSqlFile( couponCodeMaker, couponCodeMap, "coupon_code", couponCode, "t_prm_promotioncouponcode", couponCodeFile.getOneFile());
					}
				}
			} catch (IndexException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	/**
	 * PTS导入的券都默认激活，这里使用过的券，或者未激活的券，都需要将pts导入的数据设置成已使用或者未激活。这里选取出那些需要更新的券
	 * @param map
	 * @param index
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> rebuildCouponcodeMap( Map<String, String> map, int index ) throws IndexException {
		
		String isUsed = map.get("isused");
		if ( isUsed == null || isUsed.isEmpty() ) {
			return null;
		}
		
		// 使用过的
		if ( CouponCodeDictionary.USED_YES.equals(isUsed) ) {
			return map;
		}
		
		String activeMark = map.get("active_mark");
		if ( activeMark == null || activeMark.isEmpty() ) {
			return null;
		}
		
		// 未激活的
		if ( CouponCodeDictionary.ACTIVE_NO.equals(activeMark) ) {
			return map;
		}
		
		return null;
	}
	
	
	
	/**
	 * 重构coupon log 相关数据
	 * @param map
	 * @param index
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildCouponLogMap( Map<String, String> map, int index ) throws IndexException {
		
		String memberId = MemberMapping.getMemberMapping().getValue(profile.get("customer-no"));
		if ( memberId == null || memberId.isEmpty() ) {
			return null;
		}
		
		String couponCode = map.get("coupon");
		
		if ( couponCode == null || couponCode.isEmpty()) {
			return null;
		}
		
		Map<String, String> temp = new HashMap<String, String>();
		rebuildIndex(  temp, index );
		
		temp.put( "memberid", memberId );
		
		Map<String, String> couponMapping = CouponMapping.getMemberMapping().getMap(couponCode);
		if ( couponMapping != null ) {
			temp.putAll(couponMapping);
			return temp;
		}
		
		return null;
		
	}
	
	
	/**
	 * 用新的集合对象
	 */
	public void createNewCollection() {

		profile.clear();
		
	}
	
	@Override
	public void run(String inputFilePath) {
		String xpath = "/customer-list/customer";
		System.out.println("Start processing coupons of member...");
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile(inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
			System.out.println("Start loop coupons of member...");
	        VTDNavHuge vnh = vgh.getNav();
	        this.parseLoop( vnh, xpath );
		}
		
	}
	
	
	
	public static void main(String[] args) {
		String inputFilePath = findInputFilePath(args);
		CouponSqlFactory mem = new CouponSqlFactory();
		mem.run(inputFilePath);
	}
	
}
