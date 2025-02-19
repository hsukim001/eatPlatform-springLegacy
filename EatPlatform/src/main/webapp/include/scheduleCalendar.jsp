
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="calendar-wrap">
	<div class="calendar-container">
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
	<div>
		<button id="">휴무일 등록</button>
	</div>

</div>