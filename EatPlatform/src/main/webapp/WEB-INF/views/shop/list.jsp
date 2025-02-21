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
		let productList = [];
		let totalCount, subCategoryId;
		let mainCategoryId = 0;
		let pageNum = 1;
		
		loadProductList(pageNum);
		loadMainCategoryList();
		
		$('#mainCategoryList').on('click', 'li', function(){
			mainCategoryId = $(this).data('main-id');
			pageNum = 1;
			loadProductList(pageNum);
			loadMainCategoryList();
			loadSubCategoryList(mainCategoryId);
		});
		
		function loadMainCategoryList() {
			$.ajax({
				url: "/shop/product/category/mainView",
				type: "GET",
				contentType: "application/json",
				success: function(response) {
					let mainCategoryList = response.data;
					let $mainCategoryList = $('#mainCategoryList');
					$mainCategoryList.empty();
					
					mainCategoryList.forEach(function(mainCategory){
						let listItem = 
							'<li data-main-id="' + mainCategory.mainCategoryId + '">' +
								mainCategory.mainCategoryName +
							'</li>';
						$mainCategoryList.append(listItem);
					});
					if (mainCategoryId === 0 || mainCategoryId === ''){
						mainCategoryId = $('#mainCategoryList li').first().data('main-id');
						loadSubCategoryList(mainCategoryId);
					}
				}
			});
		}
		
		function loadSubCategoryList(mainCategoryId) {
			$.ajax({
				url: "/shop/product/category/subView/" + mainCategoryId,
				type: "GET",
				contentType: "application/json",
				success: function(response) {
					let subCategoryList = response.data;
					let $subCategoryList = $('#subCategoryList');
					$subCategoryList.empty();
					
					if(subCategoryList) {
						subCategoryList.forEach(function(subCategory){
							let listItem = 
								'<li data-sub-id="' + subCategory.subCategoryId + '">' + 
									subCategory.subCategoryName +
								'</li>'
							$subCategoryList.append(listItem);
							$('#subCategoryList li').first().trigger('click');
						});
					} else {
						$subCategoryList.append(`<p class="noSub">등록된 하위 카테고리가 없습니다.</p>`);
					}
				},
		        error: function(xhr, status, error) {
		            console.error("AJAX 에러 발생:", xhr.status, error);
		            alert('하위 카테고리 조회에 실패했습니다. 다시 시도해 주세요.');
		        }
			});
		}
		
		function loadProductList(pageNum, mainCategoryId, subCategoryId) {
			let keyword;
			
			if ($("#searchInput").val() != null && $("#searchInput").val() != '') {
				keyword = $("#searchInput").val();
			} else {
				keyword = '';
			}
			
			$.ajax({
				url: "/shop/list/all",
				type: "GET",
				contentType: "application/json",
				data: {
					pageNum: pageNum,
					keyword: keyword
				},
				success: function(response){
					productList = response.mergedList;
					totalCount = response.totalCount;
					let $productList = $('#productList'); 
					$productList.empty();
					
					productList.forEach(function(product) {
						let listItem = 
							'<li data-produt-id="' + product.productId + '">' + 
								'<a href="detail?productId=' + product.productId + '" target="_blank">' +
									'<p class="productName">' + product.productName + '</p>' +
									'<p class="productCategory">' +
										'<span class="mainCategoryName">' + product.mainCategoryName + '</span>' +
										'<span class="subCategoryName">' + product.subCategoryName + '</span>' +
									'</p>' +
									'<p class="seller">' +
										'<span class="sellingStore">판매처 : ' + product.productStoreName + ' /</span>' +
										'<span class="sellerName"> 판매자 : ' + product.sellerName + '</span>' +
									'</p>' +
									'<p class="productConfig">' +
										'<div class="config_left">' +
											'<span class="productPrice">' + product.productPrice + '</span>' +
											'<span class="productBundle">' + product.productBundle + '</span>' +
										'</div>' +
										'<span class="productStock">남은 수량 : ' + product.productStock + '</span>' +
									'</p>' +
								'</a>' +
							'</li>';
						$productList.append(listItem);
					});
				}
			});
		}
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="listContainer">
			<div id="categoryContainer">
				<ul id="mainCategoryList"></ul>
				<ul id="subCategoryList"></ul>
			</div>
			
			<div id="productConatainer">
				<ul id="productList">
				</ul>
			</div>
		</div>
	</div>
</body>

</html>