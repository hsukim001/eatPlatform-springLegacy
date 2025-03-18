// ajax CSRF 토큰
$(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
					
	xhr.setRequestHeader(header, token);
});
			
$(document).ready(function() {
	let searchType = 'all';
	let keyword = '';
	let storeDetailUrl;
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
			reservCancel(reservId)
				.then((status) => createdCancelHistory(reservId, status));
		}
	});// End cancelBtn
	
	// 예약 취소 ajax 함수
	function createdCancelHistory(reservId, status) {
		return new Promise((resolve) => {
			if(status == 1) {
				let requestType = "USER";
				let pageNum = $('.page-link-select').data('page');
				let commentNum = $('#cancelComment').val();
				let cancelComment = $('#cancelComment option:selected').text();
				
				let obj = [{
					"reservId" : reservId,
					"cancelComment" : cancelComment
				}];
							
				$.ajax({
					url : '/reserv/cancel/' + requestType,
					type : 'post',
					headers : {
				        "Content-Type" : "application/json"
				    },
					data : JSON.stringify(obj),
					success : function(response) {
						if(response > 0) {
							nextList(pageNum);
							$('#reservInfoModal').hide();
							$('#tableBody').removeClass('selected');
							resolve(1);
							alert("예약 취소에 성공하였습니다.");
							location.reload(true);
						} else if(response == 0) {
							alert("예약취소에 실패하였습니다.");
						}
					},
					error : function() {
						alert('예약을 취소하는중 오류 발생');
						resolve(3);
					}
				});
			} else if(status == 0) {
				alert("예약취소에 실패하였습니다.");
			} else if(status == 3) {
				alert("예약을 취소하는중 오류 발생");
			}
		});
	} // End reservCacnel
	
	function reservCancel(reservId) {
		return new Promise((resolve) => {
			let obj = [{
				"reservId" : reservId
			}];
			
			$.ajax({
				url : '/reserv/cancel/status',
				type : 'put',
				headers : {
			        "Content-Type" : "application/json"
			    },
				data : JSON.stringify(obj),
				success : function(response) {
					if(response > 0) {
						resolve(1);
					} else if(response == 0) {
						resolve(0);
					}
				},
				error : function() {
					resolve(3);
				}
			});
		});
	}
	
	function searchReservInfo(reservId) {
		$.ajax({
			url : '/reserv/search/reservInfo/' + reservId,
			type : 'get',
			success : function(response) {
				storeDetailUrl = response.storeDetailUrl;
				let date = new Date(response.info.reservDateCreated);
				let year = date.getFullYear(); // 년도
				let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
				let day = String(date.getDate()).padStart(2, '0'); // 일
				let createdDate = year + '-' + month + '-' + day;
				let formattedReservDate = String(response.info.reservDate).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
				let reservTime = response.info.reservHour + ":" + response.info.reservMin;
				let btn = '<select name="cancelComment" id="cancelComment">';
				
				for(let i = 0; i < response.selectOptionList.length; i++) {
					btn += '<option value="'+ i +'">'+ response.selectOptionList[i] +'</option>'
				}
					
				btn += '</select>' + '<button class="reservCancelBtn">예약 취소</button>';
					
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
	
	$('#shortcut').click(function() {
		location.href = storeDetailUrl;
	});
				
}); // End document ready

