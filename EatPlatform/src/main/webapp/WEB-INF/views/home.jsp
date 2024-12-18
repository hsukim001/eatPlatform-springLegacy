<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<button onclick="location.href='user/flag'">회원가입</button>
<c:if test="${not empty sessionScope }">
	<button onclick="location.href='user/detail'">${sessionScope.userId }</button>
	<button onclick="location.href='store/list'">식당리스트</button>
	<button onclick="location.href='access/logout'">로그아웃</button>
</c:if>
<c:if test="${empty sessionScope }">
	<button onclick="location.href='access/login'">로그인</button>
	<button onclick="location.href='store/list'">식당리스트</button>
</c:if>


</body>
</html>
