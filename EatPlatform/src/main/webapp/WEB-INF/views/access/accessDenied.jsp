<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>접근 제한</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
	    <!-- SPRING_SECURITY_403_EXCEPTION.getMessage() : Spring Security에서 전달된 403 예외 메시지를 출력 -->
	    <h2>${SPRING_SECURITY_403_EXCEPTION.getMessage()}</h2>
	    
	    <!-- msg : 사용자가 직접 설정한 메시지를 출력 -->
	    <h2>${msg}</h2>		
	</div>
</body>
</html>
