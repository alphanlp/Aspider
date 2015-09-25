package inteldt.aspider.custom.pre;

import inteldt.aspider.custom.framework.MyHttpClientPool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
public class Login {
	private static Logger log = Logger.getLogger(Login.class.getName());
	
	private User user = null;
	
	public Login(String username, String password){
		this.user = new User(username, password);
	}
	
	/**
	 * 登录
	 */
	public void login(){
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
		params.add(new BasicNameValuePair("password", user.getPassword()));// 密码
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
			params.add(new BasicNameValuePair("email", user.getUsername()));// 用户名
			UrlEncodedFormEntity paramsEntity=new UrlEncodedFormEntity(params, Consts.UTF_8);
			
			html = pool.fetchByPostMethod("http://www.zhihu.com/login/email", paramsEntity);
		}while(html.contains("errcode"));
		
		user.setLogined(true);
		System.out.println("登录知乎成功!开始采集数据！");
		
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
        OutputStream os = new FileOutputStream(new File("C:\\Users\\lenovo\\Desktop\\captcha.gif"));
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  

        os.close();  
        is.close();
	}

	public User getUser() {
		return user;
	}
	
	public static void main(String[] args) {
		Login login = new Login("464531351@qq.com","shisi121");
		login.login();
		
		String url = "http://www.zhihu.com/people/xjiangxjxjxjx/logs";
		MyHttpClientPool connPool = MyHttpClientPool.getClientConnectionPool();
		String html = connPool.fetchByGetMethod(url);
		System.out.println(html);
	}
}
