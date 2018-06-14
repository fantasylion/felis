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
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.serio.felis.assembly.OrderAddressParticipleFactory;
import com.serio.felis.assembly.OrderLineParticipleFactory;
import com.serio.felis.assembly.OrderParticipleFactory;
import com.serio.felis.assembly.OrderRequestParticipleFactory;
import com.serio.felis.assembly.PayInfoLogParticipleFactory;
import com.serio.felis.assembly.PayInfoParticipleFactory;
import com.serio.felis.db.MemberMapping;
import com.serio.felis.db.OrderLine;
import com.serio.felis.db.PostgreSQLJDBC;
import com.serio.felis.hamal.common.SalesOrder;
import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.hamal.exception.SqlFactoryException;
import com.serio.felis.sql.maker.sequence.IndexCounter;
import com.serio.felis.tools.CommonTools;
import com.serio.felis.tools.FilePool;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.tools.TimeTool;
import com.serio.felis.xml.parser.OrderParser;
import com.serio.felis.xml.parser.translater.OrderAddressDictionary;
import com.serio.felis.xml.parser.translater.OrderDictionary;
import com.serio.felis.xml.parser.translater.OrderLineDictionary;
import com.serio.felis.xml.parser.translater.PayInfoDictionary;
import com.ximpleware.VTDNav;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperVTDGenHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import com.ximpleware.extended.XPathParseExceptionHuge;

/**
 * @author zl.shi
 *
 */
public class OrderSqlFactory extends AddressSqlFactory {
	
//	private static final Logger		logger					= LoggerFactory.getLogger(OrderSqlFactory.class);

	private static final int START_INDEX = 1;
	
	// 处理order的xml
	OrderParser orderParser = new OrderParser();

	// 解析后数据存放的位置
	Map<String, String> order		= new HashMap<String, String>();
	Map<String, String> customer	= new HashMap<String, String>();
	Map<String, String> status		= new HashMap<String, String>();
	Map<String, String> shipments	= new HashMap<String, String>();
	Map<String, String> totals		= new HashMap<String, String>();
	Map<String, String> payments	= new HashMap<String, String>();
	
	List<Map<String, String>> productLineitems  = new ArrayList<Map<String, String>>();
	List<Map<String, String>> shippingLineitems = new ArrayList<Map<String, String>>();
	
	// ID 序列
	IndexCounter orderIndexCounter     		 =	 new IndexCounter(START_INDEX);
	IndexCounter orderRequestIndexCounter	 =	 new IndexCounter(START_INDEX);
	IndexCounter orderLineIndexCounter 		 =	 new IndexCounter(START_INDEX);
	IndexCounter payInfoIndexCounter   		 =	 new IndexCounter(START_INDEX);
	IndexCounter payInfoLogIndexCounter 	 =	 new IndexCounter(START_INDEX);
	IndexCounter orderAdrIndexCounter		 =	 new IndexCounter(START_INDEX);
	IndexCounter orderLogisticIndexCounter   =	 new IndexCounter(START_INDEX);
	
	// 文件池
	FilePool orderFile			= new FilePool("order");
	FilePool orderRequestFile	= new FilePool("orderRequest");
	FilePool orderLineFile		= new FilePool("orderLine");
	FilePool payInfoFile		= new FilePool("payInfo");
	FilePool payInfoLogFile		= new FilePool("payInfoLog");
	FilePool orderAddressFile	= new FilePool("orderAddress");
	FilePool orderLogisticFile	= new FilePool("orderLogistic");
	
	// 高速sql文件创建
	SqlScriptFileSuperMaker orderMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker orderRequestMaker	=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker orderLineMaker		=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker payInfoMaker		=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker payInfoLogMaker		=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker orderAddressMaker	=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker orderLogisticMaker	=	new SqlScriptFileSuperMaker();

	
	public OrderSqlFactory() {}
	
