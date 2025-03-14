<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 신고 상세</title>
</head>
<body>
	<!-- 신고 모달 -->
	<div id="reviewReportInfoModal" class="modal">
		<div class="modal-content">
			<span id="closeBtn" class="close">&times;</span>
			<h2>리뷰 신고 상세</h2>

			<div>
				<ul>
					<li id="phone">
						<span class="textTitle">신고 유형</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="phone">
						<span class="textTitle">작성자 명</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="address">
						<span class="textTitle">내용</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reservDate">
						<span class="textTitle">신고자</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reservTime">
						<span class="textTitle">신고 등록일</span><span class="colon">:</span><span class="textValue"></span>
					</li>
				</ul>
			</div>
			
			<div class="btnContainer">
				<!-- button load -->
			</div>
		</div>
	</div>

</body>
</html>