<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		let isUserId = false;
		let isUserPw = false;
		
		let email;
		let authCode;
		let expirationTime;
		
		$("#userChk").click(function(){
			userCheck();
			if (isUserId == true) {
				alert('사용 가능한 ID 입니다.');
			} else {
				alert('사용할 수 없는 ID 입니다.');
			}
		});
		
		$("#userPw, #userPwChk").keyup(function() {
			if($("#userPw").val() == $("#userPwChk").val()) {
				$("#authText").text("비밀번호가 일치 합니다.");
				isUserPw = true;
			} else if($("#userPw").val() != $("#userPwChk").val()) {
				$("#authText").text("비밀번호가 일치 하지않습니다.")
				isUserPw = false;
			}
		});
		
		$("#userId").keyup(function(){
			userCheck();
			console.log(isUserId);
		});

		$("form").submit(function(){
			createdUser();
			userCheck();
		});
		
		function userCheck() {
			let userId = $("#userId").val();
			$.ajax({
				url : 'check/' + userId,
				type : 'get',
				success : function(result) {
					if (result == 1) {
						isUserId = false;
					} else if (result == 0) {
						isUserId = true;
					}
				}
			});
		}
		
		
		function createdUser() {
			if(isUserId == false || isUserPw == false) {
				if(!isUserId) {
					alert("아이디 중복 체크를 다시 해주세요.");
					event.preventDefault();
				} else if(!isUserPw) {
					alert("비밀번호를 다시 입력해주세요.");
					event.preventDefault();
				}
			}
		}
		
		// 이메일 인증코드 전송
		function sendEmailCode() {
			let userEmail = $('#email').val();
			let obj = {
					"userEmail" : userEmail
			};
			$.ajax({
				url : '/email/send/authCode',
				type : 'post',
				headers : {
					"Context-Type" : "application/json"
				},
				data : {
					"userEmail" : userEmail
				},
				success : function(response) {
					if(response.authCode != null && response.expirationTime != null) {
						let msg = response.message;
						email = response.userEmail;
						$('#checkCode').prop('disabled', false);
						$('#codeChkBtn').prop('disabled', false);
						alert(msg);		
					} else {
						alert('메일 전송에 실패하였습니다.');
					}
					
				}
			});
		}
		
		// 이메일 인증 기능
		function codeChk() {
			if(email == $('#email').val()) {
				authChk();
			} else {
				alert('현재 입력된 이메일과 인증번호를 받은 이메일이 일치하지 않습니다.');
			}
		}
		
		function authChk() {
			let checkCode = $('#checkCode').val();
			
			$.ajax({
				url : '/email/authCode/' + checkCode,
				type : 'get',
				headers : {
					"Context-Type" : "application/json"
				},
				data : {
					"authCode" : authCode,
					"expirationTime" : expirationTime
				},
				success : function(response) {
					alert(response.message);
				}
			});
		}

	});
	
</script>
</head>
<body>
	<h1>회원가입</h1>
	<form action="register?flagNum=${flagNum }" method="post">
		<div>
			<span>아이디 : </span>
			<input type="text" name="userId" id="userId" required="required">
			<button type="button" id="userChk">중복 확인</button>
		</div>
		<div>
			<span>비밀번호 : </span>
			<input type="password" name="userPw" id="userPw" required="required">
		</div>
		<div>
			<span>비밀번호 확인 : </span>
			<input type="password" name="userPwChk" id="userPwChk" required="required">
			<p id="authText">비밀번호가 일치 하지않습니다.</p>
		</div>
		<div>
			<span>이름 : </span>
			<input type="text" name="userName" required="required">
		</div>
		<div>
			<span>이메일 : </span>
			<input type="email" id="email" name="userEmail" required="required">
			<button type="button" id="sendEmailCode" onclick="sendEmailCode()">인증번호 받기</button>
			<input type="text" id="checkCode" name="checkCode" disabled="disabled">
			<button type="button" id="codeChkBtn" disabled="disabled" onclick="codeChk()">확인</button>
		</div>
		<div>
			<span>휴대폰 : </span>
			<input type="tel" name="userPhone" required="required">
		</div>
		<div></div>
		<div>
			<button type="submit">등록</button>
			<button type="button" onclick="location.href='../user/flag'">취소</button>
		</div>
	</form>
	<div>
		
	</div>
</body>
</html>