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
				<span id="storeTitle"></span>
				<button id="shortcut">바로가기</button>
				<ul>
					<li id="phone">
						<span class="textTitle">연락처</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="address">
						<span class="textTitle">주소</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reservDate">
						<span class="textTitle">예약일</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reservTime">
						<span class="textTitle">시간</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="regDate">
						<span class="textTitle">신청일</span><span class="colon">:</span><span class="textValue"></span>
					</li>
				</ul>
			</div>
			
			<div class="comment-container">
				<!-- reserv cancel comment load -->
			</div>
			
			<div class="btnContainer">
				<!-- button load -->
			</div>
		</div>
	</div>

</body>
</html>