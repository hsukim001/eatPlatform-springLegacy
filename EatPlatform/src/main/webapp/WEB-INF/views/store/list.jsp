<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/list.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/store/imgSlider.js"></script>
<script>
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");			
		xhr.setRequestHeader(header, token);
	});
	
	$(function() {
		$(".item_box").click(function(event){
		    if ($(event.target).closest(".img_box button").length) {
		        return;
		    }
		    let storeId = $(this).data('store-id');
			location.href ="/store/detail?storeId=" + storeId;
		});

		
	    $(".phoneNum").each(function() {
	        let rawPhone = $(this).text().trim();
	        let formattedPhone = formatPhoneNumber(rawPhone);
	        if (formattedPhone) {
	            $(this).text(formattedPhone);
	        }
	    });
	    
	    function formatPhoneNumber(phone) {
	        phone = phone.replace(/\D/g, "");

	        if (phone.length === 9) {
	            return phone.replace(/(\d{2})(\d{3})(\d{4})/, "$1-$2-$3");
	        } else if (phone.length === 10) {
	            return phone.replace(/(\d{2,3})(\d{3,4})(\d{4})/, "$1-$2-$3");
	        } else if (phone.length === 11) {
	            return phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1-$2-$3");
	        } else if (phone.length === 12) {
	            return phone.replace(/(\d{4})(\d{4})(\d{4})/, "$1-$2-$3");
	        }

	        return phone;
	    }

	    
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
		            let allCategoryItem =
		                '<li class="all">' +
		                    '<input type="radio" value="" name="mainCategoryId" id="allCategory" checked required>' +
		                    '<img src="<%=request.getContextPath()%>/resources/img/store/map/tag_icon_1.png" alt="태그_전체보기"' +
		                    '<label for="allCategory">전체</label>' +
		                '</li>';
		            $mainCategory.append(allCategoryItem);
	
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
		
		// 하위 카테고리 클릭 시 검색
		$('#subCategory').on('click', 'li', function() {
		    let keyword = $(this).children('label').text();
		    location.href = '/store/list?pageNum=1&keyword=' + encodeURIComponent(keyword);
		});
		
		$('#mainCategory').on('click', 'li', function(){
		    if ($(this).hasClass('all')) {
			    location.href = '/store/list?pageNum=1&keyword=';
		        return;
		    }
		    $('#mainCategory li').removeClass('active');
		    $(this).addClass('active');
		});
		
		// 첫 번째 상위 카테고리의 하위 카테고리 출력
		function callFirstCategory() {
		    let $categories = $('#mainCategory li input[type="radio"]');
		
		    if ($categories.length > 1) { 
		        let firstCategory = $categories.eq(1);
		        let firstMainId = firstCategory.val();
		        
		        if (firstMainId) {
		        	firstCategory.prop('checked', true);
		        	firstCategory.parent('li').addClass('active');
		            RenewalSubCategory(firstMainId);
		        } else {
		            alert("문제가 발생했습니다. 다시 시도해주세요.");
		        }
		    } else {
		        alert("카테고리가 로드되지 않았거나, 선택할 수 있는 카테고리가 없습니다.");
		    }
		}
	}); // End $function
</script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<div id="list_container">
				<div id="list_box">
	
					<div id="category_container">
						<div class="category_title">#카테고리</div>
						<div id="category_item">
							<ul id="mainCategory"></ul>
							<ul id="subCategory"></ul>
						</div>
					</div>
	
					<div id="item_container">
						<c:if test="${not empty recentStores}">
						    <c:forEach var="store" items="${recentStores}">
						        <div class="item_box" data-store-id="${store.storeId }">
						                <c:choose>
							                    <c:when test="${store.storeImageList.size() > 0}">
							                    	<div class="img_box">
										                <div class="img_item">
															<c:forEach var="storeImageVO" items="${store.storeImageList}" varStatus="status">
							             	               		<img src="/store/image/get/${storeImageVO.storeImageId}/storeImageExtension/${storeImageVO.storeImageExtension}" data-img-index="${status.index}" />
								                 	    	</c:forEach>
										                </div>
							                        	<button class="prev-btn">&lt;</button>
	                        							<button class="next-btn">&gt;</button>
                        							</div>
							                    </c:when>
						                    <c:otherwise>
						                    	<div class="img_box">
						                        	<img class="noImg" src="<%=request.getContextPath()%>/resources/img/common/noImg.png" alt="이미지 없음 이미지" />
						                        </div>
						                    </c:otherwise>
						                </c:choose>
						            <div class="text_box">
						                <p class="store_title">${store.storeName}</p>
						                <p class="store_tag">
						                    <span>#${store.mainCategoryName} > ${store.subCategoryName}</span>
						                </p>
						                <pre class="store_comment">${store.storeComment}</pre>
						                <p class="store_hour">영업시간 | ${store.businessHour}</p>
						                <p class="store_phone">연락처 | <span class="phoneNum">${store.storePhone}</span></p>
						            </div>
						        </div>
						    </c:forEach>
						</c:if>
					</div>
					<c:if test="${empty recentStores}">
						<c:if test="${empty keyword}">
							<p>최근 매장이 없습니다.</p>
						</c:if>
						<c:if test="${not empty keyword}">
							<p>검색 결과가 없습니다.</p>
						</c:if>
					</c:if>
	
					<div class="pagination">
						<c:set var="currentPage" value="${currentPage}" />
						<c:set var="totalPages" value="${totalPages}" />
						
						<c:if test="${currentPage > 3}">
							<a class="prevBtn" href="?pageNum=${currentPage - 3}&keyword=${keyword}"></a>
						</c:if>
						
						<c:forEach var="i" begin="1" end="${totalPages}" varStatus="status">
							<c:if test="${(currentPage <= 3 and i <= 5) 
							                  or (currentPage >= totalPages - 2 and i >= totalPages - 4) 
							                  or (i >= currentPage - 2 and i <= currentPage + 2)}">
								<c:choose>
									<c:when test="${i == currentPage}">
										<a href="?pageNum=${i}&keyword=${keyword}" class="current">${i}</a>
									</c:when>
									<c:otherwise>
										<a href="?pageNum=${i}&keyword=${keyword}">${i}</a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:forEach>
						
						<c:if test="${currentPage < totalPages - 2}">
							<a class="nextBtn" href="?pageNum=${currentPage + 3}&keyword=${keyword}">></a>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>
</body>
</html>