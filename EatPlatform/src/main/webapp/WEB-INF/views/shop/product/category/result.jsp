<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String msg = (String) request.getAttribute("msg"); 
%>
<head>
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
    $(document).ready(function() {
        let msg = "<%= msg %>"; 
        alert(msg);
        window.location.href = "/";
    });
</script>
