<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}" />

<%
	// 로그인하지 않으면 전체 내용을 볼 수 없음(pUserID 가 empty) - 구현
	String userID = session.getAttribute("sUserID")==null ? "" : (String) session.getAttribute("sUserID");
	pageContext.setAttribute("pUserID", userID);  
	// 이메일 인증을 하지 않으면 등록, 신고를 할 수 없음(보는 것만 가능) - 구현
	boolean userEmailChecked = session.getAttribute("sUserEmailChecked") == null ? false : (boolean) session.getAttribute("sUserEmailChecked");
	pageContext.setAttribute("pUserEmailChecked", userEmailChecked);
	// 관리자만 관리자 페이지를 사용할 수 있음 - 미구현
	int level = session.getAttribute("sLevel") == null ? 1 : (int) session.getAttribute("sLevel");
	pageContext.setAttribute("pLevel", level);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- favicon 404 Error 방지 -->
    <link rel="shortcut icon" href="data:image/x-icon" type="image/x-icon">
    <title>강의평가 웹 사이트</title>
    <!-- 부트스트랩 추가하기 -->
    <jsp:include page="/include/bs4.jsp" />
    <!-- 커스텀 CSS 추가하기 -->
    <link rel="stylesheet" href="./css/custom.css">
    <script>
        'use strict';
		/* 
        $(document).ready(function() {
            $('#myformSearch').submit();
        });
		 */
		 
        /* 
        $(function () {
            $('#myformSearch').submit();
        });
        */

        $(document).ready(function() {
            var submitted = false;

            $('#myformSearch').submit(function() {
                if (!submitted) {
                    submitted = true;
                    // 추가로 실행할 코드 작성
                }
            });

            // 초기에 한 번은 실행
            $('#myformSearch').submit();
        });

        function confirmAndLike(evaluationID) {
            if (confirm('추천하시겠습니까?')) {
                window.location.href = "${ctp}/likeAction.li?evaluationID=" + evaluationID;
            }
        }

        function confirmAndDelete(evaluationID) {
            if (confirm('삭제하시겠습니까?')) {
                window.location.href = "${ctp}/deleteAction.li?evaluationID=" + evaluationID;
            }
        }

        // header 안의 nav 에서 로그아웃 버튼을 눌렀을 때
        function confirmLogout() {
            var response = confirm("로그아웃 하시겠습니까?");
            if (response == true) {
                // 사용자가 확인을 눌렀을 때
                window.location.href = "userLogout.us";
            } else {
                // 사용자가 취소를 눌렀을 때
                // 아무 동작 없음
            }
        }
    </script>
<!-- EvaluationValidation.js 파일을 연결합니다. -->
<script src="${ctp}/js/EvaluationValidation.js"></script>
<!-- ReportValidation.js 파일을 연결합니다. -->
<script src="${ctp}/js/ReportValidation.js"></script>
</head>
<body>
<jsp:include page="/include/header.jsp" />
<c:if test="${not empty pUserID}">
<!-- 로그인하지 않으면 전체 내용을 볼 수 없음(pUserID 가 empty) -->
<section class="container">
    <form name="myformSearch" method="get" action="${ctp}/evaluationSearchAction.ev" class="form-inline mt-3">
        <select name="lectureDivide" class="form-control mx-1 mt-2">
            <option value="전체">전체</option>
            <option value="전공">전공</option>
            <option value="교양">교양</option>
            <option value="기타">기타</option>
        </select>
        <select name="searchType" class="form-control mx-1 mt-2">
            <option value="최신순">최신순</option>
            <option value="추천순">추천순</option>
        </select>
        <input type="text" name="search" class="form-control mx-1 mt-2" placeholder="내용을 입력하세요.">
        <button type="submit" class="btn btn-primary mx-1 mt-2">검색하기</button>

        <c:if test="${pUserEmailChecked == true}">
            <a class="btn btn-primary mx-1 mt-2" data-toggle="modal" href="#registerModal">등록하기</a>
            <a class="btn btn-danger mx-1 mt-2" data-toggle="modal" href="#reportModal">신고하기</a>
        </c:if>
    </form>

