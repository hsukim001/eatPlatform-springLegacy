<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 등록</title>
</head>
<body>
	<h1>예약 등록</h1>
	<form action="register" method="post">
		<div>
			<span>식당 번호 : </span>
			<input type="text" name="storeId">
		</div>
		<div>
			<span>예약일 : </span>
			<input type="date" name="reservDate">
		</div>
		<div>
			<span>예약 시간 : </span>
			<input type="time" name="reservTime">
		</div>
		<div>
			<span>인원</span>
			<input type="number" name="reservPersonnel">
		</div>
		<div>
			<input type="submit" value="등록">
		</div>
	</form>
</body>
</html>