<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		
		<title>관리자 계정 생성</title>
	</head>
	<body>
		<div id="wrap">
			<jsp:include page="/include/header.jsp" />
			
			<h2>관리자 계정 생성</h2>
		</div>
	</body>
</html>