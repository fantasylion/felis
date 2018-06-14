package com.serio.felis.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressStore {
	
	static AddressStore addressStore;
	PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
	
	public static AddressStore getAddressStore() {
		
		if ( addressStore == null ) {
			addressStore = new AddressStore();
		}
		
		return addressStore;
	}
	
	
    /**
     * 获取对应key 的 值
     * @param cityName
     * @return
     */
    public String getValue( String cityName ) {
    	
    	List<String[]> res =  getSearch( cityName );
    	
    	if ( res != null && res.size() == 1 ) {
    		return res.get(0)[0];
    	}
    	
    	if ( res != null && res.size() > 1 ) {
    		return res.get(0)[0];
    	}
    	
    	return null;
    }
    
    
    /**
     * 城市名，parentId 获取到指定的城市地址，默认返回第一个值
     * @param cityName
     * @param parentId
     * @return
     */
    public Map<String, String> getValue( String cityName, String parentId ) {
    	
    	List<String[]> res =  query(cityName, parentId);
    	
    	if ( res == null )
    		return null;
    	
    	String[] fields = new String[]{ "code", "id" };
    	
    	List<Map<String, String>> resMapList = new ArrayList<Map<String, String>>();
    	for ( String[] str : res ) {
    		Map<String, String> resultMap = new HashMap<String, String>();
    		for ( int i = 0; i < str.length; i++ ) {
    			resultMap.put(fields[i], str[i]);
    		}
    		resMapList.add(resultMap);
    	}
    	
    	if ( resMapList.size() < 1 ) {
    		return null;
    	}
    	
    	return resMapList.get(0);
    }

    
    /**
     * 获取对应key 的 值
     * @param cityName
     * @return
     */
    public Map<String, String> getMap( String cityName ) {
    	
    	List<String[]> res =  getSearch( cityName );
    	
    	if ( res == null ) {
    		return null;
    	}
    	
    	if ( res.size() == 1 ) {
    		String[] strs = res.get(0);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("code", strs[0]);
    		return map;
    	}
    	
    	if ( res.size() > 1 ) {
//    		logger.error( promotionCouponCode + " 有" + res.size() + "条重复的数据" );
    	}
    	
    	return null;
    }
    
    
    /**
     * 
     * @param cityName
     * @return
     */
    public List<String[]> getSearch( String cityName ) {
    	
        String sql = "SELECT code FROM t_sf_delivery_area WHERE area = '" + cityName + "'";
        return postgreSQLJDBC.query( sql );
        
    }
    
    
    /**
     * 
     * @param cityName
     * @param parentId
     * @return
     */
    public List<String[]> query( String cityName, String parentId ) {
    	
        String sql = "SELECT code, id FROM t_sf_delivery_area WHERE area = '" + cityName + "'";
        
        if ( parentId != null && !parentId.isEmpty() ) {
        	sql = sql + " and parent_id = " + parentId;
        }
        
        return postgreSQLJDBC.query( sql );
        
    }
    
    
}
