<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>
<button onclick="location.href='user/register'">회원가입</button>
<sec:authorize access="isAnonymous()">
	<button onclick="location.href='access/login'">로그인</button>
	<button onclick="location.href='store/list'">식당리스트</button>
</sec:authorize>

<sec:authorize access="isAuthenticated()">
	<button onclick="location.href='user/detail'"><sec:authentication property="principal.username"/>님</button>
	<button onclick="location.href='store/list'">식당리스트</button>
	<form action="access/logout" method="post">
		<input type="submit" value="로그아웃">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
	</form>
</sec:authorize>


</body>
</html>
