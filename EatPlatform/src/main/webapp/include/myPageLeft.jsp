<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication property="principal" var="principal"/>
<div id="leftWrap" class="left">
	<p><a href="../user/detail">회원 정보</a></p>
	<p><a href="../reserv/list">나의 예약 목록</a></p>
	<p><a href="../user/businessRequestForm">사업자 회원 신청</a></p>
	
	<sec:authorize access="hasAuthority('ROLE_MEMBER')">
		<p><a>멤버</a></p>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('ROLE_STORE')">
		<p><a>사업자</a></p>
		<p><a>매장 관리</a></p>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('ROLE_ADMIN')">
		<p><a>리뷰 신고 관리</a></p>
		<p><a>사업자 등록 요청</a></p>
	</sec:authorize>
</div>