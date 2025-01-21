<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<title>사업자 등록 요청 상세</title>
	</head>
	<body>
		<div id="wrap">
			<jsp:include page="/include/header.jsp" />
			<fmt:formatDate value="${info.businessRequestRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
			
			<h2>사업자 등록 요청 상세</h2>
			<div>
				<p>식당명 : ${info.storeName }</p>
				<p>신청일 : ${createdDate }</p>
				<p>사업자명 : ${info.ownerName }</p>
				<p>전화번호 : ${info.storePhone }</p>
				<p>카테고리 : ${info.foodCategory }</p>
				<p>영업 시간 : ${info.businessHour }</p>
				<p>소개 : ${info.storeComment }</p>
				<p>상세 설명 : ${info.description }</p>
				<p>도로명 주소 : ${info.roadAddress }</p>
				<p>지번 주소 : ${info.jibunAddress }</p>
			</div>
			<div>
				<form action="requestInfo" method="post">
					<sec:authorize access="hasAuthority('ROLE_ADMIN')">
						<button type="submit">승인</button>
						<button onclick="refusal()">거부</button>	
						<button onclick="location.href='requestList'" >목록</button>
					</sec:authorize>
					<sec:authorize access="hasAuthority('ROLE_MEMBER')">
						<button onclick="requestCancel()">요청 취소</button>
					</sec:authorize>
					<div>
						<input type="hidden" id="businessRequestId" name="businessRequestId" value="${info.businessRequestId }">
						<input type="hidden" id="storeId" name="storeId" value="${info.storeId }">
						<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
					</div>					
				</form>
			</div>
		</div>
	</body>
</html>