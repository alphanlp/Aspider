package inteldt.aspider.custom.manager;

import inteldt.aspider.custom.extractor.ZhihuExtractor;
import inteldt.aspider.custom.fetcher.FetchHTTP;
import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.ProcessorChain;
import inteldt.aspider.custom.pre.ZhihuLogin;
import inteldt.aspider.custom.writer.DBWriter;

/**
 * 启动程序
 * 
 * @author pei
 *
 */
public class Main {

	public static void main(String[] args) {
		start();
	}
	
	public static void start(){		
		// 加载种子
		TaskManager.addSeedUrl("http://www.zhihu.com/people/wu-yu-msra");
		
		
		// 配置任务链
		ProcessorChain processors = new ProcessorChain();
		processors.addPreProcessr(new ZhihuLogin());// 预处理，登录
		processors.addFetcher(new FetchHTTP());// 网页下载器
		processors.addExtractor(new ZhihuExtractor());// 解析抽取器
		processors.addWriter(new DBWriter());// 存储器
		
		// 抓取数据
		while(true){
			String url = TaskManager.nextUrl(); // 取一个任务
			if(url == null){
				if(TaskManager.isActiveUrlEmpty()){
					System.exit(0); // 程序终止
				}
				
				try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();} // 暂停2s
				continue;
			}
			
			CrawlerTask task = new CrawlerTask();
			task.setUrl(url);
			task.setSeed(true);
			if(task != null){ // 任务不为空，继续
				processors.process(task); 
			}
		
			try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();} // 暂停3s
		}

	}

}
