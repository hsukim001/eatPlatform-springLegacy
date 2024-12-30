<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>비밀번호 찾기</title>
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				
				$('#searchPw').click(function(){
					let emailPattern = /^[a-zA-Z0-9%+]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
					if(emailPattern.test($('#email').val())) {
						searchUserInfo();
					} else {
						alert("이메일이 유효하지 않습니다.");
					}
				});
				
				function searchUserInfo() {
					let userId = $('#userId').val();
					let userEmail = $('#email').val();
					
					$.ajax({
						url : 'search/' + userId + '/' + userEmail,
						type : 'get',
						headers : {
							"Content-Type" : "application/json"
						},
						success : function(result) {
							if(result == 1) {
								location.href = 'authUser?userId=' + userId + '&userEmail=' + userEmail;
							} else {
								alert("회원정보가 존재하지 않습니다.");
							}
						}
					});
					
				}
				
				$('#moveLogin').click(function(){
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
			<p id="emailChkMsg"></p>
		</div>
		<div>
			<button id="searchPw">찾기</button>
			<button id="moveLogin">취소</button>
		</div>
	</body>
</html>