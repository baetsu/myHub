<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(function() {
		$("#imageFileName").change(function() {
			//이미지가 정상적으로 로드됐는지 체크
			if (this.files && this.files[0]) {
				let reader = new FileReader();
				reader.onload = function(e) {
					$("#preview").attr('src', e.target.result);	//프리뷰에 이미지 띄우기
				};
				reader.readAsDataURL(this.files[0]);
			}
		});
		
		let articleForm = $("#articleForm");
		$("#articleForm input").on("click", function(e) {
			let operation = $(this).data("oper");
			console.log(operation);
			if(operation == 'list') {
				e.preventDefault();
				articleForm.attr("action", "${contextPath}/board/listArticles.do");
				articleForm.submit();
			}
		});
	});
</script>
</head>
<body>
<h1 style="text-align:center">새글쓰기</h1>
<form id="articleForm" method="post" action="${contextPath}/board/addArticle.do" enctype="multipart/form-data">
	<table style="text-align:center">
		<tr>
			<td align="right">글제목:</td>
			<td colspan="2"><input type="text" size="67" maxlength="500" name="title"></td>
		</tr>
		<tr>
			<td align="right">글내용:</td>
			<td colspan="2"><textarea name="content" rows="10" cols="65" maxlength="4000"></textarea></td>
		</tr>
		<tr>
			<td align="right">이미지파일 첨부:</td>
			<td><input type="file" id="imageFileName" name="imageFileName"></td>
			<td><img id="preview" src="#" width=200 height=200></td>
		</tr>
		<tr>
			<td align="right"></td>
			<td colspan="2">
				<input type="submit" data-oper='add' value="글쓰기">
				<input type="button" data-oper='list' value="목록보기">
			</td>
		</tr>
	</table>
</form>
</body>
</html>