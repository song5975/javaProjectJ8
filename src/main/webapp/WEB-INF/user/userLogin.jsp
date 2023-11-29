<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>강의평가 웹 사이트</title>
    <!-- 부트스트랩 추가하기 -->
    <jsp:include page="/include/bs4.jsp" />
    <!-- 커스텀 CSS 추가하기 -->
    <link rel="stylesheet" href="./css/custom.css">
</head>
<body>
<jsp:include page="/include/header.jsp" />
<section class="container mt-3" style="max-width: 560px;">
	<form name="myform" method="post" action="${ctp}/userLoginAction.us">
		<div class="form-group">
			<label>아이디</label>
			<input type="text" name="userID" class="form-control">
		</div>
		<div class="form-group">
			<label>비밀번호</label>
			<input type="password" name="userPassword" class="form-control">
		</div>
		<button type="submit" class="btn btn-primary">로그인</button>
	</form>
</section>

<!-- =============================================================================================================== -->


<jsp:include page="/include/footer.jsp" />
<!-- headerHome.js 파일을 연결합니다. -->
<script src="${ctp}/js/headerHome.js"></script>
</body>
</html>