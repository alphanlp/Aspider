/*知乎爬虫 
 * copyright @inteldt
 */
package inteldt.aspider.custom.framework;

import java.io.Serializable;

/**
 * 抓取任务类。任务可以序列化，当出错时，或者崩溃是，可以将任务持久化，防止丢失。
 * 
 * @author pei
 *
 */
public class CrawlerTask implements Serializable {
	private static final long serialVersionUID = 7874096757350100472L;

	 /**抓取任务链接地址*/
    private String url = null;
    
	/**抓取状态*/
	private int fetchStatus = 0; 
	
	/**是否完成link抽取*/
	transient private boolean linkExtractorFinished = false;
	
	 /**种子状态 */
    private boolean isSeed = false;
    
    /**任务是否处理完毕*/
    private boolean isFinished = false;
   
    
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFetchStatus() {
		return fetchStatus;
	}

	public void setFetchStatus(int fetchStatus) {
		this.fetchStatus = fetchStatus;
	}

	public boolean isLinkExtractorFinished() {
		return linkExtractorFinished;
	}

	public void setLinkExtractorFinished(boolean linkExtractorFinished) {
		this.linkExtractorFinished = linkExtractorFinished;
	}

	public boolean isSeed() {
		return isSeed;
	}

	public void setSeed(boolean isSeed) {
		this.isSeed = isSeed;
	}
	
	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

}
