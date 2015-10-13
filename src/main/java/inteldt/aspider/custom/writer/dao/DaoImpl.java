package inteldt.aspider.custom.writer.dao;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.manager.Config;
import inteldt.aspider.custom.util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Dao接口的实现
 * @author pei
 *
 */
public class DaoImpl implements Dao {
	
	@Override
	public void insertTask(CrawlerTask task) {
		// 用户信息
		insertEntity(task);
		
		insertFolloweesLinks(task);
	}
	/**插入entity*/
	private void insertEntity(CrawlerTask task) {
		if(task.getEntity() == null) return;
		
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DBManager.getMySQLConn(Config.DB_URL, Config.PORT, Config.DB_NAME, Config.USERNAME, Config.PASSWORD);
		
		// 拼接sql
		StringBuffer sb = new StringBuffer("INSERT INTO " + task.getEntity().getTablename() + "(url,");
		for(int i = 0; i < task.getEntity().getFieldsize(); i++){
			if(i == task.getEntity().getFieldsize() - 1){
				sb.append(task.getEntity().getFieldNames().get(i) + ") VALUES(?,");
				break;
			}
			sb.append(task.getEntity().getFieldNames().get(i) + ",");
		}
		for(int i = 0; i < task.getEntity().getFieldsize(); i++) {
			if(i == task.getEntity().getFieldsize() - 1){
				sb.append("?)");
				break;
			}
			sb.append("?,");
		}
		String sql = sb.toString();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, task.getUrl());
			for(int i = 0; i < task.getEntity().getFieldsize(); i++)
			{
				if(task.getEntity().getFieldTypes().get(i) == String.class)
				{
//					System.out.println(task.getEntity().getFieldValues().get(i));
					ps.setString(i + 2, task.getEntity().getFieldValues().get(i));
				}else if(task.getEntity().getFieldTypes().get(i) == int.class)
				{
					ps.setInt(i + 2, 
							Integer.parseInt(task.getEntity().getFieldValues().get(i)));
				}else if(task.getEntity().getFieldTypes().get(i).isInterface())
				{
					if(task.getEntity().getFieldTypes().get(i) == List.class){
						ps.setString(i + 2, 
								task.getEntity().getFieldValues().get(i).replace("[", "").replace("]", ""));
					}
				}
			}
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(conn, ps, null);
		}
	}
	
	/**插入关注者连接*/
	private void insertFolloweesLinks(CrawlerTask task){
		if(task.getLinks() == null) return;
		
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DBManager.getMySQLConn(Config.DB_URL, Config.PORT, Config.DB_NAME, Config.USERNAME, Config.PASSWORD);
		String sql = "INSERT INTO url_relation(URL,FollowURL) VALUES(?,?)";
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for(String link : task.getLinks()){
				ps.setString(1, task.getUrl());
				ps.setString(2, link);
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.clearBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(conn, ps, null);
		}
	}
}
