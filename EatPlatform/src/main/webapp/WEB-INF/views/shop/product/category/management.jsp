<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>상품 카테고리 관리</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/management/category.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script type="text/javascript">
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	       
		xhr.setRequestHeader(header, token);
	});
	$(function(){
		let ExistMainCategoryId;
		
		loadCategoriesList();
		
		$('.viewMain').on('click', 'li', function() {
			ExistMainCategoryId = $(this).data('main-id');
			$('.mainCategoryName').removeClass('activeMain');
			$(this).find('.mainCategoryName').addClass('activeMain');
			RenewalSubCategory(ExistMainCategoryId);
		});

		
		// 상위 카테고리 등록
		$('#mainSubmit').click(function(){
			if($('#mainCategoryName').val() != null && $('#mainCategoryName').val() !== '') {
				let mainCategoryName = $('#mainCategoryName').val();
				
			    if (!confirm('"' + mainCategoryName + '" 카테고리를 등록하시겠습니까?')) {
			        return;
			    }
			    
				let obj = {
						'mainCategoryName' : mainCategoryName
				}

				$.ajax({
					type: 'POST',
					url: '/shop/product/category/mainRegister',
					contentType: 'application/json',
					data: JSON.stringify(obj),
					success: function(response) {
						alert(response.msg);
						$("#mainCategoryName").val("");
						loadCategoriesList();
					},
					error: function(xhr, status, error) {
						alert('신규 카테고리 등록에 실패했습니다. 다시 시도해 주세요')
					}
				});
			} else {
				alert('카테고리명을 입력해주세요.');
			}
		});			
		$('#mainCategoryName').keydown(function(e){
			if (e.keyCode === 13) {
				$('#mainSubmit').click();
			}
		});
		
		// 하위 카테고리 등록
		$('#subSubmit').click(function(){
			if($('#subCategoryName').val() != null && $('#subCategoryName').val() !== '') {
				let subCategoryName = $('#subCategoryName').val();
				let mainCategoryId = $('#mainCategorySelect').val();
				
			    if (!confirm('"' + subCategoryName + '" 카테고리를 등록하시겠습니까?')) {
			        return;
			    }
			    
				let obj = {
						'mainCategoryId' : mainCategoryId,
						'subCategoryName' : subCategoryName
				}

				$.ajax({
					type: 'POST',
					url: '/shop/product/category/subRegister',
					contentType: 'application/json',
					data: JSON.stringify(obj),
					success: function(response) {
						alert(response.msg);
						$("#mainCategorySelect").val('0');
						$("#subCategoryName").val("");
						loadCategoriesList();
					},
					error: function(xhr, status, error) {
						alert('신규 카테고리 등록에 실패했습니다. 다시 시도해 주세요')
					}
				});
			} else {
				alert('카테고리명을 입력해주세요.');
			}
		});			
		$('#subCategoryName').keydown(function(e){
			if (e.keyCode === 13) {
				$('#subSubmit').click();
			}
		});
		
		// 상위 카테고리 조회
		function loadCategoriesList() {
		    $.ajax({
		        type: 'GET',
		        url: '/shop/product/category/mainView',
		        contentType: "application/json",
		        success: function(response) {
		        	let mainCategoryList = response.data;
		            let $viewMain = $(".viewMain");
		            let $mainCategorySelect = $('#mainCategorySelect');
		            $viewMain.empty();
		            $mainCategorySelect.empty();

		            mainCategoryList.forEach(function(mainCategory) {
		            	 let listItem = 
		            		'<li data-main-id="' + mainCategory.mainCategoryId + '">' + 
		            			'<span class="mainCategoryName">' + 
		            	 			mainCategory.mainCategoryName + 
		            	 		'</span>' +
			            	 	'<button id="mainEditButton">' + 
		            	 			' <i class="fas fa-edit gray-arrow"></i>' + 
		            	 		'</button>' +
		            	 		'<button id="mainDeleteButton">' + 
	            	 				'<i class="fas fa-times red-x"></i>' + 
	            	 			'</button>' +
		            	 	'</li>';
		            	 	
		            	 let selectOption = 
		            		'<option value=' +  mainCategory.mainCategoryId + '>' +
		            	 		mainCategory.mainCategoryName + 
		            	 	'</option>';

		                $viewMain.append(listItem);
		                $mainCategorySelect.append(selectOption);
		            });
		            
		            if (ExistMainCategoryId && $('.viewMain li').filter(function() {
		                return $(this).data('main-id') === ExistMainCategoryId;
		            }).length > 0) {
		                $('.viewMain li').each(function() {
		                    if ($(this).data('main-id') === ExistMainCategoryId) {
		                        $(this).find('.mainCategoryName').addClass('activeMain');
		                        RenewalSubCategory(ExistMainCategoryId);
		                    }
		                });
		            } else {
		                callFirstCategory();
		            }
		        },
		        error: function(xhr, status, error) {
		            alert('카테고리 목록 로드에 실패했습니다. 다시 시도해 주세요.');
		        }
		    });
		}
		
		
		// 하위 카테고리 조회
		function RenewalSubCategory(ExistMainCategoryId) {
		    if (!ExistMainCategoryId) {  
		        console.error("mainCategoryId가 유효하지 않습니다.");
		        return;
		    }
		
		    let requestUrl = "/shop/product/category/subView/" + ExistMainCategoryId;
    
		    $.ajax({
		        url: requestUrl, 
		        type: "GET",
		        contentType: "application/json",
		        success: function(response) {
		            let subCategoryList = response.data;
		            let $viewSub = $(".viewSub");
		            $viewSub.empty();
		            
		            if (subCategoryList) {
			            subCategoryList.forEach(function(subCategory) {
			                let listItem = 
			                	'<li data-sub-id="' + subCategory.subCategoryId + '">' +
			                		'<span class="subCategoryName">' +
			                			subCategory.subCategoryName + 
			                		'</span>' +
				    		        '<button id="subEditButton">' +
						            	'<i class="fas fa-edit gray-arrow"></i>' +
						        	'</button>' +
						        	'<button id="subDeleteButton">' +
						            	'<i class="fas fa-times red-x"></i>' +
						        	'</button>' +
					        	'</li>';
			                $viewSub.append(listItem);
			            });
		            } else {
		            	 $viewSub.append(`<p class="noSub">등록된 하위 카테고리가 없습니다.</p>`);
		            }
		        },
		        error: function(xhr, status, error) {
		            console.error("AJAX 에러 발생:", xhr.status, error);
		            alert('하위 카테고리 조회에 실패했습니다. 다시 시도해 주세요.');
		        }
		    });
		}
		
		// 상위 카테고리 수정 버튼
		$('.viewMain').on('click', 'li #mainEditButton', function(){
		    let $li = $(this).closest('li');
		    let originName = $li.find('.mainCategoryName').text();
		    const mainCategoryId = $li.data('main-id');

		    $('.viewMain li').each(function(){
		        let $item = $(this);
		        if ($item.find('.category-input').length > 0) {
		            let prevName = $item.data('origin-name');
		            const prevMainCategoryId = $item.find('.category-input').data('main-id');

		            $item.empty();
		            let originalItem = 
		                '<span class="mainCategoryName">' + prevName + '</span>' +
		                '<button id="mainEditButton">' +
		                    '<i class="fas fa-edit gray-arrow"></i>' +
		                '</button>' +
		                '<button id="mainDeleteButton">' +
		                    '<i class="fas fa-times red-x"></i>' +
		                '</button>';
		            
		            $item.append(originalItem);
		        }
		    });

		    $li.empty();
		    let inputItem = 
		        '<input type="text" class="category-input" value="' + originName + '" data-main-id="' + mainCategoryId + '" />' +
		        '<button id="editOkBtn">' +
		            '<i class="fa fa-check" style="color:#090;"></i>' +
		        '</button>' +
		        '<button id="editCancelBtn">' + 
		            '<i class="fas fa-times red-x"></i>' + 
		        '</button>';

		    $li.append(inputItem);
		    $li.find('input').focus();
		    $li.data('origin-name', originName);
		});
		
		// 상위 카테고리 수정 취소 버튼
		$('.viewMain').on('click', 'li #editCancelBtn', function(){
		    let $li = $(this).closest('li');
		    let originName = $li.data('origin-name'); 
		    const mainCategoryId = $li.find('.category-input').data('main-id');

		    if (!confirm('수정을 취소하시겠습니까?')) {
		        return;
		    }

		    $li.empty();

		    let originalItem = 
		        '<span class="mainCategoryName">' + originName + '</span>' +
		        '<button id="mainEditButton">' +
		            '<i class="fas fa-edit gray-arrow"></i>' +
		        '</button>' +
		        '<button id="mainDeleteButton">' +
		            '<i class="fas fa-times red-x"></i>' +
		        '</button>';

		    $li.append(originalItem);
		});
		
		// 상위 카테고리명 수정
		$('.viewMain').on('click', '#editOkBtn', function(){
			const $li = $(this).closest('li');
			const mainCategoryId = $li.find('.category-input').data('main-id');
			const mainCategoryName = $li.find('.category-input').val();
			
		    if (!mainCategoryName.trim()) {
		        alert('카테고리명을 입력해주세요.');
		        return;
		    }
		    
		    if (!confirm('카테고리명을 "' + mainCategoryName + '"(으)로 변경하시겠습니까?')) {
		        return;
		    }
		    
		    $.ajax({
		        type: 'PUT',
		        url: '/shop/product/category/mainEdit',
		        contentType: 'application/json',
		        data: JSON.stringify({
		            mainCategoryId: mainCategoryId,
		            mainCategoryName: mainCategoryName
		        }),
		        success: function(response) {
		            alert(response.msg); 
		    		loadCategoriesList();
		        },
		        error: function(xhr, status, error) {
		            alert('카테고리 수정에 실패했습니다. 다시 시도해주세요.');
		            console.error('수정 오류:', error);
		        }
		    });
		});
		
		// 하위 카테고리 수정 버튼
		$('.viewSub').on('click', 'li #subEditButton', function(){
		    let $li = $(this).closest('li');
		    let originName = $li.find('.subCategoryName').text();
		    const subCategoryId = $li.data('sub-id');
		    let requestUrl = "/shop/product/category/subSelect/" + subCategoryId;

		    $.ajax({
		    	type: "GET",
		    	url: requestUrl,
		        contentType: 'application/json',
		    	success: function(response) {
		        	let currentMainCategoryId = response.currentMainCategoryId;
		        	let mainCategoryList = response.mainCategoryList;
		        	let selectElement = 
		        			'<select id="subEditSelect"></select>';
		        	let $select = $(selectElement);
		        	mainCategoryList.forEach(function(mainCategory) {
		        		let selectedAttr = (mainCategory.mainCategoryId === currentMainCategoryId) ? ' selected' : '';
		        		
		                let selectOption = 
		                	'<option value="' + mainCategory.mainCategoryId + '"' + selectedAttr + '>' +
		                    	mainCategory.mainCategoryName +
		                    '</option>';
		                
		                $select.append(
		                		selectOption);
		            });
				    let inputItem = 
				        '<input type="text" class="category-input" value="' + originName + '" data-sub-id="' + subCategoryId + '" />' +
				        '<button id="editOkBtn">' +
				            '<i class="fa fa-check" style="color:#090;"></i>' +
				        '</button>' +
				        '<button id="editCancelBtn">' + 
				            '<i class="fas fa-times red-x"></i>' + 
				        '</button>';
		        	
		            	 	
		           $li.empty();
		           $li.append($select);
		           $li.append(inputItem);
		           $li.find('input').focus();
				   $li.data('origin-name', originName);
		        }
		    });
		});		
		
		// 하위 카테고리 수정 취소 버튼
		$('.viewSub').on('click', 'li #editCancelBtn', function(){
		    let $li = $(this).closest('li');
		    let originName = $li.data('origin-name'); 
		    const subCategoryId = $li.find('.category-input').data('main-id');

		    if (!confirm('수정을 취소하시겠습니까?')) {
		        return;
		    }

		    $li.empty();

		    let originalItem = 
		        '<span class="subCategoryName">' + originName + '</span>' +
		        '<button id="subEditButton">' +
		            '<i class="fas fa-edit gray-arrow"></i>' +
		        '</button>' +
		        '<button id="subDeleteButton">' +
		            '<i class="fas fa-times red-x"></i>' +
		        '</button>';

		    $li.append(originalItem);
		});
		
		// 하위 카테고리명 수정
		$('.viewSub').on('click', '#editOkBtn', function(){
			const $li = $(this).closest('li');
			const subCategoryId = $li.find('.category-input').data('sub-id');
			const mainCategoryId = $li.find('#subEditSelect').val();
			const subCategoryName = $li.find('.category-input').val();
			
		    if (!subCategoryName.trim()) {
		        alert('카테고리명을 입력해주세요.');
		        return;
		    }
		    
		    if (!confirm('카테고리명을 "' + subCategoryName + '"(으)로 변경하시겠습니까?')) {
		        return;
		    }
		    
		    $.ajax({
		        type: 'PUT',
		        url: '/shop/product/category/subEdit',
		        contentType: 'application/json',
		        data: JSON.stringify({
		        	subCategoryId: subCategoryId,
		            mainCategoryId: mainCategoryId,
		            subCategoryName: subCategoryName
		        }),
		        success: function(response) {
		            alert(response.msg); 

		    		loadCategoriesList();
		        },
		        error: function(xhr, status, error) {
		            alert('카테고리 수정에 실패했습니다. 다시 시도해주세요.');
		            console.error('수정 오류:', error);
		        }
		    });
		});
		
		// 수정 Input Enter & ESC 처리
		$(document).on('keydown', '.category-input', function(e) {
		    if (e.keyCode === 13) {
		        $(this).siblings('#editOkBtn').first().trigger('click');
		    } else if (e.keyCode === 27) {
		        $(this).siblings('#editCancelBtn').first().trigger('click');
		    }
		});
		
		// 상위 카테고리 삭제
		$('.viewMain').on('click', '#mainDeleteButton', function(){
			const $li = $(this).closest('li');
			const mainCategoryId = $li.data('main-id');
			
			 if (!confirm("선택한 카테고리를 삭제하시겠습니까?")) {
				return;
			 }
			 
			 $.ajax({
			 	url: "/shop/product/category/mainDelete/" + mainCategoryId,
			 	type: "DELETE",
			    contentType: "application/json",
			    success: function(response) {
			    	alert(response.msg);
			        if (response.msg.includes("완료")) {
			        	loadCategoriesList();
			        }
			     },
			     error: function() {
			     	alert("카테고리 삭제에 실패했습니다.\n다시 시도해주세요.");
			 	}
			 });

		});		
		
		// 하위 카테고리 삭제
		$('.viewSub').on('click', '#subDeleteButton', function(){
			const $li = $(this).closest('li');
			const subCategoryId = $li.data('sub-id');
			
			 if (!confirm("선택한 카테고리를 삭제하시겠습니까?")) {
			        return;
			 }
			 
			 $.ajax({
			 	url: "/shop/product/category/subDelete/" + subCategoryId,
			 	type: "DELETE",
			    contentType: "application/json",
			    success: function(response) {
			    	alert(response.msg);
			        if (response.msg.includes("완료")) {
			        		loadCategoriesList();
			            }
			        },
			        error: function() {
			            alert("카테고리 삭제에 실패했습니다.\n다시 시도해주세요.");
			        }
			    });

		});
		
		// 첫 번째 상위 카테고리의 하위 카테고리 출력
		function callFirstCategory() {
		    let $firstCategory = $(".viewMain li").first();

		    if ($firstCategory.length) {
		        let firstMainId = $firstCategory.data('main-id');
		        if (firstMainId) {
		        	$(".mainCategoryName").removeClass('activeMain');
		        	$($firstCategory).children('.mainCategoryName').addClass('activeMain');
		            RenewalSubCategory(firstMainId); 
		        } else {
		            alert("문제가 발생했습니다. 다시 시도해주세요.")
		        }
		    } else {
		        alert("카테고리가 로드되지 않았습니다.");
		    }
		}
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		
		<div id="container">
			<p class="title">상품 카테고리 관리</p>
			<p class="view_title">신규 카테고리 등록</p>
			<div class="newCategory">
				<div id="newMainCateogry">
					<label for="mainCategoryName">상위 카테고리 등록</label>
					<input type="text" id="mainCategoryName" name="mainCategoryName" placeholder=" ex) 한식">
					<input id="mainSubmit" type="button" value="상위 카테고리 등록">
				</div>
				<div id="newSubCategory">
					<label for="subCategoryName">하위 카테고리 등록</label>
					<select id="mainCategorySelect" name="mainCategorySelect"></select>
					<input type="text" id="subCategoryName" name="subCategoryName" placeholder=" ex) 찜/탕">
					<input id="subSubmit" type="button" value="하위 카테고리 등록">
				</div>
			</div>
			
			<p class="view_title">카테고리 조회</p>
			<div class="viewCategory">
				<ul class="viewMain"></ul>
				<ul class="viewSub"></ul>
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>

</html>