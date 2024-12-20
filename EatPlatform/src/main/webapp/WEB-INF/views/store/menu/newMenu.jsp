<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    int representMenuCount = (Integer) request.getAttribute("representMenuCount");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(function(){
		let representCount = <%= representMenuCount %>;

		$('#represent').click(function(){
			if(representCount >= 2) {
				alert("대표 메뉴는 2 종류까지 설정할 수 있습니다.");
				$('#represent').prop('checked', false);
				return false;
			}
		});

	});
</script>
<title>메뉴 등록</title>
</head>
<body>
	<h2>${storeName } 의 메뉴 등록 페이지입니다.</h2>

	<form action="register" method="POST">
		<input type="hidden" id="storeId" name="storeId" value="${menuVO.storeId }">
		<label for="menuName">
			메뉴 이름
			<input type="text" id="menuName" name="menuName" placeholder="메뉴 이름을 25자 이내로 작성해주세요." required>
		</label><br>
		<label for="menuPrice">
			가격
			<input type="number" id="menuPrice" name="menuPrice"  placeholder="ex) 50000" required>
		</label> <br>
		<label for="menuComment">
			메뉴 설명
			<textarea id="menuComment" name="menuComment"  maxlength="100" placeholder="간단한 메뉴 설명을 100자 이내로 작성해주세요."></textarea>
		</label> <br>
		<label for="represent">
			대표 메뉴 설정 여부
			<input type="checkbox" id="represent" name="represent" value="1"><br>
			(대표 메뉴는 2 종류까지 설정 가능합니다.)
			<p>현재 대표 메뉴 종류 : <%= representMenuCount %>
		</label><br>
		<input type="submit" value="메뉴 등록">
	</form>
</body>
</html>