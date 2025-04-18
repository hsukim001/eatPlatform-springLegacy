<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/register.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<title>회원 가입</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	const autoHyphen = (target) => {
		target.value = target.value
			.replace(/[^0-9]/g, '')
			.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
	}
	
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
					
		xhr.setRequestHeader(header, token);
	});

	$(document).ready(function() {
		let isUserId = false;
		let isUserPw = false;
		
		let currentUsername = "";
		let email;
		let authCode;
		let expirationTime;
		let authStatus;
		
		let emailAddressChk;
		
		let userIdPattern = /^(?=.*[a-zA-Z])[a-zA-Z0-9]{5,}$/;
		
		// 사용자 계정 확인 버튼 이벤트
		$("#userChk").click(function(){
			userCheck();
		});
		
		// 비밀번호 확인 이벤트
		$("#userPw, #userPwChk").keyup(function() {
			let pwPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\d)[a-zA-Z\d!@#$%^&*]{8,}$/;
			
			// 비밀번호, 비밀번호 확인 input 일치 확인
			if($('#password').val() === "" && $('#userPwChk').val() === "") {
				$('#pwChkMsg').text("비밀번호를 입력해 주세요.");
				isUserPw = false;
			} else if($('#password').val() == $("#userPwChk").val()) {
				$('#pwChkMsg').text("비밀번호가 일치 합니다.");
				isUserPw = true;
			} else if($("#password").val() != $("#userPwChk").val()) {
				$('#pwChkMsg').text("비밀번호가 일치 하지않습니다.")
				isUserPw = false;
			}
			
			// 비밀번호, 비밀번호 확인 input 영문대소문자, 특수문자, 비밀번호 최소 길이 확인
			if(pwPattern.test($('#password').val()) == false && $('#userPw').val() !== "") {
				$("#pwChkMsg").text("비밀번호는 8자 이상, 영문대소문자, 특수문자(!@#$%^&*) 각 한개 이상 포함하여 작성해야 합니다.");
				isUserPw = false;
			}
			
		});
		
		// 사용자 계정 확인 keyup 이벤트
		$('#username').keyup(function(){
			
			if($(this).val() === "") {
				$('#userIdChkMsg').text('아이디를 입력해 주세요.');
			} else {
				if(userIdPattern.test($('#username').val())) {
					$('#userIdChkMsg').text('아이디 입력이 완료되었습니다.');
					//userCheck();
				} else {
					$('#userIdChkMsg').text('아이디에는 특수문자를 작성할수 없으며 5글자 이상 작성해야 합니다.');
				}
			}
		});

		// 계정 생성 버튼 클릭 이벤트
		$("form").submit(function(){
			createdUser();
		});
		
		// 중복 계정 확인
		function userCheck() {
			let username = $("#username").val();
			let type = "회원가입";
			$.ajax({
				url : 'check/' + username + '/' + type,
				type : 'get',
				success : function(result) {
					if (result == 1 || userIdPattern.test($('#username').val()) == false) {
						isUserId = false;
					} else if (result == 0 && userIdPattern.test($('#username').val())) {
						isUserId = true;
						currentUsername = username;
					}
					
					if (isUserId == true) {
						alert('사용 가능한 아이디 입니다.');
					} else {
						alert('사용할 수 없는 아이디 입니다.');
					}
				}
			});
		}
		
		// 계정 생성전 아이디,비밀번호 확인 및 이메일 인증 확인
		function createdUser() {
			let username = $("#username").val();
			
			if(username !== currentUsername) {
				isUserId = false;
			}
			
			if(isUserId == false || isUserPw == false) {
				if(!isUserId) {
					alert("아이디 중복 체크를 다시 해주세요.");
					event.preventDefault();
				} else if(!isUserPw) {
					alert("비밀번호를 다시 입력해주세요.");
					event.preventDefault();
				}
			} else if(authStatus != 0) {
				alert("이메일인증이 완료되지 않았습니다.");
				event.preventDefault();
			}
			
		}
		
		// 이메일 인증코드 전송 버튼 액션
		$('#sendEmailCodeBtn').click(function(){
			if(emailAddressChk == true) {
				sendEmailCode();				
			} else {
				alert("이메일을 올바르게 입력해주세요.");
			}
		});
		
		// 이메일 인증코드 전송
		function sendEmailCode() {
			let userEmail = $('#email').val();
			
			let obj = {
					"userEmail" : userEmail
			};
			
			$.ajax({
				url : '../email/send/authCode',
				type : 'post',
				headers : {
					"Content-Type" : "application/json"
				},
				data : JSON.stringify(obj),
				success : function(response) {
					if(response.status == 0) {
						let msg = response.message;
						email = response.userEmail;
						authCode = response.authCode;
						expirationTime = response.expirationTime;
						
						$('#checkCode').prop('disabled', false);
						$('#codeChkBtn').prop('disabled', false);
						$('#emailCheck').show();
						
						alert(msg);
					} else {
						authStatus = response.status;
						alert(response.message);
					}
					
				},
				error : function(response) {
					authStatus = response.status;
					alert(response.message);
				}
			});
		}
		
		// 이메일 코드 인증 버튼 액션
		$('#codeChkBtn').click(function(){
			console.log("input email : " + $('#email').val());
			console.log("send email : " + email);
			if(email == $('#email').val()) {
				authChk();
			} else {
				$('#emailCheck').val("");
				$('#emailCheck').hide();
				alert('현재 입력된 이메일과 인증번호를 받은 이메일이 일치하지 않습니다.');
			}
		});
		
		// 이메일 코드 인증 확인
		function authChk() {
			let checkCode = $('#checkCode').val();
			let obj = {
					"userEmail" : email,
					"authCode" : authCode,
					"expirationTime" : expirationTime
			};
			
			$.ajax({
				url : '../email/check/authCode/' + checkCode,
				type : 'post',
				headers : {
					"Content-Type" : "application/json"
				},
				data : JSON.stringify(obj),
				success : function(response) {
					authStatus = response.status;
					if(authStatus == 0) {
						$('#codeChkMsg').text('이메일 인증 성공');
						$('#checkCode').prop('disabled', true);
						$('#codeChkBtn').prop('disabled', true);
						$('#email').prop('readonly', true);
						$('#sendEmailCodeBtn').prop('disabled', true)
					} else {
						$('#codeChkMsg').text('이메일 인증 실패');
					}
					alert(response.message);
				}
			});
		}
		
		// 이메일 표현식 체크
		$('#email').keyup(function() {
			let email = $(this).val();
			let emailPattern = /^[a-zA-Z0-9%+]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
			
			if(email === "") {
				$('#emailChkMsg').text('이메일을 입력해주세요.');
			} else {
				if(emailPattern.test(email)) {
					$('#emailChkMsg').text('이메일 입력이 완료되었습니다.');
					emailAddressChk = true;
				} else {
					$('#emailChkMsg').text('이메일을 올바르게 입력해주세요.');
					emailAddressChk = false;
				}
			}
						
		});
		
	});
	
