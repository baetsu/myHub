<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${contextPath}/upload.do" method="post" enctype="multipart/form-data">
		파일1 : <input type="file" name="file1"><br>
		파일2 : <input type="file" name="file2"><br>
		파라미터1: <input type="text" name="param1"><br>
		파라미터2: <input type="text" name="param2"><br>
		파라미터3: <input type="text" name="param3"><br>
		<input type="submit" value="업로드">
	</form>
</body>
</html>