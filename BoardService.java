package com.joongang.service;

import java.util.List;

import com.joongang.dao.BoardDAO;
import com.joongang.domain.ArticleVO;

public class BoardService {
	private BoardDAO boardDAO;
	
	public BoardService() {	// 생성자
		boardDAO = new BoardDAO();
	}
	
	public List<ArticleVO> listArticles() {
		return boardDAO.selectAllArticles();
	}
	
	public void addArticle(ArticleVO vo) {
		boardDAO.addArticle(vo);
	}
	
	public ArticleVO viewArticle(int articleNO) {
		return boardDAO.viewArticle(articleNO);
	}
}
