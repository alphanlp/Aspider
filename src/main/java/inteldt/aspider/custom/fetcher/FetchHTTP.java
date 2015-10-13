package inteldt.aspider.custom.fetcher;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.MyHttpClientPool;
import inteldt.aspider.custom.framework.Processor;

import java.util.logging.Logger;

/**
 * 抓取http/https协议的web内容
 * 
 * @author pei
 *
 */
public class FetchHTTP extends Processor{
	private static Logger logger = Logger.getLogger(FetchHTTP.class.getName());
	
	protected void acceptProcess(CrawlerTask task) {
		logger.info("正在下载：" + task.getUrl());
		
		MyHttpClientPool connPool = MyHttpClientPool.getClientConnectionPool();
		String html = connPool.fetchByGetMethod(task.getUrl());
		if(html != null){
			task.setFetchStatus(1);// 抓取成功
			System.out.println("下载成功：" + task.getUrl());
		}else{
			System.out.println("下载失败：" + task.getUrl());
		}
		
		task.setHtml(html); // 抓取失败
	}

	protected void rejectProcess(CrawlerTask task) {
		// 设置任务结束
		task.setFinished(true);
	}
	
	/**下载任务，接收规则*/
	protected boolean accept(CrawlerTask task) {
		// URL不为空，并且任务没有结束，抓取状态为0的话
		if(task.getUrl() != null && task.isFinished() == false && task.getFetchStatus() == 0) 
			return true;
		return false;
	}

}
