<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
	<title>메인</title>
</head>
<body>
<h1>
	Main page
</h1>
<p>입력한 아이디 : <c:out value="${id}"/></p>
<p>입력한 패스워드 : <c:out value="${pwd}"/></p>
</body>
</html>
