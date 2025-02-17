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
	
	});
</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
    	<form action="register" method="POST" >
    		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    		<label for="productName">
    			상품명 :
    			<input type="text" id="productName" name="productName">
    		</label>
    		<label for="productPrice">
    			가격 : 
    			<input type="number" id="productPrice" name="productPrice">
    		</label>
			<div id="category_container">
				<ul id="mainCategory">
					<li>
						<input type="checkbox" id="main_kr" name="mainCategory" value="한식">
						<label for="main_kr">한식</label>
					</li>
				</ul>
			</div>
    		<label for="productBundle">
    			세트 당 묶음 수 :
    			<input type="number"  id="productBundle" name="productBundle" value="1">
    		</label>
    		<label for="productStock">
    			재고 : 
    			<input type="number" id="productStock" name="productStock">
    		</label>
    		<input type="submit" value="상품 등록">
    	</form>
		
	</div>
</body>

</html>