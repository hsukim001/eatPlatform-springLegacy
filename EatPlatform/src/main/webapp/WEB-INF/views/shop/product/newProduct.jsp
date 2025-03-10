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
<title>상품 등록</title>
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
		let ExistMainCategoryId;
		
		$(document).on("change", "#mainCategory li input[type=radio]", function() {
			ExistMainCategoryId = $(this).val();
			RenewalSubCategory(ExistMainCategoryId);
		});
		
		loadCategoriesList();
		// 상위 카테고리 조회
		function loadCategoriesList() {
		    $.ajax({
		        type: 'GET',
		        url: '/shop/product/category/mainView',
		        contentType: "application/json",
		        success: function(response) {
		        	let mainCategoryList = response.data;
		            let $mainCategory = $("#mainCategory");
		            $mainCategory.empty();

		            mainCategoryList.forEach(function(mainCategory, index) {
		            	 let listItem = 
		            		'<li>' +
		            			'<input type="radio" value="' + mainCategory.mainCategoryId + '" name="mainCategoryId" id="mainCategoryId' + index + '"' + 
		                    	(index === 0 ? ' checked required' : '') + '>' +
		            			'<label for="mainCategoryId' + index + '">' + mainCategory.mainCategoryName + '</label>'
		            		'</li>';
		            	$mainCategory.append(listItem);
		            });
		            
					if (ExistMainCategoryId) {
		 		   		RenewalSubCategory(ExistMainCategoryId);
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
		            let $subCategory = $("#subCategory");
		            $subCategory.empty();
		            
		            if (subCategoryList) {
			            subCategoryList.forEach(function(subCategory, index) {
			                let listItem = 
			                	'<li>' +
			                		'<input type="radio" value="' + subCategory.subCategoryId + '" name="subCategoryId" id="subCategoryId' + index + '"' + 
		                    		(index === 0 ? ' checked required' : '') + '>' +
		            				'<label for="subCategoryId' + index + '">' + subCategory.subCategoryName + '</label>'
					        	'</li>';
			                $subCategory.append(listItem);
			            });
		            } else {
		            	 $subCategory.append(`<p class="noSub">등록된 하위 카테고리가 없습니다.</p>`);
		            }
		        },
		        error: function(xhr, status, error) {
		            console.error("AJAX 에러 발생:", xhr.status, error);
		            alert('하위 카테고리 조회에 실패했습니다. 다시 시도해 주세요.');
		        }
		    });
		}
		
		// 첫 번째 상위 카테고리의 하위 카테고리 출력
		function callFirstCategory() {
		    let $firstCategory = $('#mainCategory li input[type="radio"]').first();

		    if ($firstCategory.length) {
		        let firstMainId = $firstCategory.val();
		        if (firstMainId) {
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
    	<form action="register" method="POST" >
    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    		<select id="productStoreId" name="productStoreId">
    			<c:forEach var="store" items="${storeList}" varStatus="status">
					<option value="${store.storeId }">${store.storeName }</option>
				</c:forEach>
    		</select>
    		<label for="productName">상품명 : </label>
    		<input type="text" id="productName" name="productName" required>
    		<label for="productPrice">가격 : </label>
    		<input type="number" id="productPrice" name="productPrice" required>

			<div id="category_container">
				<ul id="mainCategory"></ul>
				<ul id="subCategory"></ul>
			</div>
    		<label for="productBundle"> 세트 당 묶음 수 : </label>
    		<input type="number"  id="productBundle" name="productBundle" value="1" required>   		
    		<label for="productStock">재고 : </label>
    		<input type="number" id="productStock" name="productStock" value="0" required>

    		<input type="submit" value="상품 등록">
    	</form>
		
	</div>
</body>

</html>