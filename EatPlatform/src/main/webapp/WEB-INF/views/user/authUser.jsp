<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>인증코드</title>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		
		$(document).ready(function(){
			
		});
		
		function sendPwCodeEmail() {
			let userId = '${userId }';
			let userEmail = '${userEmail }';
			console.log(userId);
			console.log(userEmail);
			
			$.ajax({
				url : '../email/send/pwCode',
				type : 'post',
				headers : {
					"Context-Type" : "application/json"
				},
				data : JSON.stringify(userEmail),
				success : function(response) {
					alert(response.message);
				}
			});
		}
	</script>
</head>
	<body>
		<h1>인증코드 확인</h1>
		<p>이메일로 전달받은 인증코드를 입력해 주세요.</p>
		<div>
			<span>인증코드 : </span>
			<input type="text" id="codeChk" required="required">
		</div>
		<div>
			<button id="chkBtn">확인</button>
		</div>
	</body>
</html>