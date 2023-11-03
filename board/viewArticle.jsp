<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 상세보기</title>
</head>
<style>
	.img {
		width: 100px;
		height: 100px;
		}
	#tr_image_modify{
		display: none;
	}
	#tr_btn_modify{
		display: none;
	}
</style>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	/* 목록보기버튼 or 수정취소버튼 클릭시 게시물 리스트 화면으로 돌아가기 */
	$(function() {
		let articleForm = $("#articleForm");
		$("#articleForm input").on('click', function(e) {
			let operation = $(this).data("oper");
			console.log(operation);
			if(operation == 'list' || operation == 'modify_off') {
				e.preventDefault();
				articleForm.attr("action", "${contextPath}/board/listArticles.do");
				articleForm.submit();
			} else if (operation == 'modify_on') {
				/* disabled 속성 삭제, disabled 설정되어있으면 백엔드로 값이 전달되지 않음 */
				$("#i_title").attr("disabled", false);
				$("#i_content").attr("disabled", false);
				$("#i_imageFileName").attr("disabled", false);
				$("#tr_image_modify").show();
				$("#tr_btn_modify").show();
			} else if (operation == 'modify') {
				articleForm.attr("action", "${contextPath}/board/modArticle.do");
				articleForm.submit();				
			} else if (operation == 'remove') {
				articleForm.attr("enctype", "application/www-form-urlencoded");
				articleForm.attr("action", "${contextPath}/board/removeArticle.do");
				articleForm.submit();
			}
		});
		/* 이미지 파일 변경시 화면에 반영 */
		$("#i_imageFileName").change(function() {
			if (this.files && this.files[0]) {
				let reader = new FileReader();
				reader.onload = function(e) {
					$("#preview").attr('src', e.target.result);
				};
				reader.readAsDataURL(this.files[0]);
			}
		});
		
		/* 게시물 수정완료시 경고창 화면에 출력 */
		let modItem = '<c:out value="${param.modItem}"/>';
		if (modItem) {
			alert("글이 정상적으로 수정되었습니다.");
			let url = new URL(location.href);
			let sp = new URLSearchParams(url.search);
			sp.delete("modItem");
			history.replaceState(null, null, "?" + sp.toString());
			
			//다른방법들
			//history.replaceState(null, null, "?");
			//history.replaceState(null, null, location.pathname);
		}
		

		
	});
</script>
<body>
<form id="articleForm" name="articleForm" method="post" enctype="multipart/form-data">
	<table style="text-align: left">
		<tr>
			<td width="150" align="center" bgcolor="FF9933">글번호</td>
			<td>
				<input type="text" value="${article.articleNO}" disabled/>
				<input type="hidden" name="articleNO" value="${article.articleNO}"/>
			</td>
		</tr>
		<tr>
			<td width="150" align="center" bgcolor="FF9933">작성자 아이디</td>
			<td>
				<input type="text" value="${article.id}" name="id" disabled/>
				<input type="hidden" name="id" value="${article.id}"/>
			</td>
		</tr>
		<tr>
			<td width="150" align="center" bgcolor="FF9933">제목</td>
			<td>
				<input type="text" value="${article.title}" name="title" id="i_title" disabled/>
			</td>
		</tr>
		<tr>
			<td width="150" align="center" bgcolor="FF9933">내용</td>
			<td>
				<textarea rows="20" cols="60" name="content" id="i_content" disabled>${article.content}</textarea>
			</td>
		</tr>
		<c:if test="${not empty article.imageFileName && article.imageFileName != 'null'}">
		<tr>
			<td width="150" align="center" bgcolor="FF9933">이미지</td>
			<td>
				<input type="hidden" name="originalFileName" value="${article.imageFileName}"/>
				<img width="500" src="${contextPath}/download.do?imageFileName=${article.imageFileName}&articleNO=${article.articleNO}" id="preview"/>
			</td>
		</tr>
		<tr id="tr_image_modify">
			<td width="150" align="center" bgcolor="FF9933">파일경로</td>
			<td>
				<input type="file" name="imageFileName" id="i_imageFileName" disabled />
			</td>
		</tr>
		</c:if>
		<tr>
			<td width="150" align="center" bgcolor="FF9933">등록일자</td>
			<td>
				<input type="text" value="${article.writeDate}" disabled/>
			</td>
		</tr>
		<tr id="tr_btn_modify">
			<td align="center">
				<input type="button" data-oper='modify' value="수정반영하기" />
				<input type="button" data-oper='modify_off' value="취소"/>
			</td>
		</tr>
		<tr id="trb_btn">
			<td colspan=2 align="center">
				<input type="button" data-oper='modify_on' value="수정하기"/>
				<input type="button" data-oper='list' value="목록보기"/>
				<input type="button" data-oper='remove' value="삭제하기"/>
			</td>
		</tr>
	</table>
</form>
</body>
</html>