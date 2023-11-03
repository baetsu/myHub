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
	
	public int addArticle(ArticleVO vo) {		// t_board에 새로운 데이터 넣는 함수
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
		return articleNO;
	}
	
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO vo = new ArticleVO();
		try {
			con = dataFactory.getConnection();
			String query = "select articleNO, parentNO, title, content, "
					+ " NVL(imageFileName, 'null') as imageFileName, id, writeDate "
					+ " from t_board where articleNO=? ";
			
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				vo.setArticleNO(rs.getInt("articleNO"));
				vo.setParentNO(rs.getInt("parentNO"));
				vo.setTitle(title);
				vo.setContent(content);
				String imageFileName = rs.getString("imageFileName");
				if(imageFileName.equals("null")) {
					imageFileName = null;
				}
				vo.setImageFileName(imageFileName);
				vo.setId(id);
				vo.setWriteDate(writeDate);
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public void modArticle(ArticleVO vo) {
		try {
			int articleNO = vo.getArticleNO();
			String title = vo.getTitle();
			String content = vo.getContent();
			String imageFileName = vo.getImageFileName();
			
			con = dataFactory.getConnection();
			String query = "update t_board set title=?, content=?";
			if(imageFileName != null && imageFileName.length() != 0) {
				query += ", imageFileName=?";
			}
			query += " where articleNO=?";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if(imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Integer> selectRemoveArticles(int articleNO) {
		List<Integer> articleNOList = new ArrayList<Integer>();
		try {
			con = dataFactory.getConnection();
			String query = "select articleNO from t_board"
					+ " start with articleNO=? "
					+ " connect by prior articleNO=parentNO";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				//매개변수와 구분할 것
				int _articleNO = rs.getInt("articleNO");
				articleNOList.add(_articleNO);
				//articleNOList.add(rs.getInt("articleNO"));
			}
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNOList;
	}
	
	public void removeArticle(int articleNO) {
		try {
			con = dataFactory.getConnection();
			String query = "delete from t_board"
					+ " where articleNO in ("
					+ "	select articleNO from t_board"
					+ "		start with articleNO=?"
					+ "		connect by prior articleNO=parentNO)";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
