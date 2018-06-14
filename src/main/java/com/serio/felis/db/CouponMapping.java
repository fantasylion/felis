package com.serio.felis.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponMapping {
//	private static final Logger		logger					= LoggerFactory.getLogger(CouponMapping.class);

	static CouponMapping couponMapping;
	
	
	public static CouponMapping getMemberMapping() {
		
		if ( couponMapping == null ) {
			couponMapping = new CouponMapping();
		}
		
		return couponMapping;
	}
	
	
    /**
     * 获取对应key 的 值
     * @param promotionCouponCode
     * @return
     */
    public String getValue( String promotionCouponCode ) {
    	
    	List<String[]> res =  get( promotionCouponCode );
    	
    	if ( res.size() == 1 ) {
    		return res.get(0)[1];
    	}
    	
    	if ( res.size() > 1 ) {
//    		logger.error( promotionCouponCode + " 有" + res.size() + "条重复的数据" );
    	}
    	
    	return null;
    }
    
    
    /**
     * 获取对应key 的 值
     * @param promotionCouponCode
     * @return
     */
    public Map<String, String> getMap( String promotionCouponCode ) {
    	
    	List<String[]> res =  get( promotionCouponCode );
    	
    	if ( res == null ) {
    		return null;
    	}
    	
    	if ( res.size() == 1 ) {
    		String[] strs = res.get(0);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("promotioncouponcode", strs[0]);
    		map.put("promotioncouponcodeid", strs[1]);
    		map.put("promotioncouponname", strs[2]);
    		map.put("promotioncouponid", strs[3]);
    		return map;
    	}
    	
    	if ( res.size() > 1 ) {
//    		logger.error( promotionCouponCode + " 有" + res.size() + "条重复的数据" );
    	}
    	
    	return null;
    }
    
    
    /**
     * 
     * @param promotionCouponCode
     * @return
     */
    public List<String[]> get( String promotionCouponCode ) {
    	
        String sql = "select promotioncouponcode, promotioncouponcodeid, promotioncouponname, promotioncouponid from coupon_temp where promotioncouponcode = '" + promotionCouponCode + "'";
        return PostgreSQLJDBC.getPostgreSQLJDBC().query( sql );
        
    }
}
