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
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/modal.css">
		
		<style type="text/css">
			.spanBtn {
				cursor: pointer;
			}
			
			.selectSpan {
				font-weight: bold;
				color: blue;
				cursor: pointer;
			}
			.page-link-select {
				font-weight: bold;
				color: blue;
				pointer-events: none; /* 클릭 비활성화 */
    			cursor: default; /* 기본 커서 */
			}
			
			.reservRow {
				cursor: pointer;
			}
		</style>
		
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/management/reserv.js"></script>
		<title>예약 목록</title>
	</head>
	<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		
		<div id="container">
			<jsp:include page="/include/myPageLeft.jsp"/>
		
			<h1>나의 예약 목록s</h1>
			<div class="search-container">
				<div class="search-type">
					<span id="allListBtn" class="spanBtn selectSpan">전체</span>
					<span id="currentListBtn" class="spanBtn">예약 일정</span>
					<span id="prevListBtn" class="spanBtn">이전 예약 내역</span>
					<span id="cancelListBtn" class="spanBtn">예약 취소 내역</span>
				</div>
				<div class="search-title">
					<input id="keyword" type="text" placeholder="가게 이름을 입력해주세요.">
					<span class="searchBtn">검색</span>					
				</div>
			</div>
			
			<div>
				<p id="totalCount"></p>
				<table>
					<thead id="tableHead">
						<tr>
							<th>번호</th>
							<th>식당명</th>
							<th>예약 일자</th>
							<th>예약 인원</th>
							<th>상태</th>
							<th>예약 신청일</th>
						</tr>
					</thead>
					<tbody id="tableBody">
						<!-- ajax로 table load -->
					</tbody>
				</table>
				<div id="pagination">
					<!-- ajax로 pagination load -->
				</div>
				
				<div>
					<jsp:include page="/include/modal/reservInfo.jsp" />
				</div>
			</div>
		</div>
	</div>
	
	</body>

</html>