<!-- =============================================================================================================== -->
	
	<c:forEach var="vo" items="${evaluationList}" varStatus="st">
    <div class="card bg-light mt-3">
        <div class="card-header bg-light">
            <div class="row">
                <div class="col-8 text-left">${vo.lectureName}&nbsp;<small>${vo.professorName}</small></div>
                <div class="col-4 text-right">
                    종합&nbsp;<span style="color: red;">${vo.totalScore}</span>
                </div>
            </div>
        </div>
        <div class="card-body">
            <h5 class="card-title">
                ${vo.evaluationTitle}&nbsp;<small>(${vo.lectureYear}년 ${vo.semesterDivide}) / (작성자 : ${vo.userID})</small>
            </h5>
            <p class="card-text">${vo.evaluationContent}</p>
            <div class="row">
                <div class="col-9 text-left">
                    성적<span style="color: red;">${vo.creditScore}</span>
                    널널<span style="color: red;">${vo.comfortableScore}</span>
                    강의<span style="color: red;">${vo.lectureScore}</span>
                    <span style="color: green;">(추천: ${vo.likeCount})</span>
                </div>
                <div class="col-3 text-right">
					<a href="#" onclick="confirmAndLike(${vo.evaluationID})" class="btn btn-success btn-sm">추천</a>
					<a href="#" onclick="confirmAndDelete(${vo.evaluationID})" class="btn btn-danger btn-sm">삭제</a>
                </div>
            </div>
        </div>
    </div>
    </c:forEach>
    
    <div class="mt-3 mb-3">
    <!-- 페이지 번호 출력 및 다음 페이지로 이동하는 링크 -->
	<c:if test="${not empty nextPageNumber}">
	    <c:set var="prevPageNumber" value="${nextPageNumber - 2}" />
	    <c:choose>
	        <c:when test="${prevPageNumber >= 0}">
	            <%-- <a href="${ctp}/evaluationSearchAction.ev?pageNumber=${prevPageNumber}&lectureDivide=${param.lectureDivide}&searchType=${param.searchType}&search=${param.search}" class="btn btn-primary btn-sm">이전 페이지</a> --%>
	            <a href="${ctp}/evaluationSearchAction.ev?pageNumber=${prevPageNumber}&lectureDivide=${lectureDivide}&searchType=${searchType}&search=${search}" class="btn btn-primary btn-sm">이전 페이지</a>
	        </c:when>
	        <c:otherwise>
	            <a href="#" style="display: none;" class="btn btn-primary btn-sm">이전 페이지</a>
	        </c:otherwise>
	    </c:choose>
	    <c:if test="${not empty evaluationList}">
	    <%-- <a href="${ctp}/evaluationSearchAction.ev?pageNumber=${nextPageNumber}&lectureDivide=${param.lectureDivide}&searchType=${param.searchType}&search=${param.search}" class="btn btn-primary btn-sm">다음 페이지</a> --%>
	    <a href="${ctp}/evaluationSearchAction.ev?pageNumber=${nextPageNumber}&lectureDivide=${lectureDivide}&searchType=${searchType}&search=${search}" class="btn btn-primary btn-sm">다음 페이지</a>
	    </c:if>
	</c:if>
	</div>
</section>

<!-- =============================================================================================================== -->

<div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="modal">평가 등록</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form name="myformEvaluation" method="post" action="${ctp}/evaluationRegisterAction.ev">
					<div class="form-row">
						<div class="form-group col-sm-6">
							<label>강의명</label>	
							<input type="text" name="lectureName" class="form-control" maxlength="20">
						</div>
						<div class="form-group col-sm-6">
							<label>교수명</label>
							<input type="text" name="professorName" class="form-control" maxlength="20">
						</div>
					</div>
					<div class="form-row">
						<div class="form-group col-sm-4">
							<label>수강 연도</label>
							<select name="lectureYear" class="form-control">
								<option value="2011">2011</option>
								<option value="2012">2012</option>
								<option value="2013">2013</option>
								<option value="2014">2014</option>
								<option value="2015">2015</option>
								<option value="2016">2016</option>
								<option value="2017">2017</option>
								<option value="2018">2018</option>
								<option value="2019">2019</option>
								<option value="2020">2020</option>
								<option value="2021">2021</option>
								<option value="2022">2022</option>
								<option value="2023" selected>2023</option>
							</select>
						</div>
						<div class="form-group col-sm-4">
							<label>수강 학기</label>
							<select name="semesterDivide" class="form-control">
								<option value="1학기" selected>1학기</option>
								<option value="여름학기">여름학기</option>
								<option value="2학기">2학기</option>
								<option value="겨울학기">겨울학기</option>
							</select>
						</div>
						<div class="form-group col-sm-4">
							<label>강의 구분</label>
							<select name="lectureDivide" class="form-control">
								<option value="전공" selected>전공</option>
								<option value="교양">교양</option>
								<option value="기타">기타</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label>제목</label>
						<input type="text" name="evaluationTitle" class="form-control" maxlength="30">
					</div>
					<div class="form-group">
						<label>내용</label>
						<textarea name="evaluationContent" class="form-control" maxlength="2048" style="heigth:180px;"></textarea>
					</div>
					<div class="form-row">
						<div class="form-group col-sm-3">
							<label>종합</label>
							<select name="totalScore" class="form-control">
								<option value="A" selected>A</option>
								<option value="B">B</option>
								<option value="C">C</option>
								<option value="D">D</option>
								<option value="F">F</option>
							</select>
						</div>
						<div class="form-group col-sm-3">
							<label>성적</label>
							<select name="creditScore" class="form-control">
								<option value="A" selected>A</option>
								<option value="B">B</option>
								<option value="C">C</option>
								<option value="D">D</option>
								<option value="F">F</option>
							</select>
						</div>
						<div class="form-group col-sm-3">
							<label>널널</label>
							<select name="comfortableScore" class="form-control">
								<option value="A" selected>A</option>
								<option value="B">B</option>
								<option value="C">C</option>
								<option value="D">D</option>
								<option value="F">F</option>
							</select>
						</div>
						<div class="form-group col-sm-3">
							<label>강의</label>
							<select name="lectureScore" class="form-control">
								<option value="A" selected>A</option>
								<option value="B">B</option>
								<option value="C">C</option>
								<option value="D">D</option>
								<option value="F">F</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
						<button type="submit" class="btn btn-primary">등록하기</button>						
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<!-- =============================================================================================================== -->

<div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="modal">신고하기</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form name="myformReport" method="post" action="${ctp}/evaluationReportAction.ev" >
					<div class="form-group">
						<label>신고 제목</label>
						<input type="text" name="reportTitle" class="form-control" maxlength="30">
					</div>
					<div class="form-group">
						<label>신고 내용</label>
						<textarea name="reportContent" class="form-control" maxlength="2048" style="heigth:180px;"></textarea>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
						<button type="submit" class="btn btn-danger">신고하기</button>						
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

</c:if>

<jsp:include page="/include/footer.jsp" />
<!-- headerHome.js 파일을 연결합니다. -->
<script src="${ctp}/js/headerHome.js"></script>
</body>
</html>