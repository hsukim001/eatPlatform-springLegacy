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
<title>비밀번호 수정</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/mail/searchUser.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script type="text/javascript">
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");			
		xhr.setRequestHeader(header, token);
	});
	
	$(document).ready(function(){
		let isPwChk = false;
		
		// 비밀번호 변경 버튼 액션
		$('#chgPwBtn').click(function(){
			if(isPwChk) {
				changePw();
			} else {
				alert('비밀번호를 다시 입력해주세요.');
				$('#password').val("");
				$('#userPwChk').val("");
				$('#pwChkMsg').text("비밀번호를 입력해주세요.");
			}
		});
		
		// 취소 버튼 액션
		$('#cancelBtn').click(function(){
			location.href = '/user/detail'
		});
		
		// 비밀번호 유효성 검사
		$('#password, #userPwChk').keyup(function(){
			let pwPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\d)[a-zA-Z\d!@#$%^&*]{8,}$/;
			
			// 비밀번호 일치 여부 확인
			if($('#password').val() === "" && $('#userPwChk').val() === "") {
				$('#pwChkMsg').text("비밀번호를 입력해주세요.");
				isPwChk = false;
			} else if($('#password').val() == $('#userPwChk').val()){
				$('#pwChkMsg').text("비밀번호가 일치합니다.");
				isPwChk = true;
			} else if($('#password').val() != $('#userPwChk').val()){
				$('#pwChkMsg').text("비밀번호가 일치하지 않습니다.");
				isPwChk = false;
			}
			
			// 비밀번호 패턴 검사
			if((pwPattern.test($('#password').val()) == false && $('#userPw').val() !== "")){
				$('#pwChkMsg').text("비밀번호는 8자 이상, 영문대소문자, 특수문자(!@#$%^&*) 각 한개 이상 포함하여 작성해야 합니다.");
				isPwChk = false;
			}
		});
		
		// 비밀번호 변경
		function changePw() {
			let email = $('#email').val();
			console.log('email : ' + email);
			
			let password = $('#password').val();
			$.ajax({
				url : 'modify/password',
				type : 'put',
				headers : {
					"Content-Type" : "application/json"
				},
				data : JSON.stringify({
					"password" : password,
					"email" : email
				}),
				success : function(response){
					console.log(response);
					if(response.result == 1) {
						alert(response.message);
						location.href = '../';
					} else {
						alert(response.message);
						location.href = '../';
					}
				}
			});
		}
	});
</script>
</head>
<body>
	<div id="wrap">
		<div id="container">
			<jsp:include page="/include/header.jsp"></jsp:include>
			<p class="title">비밀번호 수정</p>
			<div>
				<span>비밀번호 : </span>
				<input type="password" id="password" name="password" required="required">
			</div>
			<div>
				<span>비밀번호 확인 : </span>
				<input type="password" id="userPwChk" name="userPwChk" required="required">
			</div>
			<div>
				<p id="pwChkMsg">비밀번호를 입력해주세요.</p>
			</div>
			<div>
				<input type="hidden" id="email" name="email" value="${email }">
			</div>
			<div>
				<button id="chgPwBtn">변경</button>
				<button id="cancelBtn">취소</button>
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>