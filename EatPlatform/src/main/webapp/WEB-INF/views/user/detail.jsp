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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
			<div class="user_container">
				<p class="detail_title">계정 정보</p>
				<form id="userInfoForm" action="modify" method="post">
					<div>
						<label for="username">아이디</label>
						<input class="no-cursor" type="text" id="username" name="username" readonly="readonly" value="${userInfo.username }">
					</div>
					<div>
						<label for="name">이름</label>
						<input class="no-cursor" type="text" id="name" name="name" required="required" value="${userInfo.name }" readonly="readonly">
					</div>
					<div>
						<label for="email">E-Mail</label>
						<input class="no-cursor" type="email" id="email" name="email" required="required" readonly="readonly" value="${userInfo.email }">
					</div>
					<div>
						<label for="phone">휴대폰</label>
						<input class="no-cursor" type="tel" id="phone" name="phone" required="required" oninput="autoHyphen(this)" value="${userInfo.phone }" readonly="readonly" maxlength="13">
					</div>
					<!-- CSRF 토큰 -->
			   		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">	
				</form>
				<div class="btn_container">
					<button class="from_chg_btn">개인 정보 수정</button>
					<button onclick="location.href='/user/modifyPw'">비밀번호 변경</button>
					
					<button id="memWithdrawal">회원 탈퇴</button>
				</div>
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>