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
    <link rel="stylesheet" href="${ctp}/css/custom.css">
    <!-- 프론트 정규식으로 유효성 검사 -->
    <script src="${ctp}/js/registrationValidation.js"></script>
    <!-- 다음 주소 API 활용 -->
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="${ctp}/js/woo.js"></script>
</head>
<body>
<jsp:include page="/include/header.jsp" />

<section class="container mt-3" style="max-width: 560px;">
    <form name="myform" method="post" action="${ctp}/userRegisterAction.us">
        <div class="form-group">
            <label>아이디</label>
            <input type="text" name="userID" class="form-control">
        </div>
        <div class="form-group">
            <label>비밀번호</label>
            <input type="password" name="userPassword" class="form-control">
        </div>
        <div class="form-group">
            <label>이메일</label>
            <input type="email" name="userEmail" class="form-control">
        </div>
        
        <!-- 다음 주소 API 활용 -->
        <div class="form-group">
            <label for="address">주소</label>
            <div class="input-group mb-1">
                <input type="text" name="postcode" id="sample6_postcode" placeholder="우편번호" class="form-control">
                <div class="input-group-append">
                    <input type="button" onclick="sample6_execDaumPostcode()" value="우편번호 찾기" class="btn btn-secondary">
                </div>
            </div>
            <input type="text" name="roadAddress" id="sample6_address" size="50" placeholder="주소" class="form-control mb-1">
            <div class="input-group mb-1">
                <input type="text" name="detailAddress" id="sample6_detailAddress" placeholder="상세주소" class="form-control"> &nbsp;&nbsp;
                <div class="input-group-append">
                    <input type="text" name="extraAddress" id="sample6_extraAddress" placeholder="참고항목" class="form-control">
                </div>
            </div>
            <!-- address 필드 추가 -->
			<input type="hidden" name="address" id="sample6_fullAddress" />
        </div>
        <button type="button" class="btn btn-primary" onclick="fCheck()">회원가입</button>
    </form>
</section>

<jsp:include page="/include/footer.jsp" />
<!-- headerHome.js 파일을 연결합니다. -->
<script src="${ctp}/js/headerHome.js"></script>
</body>
</html>
