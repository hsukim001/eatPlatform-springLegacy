<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Boolean result = (Boolean) request.getAttribute("result"); 
    if (result == null) result = false;
%>
<head>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
$(document).ready(function() {
    let result = <%= result %>;

    if (result) {
        alert("상품 정보 수정이 완료되었습니다.");
        window.location.href = "/shop/product/management";
    } else {
        alert("상품 정보 수정이 실패했습니다. 다시 시도해주세요.");
        history.back();
    }
});
</script>
