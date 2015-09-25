package inteldt.aspider.custom.writer.dao;

import inteldt.aspider.custom.framework.CrawlerTask;

/**
 * Dao接口
 * @author pei
 *
 */
public interface Dao {
	/**
	 * 插入抓取的task
	 * @param task
	 */
	void insertTask(CrawlerTask task);
}
