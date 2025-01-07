<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 등록</title>

<style>
	.btn {
		background-color: white;
		color: black;
		border: 2px solid black;
		padding: 10px 20px;
		font-size: 16px;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	/* .btn.selected 스타일 */
	.btn.selected {
		background-color: blue;
		color: white;
	}
	
	/* .btn:hover 스타일 */
	.btn:hover {
		background-color: lightgray;
	}
</style>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/calendar.css">
<script src="<%=request.getContextPath()%>/resources/js/common/calendar.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		let personnel = parseInt($('#personnel').text());
		let reservLimit = parseInt(${store.reservLimit });
		let inputPersonnel;
		$('#personnel').text(personnel);
		
		
		// "-" 버튼 클릭 이벤트
		$('#minerBtn').click(function(){
			if(personnel == 1) {
				personnel = 1;
				$('#personnel').text(personnel);
			} else {
				personnel -= 1;
				$('#personnel').text(personnel);
			}
		});
		
		// "+" 버튼 클릭 이벤트
		$('#plusBtn').click(function(){
			if(personnel == reservLimit) {
				personnel = reservLimit
				$('#personnel').text(personnel);
			} else {
				personnel += 1;
				$('#personnel').text(personnel);
			}
		});
		
		// 적용 버튼 클릭 이벤트
		$('#applyBtn').click(function(){
			inputPersonnel = $('#inputPersonnel').val()
			personnelCheck();
		});
		
		// 예약인원 직접입력 함수
		function personnelCheck(){
			if(parseInt(inputPersonnel) < 1 || parseInt(inputPersonnel) > reservLimit) {
				alert("예약 인원은 1명 이상 " + reservLimit + "명 이하로만 입력 가능합니다.");
				$('#inputPersonnel').val("");
			} else {
				personnel = parseInt(inputPersonnel);
				$('#personnel').text(personnel);
				$('#inputPersonnel').val("");
			}
		}
		
	});
</script>
</head>
<body>
	<h1>예약 등록</h1>
	<div>
		<button id="minerBtn">-</button>
		<span id="personnel">1</span>
		<button id="plusBtn">+</button>
		<input type="number" id="inputPersonnel">
		<button id="applyBtn">적용</button>	
	</div>
	<div id="reservBtnWrap" class="width100 mb30">
		<input type="button" id="reservBtn" value="온라인 예약">	
		<jsp:include page="/include/calendar.jsp" />
	</div>
</body>
</html>