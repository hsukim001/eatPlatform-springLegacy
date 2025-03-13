<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>로그인</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/access/login.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/access/loginBannerSlider.js"></script>
	<script type="text/javascript">
		function noBack(){window.history.forward(); alert('잘못된 접근 입니다.');}
		
		document.addEventListener("DOMContentLoaded", function() {
			let message = $('#message').val();
			
			if(message) {
				alert(message);
			}
		});
		
	</script>
</head>


<body onpageshow="if(event.persisted) noBack();">
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<p class="login_title">Login</p>
			<p class="login_comment">로그인을 위해 아이디와 비밀번호를 입력해주세요.</p>
			<div class="login_container">
				<div class="form_box">
					<form action="login" method="post">
						<!-- CSRF 토큰 -->
					    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
						<div class="login_input">
							<input type="text" id="username" name="username"><br>
							<input type="password" id="password" name="password">
						</div>
						<input type="submit" value="로그인">
					</form>
					<div class="btn_place">
						<button type="button" id="searchId" onclick="location.href='/user/searchId'">아이디 찾기</button>						
						<button type="button" id="searchPw" onclick="location.href='/user/searchPw'">비밀번호 찾기</button>						
					</div>
					<div class="join_btn">
						<button type="button" id="createdMember" onclick="location.href='../user/register'">회원가입</button>	
					</div>
				</div>
				<div class="img_box">
				    <div id="img_slide_box">
				        <div class="img_slide_wrapper">
				            <div class="img_slide_item"><img src="<%=request.getContextPath()%>/resources/img/access/banner1.png" alt="배너이미지1"></div>
				            <div class="img_slide_item"><img src="<%=request.getContextPath()%>/resources/img/access/banner2.png" alt="배너이미지2"></div>
				            <div class="img_slide_item"><img src="<%=request.getContextPath()%>/resources/img/access/banner3.png" alt="배너이미지3"></div>
				        </div>
				    </div>
				    <div id="slider_dots"></div>
				</div>
			</div>
			<input type="hidden" id="message" name="message" value="${message }" >
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>