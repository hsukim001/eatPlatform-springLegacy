
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
		<div>
			<input type="radio" id="reservScheduleBtn" class="scheduleType" name="scheduleType" value="reserv" checked="checked">
			<label for="reservScheduleBtn">예약</label>
			
			<input type="radio" id="holidayScheduleBtn" class="scheduleType" name="scheduleType" value="holiday">
			<label for="holidayScheduleBtn">휴무일</label>
			
			<input type="radio" id="breakTimeScheduleBtn" class="scheduleType" name="scheduleType" value="breakTime">
			<label for="breakTimeScheduleBtn">휴게 시간</label>
		</div>
		<div class="time-slots" id="time-slots">
			<div id="morning-slots">
				<h3 class="morning">오전</h3>
			</div>
			<div id="afternoon-slots">
				<h3 class="afternoon">오후</h3>
			</div>
		</div>
	</div>

</div>