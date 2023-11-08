package com.joongang.domain;

public class RegisterRequest {
	private String email;
	private String name;
	private String pwd;
	private String pwd_chck;
	
	public RegisterRequest(String email, String name, String pwd, String pwd_chck) {
		super();
		this.email = email;
		this.name = name;
		this.pwd = pwd;
		this.pwd_chck = pwd_chck;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwd_chck() {
		return pwd_chck;
	}
	public void setPwd_chck(String pwd_chck) {
		this.pwd_chck = pwd_chck;
	}
}
