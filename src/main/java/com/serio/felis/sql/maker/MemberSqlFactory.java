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
import com.serio.felis.assembly.AddressParticipleFactory;
import com.serio.felis.assembly.CouponCodeParticipleFactory;
import com.serio.felis.assembly.CrmEmailSubscribeParticipleFactory;
import com.serio.felis.assembly.CustomerParticipleFactory;
import com.serio.felis.db.CouponMapping;
import com.serio.felis.db.MemberMapping;
import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.sql.maker.sequence.IndexCounter;
import com.serio.felis.tools.FilePool;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.xml.parser.CustomerParser;
import com.serio.felis.xml.parser.translater.AddressDictionary;
import com.serio.felis.xml.parser.translater.CouponCodeDictionary;
import com.serio.felis.xml.parser.translater.CrmEmailSubscribeDictionary;
import com.serio.felis.xml.parser.translater.CustomerDictionary;
import com.ximpleware.VTDNav;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperVTDGenHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import com.ximpleware.extended.XPathParseExceptionHuge;

/**
 * <p>生成SQL脚本文件，内容包括，t_mem_member、t_mem_personal_data、t_mem_contact、t_mem_cryptoguard、t_mem_conduct，文件位于：{@link MemberSqlFactory#fileOutputDir}。
 * 如果某项文件没有生成，一般情况是XML解析后没有对应的信息</p>
 * @author zl.shi
 *
 */
public class MemberSqlFactory extends AddressSqlFactory {
	
//	private static final Logger		logger					= LoggerFactory.getLogger(MemberSqlFactory.class);
	
	private static final int START_INDEX = 1;
	
	// 解析器
	CustomerParser customer = new CustomerParser();
	
	// 将解析出来的数据放到这些集合中
	private Map<String, String> credentials			 = new HashMap<String, String>();
	private Map<String, String> profile				 = new HashMap<String, String>();
	private List<Map<String, String>> addresses 	 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> externaProfile = new ArrayList<Map<String, String>>();
	
	// 序列用于生成ID
	IndexCounter memberIndexCounter				=	new IndexCounter(START_INDEX);
	IndexCounter addressIndexCounter			=	new IndexCounter(START_INDEX);
	IndexCounter emailSubscribeLogIndexCounter	=	new IndexCounter(START_INDEX);
	IndexCounter couponCodeIndexCounter			=	new IndexCounter(START_INDEX);
	IndexCounter couponLogIndexCounter			=	new IndexCounter(START_INDEX);
	
	// 文件池，可以自动拆分文件
	FilePool memberFile				= new FilePool("member");
	FilePool personDataFile			= new FilePool("personData");
	FilePool addressFile			= new FilePool("address");
	FilePool cryptoguardFile		= new FilePool("cryptoguard");
	FilePool conductFile			= new FilePool("conduct");
	FilePool emailSubscribeLogFile	= new FilePool("emailSubscribeLog");
	FilePool couponCodeFile			= new FilePool("couponCode");
	FilePool couponLogFile			= new FilePool("couponLog");
	
	// 高速sql文件创建
	SqlScriptFileSuperMaker memberMaker				=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker personDataMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker addressMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker cryptoguardMaker		=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker conductMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker emailSubscribeLogMaker	=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker couponCodeMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker couponLogMaker			=	new SqlScriptFileSuperMaker();
	
