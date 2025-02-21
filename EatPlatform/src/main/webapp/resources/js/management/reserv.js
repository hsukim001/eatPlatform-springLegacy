$(function () {
    const calendarDays = $('#calendar-days');
    const currentMonthYear = $('#current-month-year');
    const prevMonthBtn = $('#prev-month');
    const nextMonthBtn = $('#next-month');
    const selectionDisplay = $('#selection-display');
    const timeSlotsContainer = $('#time-slots');


    const startTimeInput = $('#start-time');
    const endTimeInput = $('#end-time');

    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

    let today = new Date();
    let currentMonth = today.getMonth();
    let currentYear = today.getFullYear();
    let selectedDate = today; // 기본적으로 오늘 날짜를 선택
    let selectedTime = null;		
	let timeValue;
	let dateText;
	let timeText;
	let selectedDateValue;
	let todayText;
	
	// 예약 확인
	let reservList;
	let reservInfoList;
	
	// 휴무 시간
	let breakTimeList;
	
	// 일정 유형
	let scheduleType = "reserv";
	let selectHolidayDate;
			
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
				
		xhr.setRequestHeader(header, token);
	}); // End ajaxSend
	
	$(document).on('click', '.calendar div', function(){
		selectedDateValue = $(this).data('dateValue');
		
	}); // End document .calendar div click
	
	$(document).on('click', '#reservScheduleBtn', function(){
	    if($('#reservScheduleBtn').prop('checked')) {
	    	scheduleType = "reserv";
	    	$('.calendar .selected').removeClass('selected'); 
	        $('.today').addClass('selected');
	        $('.morning').show();
			$('.afternoon').show();
			
			console.log("scheduleType : " + scheduleType);
			
			generateCalendar(currentMonth, currentYear);
			updateSelectionDisplay();
			console.log("todayText : " + todayText);
			
	    } else {
	    	$('.today').removeClass('selected');
	    }
	});
	
	$(document).on('click', '#holidayScheduleBtn', function(){
	    if($('#holidayScheduleBtn').prop('checked')) { 
	    	scheduleType = "holiday";
	    	
	    	$('.calendar .selected').removeClass('selected');
	    	$('#morning-slots').children().not('h3').remove();
			$('#afternoon-slots').children().not('h3').remove();
	    	$('.morning').hide();
			$('.afternoon').hide();
			console.log("scheduleType : " + scheduleType);
			generateCalendar(currentMonth, currentYear);
			console.log("todayText : " + todayText);
	    } else {
	    	$('.today').addClass('selected');
	    }
	});
	
	$(document).on('click', '#breakTimeScheduleBtn', function(){
	    if($('#breakTimeScheduleBtn').prop('checked')) {
	    	scheduleType = "breakTime";
	    	$('.calendar .selected').removeClass('selected');
	        $('.today').addClass('selected');
	        console.log("scheduleType : " + scheduleType);
	        generateCalendar(currentMonth, currentYear);
	        console.log("todayText : " + todayText);
	    } else {
	    	$('.today').removeClass('selected');
	    }
	});
	
	// 캘린더 조회 함수
    function updateSelectionDisplay() {
        dateText = selectedDate ? `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')}` : '날짜를 선택하세요';
        timeText = selectedTime || '시간을 선택하세요';
    } // End updateSelectionDisplay

	// 캘린더 함수
    function generateCalendar(month, year) {
        calendarDays.empty();

        // Add day names
        dayNames.forEach(day => {
            const dayNameCell = $('<div>').text(day).addClass('day-names');
            calendarDays.append(dayNameCell);
        });

        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();

        currentMonthYear.text(`${year}.${(month + 1).toString().padStart(2, '0')}`);

        for (let i = 0; i < firstDay; i++) {
            const emptyCell = $('<div>');
            calendarDays.append(emptyCell);
        }

        for (let day = 1; day <= daysInMonth; day++) {
            const currentDay = new Date(year, month, day);
            const dateValue = `${currentDay.getFullYear()}-${(currentDay.getMonth() + 1).toString().padStart(2, '0')}-${currentDay.getDate().toString().padStart(2, '0')}`;
            const dayCell = $('<div>').text(day).attr('data-date-value', dateValue);

            // 오늘 날짜를 제외한 과거 날짜에만 'disabled' 추가
            if (currentDay < today && !(currentDay.getDate() === today.getDate() && currentDay.getMonth() === today.getMonth() && currentDay.getFullYear() === today.getFullYear()) && scheduleType != "reserv") {
                dayCell.addClass('disabled');
            }

            // 오늘 날짜는 'selected' 상태로 기본 선택
            if (
                day === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear()
            ) {
                dayCell.addClass('today selected');
                selectedDate = currentDay;
                todayText = dateValue;
            }


            dayCell.on('click', function () {
            	if(scheduleType != "holiday") {
		        	if (!$(this).hasClass('disabled')) {
		            	$('.calendar .selected').removeClass('selected');
		                $(this).addClass('selected');
		                selectedDate = new Date(year, month, day);
		                updateSelectionDisplay();
		                
		                console.log("dateText : " + dateText);
		                
		                if(scheduleType == "reserv") {
		                	choiceDayReservList();
		                }

		            }            		            		
            	} else {
            		if($(this).hasClass('selected')) {
            			$(this).removeClass('selected');
			            selectedDate = new Date(year, month, day);
			            updateSelectionDisplay();
            		} else {
			            $(this).addClass('selected');
			            selectedDate = new Date(year, month, day);
			            updateSelectionDisplay();            			
            		}
            	}
        	});

            calendarDays.append(dayCell);
        }

        updateSelectionDisplay();
    } // End generateCalendar

    prevMonthBtn.on('click', function () {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        generateCalendar(currentMonth, currentYear);
    }); // End prevMonthBtn click

    nextMonthBtn.on('click', function () {
    
    	$('div.selected').each(function () {
		    let storeId = $('#storeId').val(); // 첫 번째 id 값 설정
		    let dateValue = $(this).data('date-value'); // 날짜 값 추출
		
		    if (selectHolidayDate.storeId === null && storeId) {
		        selectHolidayDate.storeId = storeId; // 첫 번째 요소의 ID 값만 저장
		    }
		
		    if (dateValue) {
		        selectHolidayDate.date.push(dateValue); // 날짜 배열에 추가
		    }
		});
    
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentMonth, currentYear);
    }); // End nextMonthBtn click
    
    // 휴게 시간 버튼 생성 함수
    function generateBreakTimeSlots() {
		console.log(activeTimeList);
	    const morningSlotsContainer = $('#morning-slots');
	    const afternoonSlotsContainer = $('#afternoon-slots');
	
	    // 기존의 슬롯 컨테이너 초기화
		morningSlotsContainer.children(':not(h3)').remove();
    	afternoonSlotsContainer.children(':not(h3)').remove();
	
	    let startTime = startTimeInput.val().split(':');
	    let startHour = parseInt(startTime[0]);
	    let startMinutes = parseInt(startTime[1]);
	
	    let endTime = endTimeInput.val().split(':');
	    let endHour = parseInt(endTime[0]);
	    let endMinutes = parseInt(endTime[1]);
	
        if (startMinutes < 30) {
            startMinutes = 30;
        } else {
            startMinutes = 0;
            startHour++;
        }
	
	    // 선택된 날짜의 시작 시간 설정
	    let currentTime = new Date(selectedDate);
	    currentTime.setHours(startHour, startMinutes, 0, 0);
	
	    // 종료 시간 설정
	    let endTimeObject = new Date(selectedDate);
	    endTimeObject.setHours(endHour, endMinutes, 0, 0);
	
	    if (endTimeObject <= currentTime) {
	        endTimeObject.setDate(endTimeObject.getDate() + 1);
	    }
	
		//console.log(activeTimeList);
	    while (currentTime <= endTimeObject) {
		    const timeSlotText = currentTime.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
		    
		    const timeSlotText24 = currentTime.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: false });
		    const timeSlotText24Split = timeSlotText24.split(':',2);
		    const timeSlotText24Hour = timeSlotText24Split[0];
		    const timeSlotText24Minutes = timeSlotText24Split[1];
			const timeSlot = $('<button>').text(timeSlotText).attr('data-time-value', timeSlotText24);
		    const now = new Date();
		    
		    let time = timeSlotText24Hour + timeSlotText24Minutes;
		
			 if (selectedDate.getDate() === today.getDate() &&
		        selectedDate.getMonth() === today.getMonth() &&
		        selectedDate.getFullYear() === today.getFullYear()) {
		        
			    if (currentTime.getHours() < now.getHours() || 
			        (currentTime.getHours() === now.getHours() && currentTime.getMinutes() < now.getMinutes())) {
			        timeSlot.addClass('disabled');
			    }

		    }

		    
		    		    
		    if(breakTimeList !== null && breakTimeList !== undefined) {
		    
		    	for(let i = 0; i < breakTimeList.length; i++){
		    		if(timeSlotText24Hour === breakTimeList[i].hour && timeSlotText24Minutes === breakTimeList[i].min){
		    			if(breakTimeList[i].breakTime == time){
		    				console.log("disabled");
		    				console.log('not null');
		    				timeSlot.addClass('disabled');
		    			}
		    		}
		    	}
		    }
		    
		    
		    timeSlot.on('click', function () {
		        if (!$(this).hasClass('disabled')) {
		        	timeValue = $(this).data("timeValue");
		        	console.log($(this).data("timeValue"));
		            $('.time-slots .selected').removeClass('selected');
		            $(this).addClass('selected');
		            selectedTime = $(this).text();
		            console.log(currentTime);
		            updateSelectionDisplay();
		        }
		    });
		
		    // 오전과 오후를 구분하여 적절한 컨테이너에 추가
		    if (currentTime.getHours() < 12) {
		        morningSlotsContainer.append(timeSlot);
		    } else {
		        afternoonSlotsContainer.append(timeSlot);
		    }
		
		    // 30분 간격으로 증가
		    currentTime.setMinutes(currentTime.getMinutes() + 30);
		    
		}
	} // End generateBreakTimeSlots
    
	function updateSelectedTime() {
	    let timeValueSplit = timeValue.split(':');
	    selectTimeHour = timeValueSplit[0];
	    selectTimeMinutes = timeValueSplit[1];
	} // End updateSelectedTime
	
	
	function observeDOMChanges() {
   		const observer = new MutationObserver(function(mutationsList, observer) {
	        // 'button2'가 DOM에 추가되었는지 감지
	        let preSelected = $('.time-slots div button[data-time-value="' + timeValue + '"]');
	        if (preSelected.length > 0) {
	            $(preSelected).trigger('click');
	            observer.disconnect();  // DOM 감지 종료
	        }
	    });

	    // 감지할 대상과 옵션 설정
	    observer.observe(document.querySelector('.time-slots'), {
	        childList: true,   // 자식 노드 추가/삭제를 감지
	        subtree: true      // 자식 노드들까지 감지
	    });
	} // End observeDOMChanges
	
	// 선택한 일자의 예약 목록 조회 함수
	function choiceDayReservList() {
		let storeId = $('#storeId').val();
		$.ajax({
			url : '/reserv/choiceDate/' + storeId + '/' + dateText,
			type : 'get',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				console.log("reservList : " + response.list);
				if(response.list == "") {
					console.log("해당 일의 예약이 없습니다.");
					reservList = {};
				} else {
					console.log("해당 일의 예약이 존재합니다.");
					reservList = response.list;
				}
				seletedDateText = dateText;
				generateReservTimeSlots();
			},
			error : function() {
				alert("예약 목록 조회중 오류가 발생하였습니다.");
			}
		});
	} // End choiceDayReservList
	
	// 선택한 일자의 예약 목록을 예약 시간 별 버튼 생성 함수
	function generateReservTimeSlots() {
		const reservMorningSlotsContainer = $('#morning-slots');
	    const reservAfternoonSlotsContainer = $('#afternoon-slots');
		
		// 기존의 슬롯 컨테이너 초기화
		reservMorningSlotsContainer.children(':not(h3)').remove();
    	reservAfternoonSlotsContainer.children(':not(h3)').remove();
    	
    	let morningCount = 0;
    	let afternoonCount = 0;
    	
    	console.log("generateReservTimeSlots reservList : " + reservList);
	
		if(Object.keys(reservList).length !== 0) {
			for(let i = 0; i < reservList.length; i++ ) {
				let hour = reservList[i].hour;
				let min = reservList[i].min;
				
				let date = new Date();
				date.setHours(hour, min);
			
				let reservTimeSlotText = date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
				console.log("reservTimeSlotText : " + reservTimeSlotText);
				
				let time = reservList[i].hour + ":" + reservList[i].min;
				console.log("reserv Time : " + time);
				
				let totalCountText = reservList[i].reservTeam + "팀 및 " + "총 " + reservList[i].totalPersonnel + "명";
				
				let reservTimeSlot = $('<button>').text(reservTimeSlotText + ", " + totalCountText).attr('data-time-value', time);
				
				
				// 오전과 오후를 구분하여 적절한 컨테이너에 추가
				if (reservList[i].hour < 12) {
					reservMorningSlotsContainer.append(reservTimeSlot);
					morningCount++;
				} else {
				    reservAfternoonSlotsContainer.append(reservTimeSlot);
				    afternoonCount++;
				}		
				
				reservTimeSlot.on('click', function () {
			        if (!$(this).hasClass('disabled')) {
			        
			        	timeValue = $(this).data("timeValue");
			        	console.log($(this).data("timeValue"));
			        	console.log(timeValue);
			            $('.time-slots .selected').removeClass('selected');
			            $(this).addClass('selected');
			            selectedTime = $(this).text();
			            updateSelectionDisplay();
			            choiceDayReservInfo();
			            $('#reservScheduleModal').show();
			            
			        }
			    });
		    	
			}
			
			checkReservInfo(morningCount, afternoonCount, reservMorningSlotsContainer, reservAfternoonSlotsContainer);
			
		} else {
			checkReservInfo(morningCount, afternoonCount, reservMorningSlotsContainer, reservAfternoonSlotsContainer);
		}
		
	} // End generateReservTimeSlots
	
	// 예약 정보 확인
	function checkReservInfo(morningCount, afternoonCount, reservMorningSlotsContainer, reservAfternoonSlotsContainer) {
		if(morningCount == 0) {
				let morningMsg = "오전 예약이 존재하지 않습니다.";
				let morningMsgSlot = $('<p>').text(morningMsg);
				reservMorningSlotsContainer.append(morningMsgSlot);
			}
			
			if(afternoonCount == 0) {
				let afternoonMsg = "오후 예약이 존재하지 않습니다.";
				let afternoonMsgSlot = $('<p>').text(afternoonMsg);
				reservAfternoonSlotsContainer.append(afternoonMsgSlot);
			}
	} // End checkReservInfo
	
	// 선택된 시간의 예약자 상세 정보 조회 함수
	function choiceDayReservInfo() {
		let timeSplit = timeValue.split(':');
		let hour = timeSplit[0];
		let min = timeSplit[1];
		let storeId = $('#storeId').val();
		$.ajax({
			url : '/reserv/choiceDay/' + storeId + '/' + dateText + '/' + hour + '/' + min,
			type : 'get',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				reservInfoList = response.list;
				generateReservInfo();
			},
			error : function() {
				alert("예약 정보를 조회하는 중 오류가 발생하였습니다.");
			}
		});
	} // End choiceDayReservInfo
	
	// 예약자 목록 생성 함수
	function generateReservInfo() {
	
		let timeSplit = timeValue.split(':');
		let hour = timeSplit[0];
		let min = timeSplit[1];
		
		let reservInfo = "";
		for(let i = 0; i < reservInfoList.length; i++) {
			let reservId = reservInfoList[i].reservId;
		
			let reservCancelBtn = '<button id="reservCancelBtn" data-id-value="' + reservId + '">예약 취소</button>';
			
			let todayDate = new Date(todayText);
			let reservDate = new Date(reservInfoList[i].reservDate);
			
			if(todayDate <= reservDate) {
				reservInfo += 
					'<li>' + (i + 1) + '.' +
					'<span> 예약자 명 : ' + reservInfoList[i].name + '</span>' +
					'<span>, 연락처 : ' + reservInfoList[i].phone + '</span>' +
					'<span>, 인원 : ' + reservInfoList[i].reservPersonnel + '</span>' +
					'<span>, 접수일 : ' + reservInfoList[i].reservDateCreated + '</span>' +
					reservCancelBtn
					+ '</li>';
			} else {
				reservInfo += 
					'<li>' + (i + 1) + '.' +
					'<span> 예약자 명 : ' + reservInfoList[i].name + '</span>' +
					'<span>, 연락처 : ' + reservInfoList[i].phone + '</span>' +
					'<span>, 인원 : ' + reservInfoList[i].reservPersonnel + '</span>' +
					'<span>, 접수일 : ' + reservInfoList[i].reservDateCreated + '</span>'
					+ '</li>';			
			}
			
			
		};
				
		let date = new Date();
		date.setHours(hour, min);
			
		let reservTimeText = date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
				
		$('#reservDay').text(dateText + reservTimeText + " 예약 정보");
		$('#reservUserInfo').html(reservInfo);
	} // End generateReservInfo
	
	// 모달창 예약 취소 버튼 이벤트 리스너
	$(document).on('click', '#reservCancelBtn', function(){
		console.log("예약 취소");
		let isCancel = confirm("해당 회원의 예약을 취소하시겠습니까?");
		
		if(isCancel) {
			console.log("true");
			let reservId = $(this).data("id-value");
			console.log("reservId : " + reservId);
			cancelReserv(reservId);
		}
		
	}); // End #reservCancelBtn button.click
	
	// 예약 취소 함수
	function cancelReserv(reservId) {
		$.ajax({
			url : '/reserv/cancel/' + reservId,
			type : 'delete',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				if(response == 1) {
					alert("예약 취소가 완료되었습니다.");
					$().hide();
					location.reload(true);
				}
			},
			errors : function() {
				alert("예약 취소중에 오류가 발생하였습니다.");
			}
		});
	} // End cancelReserv
	
	// 예약 모달창 닫기 버튼 이벤트 리스너
	$('#reservScheduleModal #topCloseBtn, #reservScheduleModal #bottomCloseBtn').click(function() {
		$('.time-slots .selected').removeClass('selected');
		$('#reservScheduleModal').hide();
	}); // end reservScheduleModal closeBtn click
	
	// 예약 확인 버튼 클릭 이벤트
	$('#reservBtn').click(function(){
		fetch('../../access/auth/status', { method : 'get', credentials: 'include' })
			.then(response => response.json())
			.then(isAuthenticated => {
				if(isAuthenticated) {
					$('#calendar-wrap > div').stop().fadeToggle(400);
					$('#calendar-wrap').stop().slideToggle(500);						
				} else {
					alert('로그인이 필요합니다.');
				}
			})
			.catch(error => {
				console.error("Error checking authentication status:", error);
			});
	}); // End #reservBtnWrap input.click
	

    generateCalendar(currentMonth, currentYear);
    choiceDayReservList();
    updateSelectionDisplay();

});