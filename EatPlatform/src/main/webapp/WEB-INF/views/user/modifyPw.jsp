<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>비밀번호 수정</title>
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				let isPwChk = false;
				
				// 비밀번호 변경 버튼 액션
				$('#chgPwBtn').click(function(){
					if(isPwChk) {
						changePw();
					} else {
						alert('비밀번호를 다시 입력해주세요.');
						$('#userPw').val("");
						$('#userPwChk').val("");
						$('#pwChkMsg').text("비밀번호를 입력해주세요.");
					}
				});
				
				// 취소 버튼 액션
				$('#cancelBtn').click(function(){
					location.href = '../'
				});
				
				// 비밀번호 유효성 검사
				$('#userPw, #userPwChk').keyup(function(){
					let pwPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\d)[a-zA-Z\d!@#$%^&*]{8,}$/;
					
					// 비밀번호 일치 여부 확인
					if($('#userPw').val() === "" && $('#userPwChk').val() === "") {
						$('#pwChkMsg').text("비밀번호를 입력해주세요.");
						isPwChk = false;
					} else if($('#userPw').val() == $('#userPwChk').val()){
						$('#pwChkMsg').text("비밀번호가 일치합니다.");
						isPwChk = true;
					} else if($('#userPw').val() != $('#userPwChk').val()){
						$('#pwChkMsg').text("비밀번호가 일치하지 않습니다.");
						isPwChk = false;
					}
					
					// 비밀번호 패턴 검사
					if((pwPattern.test($('#userPw').val()) == false && $('#userPw').val() !== "")){
						$('#pwChkMsg').text("비밀번호는 8자 이상, 영문대소문자, 특수문자(!@#$%^&*) 각 한개 이상 포함하여 작성해야 합니다.");
						isPwChk = false;
					}
				});
				
				// 비밀번호 변경
				function changePw() {
					let userEmail = '${email}';
					console.log('email : ' + userEmail);
					
					let userPw = $('#userPw').val();
					$.ajax({
						url : 'modify/password',
						type : 'post',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify({
							"userPw" : userPw,
							"userEmail" : userEmail
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
		<h1>비밀번호 수정</h1>
		<div>
			<span>비밀번호 : </span>
			<input type="password" id="userPw" name="userPw" required="required">
		</div>
		<div>
			<span>비밀번호 확인 : </span>
			<input type="password" id="userPwChk" name="userPwChk" required="required">
		</div>
		<div>
			<p id="pwChkMsg">비밀번호를 입력해주세요.</p>
		</div>
		<div>
			<button id="chgPwBtn">변경</button>
			<button id="cancelBtn">취소</button>
		</div>
	</body>
</html>