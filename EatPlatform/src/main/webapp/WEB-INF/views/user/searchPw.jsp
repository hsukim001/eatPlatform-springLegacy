<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/search.css">
<title>비밀번호 찾기</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/mail/searchUser.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/mail/searchUser.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		// 비밀번호 찾기 버튼 액션
		$('#searchBtn').click(function(){
			if(isChkCode) {
				location.href="modifyPw?email=" + email;
			} else {
				alert('이메일 인증을 완료해주세요.');
			}
		});
		
		// 로그인 페이지 이동 버튼 액션
		$('#cancelBtn').click(function(){
			location.href="../access/login";
		});
		
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp"></jsp:include>
		<div id="container">
			<p class="title">비밀번호 찾기</p>
			<span class="comment">비밀번호를 찾고자 하는 아이디와 이메일을 입력해주세요.</span>
			<div>
				<label for="username">아이디 : </label>
				<input type="text" id="username" name="username" required="required" placeholder="가입한 아이디">
			</div>
			<div>
				<label for="email">이메일 : </label>
				<input type="email" id="email" name="email" required="required" placeholder="가입한 이메일">
				<button id="sendCodeBtn">인증번호 받기</button>
			</div>
			<div class="divHide" id="chkCodeDiv">
				<label for="chkCode">인증 번호 : </label>
				<input type="text" id="chkCode" name="chkCode">
				<button id="chkCodeBtn">확인</button>
			</div>
			<div>
				<button id="searchBtn">찾기</button>
				<button id="cancelBtn">취소</button>
			</div>
			<input type="hidden" id="mailType" name="mailType" value="비밀번호">
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>