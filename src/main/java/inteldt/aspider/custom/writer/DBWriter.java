package inteldt.aspider.custom.writer;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.Processor;
import inteldt.aspider.custom.manager.TaskManager;
import inteldt.aspider.custom.writer.dao.Dao;
import inteldt.aspider.custom.writer.dao.DaoImpl;

/**
 * 下载数据，写入数据库
 * @author lenovo
 *
 */
public class DBWriter extends Processor{

	@Override
	protected boolean accept(CrawlerTask task) {
		if(task.isFinished() == false){
			return true;
		}
		return false;
	}

	@Override
	protected void acceptProcess(CrawlerTask task) {
		Dao dao = new DaoImpl();
		dao.insertTask(task);
		
		TaskManager.deleteActiveTask(task);// 任务完成，从活动队列中删除
	}

	@Override
	protected void rejectProcess(CrawlerTask task) {
		task.setFinished(true);
		
		TaskManager.deleteActiveTask(task);
	}

}
