<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/business/list.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/management/reviewReport.js"></script>
<script type="text/javascript">
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	       
		xhr.setRequestHeader(header, token);
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<p class="list_title">리뷰 신고 목록</p>
			<p class="totalCount">총 : ${pageMaker.totalCount }건</p>
			<div id="table_container">
				<ul id="tableHead">
						<li>번호</li>
						<li>식당명</li>
						<li>신고자명</li>
						<li>신고 접수일</li>
						<li>신고 횟수</li>
				</ul>
				<div id="tableBody">
					<c:if test="${empty list}">
					    <ul>
					    	<li class="no_data">목록이 존재하지 않습니다.</li>
					    </ul>
					</c:if>
					
					<c:if test="${not empty list}">
						<c:forEach var="list" items="${list }">
							<ul data-id-value="${list.reviewId }">
								<li>${list.reviewId }</li>
								<li>${list.storeName }</li>
								<li>${list.name }</li>
								<li><fmt:formatDate value="${list.reviewUpdateDate}" pattern="yyyy-MM-dd" /></li>
								<li>${list.reviewReport }</li>
							</ul>
						</c:forEach>
					</c:if>
				
				</div>
			</div>
			<ul class="page-container">
				<!-- 이전 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isPrev() }">
					<li><a href="list?pageNum=${pageMaker.startNum - 1}">이전</a></li>
				</c:if>
				<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
				<c:forEach begin="${pageMaker.startNum }"
					end="${pageMaker.endNum }" var="num">
					<li>${num }</li>
				</c:forEach>
				<!-- 다음 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isNext() }">
					<li><a href="list?pageNum=${pageMaker.endNum + 1}">다음</a></li>
				</c:if>
			</ul>
			<div>
				<jsp:include page="/include/modal/reviewReportInfo.jsp" />
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />
	</div>
</body>

</html>