	public MemberSqlFactory() {}
	
	
	public MemberSqlFactory( int index ) {
		memberIndexCounter				=	new IndexCounter(index);
		addressIndexCounter				=	new IndexCounter(index);
		emailSubscribeLogIndexCounter	=	new IndexCounter(index);
		couponCodeIndexCounter			=	new IndexCounter(index);
		couponLogIndexCounter			=	new IndexCounter(index);
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
				
				createSqlFile();
				
				createNewCollection();
				
				// 放到最后自增，保证所有的memberId都统一
				memberIndexCounter.autoIncrease();
//				logger.debug("Finish one customer---------------------------");
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			flushAllFile();
			
		} catch (NavExceptionHuge e) {
			e.printStackTrace();
		} catch (XPathParseExceptionHuge e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建 sql 文件
	 */
	public void createSqlFile() {
		
		createMemberSqlFile();
		createPersonDataSqlFile();
		createAddressSqlFile();
		createCryptoguardSqlFile();
		createConductSqlFile();
		createCrmEmailSubscribeSqlFile();
		createCouponcodeSqlFile();// 在 pts 里面导入
		createCouponLogSqlFile();
		
	}
	
	
	/**
	 * 刷新所有文件，保证所有数据都写入文件中，文件写入完毕后可能会有剩余数据没有写入，需要重新刷新下
	 */
	public void flushAllFile() {
		this.flushSqlFile( memberMaker, this.memberFile.getOneFile());
		this.flushSqlFile( personDataMaker, this.personDataFile.getOneFile());
		this.flushSqlFile( addressMaker, this.addressFile.getOneFile());
		this.flushSqlFile( cryptoguardMaker, this.cryptoguardFile.getOneFile());
		this.flushSqlFile( conductMaker, this.conductFile.getOneFile());
		this.flushSqlFile( emailSubscribeLogMaker, this.emailSubscribeLogFile.getOneFile());
		this.flushSqlFile( couponCodeMaker, this.couponCodeFile.getOneFile());
		this.flushSqlFile( couponLogMaker, couponLogFile.getOneFile());
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
				
				if ( vnh.matchElement("credentials") ) {
					customer.parseChild(vnh, credentials);
				}
				if ( vnh.matchElement("profile") ) {
					customer.parseChild(vnh, profile);
				}
				if ( vnh.matchElement("addresses") ) {
					customer.parseGrandChild(vnh, addresses);
				}
				if ( vnh.matchElement("external-profiles") ) {
					customer.parseGrandChild(vnh, externaProfile);
				}
				
				customer.textPutToMap(vnh, profile);
				
//				logger.debug("----Finish one customer son------------");
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建 t_mem_member 表的sql文件
	 */
	public void createMemberSqlFile() {
		try {
			CustomerParticipleFactory customerFactory = buildRawmaterialData();
			
			Map<String, String> memberMap = rebuildMemberMap( translaterMap(new CustomerDictionary(), customerFactory.getMember() ), memberIndexCounter.getCurrentIndex() );
			createInsertSqlFile( this.memberMaker, memberMap, "t_mem_member", memberFile.getOneFile() );
			
			// 写入数据库用作customer_no 跟 member_id 关联
			MemberMapping post = MemberMapping.getMemberMapping();
			post.put(profile.get("customer-no"), memberIndexCounter.getCurrentIndexStr());
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建 t_mem_personal_data 表的sql文件
	 */
	public void createPersonDataSqlFile() {
		try {
			
			CustomerParticipleFactory customerFactory = buildRawmaterialData();
			
			Map<String, String> memberMap = rebuildIndex( translaterMap(new CustomerDictionary(), customerFactory.getPersonalData() ), memberIndexCounter.getCurrentIndex() );
			setVersion( memberMap );
			setThirdMobile(memberMap);
			
			// TODO personData 联系信息使其字段失效
			setFieldTestTag( memberMap, "third_mobile" );
			setFieldTestTag( memberMap, "email" );
			setFieldTestTag( memberMap, "mobile" );
			
			createInsertSqlFile( this.personDataMaker, memberMap, "t_mem_personal_data", personDataFile.getOneFile() );
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * 创建 t_mem_contact 表的sql文件
	 */
	public void createAddressSqlFile() {
		try {
			
			for ( Map<String, String> address : addresses ) {
				
				AddressParticipleFactory addressPartic = new AddressParticipleFactory();
				address = addressPartic.buildRawmaterial(address);
				setDeliver(address);
				Map<String, String> memberMap = rebuildBaseVerMap( translaterMap( new AddressDictionary(), address ), addressIndexCounter.getIndexAndautoIncrement() );
				memberMap.put("country_id", "1");
				memberMap.put("town_id", "0");
				
				// TODO 地址上的联系信息加tag
				setFieldTestTag( memberMap, "mobile" );
				setFieldTestTag( memberMap, "email" );
				setFieldTestTag( memberMap, "telphone" );
				
				createInsertSqlFile(this.addressMaker, memberMap, "t_mem_contact", addressFile.getOneFile() );
			}
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * 创建 t_mem_cryptoguard 表的sql文件
	 */
	public void createCryptoguardSqlFile() {
		try {
			
			CustomerParticipleFactory customerFactory = buildRawmaterialData();
			if ( customerFactory.getCryptoguard().isEmpty() ) {
				return;
			}
			Map<String, String> memberMap = rebuildBaseVerMap( translaterMap(new CustomerDictionary(), customerFactory.getCryptoguard() ), memberIndexCounter.getCurrentIndex() );
			createInsertSqlFile(this.cryptoguardMaker, memberMap, "t_mem_cryptoguard", cryptoguardFile.getOneFile() );
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建 t_mem_conduct 表的sql文件
	 */
	public void createConductSqlFile() {
		try {
			
			CustomerParticipleFactory customerFactory = buildRawmaterialData();
			if ( customerFactory.getConduct().isEmpty() ) {
				return;
			}
			Map<String, String> memberMap = rebuildConductMap( translaterMap(new CustomerDictionary(), customerFactory.getConduct() ), memberIndexCounter.getCurrentIndex() );
			createInsertSqlFile(this.conductMaker, memberMap, "t_mem_conduct", conductFile.getOneFile() );
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建隐私条款表
	 */
	public void createCrmEmailSubscribeSqlFile() {
		
		CrmEmailSubscribeParticipleFactory crmemail = new CrmEmailSubscribeParticipleFactory();
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(profile);
		crmemail.buildRawmaterial(originTables);
		
		if ( crmemail.getEmailSubscribeLog().size() <= 0 ) {
			return;
		}
		
		try {
			
			Map<String, String> memberMap = rebuildBaseMap( translaterMap(new CrmEmailSubscribeDictionary(), crmemail.getEmailSubscribeLog() ), emailSubscribeLogIndexCounter.getIndexAndautoIncrement() );
			
			// 如果连接是空或者连接的长度超出255不进行迁移
			String link = memberMap.get("link");
			if ( link == null || link.isEmpty() || link.length() > 255 ) {
				return;
			}
			
			createInsertSqlFile(emailSubscribeLogMaker, memberMap, "t_crm_email_subscribe_log", emailSubscribeLogFile.getOneFile() );
			
		} catch (IndexException e) {
			e.printStackTrace();
		}
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
	 * 重构coupon log 相关数据
	 * @param map
	 * @param index
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildCouponLogMap( Map<String, String> map, int index ) throws IndexException {
		
		Map<String, String> temp = new HashMap<String, String>();
		rebuildIndex(  temp, index );
		temp.put( "memberid", this.memberIndexCounter.getCurrentIndexStr() );
		
		String couponCode = map.get("coupon");
		
		if ( couponCode == null || couponCode.isEmpty()) {
			return null;
		}
		
		Map<String, String> couponMapping = CouponMapping.getMemberMapping().getMap(couponCode);
		if ( couponMapping != null ) {
			temp.putAll(couponMapping);
			return temp;
		}
		
		return null;
		
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
	
	
	public CustomerParticipleFactory buildRawmaterialData() {
		CustomerParticipleFactory customerFactory = new CustomerParticipleFactory();
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(credentials);
		originTables.add(profile);
		
		customerFactory.buildRawmaterial(originTables);
		customerFactory.buildRawmaterial(externaProfile);
		
		return customerFactory;
	}
	
	
	
	public Map<String, String> rebuildMemberMap( Map<String, String> map, int index ) throws IndexException {
		
		rebuildIndex(map, index);
		
		map.put("group_id", index+"");
		map.put("create_time", profile.get("creation-date"));
		if ( map.get("source") == null ) {
			map.put("source", "1");
		}
		map.put("lifecycle","0");
		map.put("type", "1");// 表示老数据
		setVersion( map );
		
		setEmailLowerCase( map );
		
		// TODO member 表联系信息标记tag
		setFieldTestTag( map, "login_mobile" );
		setFieldTestTag( map, "login_email" );
		return map;
	}
	
	
	/**
	 * 将登陆邮箱设置成小写
	 * @param map
	 */
	public void setEmailLowerCase( Map<String, String> map ) {
		
		String loginEmail = map.get("login_email");
		if ( loginEmail != null && !loginEmail.isEmpty() ) {
			map.put("login_email", loginEmail.toLowerCase());
		}
		
	}
	
	
	/**
	 * 设置第三方注册手机号
	 * @param map
	 */
	public void setThirdMobile(Map<String, String> map) {
		String thirdMobile = map.get("externalMobilePhone");
		if ( thirdMobile != null ) {
			map.put("third_mobile", thirdMobile);// 第三方登录，手机验证后存放手机号的字段
		}
		map.remove("externalMobilePhone");
	}
	
	
	public Map<String, String> rebuildConductMap( Map<String, String> map, int index ) throws IndexException {
		
		rebuildIndex(map, index);
		
		setVersion( map );
		
		return map;
	}
	
	
	/**
	 * 
	 * @param map
	 * @param index
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> rebuildBaseVerMap( Map<String, String> map, int index ) throws IndexException {
		
		map = rebuildBaseMap( map, index );
		setVersion( map );
		return map;
	}
	
	
	public Map<String, String> rebuildBaseMap( Map<String, String> map, int index ) throws IndexException {
		
		rebuildIndex(  map, index );
		map.put("member_id", this.memberIndexCounter.getCurrentIndex()+"");
		return map;
	}
	
	
	
	/**
	 * 用新的集合对象
	 */
	public void createNewCollection() {

		credentials.clear();
		profile.clear();
		
		addresses.clear();
		externaProfile.clear();
		
	}
	
	@Override
	public void run(String inputFilePath) {
		String xpath = "/customer-list/customer";
		System.out.println("Start processing members...");
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile(inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
			System.out.println("Start loop members...");
	        VTDNavHuge vnh = vgh.getNav();
	        this.parseLoop( vnh, xpath );
		}
		
	}
	
	
	
	public static void main(String[] args) {
		String inputFilePath = findInputFilePath(args);
		MemberSqlFactory mem = new MemberSqlFactory();
		mem.run(inputFilePath);
	}
	
}
