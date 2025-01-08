<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
	<h1>로그인</h1>
	<form action="login" method="post">
		<div>
			<span>아이디 </span>
			<input type="text" name="username">
		</div>
		<div>
			<span>비밀번호 </span>
			<input type="password" name="password">
		</div>
		<div>
			<input type="submit" value="로그인">		
		</div>
		<!-- CSRF 토큰 -->
	    <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
	</form>
	<div>
		<button type="button" id="searchId" onclick="location.href='../user/searchId'">아이디 찾기</button>
		<button type="button" id="searchPw" onclick="location.href='../user/searchPw'">비밀번호 찾기</button>
		<button type="button" id="createdMember" onclick="location.href='../user/register'">회원가입</button>	
	</div>
</body>
</html>