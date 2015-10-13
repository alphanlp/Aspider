package inteldt.aspider.custom.entity;

import inteldt.aspider.custom.manager.Config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 知乎账号实体，存储相关信息
 * 
 * @author pei
 *
 */
public class ZhihuAccount implements GenerateEntity {
	/** ID，相当于User ID*/
	private String account = "";
	
	/** 名称*/
	private String name = "";
	
	/** 昵称*/
	private String byname = "";
	
	/** 头像，存储图片本地地址*/
	private String avatar = "";
	
	/** 性别，男/女*/
	private String gender = "";
	
	/** 地址*/
	private String location = "";
	
	/** 行业*/
	private String business = "";
	
	/** 公司*/
	private String company = "";
	
	/** 职位*/
	private String position = "";
	
	/** 学校*/
	private String education = "";
	
	/** 专业*/
	private String master = "";
	
	/** 自我描述*/
	private String description = "";

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public String toString(){
		return 	"<REC>"+ "\n" +
				"[account]:" + account + "\n" +
				"[name]:" + name + "\n"+
				"[byname]:" + byname + "\n" +
				"[avatar]:" + avatar + "\n" + 
				"[gender]:" + gender + "\n" +
				"[location]:" + location + "\n" +
				"[business]:" + business + "\n" +
				"[company]:" + company + "\n" +
				"[position]:" + position + "\n" +
				"[education]:" + education + "\n" +
				"[master]:" + master + "\n" +
				"[description]:" + description + "\n" +
				"[okayNum]:" + okayNum + "\n" +
				"[thksNum]:" + thksNum + "\n" +
				"[goodTopic]:" + goodTopic + "\n";
	}
	
	/**
	 * 利用反射，将该对象的属性和属性值封装起来
	 */
	public Entity generateEntity() {
		Field[] fields = this.getClass().getDeclaredFields();
		int size = 0;
		List<String> fieldnames = new ArrayList<String>();
		List<String> fieldvalues = new ArrayList<String>();
		List<Class<?>> fieldTypes = new ArrayList<Class<?>>();
		try {
			for(int i = 0; i < fields.length; i++){
				fields[i].setAccessible(true);
				// 属性
				fieldnames.add(fields[i].getName());
				// 属性值
				fieldvalues.add(fields[i].get(this).toString());
				// 属性类型
				fieldTypes.add(fields[i].getType());
				// 属性数量
				size = i + 1;
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Entity entity = new Entity();
		entity.setFieldNames(fieldnames);
		entity.setFieldValues(fieldvalues);
		entity.setFieldTypes(fieldTypes);
		entity.setFieldsize(size);
	
		entity.setTablename(Config.ZHIHU_SAVETABLE);
		return entity;
	}

	
//	public static void main(String[] args) {
//		ZhihuAccount account = new ZhihuAccount();
//		account.setAccount("hello");
//		account.setOkayNum(10);
//		List<String> goodTopic = new ArrayList<String>();
//		goodTopic.add("你好，我好，大家好");
//		account.setGoodTopic(goodTopic);
//		account.generateEntity();
//	}
}
