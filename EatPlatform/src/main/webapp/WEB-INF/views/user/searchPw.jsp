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
			<button id="searchBtn">찾기</button>
			<button id="cancelBtn">취소</button>
		</div>
		<input type="hidden" id="mailType" name="mailType" value="비밀번호">
	</body>
</html>