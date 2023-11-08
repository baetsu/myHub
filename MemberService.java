package com.joongang.service;

import com.joongang.domain.RegisterRequest;

public class MemberService {
	
	public boolean isExisted(String id, String pwd) {
		if(id.equals("test") && pwd.equals("1234")) {
			return true;
		} else {
			return false;
		}
	}
}
