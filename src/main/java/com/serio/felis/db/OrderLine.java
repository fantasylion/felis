package com.serio.felis.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serio.felis.hamal.exception.SqlFactoryException;


public class OrderLine {
	
//	private static final Logger		logger					=	LoggerFactory.getLogger(OrderLine.class);

	static OrderLine orderLine;
	
	public static OrderLine getOrderLine() {
		
		if ( orderLine == null ) {
			orderLine = new OrderLine();
		}
		
		return orderLine;
	}
	
	
    /**
     * 获取outId 对于的订单行信息，数组结构[item_id, properties, pic_url, sku_id, title, out_id]
     * @param outId
     * @return
     */
    public List<String[]> get( String outId ) {
    	
        String sql = "select item_id, properties, pic_url, sku_id, title, out_id from order_line_temp where out_id = '" + outId + "'";
        return PostgreSQLJDBC.getPostgreSQLJDBC().query( sql );
        
    }
    
    
    /**
     * 获取outId 对于的订单行信息，数组结构[item_id, properties, pic_url, sku_id, title, out_id]
     * @param outId
     * @return
     */
    public Map<String, String> getMap( String outId ) {
    	
    	List<String[]> list = get( outId );
    	
    	if ( list.size() > 1 ) {
    		throw new SqlFactoryException("out_id:" +outId+ " over quantity 1");
    	}
    	
    	if ( list.size() <= 0  ) {
    		throw new SqlFactoryException("out_id:" +outId+ " less than 1");
    	}
    	
    	String[] str = list.get(0);
    	
		 Map<String, String> map = new HashMap<String, String>();
		 map.put("item_id", str[0]);
		 map.put("sale_property", str[1]);
		 map.put("item_pic", str[2]);
		 map.put("sku_id", str[3]);
		 map.put("item_name", str[4]);
		 map.put("extention_code", str[5]);
    	
        return map;
        
    }
    
//    public static void main(String[] args) {
//    	System.out.println(OrderLine.getOrderLine().getMap( "L2290100U" ));
//	}
}
