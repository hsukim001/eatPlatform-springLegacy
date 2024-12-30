<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>첨부 파일 리스트</title>
</head>
<body>
	
	<c:forEach var="reviewImageId" items="${idList }">
	<p>
		첨부 파일 번호 : <a href="detail?reviewImageId=${reviewImageId }">${reviewImageId }</a>
	</p>
	</c:forEach>

</body>
</html>