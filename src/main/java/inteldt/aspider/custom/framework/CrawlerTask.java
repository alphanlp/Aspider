/*知乎爬虫 
 * copyright @inteldt
 */
package inteldt.aspider.custom.framework;

import inteldt.aspider.custom.entity.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * 抓取任务类。任务可以序列化，当出错时，或者崩溃是，可以将任务持久化，防止丢失。
 * 
 * @author pei
 *
 */
public class CrawlerTask implements Serializable {
	private static final long serialVersionUID = 7874096757350100472L;

//	/**所属域名*/
//	private String domain = null;
	
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
    
    /** 网页html*/
    private String html = null;
    
    /** 解析字段封装*/
    private Entity entity = null;
    
    /** 抽取的链接*/
    private List<String> links = null;
   
    
	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

//	public String getDomain() {
//		return domain;
//	}
//
//	public void setDomain(String domain) {
//		this.domain = domain;
//	}

}
