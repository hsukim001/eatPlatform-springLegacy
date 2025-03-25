<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="_csrf" content="${_csrf.token}"/>
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reserv/list.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/modal.css">		
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/management/reserv.js"></script>
		<title>예약 목록</title>
	</head>
	<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		
		<div id="container">
			<div id="list_container">
				<p class="list_title">사업장 예약 관리</p>	
				<div id="search-container">
				
					<ul class="search-type">
						<li id="allListBtn" class="selectSpan">
							전체
						</li>
						<li id="currentListBtn">
							예약 일정
						</li>
						<li id="prevListBtn">
							이전 예약 내역
						</li>
						<li id="cancelListBtn">
							예약 취소 내역
						</li>
					</ul>
					<div class="search-title">
						<input id="keyword" type="text" placeholder="가게 이름을 입력해주세요.">
						<span class="searchBtn">검색</span>					
					</div>
				</div>
				<p id="totalCount"></p>
			</div>
			
			<div id="table_container">
				<ul id="tableHead">
					<li>번호</li>
					<li>식당명</li>
					<li>예약 일자</li>
					<li>예약 인원</li>
					<li>상태</li>
					<li>예약 신청일</li>
				</ul>
				<div id="tableBody"></div>
				<div id="pagination"></div>
			</div>
				
			<div>
				<jsp:include page="/include/modal/reservInfo.jsp" />
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
	
	</body>

</html>