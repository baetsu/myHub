<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
	<h2>회원 정보 입력</h2>
	<form action="/register/step3" method="post">
		<p>이메일: <input type="text" name="email"/></p>
		<p>이름: <input type="text" name="name"/></p>
		<p>비밀번호: <input type="password" name="pwd"/></p>
		<p>비밀번호 확인: <input type="password" name="pwd_chck"/></p>
		<input type="submit" value="가입 완료"/>
	</form>
</body>
</html>