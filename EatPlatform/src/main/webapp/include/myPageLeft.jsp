<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication property="principal" var="principal"/>
<div id="leftWrap" class="left">
	<p><a href="../user/detail">회원 정보</a></p>
	<p><a href="../reserv/list">나의 예약 목록</a></p>
	<c:if test="${principal.authorities eq 'ROLE_MEMBER' }">
	</c:if>
	<c:if test="${principal.authorities eq 'ROLE_STORE' }">
		<p><a>식당관리</a></p>
	</c:if>
</div>