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
		nextListHtml();
		//nextList();
		//prevList();
	});
	
	function nextListHtml() {
		let userId = $('#userId').val();
		let pageNum;
		console.log(`toDay/list?userId=${sessionScope.userId}&pageNum=${pageNum}`);
		let url = 'toDay/list?=?userId=' + userId + '&pageNum=' + pageNum;
		$.ajax({
			url : `toDay/list?userId=${sessionScope.userId}&pageNum=${pageNum}`,
			dataType : 'json',
			type : 'get',
			success : function(data) {
				renderNextTable(data.list); // 테이블 데이터 렌더링
	            renderPagination($(#'nextPagination'), data.pageMaker, nextListHtml); // 페이지네이션 렌더링
			},
			error : function() {
				alert('예약 목록을 가져오는데 실패 하였습니다.');
			}
		});
	}
	
	function renderNextTable(list) {
	    let tableRows = "";
	    reservations.forEach((list) => {
	        tableRows += `
	            <tr>
	                <td>${list.reservId}</td>
	                <td>${list.storeName}</td>
	                <td>${list.reservDate}</td>
	                <td>${list.reservTime}</td>
	                <td>${new Date(list.reservDateCreated).toLocaleString()}</td>
	            </tr>
	        `;
	    });
	    $('#nextTable').html(tableRows); // 테이블 갱신
	}
	
	function renderPagination(container, pageMaker, loadFunction) {
	    let paginationHtml = "";

	    if (pageMaker.prev) {
	        paginationHtml += `<a href="#" data-page="${pageMaker.startNum - 1}">&laquo; 이전</a>`;
	    }

	    for (let i = pageMaker.startNum; i <= pageMaker.endNum; i++) {
	        paginationHtml += `<a href="#" data-page="${i}">${i}</a>`;
	    }

	    if (pageMaker.next) {
	        paginationHtml += `<a href="#" data-page="${pageMaker.endNum + 1}">다음 &raquo;</a>`;
	    }

	    $('#nextPagination').html(paginationHtml); // 페이지네이션 갱신
	    
	 	// 페이지네이션 클릭 이벤트
        $(container).find(".page-link").on("click", function (e) {
            e.preventDefault();
            let pageNum = $(this).data("page");
            loadFunction(pageNum); // 호출된 테이블에 맞는 페이지 로드 함수 실행
        });
	}
	
	function nextList() {
		console.log('nextList()');
		let userId = $('#userId').val();
		let url = '../reserv/toDay/' + userId + '?pageNum=';
		$.getJSON(url, function(data) {
			console.log(data);
			let list = '';

			$(data.list).each(
					function() {
						let reservDateTime = this.reservDate + ' '
								+ this.reservTime;
						
						let date = new Date(this.reservDateCreated);
						let createdDate = date.getFullYear() + '-' 
							+ String(date.getMonth() + 1).padStart(2, '0') + '-' 
							+ String(date.getDate()).padStart(2, '0');


						list += '<tr>' + '<td>' + this.reservId + '</td>'
								+ '<td>' + this.storeName + '</td>' + '<td>'
								+ reservDateTime + '</td>' + '<td>'
								+ createdDate + '</td>' + '</tr>';
					});
			
			/* let prev = '';
			let next = '';
			let pageList = '';
			let startNum;
			let endNum;
			if($(data.pageMaker.prev) == true) {
				prev += '<li><a href="toDay/'+ userId +'?pageNum='+ ($(data.pageMaker.startNum) - 1) +'">이전</a></li>';
			}
			if($(data.pageMaker.startNum) == 1 && $(data.pageMaker.endNum) == 0) {
				startNum = data.pageMaker.startNum;
				endNum = data.pageMaker.startNum;
			} else {
				startNum = data.pageMaker.startNum;
				endNum = data.pageMaker.endNum;
			}
			for(let x = startNum; x >= endNum; x++) {
				pageList += '<li><a href="toDay/'+ userId +'?pageNum='+ x +'">'+ x +'</a></li>';
			}
			if($(data.pageMaker.next) == true) {
				next += '<li><a href="toDay/'+ userId +'?pageNum='+ (endNum + 1) +'">다음</a></li>';
			} */
			
			$('#nextReserv').html(list);
			//$('#nextPagination').html(pageList);
		});

	}

	function prevList() {
		console.log('prevList()');
		let userId = $('#userId').val();
		let url = '../reserv/prevDay/' + userId + '?pageNum=';

		$.getJSON(url, function(data) {
			console.log(data);
			let list = '';
			console.log(data.list);

			$(data.list).each(
					function() {
						let reservDateTime = this.reservDate + ' '
								+ this.reservTime;
						let date = new Date(this.reservDateCreated);
						let createdDate = date.getFullYear() + '-' 
							+ String(date.getMonth() + 1).padStart(2, '0') + '-' 
							+ String(date.getDate()).padStart(2, '0');

						list += '<tr>' + '<td>' + this.reservId + '</td>'
								+ '<td>' + this.storeName + '</td>' + '<td>'
								+ reservDateTime + '</td>' + '<td>'
								+ createdDate + '</td>' + '</tr>';
					});
			
			/* let prev = '';
			let next = '';
			let pageList = '';
			let startNum;
			let endNum;
			if($(data.pageMaker.prev) == true) {
				prev += '<li><a href="prevDay/'+ userId +'?pageNum='+ ($(data.pageMaker.startNum) - 1) +'">이전</a></li>';
			}
			if($(data.pageMaker.startNum) == 1 && $(data.pageMaker.endNum) == 0) {
				startNum = data.pageMaker.startNum;
				endNum = data.pageMaker.startNum;
			} else {
				startNum = data.pageMaker.startNum;
				endNum = data.pageMaker.endNum;
			}
			for(let x = startNum; x >= endNum; x++) {
				pageList += '<li><a href="prevDay/'+ userId +'?pageNum='+ x +'">'+ x +'</a></li>';
			}
			if($(data.pageMaker.next) == true) {
				next += '<li><a href="prevDay/'+ userId +'?pageNum='+ (endNum + 1) +'">다음</a></li>';
			} */
			
			$('#prevReserv').html(list);
			//$('#prevPagination').html(pageList);
		});

	}
</script>
</head>
<body>
	<h1>예약 목록</h1>
	<div id="nextReserv">
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
			<tbody id="nextTable">
			</tbody>
		</table>
		<ul id="nextPagination">
			
		</ul>
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
				<c:forEach var="prevList" items="${prevList }">
					<tr>
						<td>${prevList.reservId }</td>
						<td>${prevList.storeName }</td>
						<td>${prevList.reservDate } ${item.reservTime }</td>
						<td>${prevList.reservCreateDate }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<ul id="prevPagination">
			<!-- 이전 버튼 생성을 위한 조건문 -->
			<c:if test="${pageMaker.isPrev() }">
				<li><a href="prevDay/${sessionScope.userId }?pageNum=${pageMaker.startNum - 1}">이전</a></li>
			</c:if>
			<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
			<c:forEach begin="${pageMaker.startNum }"
				end="${pageMaker.endNum }" var="num">
				<li><a href="prevDay/${sessionScope.userId }?pageNum=${num }">${num }</a></li>
			</c:forEach>
			<!-- 다음 버튼 생성을 위한 조건문 -->
			<c:if test="${pageMaker.isNext() }">
				<li><a href="prevDay/${sessionScope.userId }?pageNum=${pageMaker.endNum + 1}">다음</a></li>
			</c:if>
		</ul>
	</div>
	<input type="hidden" name="userId" id="userId"
		value="${sessionScope.userId }">
	<input type="text" value="${pageMaker.startNum }">

</body>

</html>