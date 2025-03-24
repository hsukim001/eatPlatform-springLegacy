/**
 * 
 */
 
// 전화번호 정규 표현식
const autoHyphen = (target) => {
	target.value = target.value
		.replace(/[^0-9]/g, '')
   		.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
}

// ajax CSRF 토큰
$(document).ajaxSend(function(e, xhr, opt){
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	
	xhr.setRequestHeader(header, token);
});

$(function() {
	let loadName = $('#name').val();
	let loadPhone = $('#phone').val();
	
	$(document).on('click', '.from_chg_btn', function() {		
		$('#name').removeClass('no-cursor');
		$('#phone').removeClass('no-cursor');
		$('#name').prop('readonly', false);
		$('#phone').prop('readonly', false);
		
		let btn = '<button id="modifyBtn">수정</button>' +
			'<button class="modify_cancel_btn">취소</button>' +
			'<button onclick="location.href=\'/user/modifyPw\'">비밀번호 변경</button>';
		$('.btn_container').html(btn);
	});
	
	$(document).on('click', '.modify_cancel_btn', function() {
		$('#name').addClass('no-cursor');
		$('#phone').addClass('no-cursor');
		$('#name').prop('readonly', true);
		$('#phone').prop('readonly', true);
		
		let btn = '<button class="from_chg_btn">개인 정보 수정</button>' +
			'<button onclick="location.href=\'/user/modifyPw\'">비밀번호 변경</button>' +
			'<button id="memWithdrawal">회원 탈퇴</button>';
		$('.btn_container').html(btn);
	});
	
	$(document).on('click', '#modifyBtn', function() {
		let modifyName = $('#name').val();
		let modifyPhone = $('#phone').val();
		
		if(modifyName.trim() === "") {
			return alert("이름이 작성되지 않았습니다.");
		} else if(modifyPhone.trim() === "") {
			return alert("연락처가 작성되지 않았습니다.");
		}
		
		if(loadName === modifyName && loadPhone === modifyPhone) {
			return alert("수정사항이 존재하지 않습니다.");
		}
		
		if(confirm("회원정보를 수정하시겠습니까?")) {
			$('#userInfoForm').submit();
		}
	});
	
	// 회원 탈퇴 버튼 액션
	$('#memWithdrawal').click(function(){
		let isWithdrawal = confirm('회원탈퇴를 진행 하시겠습니까?');
		if(isWithdrawal == true) {
			withdrawal();
		}
	});
	
	function withdrawal() {					
		$.ajax({
			url : 'withdrawal',
			type : 'put',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response){
				if(response.result == 1){
					alert(response.msg);
					location.href='../';								
				} else {
					alert(response.msg);
				}
			}
		});
	}
});