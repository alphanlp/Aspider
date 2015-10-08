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
		
		// 从种子开始抓取。设置种子地址： http://www.zhihu.com/people/wu-yu-msra
		if(TaskManager.isReadyQueueEmpty()){// 准备队列为空时才添加种子地址
			TaskManager.addMainUrl("http://www.zhihu.com/people/wu-yu-msra");
		}
		
		// 任务链，从种子开始，抓取数据
		ProcessorChain processors = new ProcessorChain();
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
