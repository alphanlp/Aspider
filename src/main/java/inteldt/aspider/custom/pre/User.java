package inteldt.aspider.custom.pre;

/**
 * 用户
 * @author pei
 *
 */
public class User {
	private String siteDescription = "";
	
	/** 账号名称 **/
	private String username = null;
	
	/** 账号密码 **/
	private String password = null;
	
	/** 是否登录或者登录是否失效*/
	private boolean isLogined = false;

	public String getSiteDescription() {
		return siteDescription;
	}

	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}

	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLogined() {
		return isLogined;
	}

	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
	
	
}
