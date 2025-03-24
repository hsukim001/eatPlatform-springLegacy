<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/modal.css">
<title>예약 상세</title>
</head>
<body>
	<!-- 신고 모달 -->
	<div id="reservInfoModal" class="modal">
		<div class="modal-content">
			<span id="closeBtn" class="close">&times;</span>
			<p class="modal_title">예약 상세 정보</p>

			<div>
				<div id="modal_storeName">
					<span id="storeTitle"></span>
					<button id="shortcut">상세페이지</button>
				</div>
				<ul class="modal_list">
					<li id="phone">
						<span class="textTitle">연락처</span><span class="colon">: </span><span class="textValue"></span>
					</li>
					<li id="address">
						<span class="textTitle">주소</span><span class="colon">: </span><span class="textValue"></span>
					</li>
					<li id="reservDate">
						<span class="textTitle">예약일</span><span class="colon">: </span><span class="textValue"></span>
					</li>
					<li id="reservTime">
						<span class="textTitle">시간</span><span class="colon">: </span><span class="textValue"></span>
					</li>
					<li id="regDate">
						<span class="textTitle">신청일</span><span class="colon">: </span><span class="textValue"></span>
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