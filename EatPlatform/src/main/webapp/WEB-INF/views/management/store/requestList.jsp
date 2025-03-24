<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<style type="text/css">
		table, th, td {
			border-style: solid;
			border-width: 1px;
			text-align: center;
		}
		
		ul {
			list-style-type: none;
			text-align: center;
		}
		
		li {
			display: inline-block;
		}
	</style>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script type="text/javascript">
		$(function() {
			$(document).on('click', '.list_row', function() {
				let storeId = $(this).data('id-value');
				let pageNum = $('.current_page_num').data('page-value');
				location.href = "/management/store/requestInfo?storeId=" + storeId + "&pageNum=" + pageNum;
			});
		});
	</script>
	<title>가게 등록 요청</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		
		<div id="container">
			<jsp:include page="/include/myPageLeft.jsp"/>
			
			<h2>가게 등록 요청 목록</h2>
			<ul>
				<li>
					<div>번호</div>
					<div>식당명</div>
					<div>신청 아이디</div>
					<div>연락처</div>
					<div>신청일</div>
					<div>상태</div>
				</li>
			</ul>
			<ul>
				<c:forEach var="list" items="${list }">
					<li class="list_row" data-id-value="${list.storeId }">
						<div>${list.storeId }</div>
						<div>${list.storeName }</div>
						<div>${list.storeUserId }</div>
						<div>${list.phone }</div>
						<fmt:formatDate value="${list.storeApprovalsRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
						<div>${createdDate }</div>
						<c:choose>
							<c:when test="${list.approvals == 0 }">
								<div>대기</div>							
							</c:when>
							<c:when test="${list.approvals == 1 }">
								<div>승인</div>
							</c:when>
						</c:choose>
					</li>
				</c:forEach>
			</ul>
			<ul>
				<!-- 이전 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isPrev() }">
					<li><a href="/management/store/requestList?pageNum=${pageMaker.startNum - 1}">이전</a></li>
				</c:if>
				<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
				<c:forEach begin="${pageMaker.startNum }"
					end="${pageMaker.endNum }" var="num">
					<li class="current_page_num" data-page-value="${pageMaker.pagination.pageNum }"><a href="/management/store/requestList?pageNum=${num }">${num }</a></li>
				</c:forEach>
				<!-- 다음 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isNext() }">
					<li><a href="/management/store/requestList?pageNum=${pageMaker.endNum + 1}">다음</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</body>
</html>