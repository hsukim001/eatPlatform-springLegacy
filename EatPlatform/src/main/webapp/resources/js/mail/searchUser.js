/**
 * 아이디, 비밀번호 찾기
 * 이메일 인증 코드 전송
 */
 
let isChkCode = false;
let email;
let authCode;
let expirationTime;
let authStatus;

 // ajax CSRF 토큰
 $(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
				
	xhr.setRequestHeader(header, token);
 });
 
 $(document).ready(function(){
				
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
		isChkCode = false;
		let username;
		if($('#username').length > 0) {
			username = $('#username').val();
		} else {
			username = '';
		}
		let userEmail = $('#email').val();
		let emailType = $('#mailType').val();
							
		$.ajax({
			url : '../email/send/searchCode/' + emailType,
			type : 'post',
			headers : {
				"Content-Type" : "application/json"
			},
			data : JSON.stringify({
				"username" : username,
				"email" : userEmail
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
					$('#chkCode').prop('disabled', true);
					$('#chkCodeBtn').prop('disabled', true);
				} else {
					isChkCode = false;
				}
				alert(response.message);
			}
		});
	}
 });