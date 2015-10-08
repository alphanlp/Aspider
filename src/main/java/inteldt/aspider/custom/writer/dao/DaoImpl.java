package inteldt.aspider.custom.writer.dao;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.util.DBManager;
import inteldt.aspider.custom.util.RegexUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Dao接口的实现
 * @author pei
 *
 */
public class DaoImpl implements Dao {
	
	@Override
	public void insertTask(CrawlerTask task) {
		// 用户信息
		if(RegexUtil.isMatched(task.getUrl(), "http://www.zhihu.com/people/\\S+")
				&& !RegexUtil.isMatched(task.getUrl(), "http://www.zhihu.com/people/\\S+/followees")){
			insertAccount(task);
		}
		
		if(RegexUtil.isMatched(task.getUrl(), "http://www.zhihu.com/people/\\S+/followees")){
			insertFolloweesLinks(task);
		}
		
	}
	/**插入用户*/
	private void insertAccount(CrawlerTask task) {
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DBManager.getMySQLConn("192.168.0.191", 3306, "hap", "root", "root");
		String sql = "INSERT INTO zhihu_user(url,account,name,byname,avatar,gender,location," +
				"business,company,position,education,master,description,okayNum,thksNum,goodTopic) " +
				"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, task.getUrl());
			ps.setString(2, task.getAccount().getAccount());
			ps.setString(3, task.getAccount().getName());
			ps.setString(4, task.getAccount().getByname());
			ps.setString(5, task.getAccount().getAvatar());
			ps.setString(6, task.getAccount().getGender());
			ps.setString(7, task.getAccount().getLocation());
			ps.setString(8, task.getAccount().getBusiness());
			ps.setString(9, task.getAccount().getCompany());
			ps.setString(10, task.getAccount().getPosition());
			ps.setString(11, task.getAccount().getEducation());
			ps.setString(12, task.getAccount().getMaster());
			ps.setString(13, task.getAccount().getDesciption());
			ps.setInt(14, task.getAccount().getOkayNum());
			ps.setInt(15, task.getAccount().getThksNum());
			String topics = task.getAccount().getGoodTopic().toString();
			ps.setString(16,topics.substring(1,topics.length()-1));
			
			
			ps.execute();
			ps.close();
			
			if(task.getLinks() != null){
				sql = "INSERT INTO zhihu_url_relation(URL,FollowURL) VALUES(?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, task.getUrl());
				ps.setString(2, task.getLinks().get(0));
				ps.execute();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(conn, ps, null);
		}
	}
	
	/**插入关注者连接*/
	private void insertFolloweesLinks(CrawlerTask task){
		Connection conn = null;
		PreparedStatement ps = null;
		conn = DBManager.getMySQLConn("192.168.0.191", 3306, "hap", "root", "root");
		String sql = "INSERT INTO zhihu_url_relation(URL,FollowURL) VALUES(?,?)";
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
