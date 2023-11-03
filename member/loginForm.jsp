<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../includes/header.jsp" %>
<div class="container">
	<div class="jumbotron text=center p-3 my-3 gb-primary text-white">
		<h1>로그인</h1>
	</div>
	<form method="post" action="${contextPath}/member/login.do">
		<div class="form-group">
			아이디:<input type="text" class="form-control" name="id" placeholder="아이디를 입력하세요." required="required">
		</div><br>
		<div class="form-group">
			비밀번호:<input type="password" class="form-control" name="pwd" placeholder="비밀번호를 입력하세요." required="required">
		</div>
		<div class="col text-center">
			<button class="btn btn-success">로그인</button>
			<button class="btn btn-warning" type="reset">취소</button>
		</div>
	</form>
</div>
<%@ include file="../includes/footer.jsp" %>
