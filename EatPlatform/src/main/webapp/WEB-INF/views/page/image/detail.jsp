<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="https://code.jquery.com/jquery-3.7.1.js">
</script>
<title>첨부 파일 조회</title>
</head>
<body>
	<p><a href="download?reviewImageId=${reviewImageVO.reviewImageId }">
	${reviewImageVO.reviewImageRealName }.${reviewImageVO.reviewImageExtension }</a></p>

</body>
</html>