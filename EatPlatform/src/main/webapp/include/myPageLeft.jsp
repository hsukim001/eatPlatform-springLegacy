<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authentication property="principal" var="principal"/>
<div id="leftWrap" class="left">
	<p><a href="/user/detail">회원 정보</a></p>
	
	<sec:authorize access="hasAuthority('ROLE_MEMBER')">
		<p><a href="/reserv/list">나의 예약 목록</a></p>
		<p><a href="/user/business/requestForm">사업자 등록 신청</a></p>		
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('ROLE_STORE')">
		<p><a href="/management/store/list">매장 관리</a></p>
		<p><a href="management/reserv/list">예약 관리</a></p>
		<p><a href="/shop/register">상품 등록</a></p>
	</sec:authorize>
	
	<sec:authorize access="hasAuthority('ROLE_ADMIN')">
		<p><a href="/user/business/requestList">사업자 등록 요청 관리</a></p>
		<p><a href="/store/request/list">가게 등록 관리</a></p>
		<p><a href="/management/report/reviewList">신고 관리</a><p>
	</sec:authorize>
</div>