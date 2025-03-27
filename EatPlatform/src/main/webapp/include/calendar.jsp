
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="calendar-wrap">
	<div class="calendar-container">
		<p class="limit_ment">해당 시간대는 <span id="reservLimit">${storeVO.reservLimit}</span>명 까지 예약 가능합니다.</p>
		<div class="personnel_controller">
			<button id="minerBtn">-</button>
			<span id="personnel">1</span>
			<button id="plusBtn">+</button>
			<input type="number" id="inputPersonnel" class="writePersonnel">
			<button id="applyBtn">적용</button>	
		</div>
		<div class="calendar-header">
			<button id="prev-month">&lt;</button>
			<h2 id="current-month-year"></h2>
			<button id="next-month">&gt;</button>
		</div>
		<div class="calendar" id="calendar-days"></div>
	</div>

	<div class="time-container">
		<h3>시간을 선택해주세요.</h3>
		<div class="time-slots" id="time-slots">
			<div id="morning-slots">
				<h3>오전</h3>
			</div>
			<div id="afternoon-slots">
				<h3>오후</h3>
			</div>
		</div>
	</div>

	<div class="selection-display" id="selection-display">선택 결과 출력창</div>


	<input type="time" id="start-time" class="hidden-input"
		value="${startTime }"> <input type="time" id="end-time"
		class="hidden-input" value="${endTime }">
	
	<sec:authorize access="isAnonymous()">
		<input type="button" value="로그인" onclick="location.href='../access/login'">
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<input id="createdReserv" type="button" value="등록">		
	</sec:authorize>
</div>