	public OrderSqlFactory( int index ) {
		orderIndexCounter     		 =	 new IndexCounter(index);
		orderRequestIndexCounter	 =	 new IndexCounter(index);
		orderLineIndexCounter 		 =	 new IndexCounter(index);
		payInfoIndexCounter   		 =	 new IndexCounter(index);
		payInfoLogIndexCounter 		 =	 new IndexCounter(index);
		orderAdrIndexCounter		 =	 new IndexCounter(index);
		orderLogisticIndexCounter    =	 new IndexCounter(index);
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
			// 在order层元素 循环
			do{
				orderParser.attrPutToMap(vnh, order);
				parseChildEle(vnh);
				vnh.toElement(VTDNav.PARENT);
				
				createSqlFile();
				
				autoAddIndex();
				
				createNewCollection();
				
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			flushAllFile();
		} catch (NavExceptionHuge e) {
			e.printStackTrace();
		} catch (XPathParseExceptionHuge e) {
			e.printStackTrace();
		} finally {
			PostgreSQLJDBC.getPostgreSQLJDBC().closeConnection();
		}
	}
	
	
	/**
	 * 创建 sql 文件
	 */
	public void createSqlFile() {
		
		Map<String, String> orderArg = null;
		List<Map<String, String>> orderLinesArg = null;
		Map<String, String> payInfoArg = null;
		Map<String, String> payInfoLogArg = null;
		Map<String, String> orderAddrArg = null;
		Map<String, String> orderLogisticsArg = null;
		Map<String, String> orderRequestArg = null;
		
		try {

			orderArg = createOrderSqlFile();
			orderRequestArg = createOrderRequestSqlFile( orderArg );
			orderLinesArg = createOrderLineSqlFile();
			payInfoArg = createPayInfoSqlFile();
			payInfoLogArg = createPayInfoLogSqlFile();
			orderAddrArg = createOrderAddressSqlFile();
			orderLogisticsArg = createOrderLogisticsSqlMap();
			
			createInsertSqlFile( this.orderMaker, orderArg, "t_so_salesorder", orderFile.getOneFile() );
			createInsertSqlFile( this.orderRequestMaker, orderRequestArg, "t_so_salesorder_request", orderRequestFile.getOneFile() );
			for ( Map<String, String> map : orderLinesArg ) {
				createInsertSqlFile( this.orderLineMaker, map, "t_so_orderline", orderLineFile.getOneFile() );
			}
			createInsertSqlFile( this.payInfoMaker, payInfoArg, "t_so_payinfo", payInfoFile.getOneFile() );
			createInsertSqlFile( this.payInfoLogMaker, payInfoLogArg, "t_so_payinfo_log", payInfoLogFile.getOneFile() );
			createInsertSqlFile( this.orderAddressMaker, orderAddrArg, "t_so_consignee", orderAddressFile.getOneFile() );
			if ( orderLogisticsArg != null )
				createInsertSqlFile( this.orderLogisticMaker, orderLogisticsArg, "t_so_logistics", orderLogisticFile.getOneFile() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 订单请求记录
	 * @param orderArg
	 * @return
	 */
	private Map<String, String> createOrderRequestSqlFile(Map<String, String> orderArg) {
		
		OrderRequestParticipleFactory or = new OrderRequestParticipleFactory();
		
		or.buildRawmaterial(orderArg);
		Map<String, String> map = or.getBaseMap();
		try {
			rebuildIndex(map, this.orderRequestIndexCounter.getIndexAndautoIncrement());
			map.put("order_id", this.orderIndexCounter.getCurrentIndexStr());
			map.put("count", "1");
			map.put("result", "t");
			setVersion(map);
		} catch (IndexException e) {
			e.printStackTrace();
		}
		
		return map;
	}

	/**
	 * 将需要自增的index +1
	 */
	public void autoAddIndex() {
		orderIndexCounter.autoIncrease();
		payInfoIndexCounter.autoIncrease();
	}
	
	/**
	 * 刷新所有文件
	 */
	public void flushAllFile() {
		
		this.flushSqlFile( this.orderMaker, this.orderFile.getOneFile() );
		this.flushSqlFile( this.orderRequestMaker, this.orderRequestFile.getOneFile() );
		this.flushSqlFile( this.orderLineMaker, this.orderLineFile.getOneFile());
		this.flushSqlFile( this.payInfoMaker, this.payInfoFile.getOneFile());
		this.flushSqlFile( this.orderAddressMaker, this.orderAddressFile.getOneFile() );
		this.flushSqlFile( this.payInfoLogMaker, this.payInfoLogFile.getOneFile());
		this.flushSqlFile( this.orderLogisticMaker, this.orderLogisticFile.getOneFile());
	}
	
	
	/**
	 * 用新的集合对象
	 */
	public void createNewCollection() {

		shipments.clear();
		order.clear();
		customer.clear();
		productLineitems.clear();
		
	}
	
	
	/**
	 * 进到order里面
	 * @param vnh
	 */
	public  void parseChildEle( VTDNavHuge vnh ) {
		try {
			if ( !vnh.toElement(VTDNav.FIRST_CHILD) ) {
				orderParser.parseToMap(vnh, order);
				return;
			}
			
			// 在order子元素中循环
			do{
				if ( vnh.matchElement("order") ) {
					orderParser.parseChild(vnh, order);
				} else if ( vnh.matchElement("customer") ) {
					orderParser.parseChild(vnh, order);
				} else if ( vnh.matchElement("product-lineitems") ) {
					orderParser.parseGrandChild(vnh, productLineitems);
				} else if ( vnh.matchElement("shipments") ) {
					orderParser.parseAllChild(vnh, shipments);
				} else if ( vnh.matchElement("status") ) {
					orderParser.parseAllChild(vnh, order);
				} else if ( vnh.matchElement("payments") ) {
					orderParser.parseAllChild(vnh, order);
				} else if ( vnh.matchElement("totals") ) {
					parseTotals( vnh );
				} else if ( vnh.matchElement("custom-attributes") ) {
					orderParser.parseCustomChilds(vnh, order);
					vnh.toElement(VTDNav.PARENT);
				} else {
					orderParser.textPutToMap(vnh, order);
				}
				
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
				
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 解析totals中的数据
	 * @param vnh
	 */
	public void parseTotals( VTDNavHuge vnh ) {
		try {
			vnh.toElement(VTDNav.FIRST_CHILD);
			do{
				if ( vnh.matchElement("shipping-total") ) {
					findGrossPrice( vnh, "shipping-total" );
				}
				
				if ( vnh.matchElement("adjusted-shipping-total") ) {
					findGrossPrice( vnh, "adjusted-shipping-total" );
				}
				
				if ( vnh.matchElement("merchandize-total") ) {
					findGrossPrice( vnh, "merchandize-total" );
					findAdjustmentsGrossPrice( vnh, "discount" );
				}
				
				if ( vnh.matchElement("adjusted-merchandize-total") ) {
					findGrossPrice( vnh, "adjusted-merchandize-total" );
				}
				
				if ( vnh.matchElement("order-total") ) {
					findGrossPrice( vnh, "order-total" );
				}
				
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));

			vnh.toElement(VTDNav.PARENT);
		} catch (Exception e) {

		}
		
	}
	
	
	/**
	 * 找到gross-price元素的价格
	 * @param vnh
	 * @param key
	 * @throws NavExceptionHuge
	 */
	public void findGrossPrice( VTDNavHuge vnh, String key ) throws NavExceptionHuge {
		vnh.toElement(VTDNav.FIRST_CHILD);
		do{
			if ( vnh.matchElement("gross-price") ) {
				if ( vnh.getText() != -1 ) {
					order.put(key, vnh.toString(vnh.getText()));
				}
			}
		} while(vnh.toElement(VTDNav.NEXT_SIBLING));
		vnh.toElement(VTDNav.PARENT);
	}
	
	
	/**
	 * merchandize-total/price-adjustments/price-adjustment 下的gross-price
	 * @param vnh
	 * @param key
	 * @throws NavExceptionHuge
	 */
	public void findAdjustmentsGrossPrice( VTDNavHuge vnh, String key ) throws NavExceptionHuge {
		vnh.toElement(VTDNav.FIRST_CHILD);
		do{
			if ( vnh.matchElement("price-adjustments") ) {
				vnh.toElement(VTDNav.FIRST_CHILD);
				findGrossPrice( vnh, key );
				vnh.toElement(VTDNav.PARENT);
			}
		} while(vnh.toElement(VTDNav.NEXT_SIBLING));
		vnh.toElement(VTDNav.PARENT);
		
	}
	
	
	/**
	 * 创建 t_so_payinfo 表的sql文件
	 * @param string
	 */
	private Map<String, String> createPayInfoSqlFile() {
		
		try {
			PayInfoParticipleFactory factory = buildPayInfoRawmaterialData();

			if ( factory.getPayinfo().isEmpty() ) {
				return null;
			}
			
			return rebuildPayInfo( translaterMap(new PayInfoDictionary(), factory.getPayinfo() ), payInfoIndexCounter.getCurrentIndex() );
		} catch (IndexException e) {
			
		}
		return null;
		
	}
	
	
	/**
	 * 创建payInfoLog 表
	 */
	private Map<String, String> createPayInfoLogSqlFile() {
		
		try {
			PayInfoLogParticipleFactory factory = buildPayInfoLogRawmaterialData();

			if ( factory.getPayinfoLog().isEmpty() ) {
				return null;
			}
			
			return rebuildPayInfoLog( translaterMap(new PayInfoDictionary(), factory.getPayinfoLog() ), payInfoLogIndexCounter.getIndexAndautoIncrement() );
		} catch (IndexException e) {
			
		}
		return null;
		
	}
	
	
	/**
	 * 构建生成SQL 需要的数据
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> createOrderLogisticsSqlMap() throws IndexException {
		JSONObject expressJson = this.findExpressJson();
		
		if ( expressJson == null ) {
			return null;
		}
		
		Map<String, String> map = new HashMap<String, String>();
		Object trackingInfo = expressJson.get("TrackingInfo");
		if ( trackingInfo != null ) {
			String tracking_description = trackingInfo.toString().replaceAll("Tracking", "desc").replaceAll("OperationTime", "time").replaceAll("\\\\\"", "");
			map.put("tracking_description", tracking_description);
			this.rebuildBaseMap(map, this.orderLogisticIndexCounter.getIndexAndautoIncrement());
			map.put("modify_time", TimeTool.currentTime());
			this.setVersion(map);
		}
		return map;
	}
	
	
	
	/**
	 * 创建订单地址
	 */
	public Map<String, String> createOrderAddressSqlFile() {
		
		try {
			
			setDeliver(shipments);
			
			OrderAddressParticipleFactory orderAdrFactory =  buildOrderAddressRawmaterialData();
			
			if ( orderAdrFactory.getOrderAddress().isEmpty() ) {
				return null;
			}
			
			
			return rebuildOrderAddress( translaterMap(new OrderAddressDictionary(), orderAdrFactory.getOrderAddress() ), orderAdrIndexCounter.getIndexAndautoIncrement() );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * 创建 t_so_salesorder 表的sql文件
	 */
	public Map<String, String> createOrderSqlFile() {
		try {
			OrderParticipleFactory factory = buildRawmaterialData();

			if ( factory.getSalesorder().isEmpty() ) {
				return null;
			}
			
			return rebuildOrder( translaterMap(new OrderDictionary(), factory.getSalesorder() ), orderIndexCounter.getCurrentIndex() );
		} catch (IndexException e) {
		}
		return null;
	}
	
	
	/**
	 * 创建 t_so_orderline 表的sql文件
	 * 
	 * @param sqlFilePath
	 */
	public List<Map<String, String>> createOrderLineSqlFile() {
		try {
			OrderLineParticipleFactory factory = new OrderLineParticipleFactory();
			factory.buildRawmaterial( productLineitems );
			
			if ( factory.getOrderlines().isEmpty() ) {
				return null;
			}
			
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			for ( Map<String, String> orderline : factory.getOrderlines() ) {
				Map<String, String> map = translaterMap(new OrderLineDictionary(), orderline );
				result.add(rebuildOrderLineMap( map, orderLineIndexCounter.getIndexAndautoIncrement() ));
			}
			return result;
		} catch (IndexException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 用于填补一些在xml 中没有的数据
	 * @param rawmaterial
	 * @param index
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> rebuildBaseMap( Map<String, String> rawmaterial, int index ) throws IndexException {
		
		Map<String, String> map =  rebuildIndex( rawmaterial, index );
		map.put("order_id", orderIndexCounter.getCurrentIndexStr() );
		
		return map;
	}
	
	
	/**
	 * 用于填补一些在xml 中没有的数据，对订单地址
	 * @param rawmaterial
	 * @param index
	 * @return
	 * @throws IndexException 
	 */
	public Map<String, String> rebuildOrderAddress( Map<String, String> rawmaterial, int index ) throws IndexException {
		
		Map<String, String> map =  rebuildBaseMap( rawmaterial, index );
		map.put("country_id", "1");
		map.put("town_id", "0");
		map.put("appoint_type", "1");
		
		setFieldTestTag( map, "mobile" );
		setFieldTestTag( map, "email" );
		return map;
	}
	
	
	/**
	 * 重构下订单行
	 * @param rawmaterial
	 * @param index
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildOrderLineMap( Map<String, String> rawmaterial, int index ) throws IndexException {
		
		rebuildBaseMap( rawmaterial, index );
		
		// 订单行商品价格为0默认为赠品
		if ( CommonTools.isZero(rawmaterial.get("subtotal")) ) {
			rawmaterial.put("type", "0");
		} else {
			rawmaterial.put("type", "1");
		}
		
		String msrp = rawmaterial.get("msrp");
		rawmaterial.put("sale_price", msrp);
		
		setEngravedInfo( rawmaterial );
		this.setVersion(rawmaterial);
		
		// 如果订单行商品能查到用数据库查到的属性，如果查不到就只有extention_code
		try {
			Map<String, String> skuInfo = OrderLine.getOrderLine().getMap(rawmaterial.get("extention_code"));
			rawmaterial.putAll(skuInfo);
		} catch (Exception e) {
			// TODO 这里缺少商品的太多了，先不打log了
		}
		
		return rawmaterial;
	}
	
	
	/**
	 * 设置刻字
	 * @param rawmaterial
	 */
	public void setEngravedInfo( Map<String, String> rawmaterial ) {
		String engravedName				 = rawmaterial.get("engravingMessages");
		String engravedImageUrl			 = rawmaterial.get("engravingSpecialCharacter");
		if ( engravedName == null || engravedName.isEmpty() ) {
			return;
		}
		
		String[] engraveNames	    = engravedName.split(",");
		String[] engravedImageUrls  = engravedImageUrl.split(",");
		
		JSONArray ja = new JSONArray();
		if ( engraveNames.length > engraveNames.length ) {
			for ( int i = 0; i < engraveNames.length; i++ ) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("engravedName", engraveNames[i]);			// 白山,白山
				if ( i < engravedImageUrls.length )
					jsonObject.put("engravedImageUrl", engravedImageUrls[i]);	// 20180326/L9603000U_20180326100354113.png,20180326/L9603000U_20180326100354113.png
				ja.add(jsonObject);
			}
			
		} else {
			for ( int i = 0; i < engravedImageUrls.length; i++ ) {
				JSONObject jsonObject = new JSONObject();
				if ( i < engraveNames.length ) 
					jsonObject.put("engravedName", engraveNames[i]);			// 白山,白山
				jsonObject.put("engravedImageUrl", engravedImageUrls[i]);	// 20180326/L9603000U_20180326100354113.png,20180326/L9603000U_20180326100354113.png
				ja.add(jsonObject);
			}
		}
		
		rawmaterial.put("misc", ja.toJSONString());
		rawmaterial.remove("engravingMessages");
		rawmaterial.remove("engravingSpecialCharacter");
	}
	
	
	
	/**
	 * 将 saleorder 表需要的字段进行补充
	 * @param rawmaterial
	 * @param index
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildOrder( Map<String, String> rawmaterial, int index ) throws IndexException {
		
		Map<String, String> map =  rebuildIndex( rawmaterial, index );
		String customerNO = rawmaterial.get("member_id");
		
		if ( customerNO == null ) {
			throw new SqlFactoryException("This order have no member id. Order data:" + rawmaterial);
		}
		
		String memberId = MemberMapping.getMemberMapping().getValue(customerNO);
		if ( memberId != null ) {
			map.put( "member_id", memberId );
		} else {
			map.put( "member_id", null );
//			logger.error("Didn't find the customerNO: {} MemberID. MemberId will be set NULL.", customerNO);
		}
		
		// 如果付款或者是COD订单标记为已完成，如果是未付款标记为取消
		// TODO 最后两天未支付改为取消，已付款未完成状态改为6 在途
		setLogisticsStatus( map );
		
		if ( customerNO != null && !customerNO.isEmpty() ) {
			map.put( "customer_no", customerNO );
		}
		
		setOrderExpress( map );
		setVersion(map);
		map.put( "shop_id", "1" );
		map.put( "card_status", "0" );
		map.put( "is_qs", "f" );
		map.put( "order_type", "1" );
		map.put( "quantity", findAllProductCount()+"" );
		
		return map;
	}
	
	
	/**
	 * 根据订单状态模式处理
	 * <ul>
	 * 	<li>模式1：如果付款或者是COD订单标记为已完成，如果是未付款标记为取消</li>
	 * 	<li>模式2：最后两天未支付改为取消，已付款未完成状态改为6 在途</li>
	 *  <li>默	认：不做处理，按照正常逻辑进行匹配</li>
	 * </ul>
	 * @param map
	 */
	public void setLogisticsStatus( Map<String, String> map ) {
		
		String processMode = System.getProperty("mode.process.type");
		if ( "1".equals(processMode) ) {
			setLogisticsStatus1( map );
		}  else if ( "2".equals(processMode) ) {
			setLogisticsStatus2( map );
		}
		
	}
	
	
	/**
	 * 如果付款或者是COD订单标记为已完成，如果是未付款标记为取消
	 * @param map
	 */
	public void setLogisticsStatus1( Map<String, String> map ) {
		
		// 全额付款或者COD，标记为已完成
		if ( SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(map.get("financial_status")) || SalesOrder.SO_PAYMENT_TYPE_COD.equals(map.get("payment")) ) {
			map.put( "logistics_status", SalesOrder.SALES_ORDER_STATUS_FINISHED );
		}
		
		// 未额付款，标记为已会员取消
		if ( !SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(map.get("financial_status")) && !SalesOrder.SO_PAYMENT_TYPE_COD.equals(map.get("payment")) ) {
			map.put( "logistics_status", SalesOrder.SALES_ORDER_STATUS_CANCELED );
		}
		
		// 将付款状态改成cod
		if ( SalesOrder.SO_PAYMENT_TYPE_COD.equals(map.get("payment") )) {
			map.put( "financial_status", SalesOrder.SALES_ORDER_FISTATUS_COD );
		}
		
	}
	
	
	/**
	 * 最后两天未支付改为取消，已付款未完成状态改为6 在途
	 * @param map
	 */
	public void setLogisticsStatus2( Map<String, String> map ) {
		
		// 未支付改为取消
		if ( !SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(map.get("financial_status")) ) {
			map.put( "logistics_status", SalesOrder.SALES_ORDER_STATUS_CANCELED );
		}
		
		// 已付款未完成状态改为6 在途
		if ( SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT.equals(map.get("financial_status")) && !SalesOrder.SALES_ORDER_STATUS_FINISHED.equals(map.get("logistics_status")) ) {
			map.put( "logistics_status", SalesOrder.SALES_ORDER_STATUS_DELIVERIED );
		}
		
		// 将付款状态改成cod
		if ( SalesOrder.SO_PAYMENT_TYPE_COD.equals(map.get("payment") )) {
			map.put( "financial_status", SalesOrder.SALES_ORDER_FISTATUS_COD );
		}
		
	}
	
	
	/**
	 * 对订单设置物流单号和物流公司
	 * @param map
	 */
	public void setOrderExpress( Map<String, String> map ) {
		
		JSONObject expressJson = findExpressJson();
		
		if ( expressJson == null) {
			return;
		}
		
		map.put("trans_code", (String)expressJson.get("TrackingNo"));
		map.put("logistics_provider_name", (String)expressJson.get("LogisticsCompany"));
	}
	
	
	/**
	 * 从解析出来的订单map中获取物流json对象
	 * @return
	 */
	public JSONObject findExpressJson() {
		
		String expressJsonStr = order.get("OrderExpress");
		if ( expressJsonStr == null || expressJsonStr.isEmpty() ) {
			return null;
		}
		
		return JSON.parseObject(expressJsonStr);
	}
	
	
	/**
	 * 补充 PayInfo 表
	 * @param rawmaterial
	 * @param currentIndex
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildPayInfo(Map<String, String> rawmaterial, int currentIndex) throws IndexException {
		setVersion(rawmaterial);
		rebuildIndex(rawmaterial, currentIndex);
		rebuildBaseMap(rawmaterial, currentIndex);
		setPayCreateTime( rawmaterial );
		setPayModifyTime( rawmaterial );
		rawmaterial.put("pay_numerical", rawmaterial.get("pay_money"));
		
		removeThirdPayNo( rawmaterial );
		
		return rawmaterial;
	}
	
	
	/**
	 * 如果是COD删除第三方支付号
	 * @param rawmaterial
	 */
	public void removeThirdPayNo( Map<String, String> rawmaterial ) {
		if ( SalesOrder.COD_TYPE_CASH.equals(rawmaterial.get("pay_type")) ) {
			rawmaterial.remove("third_pay_no");
		}
	}
	
	
	/**
	 * 补充PayInfoLog 表
	 * @param rawmaterial
	 * @param index
	 * @return
	 * @throws IndexException
	 */
	public Map<String, String> rebuildPayInfoLog( Map<String, String> rawmaterial, int index ) throws IndexException {
		
		rebuildIndex(rawmaterial, index);
		rebuildBaseMap(rawmaterial, index);
		rawmaterial.put( "pay_info_id", payInfoIndexCounter.getCurrentIndexStr() );
		rawmaterial.put( "call_close_status", "f" );

		setPayCreateTime( rawmaterial );
		setPayModifyTime( rawmaterial );
		
		String thirdpaytype = PayInfoDictionary.thirdPayTypeDic.get(rawmaterial.get("pay_type"));
		if ( thirdpaytype != null ) {
			rawmaterial.put("thirdpaytype", thirdpaytype);
		}
		rawmaterial.put("pay_numerical", rawmaterial.get("pay_money"));
		removeThirdPayNo( rawmaterial );
		
		return rawmaterial;
	}
	
	
	/**
	 * 支付创建时间
	 * @param rawmaterial
	 */
	public void setPayCreateTime( Map<String, String> rawmaterial ) {
		
		JSONObject jsonObj = parseTransactionNote();
		
		if ( jsonObj == null ) {
			return;
		}
		
		String createTime = (String)jsonObj.get("CreationDate");
		if ( createTime != null ) {
			rawmaterial.put("create_time", createTime);
			return;
		}
		
		String orderTime = order.get("order-date");
		if ( orderTime != null && !orderTime.isEmpty() ) {
			rawmaterial.put("create_time", order.get("order-date"));
		}

	}
	
	/**
	 * 修改支付状态时间
	 * @param rawmaterial
	 */
	public void setPayModifyTime( Map<String, String> rawmaterial ) {
		
		JSONObject jsonObj = parseTransactionNote();
		
		if ( jsonObj == null ) {
			return;
		}
		
		String lastModified = (String)jsonObj.get("LastModified");
		if ( lastModified != null && !lastModified.isEmpty() ) {
			rawmaterial.put("modify_time", lastModified);
		}
	}
	
	
	/**
	 * 	解析TransactionNote json
	 */
	public JSONObject parseTransactionNote() {
		
		String transactionNote =  order.get("transactionNote");
		if ( transactionNote == null || transactionNote.isEmpty() ) {
			return null;
		}
		
		JSONArray jsonAry = JSON.parseArray(transactionNote);
		
		return (JSONObject)jsonAry.get(jsonAry.size()-1);
	}
	
	
	/**
	 * 订单总共有多少商品，通过订单行去算
	 * @return
	 */
	public int findAllProductCount() {
		
		int count = 0;
		
		for ( Map<String, String> map : productLineitems ) {
			String quantity = map.get("quantity");
			int quantityInt = CommonTools.toInt(quantity);
			if ( quantityInt <= 0 ) {
				throw new RuntimeException(quantity + " can not less than 0.");
			}
			count = count + quantityInt;
		}
		
		return count;
	}
	
	
	/**
	 * 将解析后的字段数据按表进行划分
	 * @return
	 */
	public OrderParticipleFactory buildRawmaterialData() {
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(order);
		originTables.add(shipments);
		originTables.add(customer);
		
		OrderParticipleFactory orderParticipleFactory = new OrderParticipleFactory();
		orderParticipleFactory.buildRawmaterial(originTables);
		
		return orderParticipleFactory;
	}
	
	
	/**
	 * 将解析后的支付字段数据按表进行划分
	 * @return
	 */
	public PayInfoParticipleFactory buildPayInfoRawmaterialData() {
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(order);// 支付这里解析后也放到order里面了
		
		PayInfoParticipleFactory orderParticipleFactory = new PayInfoParticipleFactory();
		orderParticipleFactory.buildRawmaterial(originTables);
		
		return orderParticipleFactory;
	}
	
	
	/**
	 * 将解析后的支付日志字段数据按表进行划分
	 * @return
	 */
	public PayInfoLogParticipleFactory buildPayInfoLogRawmaterialData() {
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(order);// 支付这里解析后也放到order里面了
		
		PayInfoLogParticipleFactory payLogParticipleFactory = new PayInfoLogParticipleFactory();
		payLogParticipleFactory.buildRawmaterial(originTables);
		
		return payLogParticipleFactory;
	}
	
	/**
	 * 将解析后的地址字段字段数据按表进行划分
	 * @return
	 */
	public OrderAddressParticipleFactory buildOrderAddressRawmaterialData() {
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(shipments);
		
		OrderAddressParticipleFactory orderAddressParticipleFactory = new OrderAddressParticipleFactory();
		orderAddressParticipleFactory.buildRawmaterial(originTables);
		
		return orderAddressParticipleFactory;
	}
	
	
	@Override
	public void run(String inputFilePath) {
		
		System.out.println("Start processing orders...");
		
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile(inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
	        VTDNavHuge vnh = vgh.getNav();
	        this.parseLoop( vnh, "/orders/order" );
		}

	}
	
	public static void main(String[] args) {
		
		String inputFilePath = findInputFilePath(args);
		
		OrderSqlFactory mem = new OrderSqlFactory();
		mem.run( inputFilePath );
	}

}
