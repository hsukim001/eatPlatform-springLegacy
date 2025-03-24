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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/business/request.css">
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
			<div id="request_container">
				<p class="request_title">사업자 등록 요청 상세</p>
				<ul>
					<fmt:formatDate value="${info.businessReqeustRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
					<fmt:formatDate value="${info.businessReqeustUpdateDate }" pattern="yyyy-MM-dd" var="updateDate" />
					<li>
						<span class="textTitle">신청일</span>
						<span class="textValue">${createdDate }</span>
					 </li>
					<li>
						<span class="textTitle">아이디</span>
						<span class="textValue">${info.username }</span>
					</li>
					<li>
						<span class="textTitle">이메일</span>
						<span class="textValue">${info.email }</span>
					</li>
					<li>
						<span class="textTitle">연락처</span>
						<span class="textValue">${info.phone }</span>
					</li>
					<li>
						<span class="textTitle">이름</span>
						<span class="textValue">${info.name }</span></li>
					
					<c:choose>
						<c:when test="${info.requestStatus eq 'WAIT' }">
							<li>
								<span class="textTitle">요청 상태</span>
								<span class="textValue">대기</span>
							</li>
						</c:when>
						<c:when test="${info.requestStatus eq 'APPROVAL' }">
							<li>
								<span class="textTitle">요청 상태</span>
								<span class="textValue">승인</span>
							</li>
							<li>
								<span class="textTitle">승인 일자</span>
								<span class="textValue">${updateDate }<</span>
							</li>
						</c:when>
						<c:when test="${info.requestStatus eq 'DENIED' }">
							<li>
								<span class="textTitle">요청 상태</span>
								<span class="textValue">거부</span>
							</li>
							<li>
								<span class="textTitle">거부 일자</span>
								<span class="textValue">${updateDate }<</span>
							</li>
						</c:when>
						<c:when test="${info.requestStatus eq 'REAPPLY' }">
							<li>
								<span class="textTitle">요청 상태</span>
								<span class="textValue">재신청</span>
							</li>
							<li>
								<span class="textTitle">재신청 일자</span>
								<span class="textValue">${updateDate }</span>
							</li>
						</c:when>
					</c:choose>
					
				</ul>
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
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>