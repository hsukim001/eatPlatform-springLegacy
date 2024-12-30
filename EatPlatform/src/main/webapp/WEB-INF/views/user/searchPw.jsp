<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>비밀번호 찾기</title>
		<style>
			.divHide {
				display : none;
			}
			
			.divShow {
				display : block;
			}
		</style>
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				let isChkCode = false;
				let email;
				let authCode;
				let expirationTime;
				let authStatus;
				
				// 인증 번호 받기 버튼 액션
				$('#sendCodeBtn').click(function(){
					let emailPattern = /^[a-zA-Z0-9%+]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
					if(emailPattern.test($('#email').val())) {
						sendCodeMail();
					} else {
						alert("이메일이 유효하지 않습니다.");
					}
				});
				
				// 인증번호 확인 버튼 액션
				$('#chkCodeBtn').click(function(){
					if(email == $('#email').val()) {
						chkCode();
					} else {
						$('#chkCodeDiv').hide();
						alert('현재 입력된 이메일과 인증번호를 받은 이메일이 일치하지 않습니다.');
					}
				});
				
				// 인증번호 발급
				function sendCodeMail() {
					let userId = $('#userId').val();
					let userEmail = $('#email').val();
					
					$.ajax({
						url : '../email/send/pwCode',
						type : 'post',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify({
							"userId" : userId,
							"userEmail" : userEmail
						}),
						success : function(response) {
							if(response.status == 0) {
								email = response.userEmail;
								authCode = response.authCode;
								expirationTime = response.expirationTime;
								
								$('#chkCodeDiv').show();
							}
							alert(response.message);
						}
					});
				}
					
				// 인증 번호 확인
				function chkCode(){
					let chkCode = $('#chkCode').val();
					$.ajax({
						url : '../email/check/authCode/' + chkCode,
						type : 'post',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify({
							"authCode" : authCode,
							"expirationTime" : expirationTime
						}),
						success : function(response){
							if(response.status == 0) {
								isChkCode = true;
							} else {
								isChkCode = false;
							}
							alert(response.message);
							//location.href();
						}
					})
				}
				
				// 비밀번호 찾기 버튼 액션
				$('#searchPwBtn').click(function(){
					if(isChkCode) {
						location.href="modifyPw?email=" + email;
					} else {
						alert('이메일 인증을 완료해주세요.');
					}
				});
				
				// 로그인 페이지 이동 버튼 액션
				$('#moveLoginBtn').click(function(){
					location.href="../access/login";
				});
				
			});
		</script>
	</head>
	<body>
		<h1>비밀번호 찾기</h1>
		<p>비밀번호를 찾고자 하는 아이디와 이메일을 입력해주세요.</p>
		<div>
			<span>아이디 : </span>
			<input type="text" id="userId" name="userId" required="required" placeholder="가입한 아이디">
		</div>
		<div>
			<span>이메일 : </span>
			<input type="email" id="email" name="email" required="required" placeholder="가입한 이메일">
			<button id="sendCodeBtn">인증번호 받기</button>
		</div>
		<div class="divHide" id="chkCodeDiv">
			<span>인증 번호 : </span>
			<input type="text" id="chkCode" name="chkCode">
			<button id="chkCodeBtn">확인</button>
		</div>
		<div>
			<button id="searchPwBtn">찾기</button>
			<button id="moveLoginBtn">취소</button>
		</div>
	</body>
</html>