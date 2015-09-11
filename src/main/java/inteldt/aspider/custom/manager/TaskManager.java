package inteldt.aspider.custom.manager;

import inteldt.aspider.custom.framework.CrawlerTask;

import java.util.LinkedList;

/**
 * 任务加载、管理
 * 
 * @author pei
 * @since 1.0
 */
public class TaskManager {
	/**准备任务队列，其中任务待处理*/
	private static LinkedList<CrawlerTask> readyQueue = new LinkedList<CrawlerTask>(); 
	
	/**活动任务队列，其中任务正在被处理*/
	private static LinkedList<CrawlerTask> activeQueue = new LinkedList<CrawlerTask>();

	/**
	 * 加载种子任务
	 */
	public static void uploadTask(String seedFilePath){// TODO
		
	}
	
	/**
	 * 添加任务
	 */
	public static void addTask(CrawlerTask task){// TODO
		readyQueue.add(task);
	}
	
	/**
	 * 从准备队列中取出一个任务，并放置到活动队列中
	 * @return
	 */
	public static CrawlerTask nextTask(){
		CrawlerTask task = readyQueue.poll();
		activeQueue.add(task);
		return task;
	}
	
	/**
	 * 删除任务
	 * @param task
	 */
	public static void deleteTask(CrawlerTask task){
		readyQueue.remove(task);
	}
	
	public static boolean isTaskEmpty(){
		return readyQueue.isEmpty() && activeQueue.isEmpty();
	}
	
}
