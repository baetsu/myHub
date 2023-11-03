package com.joongang.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.joongang.dao.MemberDAO;
import com.joongang.domain.MemberVO;

// 여러 개일 경우 중괄호
@WebServlet({"/member/listMembers.do", "/member/memberForm.do", "/member/addMember.do",
	"/member/modMember.do", "/member/modMemberForm.do", "/member/delMember.do",
	"/member/loginForm.do", "/member/login.do"}) 
public class MemberController extends HttpServlet {
	private MemberDAO memberDAO;
       
	@Override
	public void init() throws ServletException {
		memberDAO = new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String uri = request.getRequestURI();
		int index = uri.lastIndexOf("/");
		String path = uri.substring(index);
		System.out.println("path:" + path);
		if(path == null || path.equals("/listMembers.do")) {
			List<MemberVO> memberList = memberDAO.listMembers();
			request.setAttribute("membersList", memberList);
			nextPage = "/member/listMembers.jsp";
		} else if (path.equals("/memberForm.do")) {	// 회원 가입창
			nextPage = "/member/memberForm.jsp";
		} else if (path.equals("/addMember.do")) {	// 가입하기 버튼 누른 후
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email); // 아래와 동일
//			MemberVO memberVO = new MemberVO();
//			memberVO.setId(id);
//			memberVO.setPwd(pwd);
//			memberVO.setName(name);
//			memberVO.setEmail(email);
			memberDAO.addMember(memberVO);
			request.setAttribute("msg", "addMember");
			nextPage = "/member/listMembers.do";
		} else if (path.equals("/modMemberForm.do")) {		// 회원 수정
			// 수정하려는 회원 정보 DB에서 불러오기
			String id = request.getParameter("id");
			//DAO에 해당 함수 생성
			MemberVO memInfo = memberDAO.findMember(id);	
			request.setAttribute("memInfo", memInfo);
			nextPage = "/member/modMemberForm.jsp";
		} else if (path.equals("/modMember.do")) {	// 내용 수정해서 확인 버튼 클릭시
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			// 값들을 클래스를 만들어 그곳에 저장(관리하기 용이)
			MemberVO memberVO = new MemberVO();
			memberVO.setId(id);
			memberVO.setPwd(pwd);
			memberVO.setName(name);
			memberVO.setEmail(email);
			memberDAO.modMember(memberVO);
			request.setAttribute("msg", "modified");	// 확인용
			nextPage = "/member/listMembers.do";	// 수정 완료되면 다시 회원 리스트로 이동
		} else if (path.equals("/delMember.do")) {
			String id = request.getParameter("id");
			memberDAO.delMember(id);
			request.setAttribute("msg", "deleted");
			nextPage = "/member/listMembers.do";
		} else if (path.equals("/loginForm.do")) {
			nextPage = "/member/loginForm.jsp";
		} else if (path.equals("/login.do")) {
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setPwd(pwd);
			if(memberDAO.isRegistered(vo)) {
				//등록된 회원일경우
				System.out.println("existed...");
				nextPage = "/member/listMembers.do";
			} else {
				//가입x이거나 id/pw 잘못입력한경우
				System.out.println("not exist...");
			}
		}
		
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}


	
}
