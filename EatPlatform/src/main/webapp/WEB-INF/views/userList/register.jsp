<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입</title>
</head>
<body>
	<h1>회원가입</h1>
	<form action="register" method="post">
		<div>
			<span>아이디 : </span>
			<input type="text" name="userId" required="required">
		</div>
		<div>
			<span>비밀번호 : </span>
			<input type="password" name="userPw" required="required">
		</div>
		<div>
			<span>이름 : </span>
			<input type="text" name="userName" required="required">
		</div>
		<div>
			<span>이메일 : </span>
			<input type="email" name="userEmail" required="required">
		</div>
		<div>
			<span>휴대폰 : </span>
			<input type="tel" name="userPhone" required="required">
		</div>
		<input type="hidden" name="flagNum" value="${flagNum }">
		<input type="submit" value="등록">
	</form>
</body>
</html>