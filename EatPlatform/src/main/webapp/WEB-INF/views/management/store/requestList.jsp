<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/business/list.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
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
			
			<p class="list_title">사업장 등록 요청 목록</p>
			<div id="table_container">
				<ul id="tableHead">
					<li>번호</li>
					<li>식당명</li>
					<li>신청 아이디</li>
					<li>연락처</li>
					<li>신청일</li>
					<li>상태</li>
				</ul>
				<div id="tableBody">
					<c:forEach var="list" items="${list }">
						<ul class="list_row" data-id-value="${list.storeId }">
							<li>${list.storeId }</li>
							<li>${list.storeName }</li>
							<li>${list.storeUserId }</li>
							<li>${list.phone }</li>
							<fmt:formatDate value="${list.storeApprovalsRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
							<li>${createdDate }</li>
							<c:choose>
								<c:when test="${list.approvals == 0 }">
									<li>대기</li>							
								</c:when>
								<c:when test="${list.approvals == 1 }">
									<li>승인</li>
								</c:when>
								<c:when test="${list.approvals == 2 }">
									<li>거부</li>
								</c:when>
							</c:choose>
						</ul>
					</c:forEach>
				</div>
			</div>
			<ul class="page-container">
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
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>