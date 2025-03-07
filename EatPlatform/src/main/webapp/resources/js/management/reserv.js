// ajax CSRF 토큰
$(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
					
	xhr.setRequestHeader(header, token);
});
			
$(document).ready(function() {
	nextList(1);
					
	// reservList span click event
	$('#reservList').click(function() {
		nextList(1);
			if($(this).hasClass("spanBtn")) {
				modifySpanClass();
				$(this).removeClass("spanBtn");
				$(this).addClass("selectSpan");
			}
	}); // End reservList span click event
				
	// preReservHistory span click event
	$('#preReservHistory').click(function() {
		prevList(1);
		if($(this).hasClass("spanBtn")) {
			modifySpanClass();
			$(this).removeClass("spanBtn");
			$(this).addClass("selectSpan");
		}
	}); // End preReservHistory span click event
	
	// cancelReservHistory span click event
	$('#cancelReservHistory').click(function() {
		cancelList(1);
		if($(this).hasClass("spanBtn")) {
			modifySpanClass();
			$(this).removeClass("spanBtn");
			$(this).addClass("selectSpan");
		}
	}); // End cancelReservHistory span click event
	
	// table body row click event
	$('#tableBody').on('click', '.reservRow', function(){
		let reservId = $(this).data("id-value");
		console.log(reservId);
		searchReservInfo(reservId);
		$(this).addClass("selected");
	}); // End table body row click event
	
	// x span click event
	$('.close').click(function() {
		$('#tableBody .selected').removeClass('selected');
		$('#reservInfoModal').hide();
	}); // x span click event
				
}); // End document ready
			
// modifySpanClass()
function modifySpanClass() {
	if($('#reservList').hasClass("selectSpan")) {
		$('#reservList').removeClass("selectSpan");
		$('#reservList').addClass("spanBtn");
	} else if($('#preReservHistory').hasClass("selectSpan")) {
		$('#preReservHistory').removeClass("selectSpan");
		$('#preReservHistory').addClass("spanBtn");
	} else if($('#cancelReservHistory').hasClass("selectSpan")) {
		$('#cancelReservHistory').removeClass("selectSpan");
		$('#cancelReservHistory').addClass("spanBtn");
	}
} // End modifySpanClass
			
// 예약 목록 ajax 조회 함수
function nextList(pageNum) {
	let getURL = 'toDay/' + pageNum;
	$.ajax({
		url : getURL,
		type : 'get',
		success : function(data) {
			nextTable(data); // 테이블 데이터 렌더링
			pagination($('#pagination'), data.pageMaker, nextList); // 페이지네이션 렌더링
		},
		error : function() {
			alert('예약 목록을 가져오는데 실패 하였습니다.');
		}
	});
} // End nextList
			
// 예약 목록 테이블 작성 함수
function nextTable(reserv) {
	let tableBodyRows = '';
	let tableHeadRow = '<tr><th>번호</th><th>식당명</th><th>예약 일자</th><th>예약 인원</th><th>상태</th><th>예약 신청일</th><th>예약 취소</th></tr>';
	let totalCountHtml = '총 ' + reserv.pageMaker.totalCount + '건';
			    
	if(reserv.pageMaker.totalCount > 0) {
		reserv.list.forEach(function(list) {
				    	
		let date = new Date(list.reservDateCreated);
		let year = date.getFullYear(); // 년도
		let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
		let day = String(date.getDate()).padStart(2, '0'); // 일
		let createdDate = year + '-' + month + '-' + day;
		let formattedReservDate = String(list.reservDate).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
		let btnFrom = '';
					    
		if(list.reservStatus === '완료') {
			btnFrom = '<td><button onclick="cancelBtn(this)">예약 취소</button></td>';
		} else if(list.cancelStatus !== '완료') {
			btnFrom = '<td><button disabled="true">예약 취소</button></td>';
		}					    
					    
		tableBodyRows += '<tr class="reservRow" data-id-value="'+ list.reservId +'">'+
				'<td>'+ list.reservId +'</td>'+
				'<td>'+ list.storeName +'</td>'+
				'<td>'+ formattedReservDate + ' ' + list.reservHour + ':' + list.reservMin + '</td>'+
				'<td>'+ list.reservPersonnel +'</td>'+
				'<td>'+ list.reservStatus +'</td>' +
				'<td>'+ createdDate +'</td>' +
				 //btnFrom +
			'</tr>';
			console.log(tableBodyRows);
		});
	} else {
		tableBodyRows = '<tr><td colspan="6">목록이 존재하지 않습니다.</td></tr>'
	}
			    
	$('#totalCount').html(totalCountHtml);
	$('#tableHead').html(tableHeadRow);
	$('#tableBody').html(tableBodyRows); // 테이블 갱신
} // End nextTable
			
// 이전 예약 내역 ajax 조회 함수
function prevList(pageNum) {
	let getURL = 'prevDay/' + pageNum;
	$.ajax({
		url : getURL,
		type : 'get',
		success : function(data) {
			prevTable(data); // 테이블 데이터 렌더링
			pagination($('#pagination'), data.pageMaker, prevList); // 페이지네이션 렌더링
		},
		error : function() {
			alert('이전 예약 목록을 가져오는데 실패 하였습니다.');
		}
	});
} // End prevList
			
