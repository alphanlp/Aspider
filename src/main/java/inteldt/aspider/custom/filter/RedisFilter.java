package inteldt.aspider.custom.filter;

import inteldt.aspider.custom.util.JedisUtil;
import inteldt.aspider.custom.util.MD5;
import redis.clients.jedis.Jedis;

/**
 * 使用redis做的一个filter
 * @author pei
 *
 */
public class RedisFilter {
	private static Jedis redisFilter = JedisUtil.getJedis();
	
	/**
	 * 添加和检查是否存在合为一个方法。 即若不存在则添加并放回false，若存在不添加并返回true。
	 * @param value
	 * @return
	 */
	public static boolean put(String value){
		if(redisFilter.sismember("filter", value)){
			return true;
		}else{
			redisFilter.sadd("filter", value);
			return false;
		}
	}
	
	/**
	 * 合并多个字符串为一个字符串， 即若不存在则添加并放回false，若存在不添加并返回true。
	 * 
	 * @param value
	 * @return
	 */
	public static boolean put(String... values){
		StringBuffer sb = new StringBuffer();
		for(String value : values){
			sb.append(value);
		}
		if(redisFilter.sismember("filter", MD5.getMD5Code(sb.toString()))){
			return true;
		}else{
			redisFilter.sadd("filter", MD5.getMD5Code(sb.toString()));
			return false;
		}
	}
	
	public static void main(String[] args) {
		put("aaa","bbbb");
	}
}
