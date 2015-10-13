package inteldt.aspider.custom.pre;

import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.MyHttpClientPool;
import inteldt.aspider.custom.util.PropertiesUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * 账户登录。以后可以扩展为接口。
 * @author pei
 *
 */
public class ZhihuLogin extends Login{
	private static Logger log = Logger.getLogger(ZhihuLogin.class.getName());
	private String username;
	private String password;
	
	{
		Properties pro = PropertiesUtil.getProperties("conf/login.properties");
		username = pro.getProperty("zhihu_username", "464531351@qq.com");
		password = pro.getProperty("zhihu_password", "shisi121");
	}
	
	/**
	 * 模拟登录
	 */
	public void login() {
		String html = null;
		MyHttpClientPool pool = MyHttpClientPool.getClientConnectionPool();
		HttpClient httpclient = MyHttpClientPool.getHttpClient();
		
		// 第一步，获取参数_xsrf，在访问的首页中
		html = pool.fetchByGetMethod("http://www.zhihu.com/");
		String _xsrf = null;
		Document doc = Jsoup.parse(html);
		_xsrf = doc.select("input[type=hidden]").get(0).attr("value"); // <input type="hidden" name="_xsrf" value="ccc96bdfa12de832b0ba7158ff8d6a86"/>
		
		// 第二步，携带参数登陆
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(_xsrf != null){
//			System.out.println(_xsrf);
			params.add(new BasicNameValuePair("_xsrf", _xsrf));// 第一步中获取的_xsrf参数
		}
		params.add(new BasicNameValuePair("password", password));// 密码
		do{
			try {
				getIdentifyCode(httpclient);
			} catch (IOException e) {
				log.severe("验证码URL错误，程序终止！");
				log.severe(e.getMessage());
				e.printStackTrace();
				System.exit(-1);
			}
			System.out.println("请输入验证码：");
			Scanner input=new Scanner(System.in);
			String identityCode = input.nextLine();
			System.out.println(identityCode);

			params.add(new BasicNameValuePair("captcha", identityCode));// 验证码
			params.add(new BasicNameValuePair("remember_me","true"));
			params.add(new BasicNameValuePair("email", username));// 用户名
			UrlEncodedFormEntity paramsEntity=new UrlEncodedFormEntity(params, Consts.UTF_8);
			
			html = pool.fetchByPostMethod("http://www.zhihu.com/login/email", paramsEntity);
		}while(html.contains("errcode"));
		
		isLogined = true;
		System.out.println("登录知乎成功!");
	}
	
	/** 获取验证码
	 * @throws IOException */
	private void getIdentifyCode(HttpClient httpclient) throws IOException{
		String codeUrl = "http://www.zhihu.com//captcha.gif?r=";
		codeUrl += System.currentTimeMillis();
		
		HttpResponse  response = httpclient.execute(new HttpGet(codeUrl)); 
        InputStream is = response.getEntity().getContent(); 
       
        byte[] bs = new byte[1024];  
        int len = -1;  
        OutputStream os = new FileOutputStream(new File("./identityCode/captcha.gif"));
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  

        os.close();  
        is.close();
	}
	
	@Override
	protected boolean accept(CrawlerTask task) {
		if(task.getUrl().contains("www.zhihu.com")){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void acceptProcess(CrawlerTask task) {
		if(isLogined == false){
			login();
		}
	}

	@Override
	protected void rejectProcess(CrawlerTask task) {
	}

//	public static void main(String[] args) {
//		ZhihuLogin login = new ZhihuLogin();
//		login.login();
//		
//		String url = "http://www.zhihu.com/people/xjiangxjxjxjx/logs";
//		MyHttpClientPool connPool = MyHttpClientPool.getClientConnectionPool();
//		String html = connPool.fetchByGetMethod(url);
//		System.out.println(html);
//	}
}