</script>
</head>

<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<p class="membership_title">MemberShip</p>
			<form action="created" method="post">
			<!-- CSRF 토큰 -->
			    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
				<div>
					<label for="username">아이디</label>
					<div>
						<input type="text" name="username" id="username" required="required" maxlength="30">
						<button type="button" id="userChk">중복 확인</button><br>
					</div>
					<p id="userIdChkMsg">아이디를 입력해 주세요.</p>
				</div>
				<div>
					<label for="password">비밀번호</label>
					<input type="password" name="password" id="password" required="required" maxlength="50">
				</div>
				<div>
					<label for="userPwChk">비밀번호 확인</label>
					<div>
						<input type="password" name="userPwChk" id="userPwChk" required="required">
					</div>
					<p id="pwChkMsg">비밀번호를 입력해 주세요.</p>
				</div>
				<div>
					<label for="name">이름</label>
					<input type="text" id="name" name="name" required="required">
				</div>
				<div>
					<label for="email">이메일</label>
					<div>
						<input type="email" id="email" name="email" required="required">
						<button type="button" id="sendEmailCodeBtn">인증번호 받기</button>
					</div>
					<p id="emailChkMsg">이메일을 입력해주세요.</p>
				</div>
				<div class="divHide" id="emailCheck">
					<label for="checkCode">인증코드</label>
					<div>
						<input type="text" id="checkCode" name="checkCode" disabled="disabled">
						<button type="button" id="codeChkBtn" disabled="disabled">확인</button>
					</div>
					<p id="codeChkMsg">인증번호를 입력해 주세요.</p>
				</div>
				<div>
					<label for="phone">휴대폰</label>
					<input type="tel" id="phone" name="phone" required="required" oninput="autoHyphen(this)" maxlength="13">
				</div>
				<button type="submit">회원가입</button>
			</form>
			<p class="notice">* 사업자 회원 가입은 일반 회원으로 가입 후 사업자 회원 전환 신청을 진행해주세요.</p>
		</div>
		
		<jsp:include page="/include/footer.jsp" />		
	</div>
</body>


<body>
</body>
</html>