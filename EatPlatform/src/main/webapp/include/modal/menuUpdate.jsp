<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메뉴 수정</title>
</head>
<body>
	
	<div id="menuUpdateModal" class="modal">
		<div class="modal-content">
			<span id="topCloseBtn" class="close">&times;</span>
			<h2 id="modalMenuName"></h2>

			<div>
				<ul>
					<li>
						<label for="updateMenuName">메뉴 이름</label>
							<input type="text" id="updateMenuName" name="updateMenuName" placeholder="메뉴 이름을 25자 이내로 작성해주세요." required>
						<br>
						<label for="updateMenuPrice">가격</label> 
							<input type="number" id="updateMenuPrice" name="updateMenuPrice"  placeholder="ex) 50000" required>
						<br>
						<label for="updateMenuComment">메뉴 설명</label>
							<textarea id="updateMenuComment" name="updateMenuComment"  maxlength="100" placeholder="간단한 메뉴 설명을 100자 이내로 작성해주세요."></textarea>
						<br>
						<label for="updateRepresent">대표 메뉴 설정 여부</label>
							<input type="checkbox" id="updateRepresent" name="updateRepresent" value="1" data-count-value="${representMenuCount }"><br>
							(대표 메뉴는 2 종류까지 설정 가능합니다.)
							<p>현재 대표 메뉴 종류 : ${representMenuCount }</p>
						<br>
						<button id="updateMenuBtn">수정</button>
					</li>
				</ul>
			</div>

			<button id="bottomCloseBtn">닫기</button>
		</div>
	</div>

</body>
</html>