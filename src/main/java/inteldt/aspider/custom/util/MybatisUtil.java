package inteldt.aspider.custom.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * Mybatis工具类
 * @author lenovo
 *
 */
public class MybatisUtil {

    private volatile static SqlSessionFactory _instance;
    
    private MybatisUtil(){}
    
    public static SqlSessionFactory getSqlSessionFactory() {
		if (_instance == null) {
		    synchronized (MybatisUtil.class) {
				if (_instance == null) {
					try {
						 String resource = "mybatis-config.xml";
			             InputStream is = Resources.getResourceAsStream(resource);
			             _instance = new SqlSessionFactoryBuilder().build(is);	
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
		    }
		}
		return _instance;
    }

	
}
