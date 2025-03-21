<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/user/businessRequest.js"></script>
	<title>사업자 등록 요청 상세</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />

		<div id="container">
			<jsp:include page="/include/myPageLeft.jsp"/>		
			<h2>사업자 등록 요청 상세</h2>
			<div>
				<fmt:formatDate value="${info.businessReqeustRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
				<fmt:formatDate value="${info.businessReqeustUpdateDate }" pattern="yyyy-MM-dd" var="updateDate" />
				<p>신청일 : ${createdDate }</p>
				<p>아이디 : ${info.username }</p>
				<p>이메일 : ${info.email }</p>
				<p>연락처 : ${info.phone }</p>
				<p>이름 : ${info.name }</p>
				
				<c:choose>
					<c:when test="${info.requestStatus eq 'WAIT' }">
						<p>요청 상태 : 대기</p>
					</c:when>
					<c:when test="${info.requestStatus eq 'APPROVAL' }">
						<p>요청 상태 : 승인</p>
						<p>승인 일자 : ${updateDate }</p>
					</c:when>
					<c:when test="${info.requestStatus eq 'DENIED' }">
						<p>요청 상태 : 거부</p>
						<p>거부 일자 : ${updateDate }</p>
					</c:when>
					<c:when test="${info.requestStatus eq 'REAPPLY' }">
						<p>요청 상태 : 재신청</p>
						<p>재신청 일자 : ${updateDate }</p>
					</c:when>
				</c:choose>
				
			</div>
			<div>
				<form action="requestInfo" method="post">
					<sec:authorize access="hasAuthority('ROLE_ADMIN')">
						<c:if test="${info.requestStatus eq 'WAIT' || info.requestStatus eq 'REAPPLY'}">
							<button id="submitBtn" type="submit">승인</button>
							<button id="deniedBtn" type="button">거부</button>								
						</c:if>
						<button onclick="location.href='/user/business/requestList'" type="button">목록</button>
					</sec:authorize>
					<sec:authorize access="hasAuthority('ROLE_MEMBER')">
						<c:choose>
							<c:when test="${info.requestStatus eq 'WAIT' || info.requestStatus eq 'REAPPLY' }">
								<button id="requestCancelBtn" type="button">요청 취소</button>													
							</c:when>
							<c:when test="${info.requestStatus eq 'DENIED' }">
								<button id="reapplyBtn" type="button">재신청</button>
							</c:when>
						</c:choose>
					</sec:authorize>
					<div>
						<input type="hidden" id="businessRequestId" name="businessRequestId" value="${info.businessRequestId }">
						<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
					</div>					
				</form>
			</div>
		</div>
	</div>
</body>
</html>