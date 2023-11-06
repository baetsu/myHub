package com.joongang.common;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.joongang.domain.AuthVO;

@WebFilter({"/board/articleForm.do", "/board/modArticle.do", "/board/addArticle.do"})
public class BoardFilter implements Filter {

    public BoardFilter() {
    }

	public void destroy() {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		HttpSession session = req.getSession(true);
		AuthVO auth = (AuthVO) session.getAttribute("auth");
		if(auth == null) {
			String userURI = req.getRequestURI();
			if(!userURI.equals(req.getContextPath()+"/board/modArticle.do") &&
					!userURI.equals(req.getContextPath()+"/board/addArticle.do")) {
				String queryString = req.getQueryString();
				if(queryString != null && !queryString.isEmpty()) {
					userURI += "?" + queryString;
				}
				session.setAttribute("userURI", userURI);
			}
			rep.sendRedirect(req.getContextPath()+"/member/loginForm.do");
			return;
		}
		chain.doFilter(request, response);
	}
	

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
