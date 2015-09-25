package inteldt.aspider.custom.extractor;

import inteldt.aspider.custom.entity.ZhihuAccount;
import inteldt.aspider.custom.framework.CrawlerTask;
import inteldt.aspider.custom.framework.Processor;
import inteldt.aspider.custom.manager.TaskManager;
import inteldt.aspider.custom.util.RegexUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Extractor extends Processor{
	private static Logger logger = Logger.getLogger(Extractor.class.getName());

	@Override
	protected boolean accept(CrawlerTask task) {
		if(task.isFinished() == false && task.getFetchStatus() == 1)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see inteldt.aspider.custom.framework.Processor#acceptProcess(inteldt.aspider.custom.framework.CrawlerTask)
	 */
	@Override
	protected void acceptProcess(CrawlerTask task) {
		// 抽取用户信息
		if(RegexUtil.isMatched(task.getUrl(), "http://www.zhihu.com/people/\\w+")){
			/* 第一部分，将用户信息抽取 */
			ZhihuAccount account = new ZhihuAccount();
			Document doc = Jsoup.parse(task.getHtml());			
			// account ID
			account.setAccount(task.getUrl().substring(task.getUrl().lastIndexOf("/") + 1)); 
			// name
			Element elem = doc.select("span.name").last();
			String name = elem.text();
			account.setName(name);
			// byname
			elem = doc.select("span.bio").first();
			String byname = elem.text();
			account.setByname(byname);
			// avatar 
			elem = doc.select(".avatar avatar-l").first();
			String imgurl = elem.attr("src");
			downloadImage(imgurl,"tofilepath");// TODO
			account.setAvatar("tofilepath");// TODO
			// gender
			if(doc.select(".icon icon-profile-male").isEmpty()){account.setGender("女");}else{account.setGender("男");}
			// location
			elem = doc.select("span.location item").first();
			String location = elem.text();
			account.setLocation(location);
			// business
			elem = doc.select("span.business item").first();
			String business = elem.text();
			account.setBusiness(business);
			// company
			elem = doc.select("span.employment item").first();
			String company = elem.text();
			account.setCompany(company);
			// position
			elem = doc.select("span.position item").first();
			String position = elem.text();
			account.setPosition(position);
			// education
			elem = doc.select("span.education item").first();
			String education = elem.text();
			account.setEducation(education);
			// master
			elem = doc.select("span.education-extra item").first();
			String master = elem.text();
			account.setMaster(master);
			// desciption
			elem = doc.select("span.description unfold-item").first();
			String desciption = elem.text();
			account.setDesciption(desciption);
			// okayNum
			elem = doc.select("span.zm-profile-header-user-agree > strong").first();
			int okayNum = Integer.parseInt(elem.text());
			account.setOkayNum(okayNum);
			// thksNum
			elem = doc.select("span.zm-profile-header-user-thanks > strong").first();
			int thksNum = Integer.parseInt(elem.text());
			account.setThksNum(thksNum);
			// goodTopic
			Elements elems = doc.select("a.zg-gray-darker");
			List<String> goodTopic = new ArrayList<String>();
			for(Element topic : elems){
				goodTopic.add(topic.text());
			}
			account.setGoodTopic(goodTopic);
			task.setAccount(account);
			
			/* 第二部分，将关注者页面的链接抽取 */
			List<String> links = new ArrayList<String>();
			links.add(task.getUrl() + "/followees");
			task.setLinks(links);
			task.setLinkExtractorFinished(true);
			
			TaskManager.addTask(task.getUrl() + "/followees");// 添加任务
		}
		
		// 抽取关注者
		if(RegexUtil.isMatched(task.getUrl(), "http://www.zhihu.com/people/\\w+/followees")){
			Document doc = Jsoup.parse(task.getHtml());	
			Elements elems = doc.select("a[href^=http://www.zhihu.com/people/]");
			List<String> links = new ArrayList<String>();
			for(Element elem : elems){
				String url = elem.absUrl("href");
				if(RegexUtil.isMatched(url, "http://www.zhihu.com/people/\\w+", true)){
					links.add(url);
					TaskManager.addTask(url);// 添加任务
				}
			}
			
			task.setLinkExtractorFinished(true);
		}
	}

	@Override
	protected void rejectProcess(CrawlerTask task) {
		// 设置任务结束
		task.setFinished(true);
	}
	
	private void downloadImage(String imgUrl, String toFilePath) {		
		try {
			URL url = new URL(imgUrl); 
			URLConnection httpconn = url.openConnection();
			
			InputStream is = httpconn.getInputStream();
	       
	        byte[] bs = new byte[1024];  
	        int len = -1;  
	        OutputStream os = new FileOutputStream(new File(toFilePath));
	        while ((len = is.read(bs)) != -1) {  
	          os.write(bs, 0, len);  
	        }  

	        os.close();  
	        is.close();
		} catch (IOException e) {
			logger.warning("头像下载失败，头像url为：" + imgUrl);
			logger.warning(e.getMessage());
			e.printStackTrace();
		}
		
	}

}
