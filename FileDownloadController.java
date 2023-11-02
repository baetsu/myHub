package com.joongang.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download.do")
public class FileDownloadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FileDownloadController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		//파라미터로 넘어온 값 가져오기
		String imageFileName = request.getParameter("imageFileName");
		//한글 깨지는 현상 방지(아래)
		String encodedFileName = URLEncoder.encode(imageFileName, StandardCharsets.UTF_8.toString().replaceAll("\\+", "%20"));
		String articleNO = request.getParameter("articleNO");
		
		OutputStream os = response.getOutputStream();
		String downFile = BoardController.ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName;
		File file = new File(downFile);
		response.addHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);	//첨부파일 처리(한글때문에 encoding 삽입)
		FileInputStream is = new FileInputStream(file);	// 폴더에 저장된 이미지 읽어오기
		byte[] buffer = new byte[1024*8]; // 기본 파일 크기(8k 단위)
		while(true) {	// 무한반복으로 읽어들이기
			int count = is.read(buffer);
			if(count == -1) 	// 다 읽었을 때
				break;
			os.write(buffer, 0, count);
		}
		is.close();
		os.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
