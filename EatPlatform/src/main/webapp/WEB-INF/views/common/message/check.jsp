<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String message = (String) request.getAttribute("message");
	String url = (String) request.getAttribute("url");
%>
<head>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
	$(document).ready(function(){
		let message = '<%= message %>';
		let url = '<%= url %>';
		
		console.log(message);
		console.log(url);

		alert(message);
		location.href = url;
	});
</script>