<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">

<style>
#otherCategory {
    display: none;
}
</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<title>사업자 등록 신청</title>
</head>
<body onpageshow="if(event.persisted) noBack();">
    <div id="wrap">
    	<jsp:include page="/include/header.jsp" />
    	
    	<div id="container">
    		<jsp:include page="/include/myPageLeft.jsp"/>
		    <h2>사업자 등록 신청</h2>
		    <form action="request" method="POST">
		    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		    	
		    	<ul>
		    		<li id="username">
		    			<span class="textTitle">아이디</span><span class="colon">:</span><span class="textValue">${username }</span>
		    		</li>
		    		<li id="email">
		    			<span class="textTitle">이메일</span><span class="colon">:</span><span class="textValue">${email }</span>
		    		</li>
		    		<li id="phone">
		    			<span class="textTitle">연락처</span><span class="colon">:</span><span class="textValue">${phone }</span>
		    		</li>
		    		<li id="name">
		    			<span class="textTitle">이름</span><span class="colon">:</span><span class="textValue">${name }</span>
		    		</li>
		    	</ul>
				
		        <input type="submit" value="사업자 신청">
		    </form>
	    </div>
    </div>
</body>
</html>