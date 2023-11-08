package com.joongang.controller;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joongang.domain.RegisterRequest;
import com.joongang.service.MemberRegisterService;

@Controller
@RequestMapping("/register/*")
public class RegisterController {
	private MemberRegisterService memberRegisterService;
	
	@Autowired
	public RegisterController(MemberRegisterService memberRegisterService) {
		this.memberRegisterService = new MemberRegisterService();
	}
	
	@GetMapping("/step1")
		public String step1() {
			return "register/step1";
	}
	
	@PostMapping("/step2")
		//step1의 체크박스의 값을 가져오기 -> requestParam(낱개)
		public String step2(@RequestParam(value="agree", defaultValue="false") Boolean agree) {
			if(!agree) {
				return "redirect:/step1";
				//그냥 return만 하면 url은 step2고 화면만 step1임
			}
		return "register/step2";
		}
	
//	@RequestMapping(value="/step3", method=RequestMethod.POST)
//		public String step3(
//				@RequestParam("email") String email,
//				@RequestParam("name") String name,
//				@RequestParam("pwd") String pwd,
//				@RequestParam("pwd_chck") String pwd_chck) {
//		System.out.println("email: " + email + " name: " + name
//							+ " pwd: " + pwd + " pwd_chck: " + pwd_chck);
//		memberRegisterService.regist(new RegisterRequest(email, name, pwd, pwd_chck));
//		StringBuffer buf = new StringBuffer();
//		try {
//			buf.append("email=");
//			buf.append(URLEncoder.encode(email, "UTF-8"));
//			buf.append("&");
//			buf.append("name=");
//			buf.append(URLEncoder.encode(name, "UTF-8"));
//			buf.append("&");
//			buf.append("pwd=");
//			buf.append(pwd);
//			buf.append("&");
//			buf.append("pwd_chck=");
//			buf.append(pwd_chck);
//		} catch (Exception ex) {
//			
//		}
//		return "redirect:/?" + buf.toString();
//	}
		
		@RequestMapping(value="/step3", method=RequestMethod.POST)
		public String step3(
				@ModelAttribute RegisterRequest rgrq, RedirectAttributes ra) {
		System.out.println("email: " + rgrq.getEmail());
		System.out.println("name: " + rgrq.getName());
		System.out.println("pwd: " + rgrq.getPwd());
		System.out.println("pwd_chck: " + rgrq.getPwd_chck());
		memberRegisterService.regist(rgrq);
		ra.addFlashAttribute("rgrq", rgrq);
		return "redirect:/";
		
	}
		

	
	
	
	
}
