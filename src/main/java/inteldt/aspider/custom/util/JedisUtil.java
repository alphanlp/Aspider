package inteldt.aspider.custom.util;

import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 工具
 * @author hap
 * @date 2015/03/14
 */
public final class JedisUtil {

	private static JedisPool jedisPool = null;
   
	/**
     * 初始化Redis连接池
     */
    static {
        try {
        	//加载redis.properties配置文件
        	Properties pro = PropertiesUtil.getProperties("conf/redis.properties");

        	//获取配置文件中的key值
    	    int maxIdle = Integer.parseInt(pro.getProperty("redis.pool.maxIdle"));
    	   
    	    boolean testOnBorrow = Boolean.parseBoolean(pro.getProperty("redis.pool.testOnBorrow"));
    	    boolean onreturn = Boolean.parseBoolean(pro.getProperty("redis.pool.testOnReturn"));
    	   
    		//创建jedis池配置实例
            JedisPoolConfig config = new JedisPoolConfig();
            //设置池配置项值
    	    config.setMaxIdle(maxIdle);   
    	    config.setTestOnBorrow(testOnBorrow);  
    	    config.setTestOnReturn(onreturn);  
    	    jedisPool = new JedisPool(config, 
    	    		(String)pro.get("redis.ip"), 
    	    		Integer.parseInt((String)pro.get("redis.port")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Jedis实例
     * 
     * @return
     */
    public static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                return jedisPool.getResource();
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 释放jedis资源
     * 
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnResource(jedis);
        }
    }

	/**
	 * 查询数据
	 */
	public String get(String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();  
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            jedisPool.returnResource(jedis);  
        }
		
		return null;
	}
	
	/**
	 * 查询特定字符串
	 */
	public String getrange(String key,int startOffset,int endOffset){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{  
            jedisPool.returnResource(jedis);  
        }
		
		return null;
	}
	 /** 
     * 向缓存中设置字符串内容 新增数据|修改
     * @param key key 
     * @param value value 
     * @return 
     * @throws Exception 
     */  
    public static boolean set(String key,String value) throws Exception{  
        Jedis jedis = null;  
        try {  
            jedis = jedisPool.getResource();  
            jedis.set(key, value);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();
        }finally{  
            jedisPool.returnResource(jedis);  
        }
        
        return false;
    }  
	
	/** 
     * 删除缓存中得对象，根据key 
     * @param key 
     * @return 
     */  
    public static boolean del(String key){  
        Jedis jedis = null;  
        try {  
            jedis = jedisPool.getResource();  
            jedis.del(key);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            jedisPool.returnResource(jedis);  
        }  
        
        return false;
    }  
      
    public static void main(String[] args) {
//    	Jedis jedis1 = JedisUtil.getJedis();
//    	jedis1.sadd("bbb","aaa");
    	
    	Jedis jedis2 = JedisUtil.getJedis();
    	jedis2.del("urlqueue");
//    	System.out.println(jedis.get("test"));
	}
}
