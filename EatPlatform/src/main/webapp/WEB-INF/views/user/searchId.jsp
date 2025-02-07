<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
		<title>아이디 찾기</title>
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
		<h1>아이디 찾기</h1>
		<p>회원가입시 인증한 이메일을 입력해주세요.</p>
		<div>
			<span>이메일 : </span>
			<input type="email" id="email" name="email">
			<button id="sendCodeBtn">인증번호 받기</button>
		</div>
		<div class="divHide" id="chkCodeDiv">
			<span>인증 번호 : </span>
			<input type="text" name="chkCode" id="chkCode">
			<button id="chkCodeBtn">확인</button>
		</div>
		<div>
			<button id="searchBtn">찾기</button>
			<button id="cancelBtn">취소</button>
		</div>
		<input type="hidden" id="mailType" name="mailType" value="아이디">
	</body>
</html>