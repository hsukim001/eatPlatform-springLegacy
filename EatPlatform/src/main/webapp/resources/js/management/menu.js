/**
 * 
 */
 
 $(function(){
	let menuId;
	let modalMenuId;
			
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");			
		xhr.setRequestHeader(header, token);
	}); // end ajaxSend
			
	// 메뉴 삭제 버튼 이벤트 리스너
	$(document).on('click', '.deleteMenuBtn', function() {
				
		let isDelte = confirm("메뉴를 정말로 삭제하시겠습니까?");
		if(isDelte) {
			menuId = $(this).data("id-value");
			console.log("true");
			deleteMenu();
		}
				
	}); // end deleteMenuBtn click
			
	// 메뉴 삭제 함수
	function deleteMenu() {
		$.ajax({
			url : '/store/menu/delete/' + menuId,
			type : 'delete',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response){
				if(response == 1) {
					menuId = "";
					alert("삭제가 완료되었습니다.");
					location.reload(true);
				} else {
					menuId = "";
					alert("삭제에 실패하였습니다.");
				}
			},
			errors : function(){
				menuId = "";
				alert("삭제를 진행중 오류가 발생하였습니다.");
			}
		});
	} // end deleteMenu
			
	// 메뉴 등록 버튼 이벤트 리스너
	$('#showMenuModal').click(function() {
		console.log("hoho");
		$('#menuCreatedModal').show();
	}); // end menuModal open click
			
	// 메뉴 모달창 닫기 버튼 이벤트 리스너
	$('#menuCreatedModal #topCloseBtn, #menuCreatedModal #bottomCloseBtn' ).click(function() {
		$('#menuCreatedModal').hide();
	}); // end menuCreatedModal closeBtn click
			
	// 대표 메뉴 체크 박스 이벤트 리스너
	$('#represent, #updateRepresent').on('click', function(){
		//let representCount = $('#represent').data("count-value");
		let representCount = $(this).data("count-value");
		console.log("대표 메뉴 갯수 : " + representCount);
		if(representCount >= 2) {
			alert("대표 메뉴는 2 종류까지 설정할 수 있습니다.");
			$('#represent').prop('checked', false);
			return false;
		}
	}); // end represent click
			
	// 메뉴 모달창 등록 버튼 이베트 리스너
	$('#createdMenuBtn').click(function() {
		createdMenu();
	}); // end createdMenuBtn click
			
	// 메뉴 등록 함수
	function createdMenu() {
		let represent;
				
		if ($("#represent").prop("checked")) {
		    console.log("체크됨");
		    represent = 1;
		} else {
		    console.log("체크 안 됨");
			represent = 0;
		}

				
		let obj = {
				"storeId" : $('#storeId').val(),
				"menuName" : $('#menuName').val(),
				"menuPrice" : $('#menuPrice').val(),
				"menuComment" : $('#menuComment').val(),
				"represent" : represent
		};
				
		$.ajax({
			url : '/store/menu/created',
			type : 'post',
			headers : {
				"Content-Type" : "application/json"
			},
			data : JSON.stringify(obj),
			success : function(response){
				if(response == 1) {
					alert("메뉴 등록이 완료되었습니다.");
					location.reload(true);
				} else {
					alert("메뉴 등록에 실패하였습니다.");
					$('#menuCreatedModal').hide();
				}
			},
			error : function(){
				alert("메뉴 등록중에 오류가 발생하였습니다.");
				$('#menuCreatedModal').hide();
			}
		});
	} // end createdMenu
			
	// 수정 모달창 닫기 버튼 이벤트 리스너
	$('#menuUpdateModal #topCloseBtn, #menuUpdateModal #bottomCloseBtn').click(function(){
		$('#menuUpdateModal').hide();
	}); // end updateMenuModal close click event
			
	$('.updateMenuBtn').on('click', function() {
		menuId = $(this).data('id-value');
		searchMenuInfo();
	}); // end updateMenuBtn open click
			
	function searchMenuInfo() {
		$.ajax({
			url : '/store/menu/search/menu/' + menuId,
			type : 'get',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				$('#updateMenuName').val(response.menuName);
				$('#updateMenuPrice').val(response.menuPrice);
				$('#updateMenuComment').val(response.menuComment);
				$('#modalMenuName').text(response.menuName);
				modalMenuId = response.menuId;
						
				if(response.represent == 1) {
					$("#updateRepresent").prop("checked", true);							
				} else {
					$("#updateRepresent").prop("checked", false);	
				}
						
				$('#menuUpdateModal').show();
				menuId = "";
						
			},
			error : function() {
				alert("모달창 호출중에 오류가 발생하였습니다.");
				menuId = "";
				$('#menuUpdateModal').hide();
			}
		});
	} // end searchMenuInfo
			
	// 메뉴 등록 버튼 이벤트 리스너
	$('#updateMenuBtn').click(function() {
		updateMenu();
	}); // end updateMenuBtn click event
			
	// 메뉴 수정 함수
	function updateMenu() {
		let represent;
				
		if ($("#updateRepresent").prop("checked")) {
		    console.log("체크됨");
		    represent = 1;
		} else {
		    console.log("체크 안 됨");
		    represent = 0;
		}

				
		let obj = {
				"menuId" : modalMenuId,
				"storeId" : $('#storeId').val(),
				"menuName" : $('#updateMenuName').val(),
				"menuPrice" : $('#updateMenuPrice').val(),
				"menuComment" : $('#updateMenuComment').val(),
				"represent" : represent
		};
				
		$.ajax({
			url : '/store/menu/update',
			type : 'put',
			headers : {
				"Content-Type" : "application/json"
			},
			data : JSON.stringify(obj),
			success : function(response) {
				if(response == 1) {
					alert("메뉴 수정이 완료되었습니다.");
					location.reload(true);
				} else {
					alert("메뉴 수정에 실패하였습니다.");
					modalMenuId = "";
				}
			},
			error : function() {
				alert("메뉴 수정중 오류가 발생하였습니다.");
				modalMenuId = "";
			}
		});
	} // end updateMenu
			
});