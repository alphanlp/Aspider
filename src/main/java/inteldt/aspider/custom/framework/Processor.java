package inteldt.aspider.custom.framework;

/**
 * 处理器 “接口”。这里使用了一个算法模板的设计模式
 * @author pei
 *
 */
public abstract class Processor {
	
	public final void process(CrawlerTask task){
		if(accept(task)){
			acceptProcess(task);
		}else{
			rejectProcess(task);
		}
	}
	
	/**
	 * 默认情况下，返回false
	 * @param task
	 * @return
	 */
	protected abstract boolean accept(CrawlerTask task);
	
	protected abstract void acceptProcess(CrawlerTask task);
	
	protected abstract void rejectProcess(CrawlerTask task);
}
