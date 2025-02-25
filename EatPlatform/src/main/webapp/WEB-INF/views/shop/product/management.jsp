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
<title>상품 관리</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<style>
	#productList li button li {
		cursor: pointer;
	}
	
	#productList li button {
		background: none;
		border: none;
		outline: none;
		cursor: pointer;
	}
	
	.gray-arrow {
		color: #888;
	}
	
	.red-x {
		color: #f00;
		font-size: 16px;
	}
</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script type="text/javascript">
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	       
		xhr.setRequestHeader(header, token);
	});
	$(function() {
	    let currentPageNum = 1; 

	    loadManagementList(currentPageNum);

	    function loadManagementList(pageNum) {
	        $.ajax({
	            type: "GET",
	            url: '/shop/product/management/all',
	            contentType: "application/json",
	            data: {
	                pageNum: pageNum
	            },
	            success: function(response) {
	                let productList = response.productList;
	                let $productList = $('#productList');
	                $productList.find('li').not('.productListHead').remove();

	                productList.forEach(function(product) {
	                	let createAt = product.createAt;

	                	let date = new Date(createAt);

	                	let year = date.getFullYear();
	                	let month = date.getMonth() + 1; 
	                	let day = date.getDate();

	                	if (month < 10) month = '0' + month;
	                	if (day < 10) day = '0' + day;

	                	let formattedCreateAt = year + '. ' + month + '. ' + day;

	                    let listItem = 
	                        '<li data-product-id="' + product.productId + '">' +
	                            '<span class="productName">' + product.productName +'</span>' +
	                            '<span class="productPrice">' + product.productPrice +'</span>' +
	                            '<span class="productBundle">' + product.productBundle +'</span>' +
	                            '<span class="productStock">' + product.productStock +'</span>' +
	                            '<span class="productCreateDate">' + formattedCreateAt +'</span>' +
	                            '<button id="editBtn" onclick="location.href=\'/shop/product/updateProduct?productId=' + product.productId + '\'">' +
	                                '<i class="fas fa-edit gray-arrow"></i>' +
	                            '</button>' +
	                            '<button id="deleteBtn">' +
	                                '<i class="fas fa-times red-x"></i>' +
	                            '</button>' +
	                        '</li>';
	                    $productList.append(listItem);
	                });

	                let currentPage = response.currentPage;
	                let totalPages = response.totalPages;
	                let $pagination = $('.pagination');
	                $pagination.empty();

	                if (currentPage > 3) {
	                    $pagination.append('<a href="javascript:void(0);" class="page-link" data-page="' + (currentPage - 3) + '">이전</a>');
	                } else {
	                    $pagination.append('<a href="javascript:void(0);" class="page-link" data-page="1">이전</a>');
	                }
	                
	                for (let i = 1; i <= totalPages; i++) {
	                    if ((currentPage <= 3 && i <= 5) || 
	                        (currentPage >= totalPages - 2 && i >= totalPages - 4) || 
	                        (i >= currentPage - 2 && i <= currentPage + 2)) {

	                        if (i === currentPage) {
	                            $pagination.append('<a href="javascript:void(0);" class="page-link current" data-page="' + i + '">' + i + '</a>');
	                        } else {
	                            $pagination.append('<a href="javascript:void(0);" class="page-link" data-page="' + i + '">' + i + '</a>');
	                        }
	                    }
	                }

	                if (currentPage < totalPages - 2) {
	                    $pagination.append('<a href="javascript:void(0);" class="page-link" data-page="' + (currentPage + 3) + '">다음</a>');
	                } else {
	                    $pagination.append('<a href="javascript:void(0);" class="page-link" data-page="' + totalPages + '">다음</a>');
	                }
	            }
	        });
	    }

	    $(document).on('click', '.page-link', function() {
	        currentPageNum = $(this).data('page');  
	        loadManagementList(currentPageNum); 
	    });

	    $('#productList').on('click', '#deleteBtn', function() {
	        const productId = $(this).parent('li').data('product-id');
	        
	        if (!confirm("삭제된 정보는 복구할 수 없습니다.\n선택한 상품 정보를 삭제하시겠습니까?")) {
	            return;
	        }

	        $.ajax({
	            url: "/shop/product/delete/" + productId,
	            type: "DELETE",
	            contentType: "application/json",
	            success: function(response) {
	                alert(response.msg);
	                if (response.msg.includes("완료")) {
	                    loadManagementList(currentPageNum); 
	                }
	            }
	        });
	    });
	});


</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="contatiner">
			<div id="productContainer">
				<ul id="productList">
					<li class="productListHead">
						<span class="productName">상품명</span>
						<span class="productPrice">가격</span>
						<span class="productBundle">묶음 수량</span>
						<span class="productStock">재고량</span>
						<span class="productCreateDate">등록일</span>
					</li>
				</ul>
				<div class="pagination">
				</div>
			</div>
		</div>
	</div>
</body>

</html>