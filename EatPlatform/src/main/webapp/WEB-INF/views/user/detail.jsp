<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<title>가입 정보</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/user/detail.js"></script>
</head>
<body>
	<sec:authentication property="principal" var="principal"/>
	<div id="wrap">
		<jsp:include page="/include/header.jsp"></jsp:include>
		
		<div id="container">
			<jsp:include page="/include/myPageLeft.jsp" />
			
			<div class="right">
				<h1>가입 정보</h1>
				<form id="userInfoForm" action="modify" method="post">
					<div>
						<span>아이디 : </span>
						<input class="no-cursor" type="text" id="username" name="username" readonly="readonly" value="${userInfo.username }">
					</div>
					<div>
						<span>이름 : </span>
						<input class="no-cursor" type="text" id="name" name="name" required="required" value="${userInfo.name }" readonly="readonly">
					</div>
					<div>
						<span>이메일 : </span>
						<input class="no-cursor" type="email" id="email" name="email" required="required" readonly="readonly" value="${userInfo.email }">
					</div>
					<div>
						<span>휴대폰 : </span>
						<input class="no-cursor" type="tel" id="phone" name="phone" required="required" oninput="autoHyphen(this)" value="${userInfo.phone }" readonly="readonly" maxlength="13">
					</div>
					<!-- CSRF 토큰 -->
			   		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">	
				</form>
				<div class="btn_container">
					<!-- <input type="submit" value="수정"> -->
					<button class="from_chg_btn">개인 정보 수정</button>
					<button onclick="location.href='modifyPw'">비밀번호 변경</button>
				</div>
				<div>
					<button id="memWithdrawal">회원 탈퇴</button>
				</div>		
			</div>
		</div>
	</div>
</body>
</html>