<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>예약 목락</title>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		nextList();
		prevList();
	});

	function nextList() {
		console.log('nextList()');
		let userId = $('#userId').val();
		let url = '../reserv/toDay/' + userId;
		$.getJSON(url, function(data) {
			console.log(data);
			let list = '';

			$(data).each(
					function() {
						let reservDateTime = this.reservDate + ' '
								+ this.reservTime;
						
						let date = new Date(this.reservDateCreated);
						let createdDate = date.getFullYear() + '-' 
							+ String(date.getMonth() + 1).padStart(2, '0') + '-' 
							+ String(date.getDate()).padStart(2, '0');


						list += '<tr>' + '<td>' + this.reservId + '</td>'
								+ '<td>' + this.storeId + '</td>' + '<td>'
								+ reservDateTime + '</td>' + '<td>'
								+ createdDate + '</td>' + '</tr>';
					});
			$('#nextReserv').html(list);
		});

	}

	function prevList() {
		console.log('prevList()');
		let userId = $('#userId').val();
		let url = '../reserv/prevDay/' + userId;

		$.getJSON(url, function(data) {
			console.log(data);
			let list = '';

			$(data).each(
					function() {
						let reservDateTime = this.reservDate + ' '
								+ this.reservTime;
						let date = new Date(this.reservDateCreated);
						let createdDate = date.getFullYear() + '-' 
							+ String(date.getMonth() + 1).padStart(2, '0') + '-' 
							+ String(date.getDate()).padStart(2, '0');

						list += '<tr>' + '<td>' + this.reservId + '</td>'
								+ '<td>' + this.storeId + '</td>' + '<td>'
								+ reservDateTime + '</td>' + '<td>'
								+ createdDate + '</td>' + '</tr>';
					});
			$('#prevReserv').html(list);
		});

	}
</script>
</head>
<body>
	<h1>예약 목록</h1>
	<div>
		<h2>예정 예약 목록</h2>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>식당명</th>
					<th>예약 일자</th>
					<th>예약 등록일</th>
				</tr>
			</thead>
			<tbody id="nextReserv">

			</tbody>
		</table>
	</div>

	<div>
		<h2>지난 예약 목록</h2>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>식당명</th>
					<th>예약 일자</th>
					<th>예약 등록일</th>
				</tr>
			</thead>
			<tbody id="prevReserv">

			</tbody>
		</table>
	</div>
	<input type="hidden" name="userId" id="userId"
		value="${sessionScope.userId }">

</body>

</html>