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
<%-- <script src="<%=request.getContextPath()%>/resources/reservRegister.js" ></script> --%>
<script type="text/javascript">
	
	$(document).ready(function() {
		let reservDay;
		let reservTime;
		let reservPersonnel;
		let storeId = ${store.storeId };
		console.log('storeId : ' + storeId);
		
		// 날짜 선택 이벤트
		$('#date').change(function() {
			reservDay = $(this).val();
			$('#personnel').disabled = false;
			
			reservSchedule();
			
		});
		
		// 예약 가능 시간 조회
		function reservSchedule() {
			let reservLimit = ${store.reservLimit };
			console.log(reservLimit);
			
			$.ajax({
				url : 'schedule/'+ storeId + '/' + reservDate + '/' + reservLimit,
				type : 'get',
				success : function(data) {
					console.log('data : ' + data);
					addTimeBtn(data);
				}
			});
		}
		
		// 예약 가능 시간 버튼 추가
		function addTimeBtn(isTime) {
			timeBtn = '';
			
			let businessHour = '${store.businessHour }';
			let [storeStartTime, storeEndTime] = businessHour.split(' - ');
			console.log('storeStartTime : ' + storeStartTime);
			console.log('storeEndtime : ' + storeEndTime);
			
			for(let i = 1; i <= 24; i++) {
				
				if(i.length == 1) {
					let setTime	= '0' + i;				
				} else {
					let setTime = i;
				}
				
				let addTime = setTime + ':00';
				
				if(addTime >= storeStartTime && addTime <= storeStartTime) {
					if(time == addTime && active == true) {
						reservTimeBtn += '<button type="button" class="btn" id="timeBtn">'+ addTime +'</button>';
					} else {
						reservTimeBtn += '<button type="button" disabled>'+ addTime +'</button>';
					}					
				}
				
			}
			
			$('#timeDiv').html(timeBtn);
		}
		
		// 예약 시간 선택 이벤트
		$('#timeBtn').on('click', function() {
			$(this).addClass('selected');
			$('.btn').not(this).removeClass('selected');
			
			// 선택한 버튼의 text 값 가져오기
			reservTime = $(this).text();
		});
		
		// 예약 등록 버튼 이벤트
		function createdBtn() {
			reservPersonnel = $('#personnel').val();
			createdReserv();
		}
		
		// 예약 등록
		function createdReserv() {
			$.ajax({
				url : 'created',
				type : 'post',
				headers : {
					"Content-Type" : "application/json"
				},
				data : {
					"storeId" : storeId,
					"reservDate" : reservDate,
					"reservTime" : reservTime,
					"reservPersonnel" : reservPersonnel
				},
				success : function(result) {
					if(result == 1) {
						alert('식당 예약이 완료되었습니다.');
					} else {
						alert('식당 예약에 실패하였습니다.');
					}
				}
			});
		}
		
	});
	
</script>
</head>
<body>
	<h1>예약 등록</h1>
	<div id="register">
		<div>
			<input type="date" name="date" id="date">
		</div>
		<div id="timeDiv">
			<!-- 날짜선택시 버튼 출력 -->
		</div>
		<div>
			<input type="number" name="personnel" id="personnel" disabled>
		</div>
	</div>
	<div>
		<button type="button" id="created">예약 등록</button>
	</div>
</body>
</html>