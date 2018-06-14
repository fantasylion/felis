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
package com.serio.felis.sql.maker.review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serio.felis.assembly.ReplyParticipleFactory;
import com.serio.felis.assembly.ReviewParticipleFactory;
import com.serio.felis.hamal.exception.IndexException;
import com.serio.felis.sql.maker.SqlFactory;
import com.serio.felis.sql.maker.sequence.IndexCounter;
import com.serio.felis.tools.FilePool;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.tools.TimeTool;
import com.serio.felis.xml.parser.RatingsAndReviewParser;
import com.serio.felis.xml.parser.translater.RateAndReViewDictionary;
import com.serio.felis.xml.parser.translater.ReplyDictionary;
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
public class RateAndReViewSqlFactory extends SqlFactory {

//	private static final Logger		logger					= LoggerFactory.getLogger(RateAndReViewSqlFactory.class);

	
	RatingsAndReviewParser rateAndReviewParser	=		new RatingsAndReviewParser();
	
	Map<String, String> rateAndReview			=		new HashMap<String, String>();
	
	// ID 序列
	IndexCounter rateAndReViewIndexCounter		=		new IndexCounter();
	IndexCounter rateImgIndexCounter			=		new IndexCounter();
	

	// 文件池
	FilePool rateAndReViewFile					=		new FilePool("rateAndReView");
	FilePool rateAndReViewReplyFile				=		new FilePool("rateAndReViewReply");
	FilePool rateImgFile						=		new FilePool("rateImgFile");
	
	
	// 高速sql文件创建
	SqlScriptFileSuperMaker rateAndReViewMaker			=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker rateAndReViewReplyMaker		=	new SqlScriptFileSuperMaker();
	SqlScriptFileSuperMaker rateImgMaker				=	new SqlScriptFileSuperMaker();
	
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
			// 在custom-object层元素 循环
			do{
				rateAndReviewParser.attrPutToMap(vnh, rateAndReview);
				parseChildEle(vnh);
				vnh.toElement(VTDNav.PARENT);
				
//				logger.debug(rateAndReview.toString());
				
				if ( isRateAndReview(rateAndReview) ) {
					createRateAndReViewSqlFile();
				} else {
					createReviewReplySqlFile();
				}
				rateAndReview.clear();
//				logger.debug("Finish one rateAndReview---------------------------");
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			flushAllFile();
		} catch (NavExceptionHuge e) {
//			logger.error("Has exception",e);
		} catch (XPathParseExceptionHuge e) {
//			logger.error("Has exception",e);
		}
	}
	
	/**
	 * 刷新所有文件
	 */
	public void flushAllFile() {
		
		this.flushSqlFile( this.rateAndReViewMaker, this.rateAndReViewFile.getOneFile() );
		this.flushSqlFile( this.rateAndReViewReplyMaker, this.rateAndReViewReplyFile.getOneFile());
	}
	
	
	/**
	 * 进到custom-object里面
	 * @param vnh
	 */
	public  void parseChildEle( VTDNavHuge vnh ) {

		try {
			rateAndReviewParser.parseChildSimple(vnh, rateAndReview);
		
//			logger.debug("----Finish one rateAndReview son------------");
				
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 创建 t_pd_item_rate 表的sql文件
	 */
	public void createRateAndReViewSqlFile() {
		try {
			ReviewParticipleFactory review = buildReviewRawmaterialData();
			Map<String, String> rateMap = rebuildMap( translaterMap(new RateAndReViewDictionary(), review.getBaseMap() ), rateAndReViewIndexCounter.getIndexAndautoIncrement() );
			createInsertSqlFile(rateAndReViewMaker, rateMap, "t_pd_item_rate", rateAndReViewFile.getOneFile() );
			
		} catch (IndexException e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 回复评论，需要更新评论表
	 */
	public void createReviewReplySqlFile() {
		
		ReplyParticipleFactory reply = buildReplyRawmaterialData();
		
		Map<String, String> rateMap = translaterMap(new ReplyDictionary(), reply.getBaseMap() );
		
		this.createUpdateSqlFile(rateAndReViewReplyMaker, rateMap, "original_id", rateAndReview.get("sid"), "t_pd_item_rate", rateAndReViewReplyFile.getOneFile());
		
	}
	
	
	public ReviewParticipleFactory buildReviewRawmaterialData() {
		ReviewParticipleFactory reviewFactory = new ReviewParticipleFactory();
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(this.rateAndReview);
		
		reviewFactory.buildRawmaterial(originTables);
		
		return reviewFactory;
	}
	
	
	public ReplyParticipleFactory buildReplyRawmaterialData() {
		ReplyParticipleFactory replyFactory = new ReplyParticipleFactory();
		
		List<Map<String, String>> originTables = new ArrayList<Map<String, String>>();
		originTables.add(this.rateAndReview);
		
		replyFactory.buildRawmaterial(originTables);
		
		return replyFactory;
	}
	
	
	
	
	/**
	 * 是否为评论，如果 type 为2 认为是回复， 非2则认为是评论
	 * @param map
	 * @return
	 */
	public boolean isRateAndReview( Map<String, String> map ) {
		
		String type = map.get("type");
		
		return !"2".equals(type);
		
	}
	
	
	public Map<String, String> rebuildMap( Map<String, String> map, int index ) throws IndexException {
		
		map = rebuildIndex(map, index);
		map.put("pass_time", TimeTool.currentTime());
		map.put("useful_count", "0");
		return map;
	}
	
	
	@Override
	public void run(String inputFilePath) {
		
		System.out.println("Start processing reviews...");
		
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile( inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
	        VTDNavHuge vnh = vgh.getNav();
	        this.parseLoop( vnh, "/custom-objects/custom-object" );
		}
	}
	
	
	public static void main(String[] args) {

		RateAndReViewSqlFactory mem = new RateAndReViewSqlFactory();
        mem.run(findInputFilePath(args));
		
	}

}
