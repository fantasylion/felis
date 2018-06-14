package com.serio.felis.sql.maker;

import java.util.Map;

import com.serio.felis.db.AddressStore;
import com.serio.felis.xml.parser.translater.AddressDictionary;


public class AddressSqlFactory extends SqlFactory {
	
	
	public void setDeliver( Map<String, String> map ) {
		
		Map<String, String> cityMap1 = setDeliver( map, "state-code", null );
		setDeliverId( cityMap1, map, "state-code" );
		
		if ( cityMap1 != null ) {
			Map<String, String> cityMap2 = setDeliver( map, "city", cityMap1.get("id") );
			setDeliverId( cityMap2, map, "city" );
			
			if ( cityMap2 != null ) {
				Map<String, String> cityMap3 = setDeliver( map, "district", cityMap2.get("id") );
				setDeliverId( cityMap3, map, "district" );
			}
		}
	}
	

	public Map<String, String> setDeliver( Map<String, String> map, String deliverAddress, String parentId ) {
		
		String deliverAre = map.get(deliverAddress);
		
		if ( deliverAre == null ) {
			return null;
		}
		
		if ( "state-code".equalsIgnoreCase(deliverAddress) ) {
			deliverAre = deliverAre.replaceAll("市", ""); // 省级 带市的话，官网地址信息将显示不出来
		}
		
		return AddressStore.getAddressStore().getValue(deliverAre, parentId);
		
	}
	
	/**
	 * 设置id
	 * @param cityMap
	 * @param map
	 * @param deliverAddress
	 */
	public void setDeliverId( Map<String, String> cityMap, Map<String, String> map, String deliverAddress ) {
		
		if ( cityMap == null || cityMap.isEmpty() || cityMap.get("code") == null || cityMap.get("code").isEmpty() ) {
			return;
		}
		
		AddressDictionary addressDic = new AddressDictionary();
		map.put(addressDic.searchKey(deliverAddress)+"_id", cityMap.get("code"));
		
	}
	
	
	/**
	 * 设置cityId provinceId areId
	 * @param map
	 * @param deliverAddress	原数据中的字段名
	 */
	@Deprecated
	public void setDeliverId( Map<String, String> map, String deliverAddress ) {
		
		String deliverAre = map.get(deliverAddress);
		if ( deliverAre == null ) {
			return;
		}
		
		if ( "state-code".equalsIgnoreCase(deliverAddress) ) {
			deliverAre = deliverAre.replaceAll("市", ""); // 省级 带市的话，官网地址信息将显示不出来
		}
		
		String codeId = AddressStore.getAddressStore().getValue(deliverAre);
		if ( codeId != null ) {
			AddressDictionary addressDic = new AddressDictionary();
			map.put(addressDic.searchKey(deliverAddress)+"_id", codeId);
		}
	}

	@Override
	public void run(String inputFilePath) {
		
	}
	
}
