/**
 * 
 */

$(function(){
	
	$('#reviewReportInfoModal').click(function(e) {
	    if (!$(e.target).closest('.modal-content').length) {
	        $('.close').click();
	    }
	});
	
	let reviewReportList;
	
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	       
		xhr.setRequestHeader(header, token);
	}); // End document ajaxSend
	
	$('#tableBody').on('click', 'ul', function() {
		let reviewId = $(this).data('id-value');
		reviewReportInfo(reviewId);
	});
	
	$('#closeBtn').click(function() {
		$('#reviewReportInfoModal').hide();
	});
	
	$('#deleteBtn').click(function() {
		deleteReviewReport();
	});
	
	function reviewReportInfo(reviewId) {
		$.ajax({
			url : '/management/report/review/' + reviewId,
			type : 'get',
			success : function(response) {
				reviewReportList = response.list;
				let reportType = '';
				let date = new Date(response.info.reviewUpdateDate);
				let year = date.getFullYear(); // 년도
				let month = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0부터 시작하므로 +1)
				let day = String(date.getDate()).padStart(2, '0'); // 일
				let createdDate = year + '-' + month + '-' + day;
				
				for(let i = 0; i < response.list.length; i++) {
					reportType += '<li><span class="textTitle">'+ (i + 1) +
							'</span><span class="colon">:</span><span class="textValue">'+ response.list[i].reviewReportMessage +
							'</span></li>';
				}
				
				$('#reviewReport .textValue').text(response.info.reviewReport);
				$('#reviewId .textValue').text(response.info.reviewId);
				$('#storeName .textValue').text(response.info.storeName);
				$('#writer .textValue').text(response.info.name);
				$('#reviewContent .textValue').text(response.info.reviewContent);
				$('#reviewUpdateDate .textValue').text(createdDate);
				$('#reportType').html(reportType);
				$('#reviewReportInfoModal').show();
			},
			error : function() {
				alert("조회 중 오류가 발생하였습니다.");
			}
		});
	}
	
	function deleteReviewReport() {
		let reviewId = $('#reviewId .textValue').text();
		
		$.ajax({
			url : '/management/report/review/delete/'+ reviewId,
			type : 'delete',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				if(response == 1) {
					alert("신고처리가 완료되었습니다.");
					location.reload(true);
				} else if(response == 0) {
					alert("신고처리에 실패하였습니다.");
				}
			},
			error : function() {
				alert("신고처리 중 오류가 발생하였습니다.");
			}
		});
	}
	
});