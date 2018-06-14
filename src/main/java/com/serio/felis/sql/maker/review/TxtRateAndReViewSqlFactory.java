package com.serio.felis.sql.maker.review;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.serio.felis.assembly.ReplyParticipleFactory;
import com.serio.felis.assembly.ReviewParticipleFactory;
import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.sql.maker.sequence.IndexCounter;
import com.serio.felis.tools.TimeTool;
import com.serio.felis.xml.parser.translater.RateAndReViewDictionary;
import com.serio.felis.xml.parser.translater.ReplyDictionary;

public class TxtRateAndReViewSqlFactory extends RateAndReViewSqlFactory {
	
	public TxtRateAndReViewSqlFactory(){}
	
	
	
	
	public TxtRateAndReViewSqlFactory( int index ) {
		rateAndReViewIndexCounter		=		new IndexCounter(index);
		rateImgIndexCounter				=		new IndexCounter(index);
	}
	
	@Override
	public void run( String inputFilePath ) {
		try {
			System.out.println("Process RateAndReview start...");
			
			BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));

			String tempString = null;

			while ((tempString = reader.readLine()) != null) {
				parseRateReview( tempString );
			}
			
			this.flushAllFile();
			this.flushSqlFile(rateImgMaker, rateImgFile.getOneFile());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * @param reviewStr
	 */
	public void parseRateReview( String reviewStr ) {

		String[] reviews = reviewStr.split("\\|");
		if ( reviews.length < 4 ) {
			return;
		}
		
		parseJson(reviews[3]);
	}
	
	
	/**
	 * 
	 * @param json
	 */
	public void parseJson( String json ) {
		
		JSONArray jsonAry = JSON.parseArray(json);
		
		ReviewParticipleFactory reviewFactory = new ReviewParticipleFactory();
		ReplyParticipleFactory replyFactory   = new ReplyParticipleFactory();
		
		for ( Object obj : jsonAry ) {
			try {
				
				JSONObject jobj = (JSONObject)obj;
				Map map = jobj.toJavaObject(Map.class);
				map = reviewFactory.convertStrMap(map);
				if ( isRateAndReview(map) ) {
					createRateReview( reviewFactory, map );
					createRateImg( rateAndReViewIndexCounter.getCurrentIndexStr() );
					rateAndReViewIndexCounter.autoIncrease();
				} else {
					createReply( replyFactory, map );
				}
				
			} catch (IndexException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	/**
	 * 创建评论
	 * @param reviewFactory
	 * @param map
	 * @throws IndexException
	 */
	public void createRateReview( ReviewParticipleFactory reviewFactory, Map map ) throws IndexException {
		Map<String, String> temp = reviewFactory.buildRawmaterials(map);
		Map<String, String> rateMap = rebuildMap( translaterMap(new RateAndReViewDictionary(), temp ), rateAndReViewIndexCounter.getCurrentIndex() );
		createInsertSqlFile(rateAndReViewMaker, rateMap, "t_pd_item_rate", rateAndReViewFile.getOneFile() );
	}
	
	
	/**
	 * 创建回复
	 * @param replyFactory
	 * @param map
	 */
	public void createReply( ReplyParticipleFactory replyFactory, Map map ) {
		Map<String, String> temp = replyFactory.buildRawmaterials(map);
		Map<String, String> rateMap = translaterMap(new ReplyDictionary(), temp );
		String original_id = rateMap.get("sid");
		rateMap.remove("sid");
		this.createUpdateSqlFile(rateAndReViewReplyMaker, rateMap, "original_id", original_id, "t_pd_item_rate", rateAndReViewReplyFile.getOneFile());
	}
	
	
	/**
	 * 创建评论图片
	 * @param index rate_id
	 */
	public void createRateImg( String index ) {
		
		Map<String, String> rateImgMap = new HashMap<String, String>();
		rateImgMap.put("create_time", TimeTool.currentTime());
		rateImgMap.put("rate_id", index);
		try {
			rebuildIndex(rateImgMap, rateImgIndexCounter.getIndexAndautoIncrement());
		} catch (IndexException e) {
			e.printStackTrace();
		}
		
		createInsertSqlFile(rateImgMaker, rateImgMap, "t_store_pd_item_rate_image", rateImgFile.getOneFile() );
		
	}
	
	public static void main( String[] args ) {

		TxtRateAndReViewSqlFactory mem = new TxtRateAndReViewSqlFactory();
		mem.run(findInputFilePath(args));

	}
}
