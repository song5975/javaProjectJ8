<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
  String userID = session.getAttribute("sUserID")==null ? "" : (String) session.getAttribute("sUserID");
  pageContext.setAttribute("pUserID", userID);  
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="http://192.168.50.68:9090/javaProjectJ8">강의평가 웹 사이트</a>
    <!-- navbar 라는 이름을 가진 요소가 보였다가 보이지 않았다가 반복 -->
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div id="navbar" class="collapse navbar-collapse">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" id="dropdown" data-toggle="dropdown">
                    메뉴
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdown">
                    <c:if test="${empty pUserID}"><a class="dropdown-item" href="userLogin.us">로그인</a></c:if>
                    <c:if test="${empty pUserID}"><a class="dropdown-item active" href="userJoin.us">회원가입</a></c:if>
                    <c:if test="${not empty pUserID}"><a class="dropdown-item" href="#" onclick="confirmLogout()">로그아웃</a></c:if>
                </div>
            </li>
            <c:if test="${sLevel == 0}">
	            <li class="nav-item dropdown">
	                <a class="nav-link dropdown-toggle" id="dropdown" data-toggle="dropdown">
	                    관리자용
	                </a>
	                <div class="dropdown-menu" aria-labelledby="dropdown">
	                    <a class="dropdown-item" href="#">회원 관리</a>
	                    <a class="dropdown-item" href="#">강의 관리</a>
	                    <a class="dropdown-item" href="#">기타</a>
	                </div>
	            </li>
            </c:if>
        </ul>
<%--         <c:if test="${not empty pUserID}">
        <form action="./index.jsp" class="form-inline my-2 my-lg-0">
            <input type="text" name="search" class="form-control mr-sm-2" type="search" placeholder="내용을 입력하세요." aria-label="search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
        </form>
        </c:if> --%>
    </div>
</nav>
