package cn.gdut.edu.entity;

public class StatusCode {

	/**
	 * 注册失败
	 */
	public final static int REGFAILL = 000;
	/**
	 * 用户名或密码 格式 错误
	 */
	public final static int WRONGUSEORPASSWORDTYPE = 100;
	/**
	 * 用户已经存在
	 */
	public final static int USEEXIST = 101;
	/**
	 * 注册成功
	 */
	public final static int REGSUCCESSFUL = 111;

	/**
	 * 用户名或密码 错误
	 */
	public final static int WRONGUSEORPASSWORD = 200;
	/**
	 * 重复登录
	 */
	public final static int REPEATLOG = 201;

	/**
	 * 登录成功
	 */
	public final static int LOGSUCCESSFUL = 211;
	/**
	 * 注销成功
	 */
	public final static int OFFLINE = 555;
	
	
}
