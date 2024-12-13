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
	<form action="login">
		<div>
			<span>아이디 </span>
			<input type="text" name="userId">
		</div>
		<div>
			<span>비밀번호 </span>
			<input type="password" name="userPw">
		</div>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
		<input type="submit" value="로그인">
	</form>
</body>
</html>