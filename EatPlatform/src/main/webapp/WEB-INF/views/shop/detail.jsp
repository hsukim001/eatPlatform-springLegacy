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
<style>
	#productContainer {
		width:1200px;
		display: inline-block;
	}
	
	#productItem {
		width: 100%;
		display: flex;
		margin: 0 auto;
	    padding-bottom: 10px;
	    border-bottom: 1px solid #ddd;
	}
	
	#productItem img {
		width: 85%;
		padding: 2%;
	}
	
	#itemImg {
		width: 40%;
	}
	
	#itemText {
		width: 60%;
	}
	
	.itemText {
		text-align: left;
	}
	
	#productName {
	    padding: 10px 0 7px;
	    font-size: 24px;
	    border-bottom: 1px solid #ddd;
	    margin-bottom: 7px;
	}
	
	#productCategory {
		margin-bottom: 30px;
	}
	
	#productCategory span {
	    border: none;
	    border-radius: 15px;
	    background: #ddd;
	    padding: 2px 10px;
	    color: #444;
	    font-size: 14px;
	}
	
	#productPrice {
		margin-bottom: 105px;
	}
	
	.mainPrice {
		color: #f00;
	    font-weight: bold;
	    font-size: 22px;
	}
	
	.unitPrice {
	    font-size: 14px;
    	color: #444;
	}
	
	.buy_option {
		display: flex;
 		align-items: center;
 		width: 60%;
 		margin: 0 auto;
	}
	
	.buyBtnPlace {
		width: 65%;
		display: flex;
    	justify-content: space-around;
    	margin: 0 auto;
	}
	
	.buy_option button {
	    width: 45%;
	    font-size: 17px;
	    font-weight: bold;
	    background: #40a2ff;
	    color: #fff;
	    border: none;
	    line-height: 40px;
	    cursor: pointer;
	}
	
	#quantityContainer {
		width: 25%;
		display: flex;
		justify-content: space-evently;
	}
	
	#quantity {
		width: 50%;
		float: left;
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
	$(function(){
		
		// 묶음 가격 계산
		let productPrice = parseInt($('#productPrice .price').first().text(), 10);
		let unitQuantity = parseInt($('#unit').text(), 10);
		if (!isNaN(productPrice) && !isNaN(unitQuantity)) {
		    let calcPrice = productPrice / unitQuantity;
		    if (calcPrice % 1 === 0) {
		        calcPrice = calcPrice.toFixed(0);
		    } else {
		        calcPrice = calcPrice.toFixed(1);
		    }
		    $('#calcPrice').text(calcPrice);
		    console.log(calcPrice);
		}
		
		// 마이너스 수량 제한
		$('#quantity').on('keydown change', function(event) {
		    if (event.type === 'keydown') {
		        let value = Number($(this).val());

		        if (event.key === 'ArrowDown' || event.key === '-') {
		            if (value <= 1) {
		            	alert('최소 1개의 상품을 선택해주세요');
		                event.preventDefault();
		            }
		        }
		    }

		    if (event.type === 'change') {
		        essentialQuantity();
		    }
		});
		
		formatPriceText('.price');
		
		// 최소 수량 확인
		function essentialQuantity() {
		    let currentQuantity = parseInt($('#quantity').val(), 10);
		    if (currentQuantity < 1 || isNaN(currentQuantity)) {
		        alert("최소 1개의 상품을 선택해주세요.");
		        $('#quantity').val(1);
		    }
		}
		
		// 원화 단위 구분
		function formatPriceText(selector) {
		    $(selector).each(function () {
		        let priceText = $(this).text();
		        let formattedPrice;

		        if (priceText.includes('.')) {
		            let parts = priceText.split('.');
		            parts[0] = Number(parts[0]).toLocaleString(); 
		            formattedPrice = parts.join('.');
		        } else {
		            formattedPrice = Number(priceText).toLocaleString();
		        }

		        $(this).text(formattedPrice);
		    });
		}
		
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<div id="productContainer">
				<div id="productItem">
					<div id="itemImg">
						<img src="<%=request.getContextPath()%>/resources/img/sample4.jpg" alt="sample">
					</div>
					<div id="itemText">
 						<p id="productName" class="itemText">${product.productName }</p>
						<p id="productCategory" class="itemText">
							<span>${productCategory.mainCategoryName } > ${productCategory.subCategoryName }</span>
						</p>
						<p id="productPrice" class="itemText">
							<span class="mainPrice">
								<span class="price">${product.productPrice }</span>원
							</span><br>
							<span class="unitPrice">
							 	한 묶음 당 
								<span id="unit">${product.productBundle }</span>개 
								(개당 <span id="calcPrice" class="price"></span>원)
							</span>
						</p>
						<p id="calcUnit"></p>
						<div class="buy_option">
							<div id="quantityContainer">
								<input id="quantity" name="quantity" type="number" value="1"> 개
							</div>
							<div class="buyBtnPlace">
								<button id="buyBtn">
									구매
								</button>
								<button id="pickBackBtn">
									장바구니 담기
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
				
			<div id="detailContainer">
				<img src="<%=request.getContextPath()%>/resources/img/detailSample.jpg" alt="상세이미지">
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />
	</div>
</body>

</html>