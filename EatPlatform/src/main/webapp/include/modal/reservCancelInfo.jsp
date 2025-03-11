<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 취소 요청 상세 정보</title>
</head>
<body>
	<div id=reservCancelInfoModal class="modal">
		<div class="modal-content">
			<span id="topCloseBtn" class="close">&times;</span>
			<h2>예약 취소 요청 상세 정보</h2>
			
			<div>
				<label><input type="checkbox" id="chkAll">전체 선택</label>
			</div>
			<div>
				<ul id="cancelInfo"></ul>
			</div>
			<div class="btn-container">
			</div>
		</div>
	</div>
</body>
</html>