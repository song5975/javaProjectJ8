<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>userLogout.jsp</title>
	<jsp:include page="/include/bs4.jsp" />
	<style>
		.center-text {
			text-align: center;
		}
	</style>
</head>
<body>
<jsp:include page="/include/header.jsp" />
<p><br/></p>
<div class="container">
    <div class="center-text">
        <h3>다음에 또 이용해주세요!</h3>
    </div>
</div>
<p><br/></p>
<jsp:include page="/include/footer.jsp" />
</body>
</html>
