<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp" %>
<style>
	.cls1{text-decoration: none;}
	.cls2{text-align: center, font-size: 30px;}
</style>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	$(function() {
		let addItem = '<c:out value="${param.addItem}"/>';
		if(addItem) {
			alert("새글이 추가되었습니다.");
			/* history.replaceState(null, null, location.pathname); */
			console.log(location.pathname);
		}
		
		/* 게시물 삭제시 경고창 화면에 출력 */
		let delItem = '<c:out value="${param.delItem}"/>';
		if(delItem) {
			alert("글이 삭제되었습니다.");
			let url = new URL(location.href);
			let sp = new URLSearchParams(url.search);
			sp.delete("delItem");
			history.replaceState(null, null, "?" + sp.toString());
		}
	});
</script>
	<table align="center" border="1" width="80%">
		<tr height="10" align="center" bgcolor="lightgreen">
			<td>글번호</td>
			<td>작성자</td>
			<td>제목</td>
			<td>작성일</td>
		</tr>
		<c:choose>
		<c:when test="${empty articlesList}">
		<tr height="10">
			<td colspan="4">
				<p align="center">
					<b><span style="font-size:9pt">등록된 게시글이 없습니다.</span></b>
				</p>
			</td>
		</tr>
		</c:when>
		<c:otherwise>
		<c:forEach items="${articlesList}" var="articleVO" varStatus="articleNum">
		<tr align="center">
			<td width="5%">${articleNum.count}</td>
			<td width="10%">${articleVO.id}</td>	<!-- ArticleVO의 getId() 호출 -->
			<td align="left" width="35%">
				<span style="padding-right:30px"></span>
				<c:if test="${articleVO.level > 1}">
					<c:forEach begin="1" end="${articleVO.level}" step="1">
						<span style="padding-left:20px"></span>
					</c:forEach>
						<span style="font0size:12px">&#9492;[답변]</span>
				</c:if>
				<a class="cls1" href="${contextPath}/board/viewArticle.do?articleNO=${articleVO.articleNO}">${articleVO.title}</a>
			</td>
			<td width="10%">${articleVO.writeDate}</td>			
		</tr>
		</c:forEach>
		</c:otherwise>
		</c:choose>
	</table>
<a class="cls1" href="${contextPath}/board/articleForm.do"><p class="cls2">글쓰기</p></a>
<%@ include file="../includes/footer.jsp" %>