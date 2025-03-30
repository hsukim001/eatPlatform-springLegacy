<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
<script>
	$(function(){
		$('#tableBody ul li').click(function(){
			let listNum = $(this).closest('ul').find('.list_num').text();
			location.href = 'requestInfo?businessRequestId=' + listNum;
		});
	});
</script>
<title>사업자 등록 요청</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="continer">
			<p class="list_title">사업자 등록 요청 목록</p>
			
			
			<div id="table_container">
				<ul id="tableHead">
					<li>번호</li>
					<li>아이디</li>
					<li>이름</li>
					<li>신청일</li>
					<li>상태</li>
				</ul>
			
				<div id="tableBody">
					<c:choose>
						<c:when test="${not empty list }">
							<c:forEach var="list" items="${list }">
								<ul>
									<li class="list_num">${list.businessRequestId }</li>
									<li>${list.username }</li>
									<li>${list.name }</li>
									<fmt:formatDate value="${list.businessReqeustUpdateDate }" pattern="yyyy-MM-dd" var="updateDate" />
									<li>${updateDate }</li>
									<c:if test="${list.requestStatus eq 'APPROVAL' }">
										<li>승인</li>											
									</c:if>
									<c:if test="${list.requestStatus eq 'DENIED' }">
										<li>거부</li>
									</c:if>
									<c:if test="${list.requestStatus eq 'WAIT' }">
										<li>대기</li>
									</c:if>
									<c:if test="${list.requestStatus eq 'REAPPLY' }">
										<li>재신청</li>
									</c:if>
								</ul>							
							</c:forEach>							
						</c:when>
						<c:otherwise>
							<li class="no_data">목록이 존재하지 않습니다.</li>									
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<ul class="page-container">
				<!-- 이전 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isPrev() }">
					<li><a href="requestList?pageNum=${pageMaker.startNum - 1}">이전</a></li>
				</c:if>
				<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
				<c:forEach begin="${pageMaker.startNum }"
					end="${pageMaker.endNum }" var="num">
					<li><a href="requestList?pageNum=${num }">${num }</a></li>
				</c:forEach>
				<!-- 다음 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isNext() }">
					<li><a href="requestList?pageNum=${pageMaker.endNum + 1}">다음</a></li>
				</c:if>
				</ul>
			</div>
		<jsp:include page="/include/footer.jsp" />	
		</div>
	</body>
</html>