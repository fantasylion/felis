package com.serio.felis.assembly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 默认简单版挑选器，直接初始化{@link #baseList}即可
 * @author SSH5662
 *
 */
public class DefaultParticipleFactory extends ParticipleFactory {

	protected String[]            baseArray			=	null;
	protected List<String>        baseList			=	null;
	protected Map<String, String> baseMap			=	new HashMap<String, String>();
	
	
	@Override
	public void buildRawmaterial( List<Map<String, String>> rawmaterial ) {
		for ( Map<String, String> map :  rawmaterial ) {
			
			for ( Entry<String,String> entry : map.entrySet() ) {
				if ( baseList.contains(entry.getKey()) ) {
					baseMap.put(entry.getKey(), entry.getValue());
				}
				
			}
		}
	}
	
	
	public Map<String, String> buildRawmaterials( Map<String, String> map ) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		for ( Entry<String,String> entry : map.entrySet() ) {
			if ( baseList.contains(entry.getKey()) ) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		
		return result;
	}
	
	
	/**
	 * 将对象集合转换成字符集合
	 * @param map
	 * @return
	 */
	public Map<String, String> convertStrMap( Map map ) {
		Map<String, String> result = new HashMap<String, String>();

		Set keys = map.keySet();
		
		Iterator iterator = keys.iterator();
		while( iterator.hasNext() ) {
			String key = (String)iterator.next();
			result.put(key, map.get(key)+"");
		}
		
		return result;
		
	}
	

	public Map<String, String> getBaseMap() {
		return baseMap;
	}
	
}
