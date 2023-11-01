package com.joongang.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.joongang.domain.ArticleVO;
import com.joongang.domain.MemberVO;

public class BoardDAO {
	private Connection con;
	private Statement stmt;
	private DataSource dataFactory;
	private PreparedStatement pstmt;
	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List selectAllArticles() {
		List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
		try {
			con = dataFactory.getConnection();
			String query = "select level, articleNO, parentNO, title, content, id, writeDate, imageFileName"
					+ " from t_board"
					+ " start with parentNO=0 connect by prior articleNO=parentNO"
					+ " order siblings by articleNO desc";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			System.out.println(query);
			while(rs.next()) {
				int level = rs.getInt("level");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String imageFileName = rs.getString("imageFileName");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				ArticleVO vo = new ArticleVO();
				vo.setLevel(level);
				vo.setArticleNO(articleNO);
				vo.setParentNO(parentNO);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				vo.setId(id);
				vo.setWriteDate(writeDate);
				articlesList.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articlesList;
	}
	
	private int getNewArticleNO() {
		// 번호 자동 생성
		int retValue = 0;
		try {
			con = dataFactory.getConnection();
			// 해당 테이블의 articleNO 최대값 불러오기
			String query = "select max(articleNO) from t_board";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			if(rs.next()) {
				retValue = (rs.getInt(1) + 1);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	public void addArticle(ArticleVO vo) {		// t_board에 새로운 데이터 넣는 함수
		// 위에서 만든 ArticleNO 가져오기
		int articleNO = getNewArticleNO();
		try {
			con = dataFactory.getConnection();
			int parentNO = vo.getParentNO();
			String title = vo.getTitle();
			String content = vo.getContent();
			String id = vo.getId();
			String imageFileName = vo.getImageFileName();
			String query = "insert into t_board (articleNO, parentNO, title, content, imageFileName, id)"
					+ " values (?, ?, ?, ?, ?, ?)";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO vo = new ArticleVO();
		try {
			con = dataFactory.getConnection();
			String query = "select * from t_board"
					+ " where articleNO=? ";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery(query);
			while(rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setId(id);
				vo.setWriteDate(writeDate);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
}
