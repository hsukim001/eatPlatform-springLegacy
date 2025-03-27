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
<title>아이디 찾기</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/mail/searchUser.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/user/detail.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		function searchId(){
			$.ajax({
				url : 'search/id/' + email + '/',
				type : 'get',
				hearders : {
					"Content-Type" : "application/json"
				},
				success : function(response){
					alert('아이디는 ' + response + ' 입니다.');
					location.href='../';
				}
			});
		}
		
		// 찾기 버튼 액션
		$('#searchBtn').click(function(){
			if(isChkCode) {
				searchId();
			} else {
				alert('이메일 인증을 완료해주세요.');
			}
		});
		
		// 취소 버튼 액션
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
			<p class="title">아이디 찾기</p>
			<span class="comment">회원가입에 사용한 이메일을 입력해주세요.</span>
			<div>
				<label for="email">이메일 : </label>
				<input type="email" id="email" name="email">
				<button id="sendCodeBtn">인증번호 받기</button>
			</div>
			<div class="divHide" id="chkCodeDiv">
				<label for="chkCode">인증 번호 : </label>
				<input type="text" name="chkCode" id="chkCode">
				<button id="chkCodeBtn">확인</button>
			</div>
			<div>
				<button id="searchBtn">찾기</button>
				<button id="cancelBtn">취소</button>
			</div>
			<input type="hidden" id="mailType" name="mailType" value="아이디">
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>