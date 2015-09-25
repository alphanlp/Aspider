package inteldt.aspider.custom.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 基于HttpClient4.5封装的一个HttpClient线程池
 * 
 * @author pei
 * @since 4.0
 */
public class MyHttpClientPool {
	private static Logger log = Logger.getLogger(MyHttpClientPool.class.getName());

	private volatile static MyHttpClientPool HttpClientConnectionPool;

	public static final int MAX_TOTAL_CONNECTIONS = 50;
	public static final int MAX_ROUTE_CONNECTIONS = 20;
	public static final int CONNECT_TIMEOUT = 3000; // 连接时间
	public static final int SOCKET_TIMEOUT = 5000; // 获取内容时间

	private static PoolingHttpClientConnectionManager cm = null;
	private static CloseableHttpClient httpclient;

	/**
	 * 初始化连接池
	 */
	static{
		try {
			cm = new PoolingHttpClientConnectionManager();
			cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
			cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);// 默认设置为2

			// 客户端请求的默认设置
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					  .setSocketTimeout(SOCKET_TIMEOUT)
					  .setConnectTimeout(CONNECT_TIMEOUT)
					  .setConnectionRequestTimeout(CONNECT_TIMEOUT)
					  .setRedirectsEnabled(false)
					  .setCookieSpec(CookieSpecs.STANDARD_STRICT)
					  .build();

			// 请求重试处理
			HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
				public boolean retryRequest(IOException exception,
						int executionCount, HttpContext context) {
					if (executionCount >= 2) {// 如果超过最大重试次数，那么就不要继续了
						return false;
					}
					
					if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
						return true;
					}
					
					if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
						return false;
					}
					HttpRequest request = (HttpRequest) context.getAttribute(HttpClientContext.HTTP_REQUEST);
					boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
					if (idempotent) {// 如果请求被认为是幂等的，那么就重试
						return true;
					}

					return false;
				}

			};

			httpclient = HttpClients.custom()
						.setConnectionManager(cm)
						.setDefaultRequestConfig(defaultRequestConfig)
						.setRetryHandler(httpRequestRetryHandler)
						.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MyHttpClientPool(){}
	
	/**
	 * 获取MyHttpClientPool对象，这是单例方法
	 * 
	 * @return
	 */
	public static MyHttpClientPool getClientConnectionPool() {   
	     if (HttpClientConnectionPool == null) {   
	         synchronized (MyHttpClientPool.class) {   
		         if (HttpClientConnectionPool == null) {   
		        	 HttpClientConnectionPool = new MyHttpClientPool();   
		         }   
	         }   
	     }
	     return HttpClientConnectionPool;   
	}
	
	/**
	 * 获取HttpClient。在获取之前，确保MyHttpClientPool对象已创建。
	 * @return
	 */
	public static CloseableHttpClient getHttpClient() {
		return httpclient;
	}

	
	/**
	 * 关闭整个连接池
	 */
	public static void close() {
		if (cm != null) {
			cm.shutdown();
		}
		if(httpclient != null){
			try {
				httpclient.close();
			} catch (IOException e) {
				log.severe(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Get方法发送请求，获取响应内容
	 */
	public String fetchByGetMethod(String getUrl){
		String charset = null;
		HttpGet httpget = null;
		String html = null;
		try {
			httpget = new HttpGet(getUrl);
			httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			httpget.addHeader("Accept", "text/html, application/xhtml+xml, */*");
			httpget.addHeader("Accept-Language", "zh-CN");
			httpget.addHeader("Accept-Encoding", "gzip, deflate");
			
			HttpResponse response = null;
			
			response = httpclient.execute(httpget);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				log.severe("statusCode=" + statusCode);
				log.severe(getUrl + "HttpGet Method failed: " + response.getStatusLine());
				return null;
			}
			
			HttpEntity entity = response.getEntity();
			if(entity == null){
				return null;
			}
			
			byte[] bytes = getByteArrayOutputStream(entity.getContent());
			if(bytes == null){
				return null;
			}
			
			// 从content-type中获取编码方式
			Header header=response.getFirstHeader("Content-Type");
			if(header != null) charset = getCharSet2(header.getValue());
			if(charset != null && !"".equals(charset)){
				charset = charset.replace("\"", "");
				if("gb2312".equalsIgnoreCase(charset)) charset = "GBK";
				html = new String(bytes,Charset.forName(charset));
			}else{//从Meta中获取编码方式
				html = new String(bytes,Charset.forName("utf-8"));
				charset = getCharSet(html);
				if(charset != null && !charset.equalsIgnoreCase("utf-8")){
					if("gbk2312".equalsIgnoreCase(charset)) charset = "GBK";
					html = new String(bytes,charset);
				}
			}			
		} catch (Exception e) {
			log.severe(e.getMessage());
			log.severe(getUrl + "抓取失败");
			e.printStackTrace();
		} finally{
			httpget.abort();
		}
		
		return html;
	}
	
	
	public String fetchByPostMethod(String postUrl, UrlEncodedFormEntity paramsEntity){
		String html = null;
		HttpPost httpPost = new HttpPost(postUrl);
		httpPost.setEntity(paramsEntity);
		
		HttpResponse response;
		try {
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			html = EntityUtils.toString(entity,"utf-8");
			EntityUtils.consume(entity);
		} catch (IOException e) {
			log.severe(e.getMessage());
			e.printStackTrace();
		} finally{
			httpPost.abort();
		}

		return html;
	}
	
	public static byte[] getByteArrayOutputStream(InputStream is) {
		ByteArrayOutputStream bios = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		try {
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				bios.write(buffer, 0, len);
				buffer = new byte[4096];
			}
			return bios.toByteArray();
		} catch (Exception e) {
			log.severe(e.getMessage());
			return null;
		} finally {
			try {
				if(is != null) is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (bios != null) {
				try {
					bios.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**  
	 * 正则获取字符编码  
	 * @param content  
	 * @return  
	 */    
	private static String getCharSet(String content){ 
	    String regex = "charset=\\s*\"*(\\S*?)\"";    
	    Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);    
	    Matcher matcher = pattern.matcher(content);    
	    if(matcher.find())    
	        return matcher.group(1);    
	    else    
	        return null;    
	}    
	
	/**  
	 * 正则获取字符编码  
	 * @param content  
	 * @return  
	 */    
	private static String getCharSet2(String content_type){ 
	    String regex = "charset=\\s*(\\S*[^;])";    
	    Pattern pattern = Pattern.compile(regex,Pattern.DOTALL);    
	    Matcher matcher = pattern.matcher(content_type);    
	    if(matcher.find())    
	        return matcher.group(1);    
	    else    
	        return null;    
	}    
	
}