// 이전 예약 내역 테이블 작성 함수
function prevTable(reserv) {
	let tableBodyRows = '';
	let tableHeadRow = '<tr><th>번호</th><th>식당명</th><th>예약 일자</th><th>예약 인원</th><th>상태</th><th>예약 신청일</th></tr>';
	let totalCountHtml = '총 ' + reserv.pageMaker.totalCount + '건';
			    
	if(reserv.pageMaker.totalCount > 0) {
		reserv.list.forEach(function(list) {
			let date = new Date(list.reservDateCreated);
			let year = date.getFullYear(); // 년도
			let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
			let day = String(date.getDate()).padStart(2, '0'); // 일
			let createdDate = year + '-' + month + '-' + day;
			let formattedReservDate = String(list.reservDate).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
			let status = '';
					    
			if(list.cancelStatus == 0) {
				status = '예약 완료';
			} else if(list.cancelStatus == 1) {
					if(list.processingStatus === 'WAIT') {
						status = '취소 요청';
					}
			}
				    	
			tableBodyRows += '<tr class="reservRow" data-id-value="'+ list.reservId +'">'+
					'<td>'+ list.reservId +'</td>'+
					'<td>' + list.storeName +'</td>'+
					'<td>'+ formattedReservDate + ' ' + list.reservHour + ':' + list.reservMin + '</td>'+
					'<td>'+ list.reservPersonnel +'</td>'+
					'<td>' + list.reservStatus + '</td>' + 
					'<td>'+ createdDate +'</td>'+
					'</tr>';
		});
	} else {
		tableBodyRows = '<tr><td colspan="6">목록이 존재하지 않습니다.</td></tr>'
	}
			    
	$('#totalCount').html(totalCountHtml);
	$('#tableHead').html(tableHeadRow);
	$('#tableBody').html(tableBodyRows); // 테이블 갱신
} // End prevTable
			
// 예약 취소 목록 ajax 조회 함수
function cancelList(pageNum) {
	let getURL = 'cancel/' + pageNum;
	$.ajax({
		url : getURL,
		type : 'get',
		success : function(data) {
			prevTable(data); // 테이블 데이터 렌더링
			pagination($('#pagination'), data.pageMaker, cancelList); // 페이지네이션 렌더링
		},
		error : function() {
			alert('예약 목록을 가져오는데 실패 하였습니다.');
		}
	});
} // End cancelList
			
// 테이블 페이지네이션
function pagination(container, pageMaker, loadFunction) {
	let paginationHtml = "";
		        
	if (pageMaker.prev) {
		paginationHtml += '<a href="#" class="page-link" data-page="'+ (pageMaker.startNum - 1) +'">이전</a>';
	}
		
	for (let i = pageMaker.startNum; i <= pageMaker.endNum; i++) {
		if(pageMaker.pagination.pageNum == i) {
			paginationHtml += '<a href="#" class="page-link-select" data-page="'+ i +'">'+ i +'</a>';
		} else {
			paginationHtml += '<a href="#" class="page-link" data-page="'+ i +'">'+ i +'</a>';
		}
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
} // End pagination
			
// 예약 취소 버튼 함수
function cancelBtn() {
	//let row = obj.closest('tr');
	//let reservId = row.querySelector('.reserv_id').innerText;
	
	let reservId = $('#tableBody .selected').data('id-value');
			    
	let check = confirm('선택하신 예약을 취소 하시겠습니까?');
	if(check) {
		reservCancel(reservId);
	}
} // End cancelBtn
			
// 예약 취소 ajax 함수
function reservCancel(reservId) {
	let requestType = "MEMBER";
	let pageNum = $('.page-link-select').data('page');
	let obj = [{
		"reservId" : reservId
	}];
				
	$.ajax({
		url : '/reserv/cancel/' + requestType,
		type : 'put',
		headers : {
			"Content-Type" : "application/json",
		},
		data : JSON.stringify(obj),
		success : function(response) {
			if(response.result == 1) {
				alert('예약 취소 성공');
				nextList(pageNum);
				$('#reservInfoModal').hide();
				$('#tableBody').removeClass('selected');
			}
		},
		error : function() {
			alert('예약을 취소하는중 오류 발생');
		}
	});
} // End reservCacnel

function searchReservInfo(reservId) {
	$.ajax({
		url : '/reserv/search/reservInfo/' + reservId,
		type : 'get',
		success : function(response) {
			let reservTime = response.info.reservHour + ":" + response.info.reservMin;
			$('#storeTitle').text(response.info.storeName);
			$('#phone .textValue').text(response.info.storePhone);
			$('#reservDate .textValue').text(response.info.reservDate + reservTime);
			$('#status .textValue').text(response.info.reservStatus);
			$('#regDate .textValue').text(response.info.reservDateCreated);
			
			if(response.info.roadAddress == null && response.info.jibunAddress == null) {
				return alert("예약 상세 정보를 불러오는데 실패하였습니다.");
			}
			
			if(response.info.roadAddress != null) {
				let address = response.info.roadAddress + " " + response.info.detailAddress
				$('#address .textValue').text(address);
			} else if(response.info.jibunAddress != null) {
				let address = response.info.jibunAddress + " " + response.info.detailAddress
				$('#address .textValue').text(address);
			}
			
			$('#reservInfoModal').show();
		},
		error : function() {
			alert("예약 상세 정보를 불러오는 중 오류가 발생하였습니다.");
		}
	});
}

