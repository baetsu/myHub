package com.joongang.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.joongang.domain.MemberVO;

public class MemberDAO {
	private static final String driver = "oracle.jdbc.driver.OracleDriver";
	private static final String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private static final String user = "spring";
	private static final String pwd = "1234";
	private Connection con;
	private Statement stmt;
	private DataSource dataFactory;
	private PreparedStatement pstmt;
	
	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//아래는 id, pwd, ... 데이터를 하나씩 불러와 저장하는 과정이며 MyBatis가 수행함
	public List<MemberVO> listMembers() {
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			connDB();
			String query = "select * from t_member ";
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String id = rs.getString("id");
				String pwd = rs.getString("pwd");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date joinDate = rs.getDate("joinDate");
				MemberVO vo = new MemberVO();
				vo.setId(id);
				vo.setPwd(pwd);
				vo.setName(name);
				vo.setEmail(email);
				vo.setJoinDate(joinDate);
				list.add(vo);
			}
			rs.close();
			stmt.close();
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private void connDB() {
		try {
			Class.forName(driver);	//자료를 string형으로 변환해주는 java만의 함수
			System.out.println("Oracle 드라이버 로딩 성공");
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("Connerction 생성 성공");
			stmt = con.createStatement();
			System.out.println("Statement 생성 성공");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addMember(MemberVO vo) {
		try {
			con = dataFactory.getConnection();
			String id = vo.getId();
			String pw = vo.getPwd();
			String name = vo.getName();
			String email = vo.getEmail();
			String query = "insert into t_member";
			query += " (id, pwd, name, email) ";
			query += " values(?, ?, ?, ?) ";
			System.out.println("prepareStatement : " + query);	// 오류 체크
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);	// ?에 대한 순서 (DB는 1부터 시작)
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, email);
			pstmt.executeUpdate();	// 실행
			
			pstmt.close();	// 종료
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public MemberVO findMember(String _id) {	// 122줄의 id와 구분하기 위해 _id로 작성
		MemberVO vo = new MemberVO();
		try {
			con = dataFactory.getConnection();
			String query = "select * from t_member where id = ? ";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); // while로 작성 권장
			String id = rs.getString("id"); // 쿼리의 필드명
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setEmail(email);
			vo.setJoinDate(joinDate);
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public void modMember(MemberVO memberVO) {
		try {
			con = dataFactory.getConnection();
			String query = "update t_member set pwd=?, name=?, email=? where id=?";
			System.out.println(query);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, memberVO.getPwd());
			pstmt.setString(2, memberVO.getName());
			pstmt.setString(3, memberVO.getEmail());
			pstmt.setString(4, memberVO.getId());
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delMember(String id) {
		try {
			con = dataFactory.getConnection();
			String query = "delete from t_member where id=?";
			System.out.println("prepareStatement: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRegistered(MemberVO vo) {
		boolean retValue = false;
		//보통 초기값으로 false를 함
		String id = vo.getId();
		String pwd = vo.getPwd();
		//등록된 사람인지 DB와 체크
		try {
			con = dataFactory.getConnection();
			//1일경우 true, 1이 아니라면 false
			String query = "select decode(count(*), 1, 'true', 'false') as result from t_member where id=? and pwd=?";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pwd);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				retValue = Boolean.parseBoolean(rs.getString("result"));
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
}