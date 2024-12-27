<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰 신고</title>
</head>
<body>
	<!-- 신고 모달 -->
	<div id="reportModal" class="modal">
		<div class="modal-content">
			<span id="closeBtn" class="close">&times;</span>
			<h2>리뷰 신고하기</h2>

			<!-- 신고 이유 라디오 버튼 선택 -->
			<div>
				<input type="radio" id="reason1" name="reportReason" value="부적절한 내용">
				<label for="reason1">부적절한 내용</label><br> <input type="radio"
					id="reason2" name="reportReason" value="허위 정보"> <label
					for="reason2">허위 정보</label><br> <input type="radio"
					id="reason3" name="reportReason" value="욕설 및 비방"> <label
					for="reason3">욕설 및 비방</label><br>
			</div>

			<!-- 신고 제출 버튼 -->
			<button id="submitReport">신고 제출</button>
		</div>
	</div>

</body>
</html>