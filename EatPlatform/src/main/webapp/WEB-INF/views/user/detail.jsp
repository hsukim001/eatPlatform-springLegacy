<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가입 정보</title>
</head>
<body>
	<h1>가입 정보</h1>
	<form action="modify" method="post">
		<div>
			<span>아이디 : </span>
			<input type="text" name="userId" disabled="disabled" value="${vo.userId }">
		</div>
		<div>
			<span>이름 : </span>
			<input type="text" name="userName" required="required" value="${vo.userName }">
		</div>
		<div>
			<span>이메일 : </span>
			<input type="email" name="userEmail" required="required" value="${vo.userEmail }">
		</div>
		<div>
			<span>휴대폰 : </span>
			<input type="tel" name="userPhone" required="required" value="${vo.userPhone }">
		</div>
		<input type="submit" value="수정">
	</form>
	<div>
		<button onclick="location.href='modifyPw'">비밀번호 변경</button>
		<button onclick="location.href='../'">메인 이동</button>
	</div>
</body>
</html>