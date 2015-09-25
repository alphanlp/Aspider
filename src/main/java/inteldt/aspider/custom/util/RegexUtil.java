package inteldt.aspider.custom.util;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * @author pei
 *
 */
public class RegexUtil {	
	/**
	 * 正则，提取匹配数据
	 */
	public static synchronized String getMatched(String str, String regex) {
		return getMatched(str, regex, false);
	}
	
	/**
	 * 正则，提取匹配数据
	 */
	public static synchronized String getMatched(String str, String regex, boolean group) {
		Pattern pattern = Pattern.compile(regex,Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			if(group == true){
				return matcher.group(1);
			}
			return matcher.group();
		}
		return null;
	}
	
	/**
	 * 字符串是否包含正则匹配的子串
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean isMatched(String str, String regex){
		return isMatched(str, regex ,false);
	}

	/**
	 * 字符串是否包含正则匹配的子串，当rightMatch为true时，表示为又包含关系
	 * @param str
	 * @param regex
	 * @param rightMatch
	 * @return
	 */
	public static boolean isMatched(String str, String regex, boolean rightMatch) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
	
		if(rightMatch == true){
			if (matcher.find()){
				String matchStr = matcher.group();
				if(str.lastIndexOf(matchStr) + matchStr.length() >= str.length()){
					return true;
				}else{
					return false;
				}
			}
		}
		
		if (matcher.find())
			return true;
		return false;
	}


	/**
	 * 获取Matcher
	 * 
	 * @param str
	 * @param regex
	 * @return Matcher
	 */
	public static Matcher getMatcher(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher;
	}

	public static void main(String[] args) {
//		System.out.println(new Regex().findMatch(
//				"<span class=\"name\">Product name</span><span class=\"value\">Anti- AVPR1A antibody<br><a class=\"pws-btn pws-btn-",
//				"Product name</span><span class=\"value\">.*?<br><a class=",false));
		
		if(RegexUtil.isMatched("http://www.zhihu.com/people/gaojihe", "http://www.zhihu.com/people/\\w+", true)){
			System.out.println(111);
		}
	}
}
