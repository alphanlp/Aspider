package inteldt.aspider.custom.entity;

import java.util.List;

/**
 * 知乎账号实体，存储相关信息
 * 
 * @author pei
 *
 */
public class ZhihuAccount {
	/** ID，相当于User ID*/
	private String account;
	
	/** 名称*/
	private String name;
	
	/** 昵称*/
	private String byname;
	
	/** 头像，存储图片本地地址*/
	private String avatar;
	
	/** 性别，男/女*/
	private String gender;
	
	/** 地址*/
	private String location;
	
	/** 行业*/
	private String business;
	
	/** 公司*/
	private String company;
	
	/** 职位*/
	private String position;
	
	/** 学校*/
	private String education;
	
	/** 专业*/
	private String master;
	
	/** 自我描述*/
	private String desciption;

	/** 获得赞同数量*/
	private int okayNum;
	
	/** 获得感谢数量*/
	private int thksNum;
	
	/** 擅长话题*/
	private List<String> goodTopic;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getByname() {
		return byname;
	}

	public void setByname(String byname) {
		this.byname = byname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public int getOkayNum() {
		return okayNum;
	}

	public void setOkayNum(int okayNum) {
		this.okayNum = okayNum;
	}

	public int getThksNum() {
		return thksNum;
	}

	public void setThksNum(int thksNum) {
		this.thksNum = thksNum;
	}

	public List<String> getGoodTopic() {
		return goodTopic;
	}

	public void setGoodTopic(List<String> goodTopic) {
		this.goodTopic = goodTopic;
	}

}
