package com.serio.felis.db;

public class RecordCoupon {
	
	static RecordCoupon recordCoupon;
	
	PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
	
	public static RecordCoupon getRecordCoupon() {
		
		if ( recordCoupon == null ) {
			recordCoupon = new RecordCoupon();
		}
		
		return recordCoupon;
	}
	
	
	public void doInsert( String sql ) {
		
		postgreSQLJDBC.insert( sql );
		
	}
    
}
