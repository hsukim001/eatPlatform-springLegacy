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
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
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
		RenewalSubCategory(1);
		
		// 상위 카테고리 등록
		$('#mainSubmit').click(function(){
			if($('#mainCategoryName').val() != null && $('#mainCategoryName').val() !== '') {
				let mainCategoryName = $('#mainCategoryName').val();
				
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
		
		
		// 하위 카테고리 조회
		function RenewalSubCategory(mainCategoryId){
			$.ajax({
				url: "/shop/product/category/subView", 
		        type: "GET",
		        data: { mainCategoryId: mainCategoryId },
		        success: function(response) {
		        	let subCategoryList = response.subCategoryList;
		            let $viewSub = $(".viewSub");
		            
		            $viewSub.empty();
		            
		            subCategoryList.forEach(function(subCategory) {
		                $viewSub.append(
		                		 `<li data-sub-id="${subCategory.subCategoryId}">${subCategory.subCategoryName}</li>`
		                );
		            });
		            
		            console.log(response.msg);
		        },
		        error: function(xhr, status, error) {
		            alert('하위 카테고리 조회에 실패했습니다. 다시 시도해 주세요.');
		        }
			});
		}
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		
		<div id="container">
			<p> 상품 카테고리 관리</p>
			<div class="newCategory">
				<label for="mainCategoryName">상위 카테고리 등록: </label>
				<input type="text" id="mainCategoryName" name="mainCategoryName" placeholder=" ex) 한식">
				<input id="mainSubmit" type="button" value="상위 카테고리 등록">
			</div>
			
			<div class="newCategory">
				<label for="subCategoryName">하위 카테고리 등록: </label>
				<select id="mainCategorySelect" name="mainCategorySelect">
					<option value="0" selected>상위 카테고리 선택</option>
		        	<c:forEach var="category" items="${mainCategories}">
		         	   <option value="${category.mainCategoryId}">
		            	${category.mainCategoryName}
		            	</option>
		        	</c:forEach>
				</select>
				<input type="text" id="subCategoryName" name="subCategoryName" placeholder=" ex) 찜/탕">
				<input id="subSubmit" type="button" value="하위 카테고리 등록">
			</div>
			
			<div class="viewCategory">
				<ul class="viewMain">
		        	<c:forEach var="category" items="${mainCategories}">
						<li data-main-id="${category.mainCategoryId}">
							${category.mainCategoryName}
						</li>
		        	</c:forEach>
				</ul>
				
				<ul class="viewSub"></ul>
			</div>
		</div>
		
	</div>
</body>

</html>