package com.joongang.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.joongang.domain.ArticleVO;
import com.joongang.service.BoardService;
import com.joongang.dao.BoardDAO;

@WebServlet({"/board/listArticles.do", "/board/articleForm.do", "/board/addArticle.do",
	"/board/viewArticle.do", "/board/modArticle.do", "/board/removeArticle.do"})
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//이미지 저장할 path 생성
	public static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	private BoardService boardService;
	private ArticleVO articleVO;
	private BoardDAO boardDAO;
       
    public BoardController() {
        super();
        boardService = new BoardService();
        articleVO = new ArticleVO();
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
		List<ArticleVO> articleList = new ArrayList<ArticleVO>();
		if(path == null || path.equals("/listArticles.do")) {
			articleList = boardService.listArticles();
			request.setAttribute("articlesList", articleList);
			//boardService.listArticles();
			nextPage = "/board/listArticles2.jsp";
		} else if(path.equals("/articleForm.do")) {
			nextPage = "/board/articleForm.jsp";
		} else if(path.equals("/addArticle.do")) {	// 글쓰기 버튼 클릭시 이동
			Map<String, String> articleMap = new HashMap<String, String>();
			int articleNO = 0;
			upload(request, response, articleMap); // 불러온 값들을 articleMap에 저장
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");
			articleVO.setParentNO(0);	// 테스트용
			articleVO.setId("hong"); // 임의로 넣은 데이터
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			articleNO = boardService.addArticle(articleVO);
			// 첨부파일 있는지 체크
			if(imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				destDir.mkdir();
				FileUtils.moveToDirectory(srcFile, destDir, true);
			}
			// 위에서 만든 함수 화면에 출력하기 위한(html로) 간단한 방법(잘 안쓰임)
//			PrintWriter pw = response.getWriter();
			//location => 원하는 위치로 이동
//			pw.print("<script>"
//					+ "  alert('새글추가');"
//					+ "  location.href='" + request.getContextPath() + "/board/listArticles.do';"
//					+ "</script>");
			response.sendRedirect(request.getContextPath()+"/board/listArticles.do?addItem=true");
			return; //redirect기능을 쓰기 위해 추가
		} else if(path.equals("/viewArticle.do")) {
			String articleNO = request.getParameter("articleNO");
			articleVO = boardService.viewArticle(Integer.parseInt(articleNO));
			System.out.println("articleVO" + articleVO.getArticleNO());
			request.setAttribute("article", articleVO);
			nextPage = "/board/viewArticle.jsp";
		} else if(path.equals("/modArticle.do")) {
			Map<String, String> articleMap = new HashMap<String, String>();
			upload(request, response, articleMap);
			int articleNO = Integer.parseInt(articleMap.get("articleNO"));
			articleVO.setArticleNO(articleNO);
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");
			articleVO.setParentNO(0);
			articleVO.setId("hong");
			articleVO.setTitle(title);
			articleVO.setContent(content);
			articleVO.setImageFileName(imageFileName);
			boardService.modArticle(articleVO);
			if(imageFileName != null && imageFileName.length() != 0) {
				//사진 수정전 파일명
				String originalFileName = articleMap.get("originalFileName");
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				destDir.mkdir();
				FileUtils.moveToDirectory(srcFile, destDir, true);
				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
				oldFile.delete();
			}
//			PrintWriter pw = response.getWriter();
//			pw.print("<script>"
//					+ "  alert('수정완료');"
//					+ "  location.href='" + request.getContextPath() + "/board/viewArticle.do?articleNO="
//					+ articleNO + "';"
//					+ "</script>");
			response.sendRedirect(request.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "&modItem=true");
			return;
		} else if(path.equals("/removeArticle.do")) {
			int articleNO = Integer.parseInt(request.getParameter("articleNO"));
			List<Integer> articleNOList = boardService.removeArticle(articleNO);
			// 첨부파일이 있는 경우, 생성된 폴더 삭제(delete directory)
			//방법1
			for(int _articleNO : articleNOList) {
				File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
				if(imgDir.exists()) {
					FileUtils.deleteDirectory(imgDir);
				}
			}
			//방법2
//			for(int i=0; i<articleNOList.size(); i++) {
//				int _articleNO = articleNOList.get(i);
//				File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
//				if(imgDir.exists()) {
//					FileUtils.deleteDirectory(imgDir);
//				}
//			}
//			PrintWriter pw = response.getWriter();
//			pw.print("<script>"
//					+ "  alert('글 삭제');"
//					+ "  location.href='" + request.getContextPath() + "/board/listArticles.do';"
//					+ "</script>");
			response.sendRedirect(request.getContextPath() + "/board/listArticles.do?delItem=true");
			return;
			
		}
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
	
	private void upload(HttpServletRequest request, HttpServletResponse response,
			Map<String, String> articleMap) throws ServletException, IOException {
		//현재 이미지
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		//특정 설정값들을 저장하는 클래스 = DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(int i=0; i<items.size(); i++) {
				FileItem fileItem = items.get(i);
				if(fileItem.isFormField()) {
					articleMap.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				} else {
					System.out.println("파라미터명: " + fileItem.getFieldName());
					System.out.println("파일명: " + fileItem.getName());
					System.out.println("파일크기: " + fileItem.getSize());
					if(fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\"); // 글자만 잘라내기
						if(idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}
						String fileName = fileItem.getName().substring(idx+1);
						articleMap.put(fileItem.getFieldName(), fileName);
						File uploadFile = new File(currentDirPath + "\\temp\\" + fileName);
						fileItem.write(uploadFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
