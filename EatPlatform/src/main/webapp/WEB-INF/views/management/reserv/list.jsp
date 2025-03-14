<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="_csrf" content="${_csrf.token}"/>
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
		
			<h1>나의 예약 목록</h1>
			<div>
				<span id="reservList" class="selectSpan">예약 목록</span>
				<span id="preReservHistory" class="spanBtn">이전 예약 내역</span>
				<span id="cancelReservHistory" class="spanBtn">예약 취소 내역</span>
			</div>
			
			<div>
				<p id="totalCount"></p>
				<table>
					<thead id="tableHead">
						<!-- js로 table head load -->
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