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

/* 모달 내용 */
.reply-modal {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
  width: 80%;
  max-width: 500px;
  padding: 20px;
  text-align: center;
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
  margin-bottom: 20px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  color: #333;
  resize: none;
  box-sizing: border-box;
  background-color: #f9f9f9;
}
</style>
<title>댓글 수정</title>
</head>
<body>
	<!-- 댓글 수정 모달 -->
	<div id="replyUpdateModal" class="modal">
		<div class="reply-modal">
			<span id="replycloseBtn" class="close">&times;</span>
			<h2>댓글 수정하기</h2>
			<div>
				<textarea id="replyUpdateContent" placeholder="수정할 댓글 내용을 작성하세요"></textarea>
			</div>
			<button id="submitreply">댓글 수정</button>
		</div>
	</div>

</body>
</html>