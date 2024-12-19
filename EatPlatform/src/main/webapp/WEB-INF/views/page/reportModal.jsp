<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
/* 모달 스타일 */
.modal {
  display: none; /* 기본적으로 숨겨짐 */
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4); /* 반투명 배경 */
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  width: 80%;
  max-width: 500px;
  border: 1px solid #888;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  cursor: pointer;
}

/* 텍스트박스 */
textarea {
  width: 100%;
  height: 100px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}
</style>
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