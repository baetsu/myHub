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
	
	public int addArticle(ArticleVO vo) {
		return boardDAO.addArticle(vo);
	}
	
	public ArticleVO viewArticle(int articleNO) {
		return boardDAO.viewArticle(articleNO);
	}
	
	public void modArticle(ArticleVO vo) {
		boardDAO.modArticle(vo);
	}
	
	public List<Integer> removeArticle(int articleNO) {
		List<Integer> articleNOList = boardDAO.selectRemoveArticles(articleNO);
		boardDAO.removeArticle(articleNO);
		//첨부파일 때문에 return 필요(자동생성되는 폴더때문에)
		return articleNOList;
		
	}
}
