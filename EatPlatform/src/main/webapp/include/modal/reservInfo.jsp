<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 상세</title>
</head>
<body>
	<!-- 신고 모달 -->
	<div id="reservInfoModal" class="modal">
		<div class="modal-content">
			<span id="closeBtn" class="close">&times;</span>
			<h2>예약 상세</h2>

			<div>
				<p id="storeTitle"></p>
				<ul>
					<li id="phone">
						<span class="textTitle">연락처</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="address">
						<span class="textTitle">주소</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reservDate">
						<span class="textTitle">예약일자</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="status">
						<span class="textTitle">상태</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="regDate">
						<span class="textTitle">신청일</span><span class="colon">:</span><span class="textValue"></span>
					</li>
				</ul>
			</div>
			<div class="btnContainer">
			</div>
		</div>
	</div>

</body>
</html>