<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>메인</title>
</head>
<body>
<h1>
	환영합니다.
</h1>
<a href="<c:url value="/register/step1/"/>">[회원 가입하기]</a>
<p>이메일<c:out value="${rgrq.email}"/></p>
<p>이름<c:out value="${rgrq.name}"/></p>
<p>비밀번호<c:out value="${rgrq.pwd}"/></p>
<p>비밀번호 확인<c:out value="${rgrq.pwd_chck}"/></p>
<p>  The time on the server is ${serverTime}. </p>
</body>
</html>
