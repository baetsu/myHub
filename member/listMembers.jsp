<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath}"/> 

<c:out value=""/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(function() {
		let msg = '<c:out value="${msg}"/>';
		if(msg === 'addMember') {
			alert("회원이 정상적으로 등록되었습니다.");
		} else if(msg === 'modified') {
			alert("회원정보가 정상적으로 수정되었습니다.")
		} else if(mesg === 'deleted') {
			alert("회원이 정상적으로 삭제되었습니다.");
		}
	});
/* 	document.addEventListener("DOMContentLoaded", function() {
		let msg = '<c:out value="${msg}"/>';
		if(msg == 'addMember') {
			alert("회원이 정상적으로 등록되었습니다.");
		} else if(msg == 'modified') {
			alert("회원정보가 정상적으로 수정되었습니다.")
		} else if(mesg == 'deleted') {
			alert("회원이 정상적으로 삭제되었습니다.");
		}
	}) */
</script>
</head>
<body>
	<table align="center" border="1">
		<tr align="center" bgcolor="lightgreen">
			<td width="7%">아이디</td>
			<td width="7%">비밀번호</td>
			<td width="7%">이름</td>
			<td width="7%">이메일</td>
			<td width="7%">가입일</td>
		</tr>
		<c:choose>
		<c:when test="${empty membersList}">
			<tr>
				<td colspan=5 align="center"><b>등록된 회원이 없습니다.</b></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach var="mem" items="${membersList}">
				<tr>
					<td><c:out value="${mem.id}"/></td>
					<td><c:out value="${mem.pwd}"/></td>
					<td><c:out value="${mem.name}"/></td>
					<td><c:out value="${mem.email}"/></td>
					<td><c:out value="${mem.joinDate}"/></td>
					<td><a href="${contextPath}/member/modMemberForm.do?id=${mem.id}">수정</a></td>
					<td><a href="${contextPath}/member/delMember.do?id=${mem.id}">삭제</a></td>
				</tr>
			</c:forEach>
		</c:otherwise>
		</c:choose>
	</table>
</body>
</html>