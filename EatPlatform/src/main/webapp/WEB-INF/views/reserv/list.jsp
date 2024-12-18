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
		nextList(1);
		prevList(1);
		
	});
	
	// 예약 목록 조회
	function nextList(pageNum) {
		let getURL = 'toDay/' + pageNum;
		$.ajax({
			url : getURL,
			//dataType : 'json',
			type : 'get',
			success : function(data) {
				nextTable(data.list); // 테이블 데이터 렌더링
	            pagination($('#nextPagination'), data.pageMaker, nextList); // 페이지네이션 렌더링
			},
			error : function() {
				alert('예약 목록을 가져오는데 실패 하였습니다.');
			}
		});
	}
	
	// 예약 목록 테이블
	function nextTable(reserv) {
	    let tableRows = '';
	    
	    reserv.forEach(function(list) {
	    	
	    	let date = new Date(list.reservDateCreated);
		   	let year = date.getFullYear(); // 년도
		    let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
		    let day = String(date.getDate()).padStart(2, '0'); // 일
		    let createdDate = year + '-' + month + '-' + day;
		    
	        tableRows += '<tr>'+
	                '<td class="reserv_id">'+ list.reservId+'</td>'+
	                '<td><a href="../store/list">'+ list.storeName +'</a></td>'+
	                '<td>'+ list.reservDate + ' ' + list.reservTime+'</td>'+
	                '<td>'+ list.reservPersonnel +'</td>'+
	                '<td>'+ createdDate +'</td>'+
	                '<td><button onclick="cancelBtn(this)">예약 취소</button></td>'+
	            '</tr>';
	    });
	    $('#nextTable').html(tableRows); // 테이블 갱신
	}
	
	// 이전 예약 목록 조회
	function prevList(pageNum) {
		let getURL = 'prevDay/' + pageNum;
		$.ajax({
			url : getURL,
			//dataType : 'json',
			type : 'get',
			success : function(data) {
				prevTable(data.list); // 테이블 데이터 렌더링
	            pagination($('#prevPagination'), data.pageMaker, prevList); // 페이지네이션 렌더링
			},
			error : function() {
				alert('이전 예약 목록을 가져오는데 실패 하였습니다.');
			}
		});
	}
	
	// 이전 예약 목록 테이블
	function prevTable(reserv) {
	    let tableRows = '';
	    reserv.forEach(function(list) {
	    	let date = new Date(list.reservDateCreated);
		   	let year = date.getFullYear(); // 년도
		    let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
		    let day = String(date.getDate()).padStart(2, '0'); // 일
		    let createdDate = year + '-' + month + '-' + day;
	    	
	        tableRows += '<tr>'+
	                '<td>'+ list.reservId+'</td>'+
	                '<td><a href="../store/list">'+ list.storeName+'</a></td>'+
	                '<td>'+ list.reservDate + ' ' + list.reservTime+'</td>'+
	                '<td>'+ list.reservPersonnel +'</td>'+
	                '<td>'+ createdDate +'</td>'+
	            '</tr>';
	    });
	    $('#prevTable').html(tableRows); // 테이블 갱신
	}
	
	// 페이지네이션
	function pagination(container, pageMaker, loadFunction) {
		let paginationHtml = "";
        
        if (pageMaker.prev) {
            paginationHtml += '<a href="#" class="page-link" data-page="'+ (pageMaker.startNum - 1) +'">이전</a>';
        }

        for (let i = pageMaker.startNum; i <= pageMaker.endNum; i++) {
            paginationHtml += '<a href="#" class="page-link" data-page="'+ i +'">'+ i +'</a>';
        }

        if (pageMaker.next) {
            paginationHtml += '<a href="#" class="page-link" data-page="'+ (pageMaker.endNum + 1) +'">다음</a>';
        }

        $(container).html(paginationHtml);

        // 페이지네이션 클릭 이벤트
        $(container).find(".page-link").on("click", function () {
            let pageNum = $(this).data("page");
            loadFunction(pageNum); // 호출된 테이블에 맞는 페이지 로드 함수 실행
        });
	}
	
	// 예약 취소 버튼
	function cancelBtn(obj) {
	    let row = obj.closest('tr');
	    let reservId = row.querySelector('.reserv_id').innerText;
	    
	    let check = confirm('선택하신 예약을 취소 하시겠습니까?');
	    if(check) {
	    	reservCancel(reservId);
	    }
	}
	
	function reservCancel(reservId) {
		$.ajax({
			url : 'cancel/' + reservId,
			type : 'delete',
			headers : {
				"Content-Type" : "application/json",
			},
			success : function(result) {
				if(result == 1) {
					alert('예약 취소 성공');
					nextList(1);
				}
			},
			error : function() {
				alert('예약을 취소하는중 오류 발생');
			}
		});
	};

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
					<th>예약 인원</th>
					<th>예약 등록일</th>
					<th>예약 취소</th>
				</tr>
			</thead>
			<tbody id="nextTable">
				<!-- ajax로 table load -->
			</tbody>
		</table>
		<div id="nextPagination">
			<!-- ajax로 pagination load -->
		</div>
	</div>

	<div>
		<h2>지난 예약 목록</h2>
		<table>
			<thead>
				<tr>
					<th>번호</th>
					<th>식당명</th>
					<th>예약 일자</th>
					<th>예약 인원</th>
					<th>예약 등록일</th>
				</tr>
			</thead>
			<tbody id="prevTable">
				<!-- ajax로 table load -->
			</tbody>
		</table>
	</div>
	<div id="prevPagination">
		<!-- ajax로 pagination load -->
	</div>

</body>

</html>