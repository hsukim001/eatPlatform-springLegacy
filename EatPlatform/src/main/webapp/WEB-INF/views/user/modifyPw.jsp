<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 수정</title>
</head>
<body>
	<h1>비밀번호 수정</h1>
	<form action="modifyPw" method="post">
		<div>
			<span>비밀번호 : </span>
			<input type="password" name="userPw">
			<input type="submit" value="변경">
		</div>
	</form>
	<div>
		<button onclick="location.href='detail'">취소</button>
	</div>
</body>
</html>