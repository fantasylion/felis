package com.serio.felis.sql.maker.review;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.serio.felis.sql.maker.SqlFactory;
import com.serio.felis.tools.FilePool;
import com.serio.felis.tools.SqlScriptFileSuperMaker;
import com.serio.felis.xml.parser.ReviewVoteParser;
import com.ximpleware.VTDNav;
import com.ximpleware.extended.AutoPilotHuge;
import com.ximpleware.extended.NavExceptionHuge;
import com.ximpleware.extended.SupperVTDGenHuge;
import com.ximpleware.extended.VTDGenHuge;
import com.ximpleware.extended.VTDNavHuge;
import com.ximpleware.extended.XPathParseExceptionHuge;

public class ReviewVoteSqlFactory extends SqlFactory {

//	private static final Logger		logger		=		LoggerFactory.getLogger(ReviewVoteSqlFactory.class);

	ReviewVoteParser reviewVoteParser			=		new ReviewVoteParser();
	
	Map<String, String>		reviewVote			=		new HashMap<String, String>();
	
	// 文件池
	FilePool				voteFile			=		new FilePool("rateVote");
	
	// 高速sql文件创建
	SqlScriptFileSuperMaker voteMaker			=		new SqlScriptFileSuperMaker();
	
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
				reviewVoteParser.attrPutToMap(vnh, reviewVote);
				parseChildEle(vnh);
				
//				logger.debug(reviewVote.toString());
				
				createReviewVoteSqlFile();
				
				reviewVote.clear();
//				logger.debug("Finish one rateAndReview---------------------------");
			} while(vnh.toElement(VTDNav.NEXT_SIBLING));
			
			voteMaker.flushSuperSqlScript(voteFile.getOneFile());
			
		} catch (NavExceptionHuge e) {
//			logger.error("Has exception",e);
		} catch (XPathParseExceptionHuge e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	/**
	 * 进到custom-object里面
	 * @param vnh
	 */
	public  void parseChildEle( VTDNavHuge vnh ) {

		try {
			reviewVoteParser.parseChildSimple(vnh, reviewVote);
		
//			logger.debug("----Finish one rateAndReview son------------");
				
		} catch (Exception e) {
//			logger.error("Has exception",e);
		}
	}
	
	
	
	/**
	 * 评论投票数量
	 */
	public void createReviewVoteSqlFile() {
		
		String jsondata = reviewVote.get("object-attribute");
		
		JSONObject vo = JSON.parseObject( jsondata );
		Iterator iterator =  vo.entrySet().iterator();
		
		while( iterator.hasNext() ) {
			
			Entry entry = (Entry)iterator.next();

			JSONObject map = JSON.parseObject(entry.getValue().toString());
			
			HashMap<String, String> voteMap = new HashMap<String, String>();
			voteMap.put("useful_count", map.get("helpfull").toString());
			voteMap.put("help_less", map.get("helpless").toString());
			
			this.createUpdateSqlFile(voteMaker, voteMap, "original_id", entry.getKey().toString(), "t_pd_item_rate", voteFile.getOneFile());
		}
		
		
	}
	
	
	@Override
	public void run(String inputFilePath) {
		
		System.out.println("Start processing vote...");
		
		VTDGenHuge vgh = new SupperVTDGenHuge();
		
		if ( vgh.parseFile(inputFilePath, true, VTDGenHuge.MEM_MAPPED) ) {
	        VTDNavHuge vnh = vgh.getNav();
	        parseLoop( vnh, "/custom-objects/custom-object" );
		}
		
	}
	
	public static void main( String[] args ) {
		
		String inputFilePath = findInputFilePath(args);;
		ReviewVoteSqlFactory mem = new ReviewVoteSqlFactory();
		mem.run(inputFilePath);
		
	}

}
