package inteldt.aspider.custom.pre;

import inteldt.aspider.custom.framework.Processor;

/**
 * 登录接口
 * 
 * @author lenovo
 *
 */
public abstract class Login extends Processor {
	protected boolean isLogined = false;
	
	public abstract void login();
}
