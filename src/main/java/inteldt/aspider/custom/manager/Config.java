package inteldt.aspider.custom.manager;

import inteldt.aspider.custom.util.PropertiesUtil;

import java.util.Properties;


public class Config {
	public static String DB_URL;
	public static int PORT;
	public static String DB_NAME;
	public static String USERNAME;
	public static String PASSWORD;
	
	/** ZhihuAccount对象存储表名*/
	public static String ZHIHU_SAVETABLE;
	
	static {
		Properties pro =PropertiesUtil.getProperties("conf/mysql.properties");
		DB_URL = pro.getProperty("dburl","192.168.0.191");
		PORT = Integer.parseInt(pro.getProperty("port","3306"));
		DB_NAME = pro.getProperty("dbname","hap");
		USERNAME = pro.getProperty("username","root");
		PASSWORD = pro.getProperty("pasword","root");
	
		ZHIHU_SAVETABLE = pro.getProperty("zhihu_savetable","zhihu_user");
	}
	
}
