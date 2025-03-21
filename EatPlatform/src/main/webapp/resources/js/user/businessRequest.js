/**
 * 
 */
 
// ajax CSRF 토큰
$(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");			
	xhr.setRequestHeader(header, token);
});

$(function() {
	let businessRequestId = $('#businessRequestId').val();
	let storeId = $('#storeId').val();
	let buttonType;
	
	// 요청 거부 버튼 이벤트리스너
	$('#deniedBtn').on('click', function(){
		buttonType = 1;
		let requestStatus = "DENIED";
		updateRequestStatus(requestStatus);
	}); // end refusalBtn
	
	// 요청 취소 버튼 이벤트리스너
	$('#requestCancelBtn').on('click', function(){
		buttonType = 2;
		cancelRequest();
	}); // end requestCancelBtn
	
	// 재신청 버튼 click event
	$('#reapplyBtn').on('click', function() {
		buttonType = 3;
		let requestStatus = "REAPPLY";
		updateRequestStatus(requestStatus);
	});
					
	// 사업자 요청 상태 변경 함수
	function updateRequestStatus(requestStatus) {
		$.ajax({
			url : "/user/business/request/status/" + businessRequestId + "/" + requestStatus,
			type : 'put',
			success : function(response) {
				if(response == 1) {
					if(buttonType == 1) {
						alert("등록 거부가 완료되었습니다.");
						location.href="requestList";
					} else if(buttonType == 2) {
						alert("사업자 등록 요청이 취소되었습니다.");
						location.href="../detail"
					} else if(buttonType == 3) {
						alert("사업자 등록 재신청이 완료되었습니다.");
						location.href="../detail";
					}
				} else {
					if(buttonType == 1) {
						alert("등록 거부에 실패하였습니다.");									
					} else if(buttonType == 2) {
						alert("사업자 등록 요청에 대한 취소가 실패하였습니다.");
					} else if(buttonType == 3) {
						alert("사업자 등록 재신청에 실패하였습니다.");
					}
				}
			},
			error : function() {
				if(buttonType == 1) {
					alert("승인 거부중 오류가 발생하였습니다.");								
				} else if(buttonType == 2) {
					alert("사업자 등록 요청 취소 중 오류가 발생하였습니다.");
				} else if(buttonType == 3) {
					alert("사업자 등록 재신청 중 오류가 발생하였습니다.");
				}
			}
		});
	} // end updateRequestStatus()
	
	function cancelRequest() {
		$.ajax({
			url : "/user/business/request/cancel/" + businessRequestId,
			type : 'delete',
			success : function(response) {
				if(response == 1) {
					if(buttonType == 1) {
						alert("등록 거부가 완료되었습니다.");
						location.href="requestList";
					} else if(buttonType == 2) {
						alert("사업자 등록 요청이 취소되었습니다.");
						location.href="../detail"
					}
				} else {
					if(buttonType == 1) {
						alert("등록 거부에 실패하였습니다.");									
					} else if(buttonType == 2) {
						alert("사업자 등록 요청에 대한 취소가 실패하였습니다.");
					}
				}
			},
			error : function() {
				if(buttonType == 1) {
					alert("승인 거부중 오류가 발생하였습니다.");								
				} else if(buttonType == 2) {
					alert("사업자 등록 요청 취소 중 오류가 발생하였습니다.");
				}
			}
		});
	}
	
}); // end function