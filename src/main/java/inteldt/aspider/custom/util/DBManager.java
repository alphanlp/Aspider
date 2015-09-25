package inteldt.aspider.custom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 数据库工具类，主要提供常用数据库的连接对象的获取方法，目的是为了简化开发工作量
 * @author apei
 *
 */
public class DBManager {
	
	/**
	 * Mysql数据库连接对象获取方法，用户名和密码默认为root,默认编码为utf-8
	 * @param dburl
	 * @param port
	 * @param dbname
	 * @return
	 */
	public static Connection getMySQLConn(String dburl, int port, String dbname){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + dburl + ":" + port + "/" + dbname + "?characterEncoding=utf-8&useUnicode=TRUE", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * Mysql数据库连接对象获取方法，默认编码为utf-8
	 * @param dburl
	 * @param port
	 * @param dbname
	 * @param username
	 * @param password
	 * @return
	 */
	public static Connection getMySQLConn(String dburl, int port, String dbname, String username, String password){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + dburl + ":" + port + "/" + dbname + "?characterEncoding=utf-8&useUnicode=TRUE", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void close(Connection conn, PreparedStatement ps,
			ResultSet rs) {
		try {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//System.gc();
		}
	}	
}
