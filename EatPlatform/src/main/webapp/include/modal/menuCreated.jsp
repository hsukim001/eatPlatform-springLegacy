<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메뉴 등록</title>
</head>
<body>
	
	<div id="menuCreatedModal" class="modal">
		<div class="modal-content">
			<span id="topCloseBtn" class="close">&times;</span>
			<h2>메뉴 등록</h2>

			<div>
				<ul>
					<li>
						<label for="menuName">메뉴 이름</label>
							<input type="text" id="menuName" name="menuName" placeholder="메뉴 이름을 25자 이내로 작성해주세요." required>
						<br>
						<label for="menuPrice">가격</label> 
							<input type="number" id="menuPrice" name="menuPrice"  placeholder="ex) 50000" required>
						<br>
						<label for="menuComment">메뉴 설명</label>
							<textarea id="menuComment" name="menuComment"  maxlength="100" placeholder="간단한 메뉴 설명을 100자 이내로 작성해주세요."></textarea>
						<br>
						<label for="represent">대표 메뉴 설정 여부</label>
							<input type="checkbox" id="represent" name="represent" value="1" data-count-value="${representMenuCount }"><br>
							(대표 메뉴는 2 종류까지 설정 가능합니다.)
							<p>현재 대표 메뉴 종류 : ${representMenuCount }</p>
						<br>
						<button id="createdMenuBtn">등록</button>
					</li>
				</ul>
			</div>

			<button id="bottomCloseBtn">닫기</button>
		</div>
	</div>

</body>
</html>