package inteldt.aspider.custom.manager;

import inteldt.aspider.custom.util.JedisUtil;

import java.util.LinkedList;

import redis.clients.jedis.Jedis;

/**
 * 任务加载、管理
 * 
 * @author pei
 * @since 1.0
 */
public class TaskManager {
	/**准备任务队列，其中任务待处理*/
//	private static LinkedList<String> readyQueue = new LinkedList<String>(); 
	private static Jedis readyQueue = JedisUtil.getJedis(); // 准备队列改成用redis来实现，这样可以方面的进行去重。
	
	/**活动任务队列，其中任务正在被处理*/
	private static LinkedList<String> activeQueue = new LinkedList<String>();

	/**
	 * 加载种子任务
	 */
	public static void uploadTask(String seedFilePath){// TODO
		
	}
	
	public static void addSeedUrl(String url){
		addMainUrl(url);
	}
	
	/**
	 * 添加主要任务（优先级分为2个，这是优先级高的任务），并负责去重。
	 */
	public static void addMainUrl(String url){
		if(!readyQueue.sismember("urlset", url)){
			readyQueue.sadd("urlset", url);
			readyQueue.lpush("urlqueue", url);
		}
	}
	
	/**
	 * 添加次要任务（优先级分为2个，这是优先级高的任务），并负责去重
	 * @param url
	 */
	public static void addSecondaryUrl(String url){
		if(!readyQueue.sismember("urlset", url)){
			readyQueue.sadd("urlset", url);
			readyQueue.lpush("secondurlqueue", url);
		}
	}
	
	/**
	 * 不建议在大量循环中使用
	 * @return
	 */
	public static boolean isReadyQueueEmpty(){
		String url = readyQueue.rpop("urlqueue");
		if(url == null){
			url = readyQueue.rpop("secondurlqueue");
			if(url == null){
				return true;	
			}else{
				readyQueue.rpush("secondurlqueue", url);
				return false;
			}
		}
		readyQueue.rpush("urlqueue", url);
		return false;
	}
	
	/**
	 * 从准备队列中取出一个任务，并放置到活动队列中
	 * @return
	 */
	public static String nextUrl(){
		String url = readyQueue.rpop("urlqueue");
		if(url == null){
			url = readyQueue.rpop("secondurlqueue");
			if(url == null){
				return null;	
			}
		}
		activeQueue.add(url);
		return url;
	}
	
	/**
	 * 删除活动队列中的任务
	 * @param task
	 */
	public static void deleteActiveUrl(String activeUrl){
		activeQueue.remove(activeUrl);
	}
	
	/**
	 * 任务全部完成，程序停止
	 * @return
	 */
	public static boolean isActiveUrlEmpty(){
		return activeQueue.isEmpty();
	}
	
}
