<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/modal.css">
<title>리뷰 신고 상세</title>
<script>
</script>
</head>
<body>
	<!-- 신고 모달 -->
	<div id="reviewReportInfoModal" class="modal">
		<div class="modal-content">
			<span id="closeBtn" class="close">&times;</span>
			<p class="modal_title">리뷰 신고 상세</p>

			<div>
				<ul class="modal_list">
					<li id="reviewReport">
						<span class="textTitle">총 신고 건수</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reviewId">
						<span class="textTitle">번호</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="storeName">
						<span class="textTitle">매장명</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="writer">
						<span class="textTitle">작성자</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reviewContent">
						<span class="textTitle">리뷰 내용</span><span class="colon">:</span><span class="textValue"></span>
					</li>
					<li id="reviewUpdateDate">
						<span class="textTitle">신고 등록일</span><span class="colon">:</span><span class="textValue"></span>
					</li>
				</ul>
				<ul id="reportType modal_list">
				</ul>
			</div>
			
			<div class="btnContainer">
				<button id="deleteBtn">리뷰 삭제</button>
			</div>
		</div>
	</div>

</body>
</html>