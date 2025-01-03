    <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
    
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
            <div class="time-slots" id="time-slots"></div>
        </div>

        <div class="selection-display" id="selection-display">
          	선택 결과 출력창
        </div>

       
        <input type="time" id="start-time" class="hidden-input" value="${startTime }">
        <input type="time" id="end-time" class="hidden-input" value="${endTime }">
    </div>