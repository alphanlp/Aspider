package inteldt.aspider.custom.manager;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.ProcessorChain;
import inteldt.aspider.custom.pre.Login;

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
		// 登录知乎
		Login login = new Login("464531351@qq.com","shisi121");
		login.login();
		
		// 从种子开始抓取 种子地址： http://www.zhihu.com/people/inteldt
		CrawlerTask seedTask = new CrawlerTask();
		seedTask.setUrl("http://www.zhihu.com/people/inteldt");
		seedTask.setSeed(true);
		TaskManager.addTask(seedTask);
		
		// 任务链，从种子开始，抓取数据
		ProcessorChain processors = new ProcessorChain();
		while(!TaskManager.isTaskEmpty()){
			CrawlerTask task = TaskManager.nextTask(); // 取一个任务
			if(task != null){ // 任务不为空，继续
				processors.process(task); 
			}
		
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		}

	}

}
