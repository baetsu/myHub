package com.joongang.controller;



import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joongang.domain.LoginData;
import com.joongang.service.MemberService;

@Controller
public class LoginController implements InitializingBean, DisposableBean{
	MemberService memberService;
	
	LoginController() {
		memberService = new MemberService();
	}
	
	//private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(
			@RequestParam("id") String id, @RequestParam("pwd") String pwd,
			Model model, RedirectAttributes ra) {
		if(memberService.isExisted(id, pwd)) {
			model.addAttribute("id", id);
			model.addAttribute("pwd", pwd);
			return "home2";
		} else {
			ra.addAttribute("error", true);
			return "redirect:/login";
		}
	}
	
//	@RequestMapping(value="/login", method = RequestMethod.POST)
//	public String login(
//			@ModelAttribute("lData") LoginData lData, RedirectAttributes ra) {
//		if(memberService.isExisted(lData.getId(), lData.getPwd())) {
//			return "home2";
//		} else {
//			ra.addAttribute("error", true);
//			return "redirect:/login";
//		}
//	}

	@Override
	public void destroy() throws Exception {
		System.out.println("destroy");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("afterPropertiesSet");
	}
	
	
	
	
	
}
