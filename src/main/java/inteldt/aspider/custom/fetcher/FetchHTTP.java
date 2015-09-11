package inteldt.aspider.custom.fetcher;

import java.util.logging.Logger;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.Processor;
import inteldt.aspider.custom.framework.ProcessorChain;

/**
 * 抓取http/https协议的web内容
 * 
 * @author pei
 *
 */
public class FetchHTTP extends Processor{
	private static Logger logger = Logger.getLogger(FetchHTTP.class.getName());
	
	protected void acceptProcess(CrawlerTask task) {
		
	}

	protected void rejectProcess(CrawlerTask task) {
		
	}

	protected boolean accept(CrawlerTask task) {
		return false;
	}

}
