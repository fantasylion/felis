package com.serio.felis.db;

import java.util.List;

public class MemberMapping {
	
//	private static final Logger		logger					= LoggerFactory.getLogger(MemberMapping.class);

	static MemberMapping memberMapping;
	
	
	public static MemberMapping getMemberMapping() {
		
		if ( memberMapping == null ) {
			memberMapping = new MemberMapping();
		}
		
		return memberMapping;
	}
	
	
    /**
     * 写入会员关系表
     * @param key
     * @param value
     */
    public void put( String key, String value ) {
    	
    	try {
    		String sql = "insert into customermapping (customer_no,member_id) values('"+key+"', '"+value+"')";
    		PostgreSQLJDBC.getPostgreSQLJDBC().insert( sql );
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
    	
    }
    
    
    /**
     * 获取对应key 的 值
     * @param customerNo
     * @return
     */
    public String getValue( String customerNo ) {
    	
    	List<String[]> res =  get( customerNo );
    	
    	if ( res.size() == 1 ) {
    		return res.get(0)[1];
    	}
    	
    	if ( res.size() > 1 ) {
//    		logger.error( customerNo + " 有" + res.size() + "条重复的数据" );
    	}
    	
    	return null;
    }
    
    
    /**
     * 
     * @param customerNo
     * @return
     */
    public List<String[]> get( String customerNo ) {
    	
        String sql = "select customer_no, member_id from customermapping where customer_no = '" + customerNo + "'";
        return PostgreSQLJDBC.getPostgreSQLJDBC().query( sql ); 
    }
}
