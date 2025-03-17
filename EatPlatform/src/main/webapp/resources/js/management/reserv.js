// ajax CSRF 토큰
$(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
					
	xhr.setRequestHeader(header, token);
});
			
$(document).ready(function() {
	let searchType = 'all';
	let keyword = '';
	nextList(1, searchType, keyword);
	
	$('.searchBtn').click(function() {
		keyword = $('#keyword').val();
				
		nextList(1, searchType, keyword);
	});
	
	$('#allListBtn').click(function() {
		searchType = 'all';
		keyword = $('#keyword').val();
				
		nextList(1, searchType, keyword);
		$('.search-type .selectSpan').removeClass('selectSpan');
		if($(this).hasClass("spanBtn")) {
			$(this).addClass("selectSpan");
		}
	}); // End reservList span click event
	
	// reservList span click event
	$('#currentListBtn').click(function() {
		searchType = 'current';
		keyword = $('#keyword').val();
		
		console.log(searchType);
		
		nextList(1, searchType, keyword);
		$('.search-type .selectSpan').removeClass('selectSpan');
		if($(this).hasClass("spanBtn")) {
			$(this).addClass("selectSpan");
		}
	}); // End reservList span click event
				
	// preReservHistory span click event
	$('#prevListBtn').click(function() {
		searchType = 'prev';
		keyword = $('#keyword').val();
		
		nextList(1, searchType, keyword);
		$('.search-type .selectSpan').removeClass('selectSpan');
		if($(this).hasClass("spanBtn")) {
			$(this).addClass("selectSpan");
		}
	}); // End preReservHistory span click event
	
	// cancelReservHistory span click event
	$('#cancelListBtn').click(function() {
		searchType = 'cancel';
		keyword = $('#keyword').val();
		
		nextList(1, searchType, keyword);
		$('.search-type .selectSpan').removeClass('selectSpan');
		if($(this).hasClass("spanBtn")) {
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
	
	// 예약 목록 ajax 조회 함수
	function nextList(pageNum, searchType, keyword) {
		let getURL = '/reserv/toDay/' + pageNum + '/' + searchType + '?keyword=' + keyword;
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
			let status = "예약";
			
			if(list.cancelStatus == 1) {
				status = "취소";
			}
						    
			tableBodyRows += '<tr class="reservRow" data-id-value="'+ list.reservId +'">'+
					'<td>'+ list.reservId +'</td>'+
					'<td>'+ list.storeName +'</td>'+
					'<td>'+ formattedReservDate + ' ' + list.reservHour + ':' + list.reservMin + '</td>'+
					'<td>'+ list.reservPersonnel +'</td>'+
					'<td>'+ status +'</td>' +
					'<td>'+ createdDate +'</td>' +
					 //btnFrom +
				'</tr>';
			});
		} else {
			tableBodyRows = '<tr><td colspan="6">목록이 존재하지 않습니다.</td></tr>'
		}
				    
		$('#totalCount').html(totalCountHtml);
		$('#tableBody').html(tableBodyRows); // 테이블 갱신
	} // End nextTable
	
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
			loadFunction(pageNum, searchType, keyword); // 호출된 테이블에 맞는 페이지 로드 함수 실행
		});
	} // End pagination
	
	// 예약 취소 버튼 함수
	$(document).on('click', '.reservCancelBtn', function() {
		//let row = obj.closest('tr');
		//let reservId = row.querySelector('.reserv_id').innerText;
		console.log("ok");
		let reservId = $('#tableBody .selected').data('id-value');
				    
		let check = confirm('선택하신 예약을 취소 하시겠습니까?');
		if(check) {
			reservCancel(reservId);
		}
	});// End cancelBtn
	
	// 예약 취소 ajax 함수
	function reservCancel(reservId) {
		let requestType = "USER";
		let pageNum = $('.page-link-select').data('page');
		let commentNum = $('#cancelComment').val();
		let cancelComment;
		
		if(commentNum == 0) {
			return alert("예약 취소사유를 선택해주세요.");
		} else if(commentNum == 1) {
			cancelComment = "개인적인 사유";
		} else if(commentNum == 2) {
			cancelComment = "건강상의 문제";
		}
		
		let obj = [{
			"reservId" : reservId,
			"cancelComment" : cancelComment
		}];
					
		$.ajax({
			url : '/reserv/cancel/' + requestType,
			type : 'post',
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
				let date = new Date(response.info.reservDateCreated);
				let year = date.getFullYear(); // 년도
				let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
				let day = String(date.getDate()).padStart(2, '0'); // 일
				let createdDate = year + '-' + month + '-' + day;
				let formattedReservDate = String(response.info.reservDate).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
				let reservTime = response.info.reservHour + ":" + response.info.reservMin;
				let btn = '<select name="cancelComment" id="cancelComment">' +
					'<option value="0">예약 취소사유를 선택해주세요.</option>' +
					'<option value="1">개인적인 사유</option>' +
					'<option value="2">건강상의 문제</option>' +
					'</select>' + 
					'<button class="reservCancelBtn">예약 취소</button>';
					
				$('#storeTitle').text(response.info.storeName);
				$('#phone .textValue').text(response.info.storePhone);
				$('#reservDate .textValue').text(formattedReservDate);
				$('#reservTime .textValue').text(reservTime);
				$('#regDate .textValue').text(createdDate);
				
				if(response.info.roadAddress == null && response.info.jibunAddress == null) {
					return alert("예약 상세 정보를 불러오는데 실패하였습니다.");
				}
				
				if(response.info.cancelStatus == 1) {
					let comment = '<p> 취소 사유 : '+ response.cancelComment +'</p>'
					$('.comment-container').html(comment);
					$('.btnContainer').empty();
				} else if(response.info.cancelStatus == 0) {
					$('.comment-container').empty();
					$('.btnContainer').html(btn);
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
				
}); // End document ready

