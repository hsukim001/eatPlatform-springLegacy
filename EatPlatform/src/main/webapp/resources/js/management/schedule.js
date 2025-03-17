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
    let currentDate = today.getDate();
    let currentMonth = today.getMonth();
    let originMonth = currentMonth + 1;
    let currentYear = today.getFullYear();
	let formattedMonth = (currentMonth < 10) ? '0' + originMonth : originMonth;
	let formattedDate = (currentDate < 10) ? '0' + currentDate : currentDate;
	let todayDate = parseInt(currentYear + '' + formattedMonth + formattedDate);
	let dateReplace = todayDate;
    let selectedDate = today; 
    let selectedTime, timeValue, dateText, timeText, selectedDateValue, todayText;
	
	// 예약 확인
	let reservList;
	let reservInfoList;
	let cancelReservInfoList;
	
	// 휴무 시간
	let holidayList;
	
	// 일정 유형
	let scheduleType = "reserv";
	let selectHolidayDate = {
		"list" : [] 
	};
	let selectHolidayCount = 0;
	let cancelHolidayDate ={
		"list" : []
	};
	let registrationHolidayStatus;
	let cancelHolidayStatus;
	let isCancelReservStatus;
	let cancelReservStatus;
	let reservStatus;
	let totalCancel;
			
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
			$('#currentSelectedDate').show();
			$('#holidayRegisterBtn').hide();
			selectHolidayDate.list = [];
			cancelHolidayDate.list = [];
			
			console.log("scheduleType : " + scheduleType);
			
			searchHoliday();
			generateCalendar(currentMonth, currentYear);
			updateSelectionDisplay();
			generateReservTimeSlots();
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
			$('#currentSelectedDate').hide();
			$('#holidayRegisterBtn').show();
			selectHolidayDate.list = [];
			cancelHolidayDate.list = [];
			console.log("scheduleType : " + scheduleType);
			
			searchHoliday();
			generateCalendar(currentMonth, currentYear);
			console.log("todayText : " + todayText);
	    } else {
	    	$('.today').addClass('selected');
	    }
	});
	
	// 캘린더 조회 함수
    function updateSelectionDisplay() {
        dateText = selectedDate ? `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')}` : '날짜를 선택하세요';
        timeText = selectedTime || '시간을 선택하세요';
        
        let date = String(dateReplace).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
        $('#currentSelectedDate').text(date + " 예약 정보");
    } // End updateSelectionDisplay

	// 캘린더 함수
    function generateCalendar(month, year) {
        calendarDays.empty();
		20250227
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
            //const isTodayHoliday;
			
            // 오늘 날짜를 제외한 과거 날짜에만 'disabled' 추가
            if (currentDay < today && !(currentDay.getDate() === today.getDate() && currentDay.getMonth() === today.getMonth() && currentDay.getFullYear() === today.getFullYear()) && scheduleType != "reserv") {
                dayCell.addClass('disabled');
            }
            
            if(holidayList.length > 0) {
            	const dateReplace = dateValue.replace(/-/g, '');
            	const isCancelHolidayDuplicate = cancelHolidayDate.list.some(item => item.holiday === dateReplace);
            	if(!isCancelHolidayDuplicate) {
		            for(let i = 0; i < holidayList.length; i++) {
		            	const date = (holidayList[i].holiday.slice(0, 4) + "-" + holidayList[i].holiday.slice(4)).slice(0, 7) + "-" + (holidayList[i].holiday.slice(0, 4) + "-" + holidayList[i].holiday.slice(4)).slice(7);
		            	if(dateValue == date) {
		            		dayCell.addClass('holiday');
		            	}
		            }
	            }
            }

            // 오늘 날짜는 'selected' 상태로 기본 선택
            if (
                day === today.getDate() &&
                month === today.getMonth() &&
                year === today.getFullYear() && scheduleType == "reserv"
            ) {
                dayCell.addClass('today selected');
                selectedDate = currentDay;
                todayText = dateValue;
            }
            
            // 선택됬던 일자 선택
            if(selectHolidayDate.list.length > 0 && scheduleType != "reserv") {
            	const dateReplace = dateValue.replace(/-/g, '');
            	const isSelectHolidayDuplicate = selectHolidayDate.list.some(item => item.holiday === dateReplace);
            	
            	if(isSelectHolidayDuplicate) {
            		dayCell.addClass('selected');
            	}
            }


            dayCell.on('click', function () {
            	let date = $(this).data('date-value');
				dateReplace = date.replace(/-/g, '');
				console.log("dayCell : " + dateReplace);
            	
	            if(scheduleType != "holiday") {
	            	if (!$(this).hasClass('disabled') && !$(this).hasClass('holiday')) {
			            $('.calendar .selected').removeClass('selected');
			            $(this).addClass('selected');
			            selectedDate = new Date(year, month, day);
			            updateSelectionDisplay();
			                
			            console.log("dateText : " + dateText);
			                
			            if(scheduleType == "reserv") {
			            	choiceDayReservList(dateReplace);
			            }
			          	
			        }
	
	            } else if(scheduleType == "holiday" && !$(this).hasClass('disabled')) {
	            	if($(this).hasClass('selected') && !$(this).hasClass('holiday')) {
	            		
	            		$(this).removeClass('selected');
				        selectedDate = new Date(year, month, day);
				        
				            
				        let isDuplicate = selectHolidayDate.list.some(item => item.holiday === dateReplace);
			            if(isDuplicate) {
			            	console.log("coooo");
			                	
						   	for (let i = selectHolidayDate.list.length - 1; i >= 0; i--) {
								if (selectHolidayDate.list[i].holiday === dateReplace) {
									console.log("delete");
							    	selectHolidayDate.list.splice(i, 1); // 해당 인덱스의 요소 삭제
							    }
							}
							    
						}
						
						selectHolidayCount--;
				        updateSelectionDisplay();
	            	} else if(!$(this).hasClass('selected') && !$(this).hasClass('holiday')) {
	            		let isHolidayList = holidayList.some(item => item.holiday === dateReplace);
	            		if(selectHolidayCount <= 20 && !isHolidayList) {
					        $(this).addClass('selected');
					        selectedDate = new Date(year, month, day);
					        selectHolidayCount++;
					        updateSelectionDisplay();
				        } else if(selectHolidayCount > 20 && !isHolidayList) {
				        	alert("한번에 20개의 휴무일 등록이 가능합니다.");
				        } else if(isHolidayList) {
				        	for (let i = cancelHolidayDate.list.length - 1; i >= 0; i--) {
								if (cancelHolidayDate.list[i].holiday === dateReplace) {
							    	cancelHolidayDate.list.splice(i, 1); // 해당 인덱스의 요소 삭제
							    }
							}
							$(this).addClass('holiday');
							console.log("cancelHolidayDate.list : " + cancelHolidayDate.list);
				        }
	            	} else if($(this).hasClass('holiday')) {
	            		console.log("휴무일 취소");
	            		let storeId = $('#storeId').val();
	            		$(this).removeClass('holiday');
	            		let obj = {
	            			"storeId" : storeId,
	            			"holiday" : dateReplace
	            		};
	            		cancelHolidayDate.list.push(obj);
	            		console.log("cancelHolidayDate.list : " + cancelHolidayDate.list);
	            	} else if(!$(this).hasClass('holiday')) {}
	            }
	            	
        	});

            calendarDays.append(dayCell);
        }

        updateSelectionDisplay();
    } // End generateCalendar

    prevMonthBtn.on('click', function () {
    	let storeId = $('#storeId').val();
    
    	if(scheduleType != "reserv") {
	    	$('div.selected').each(function () {
			    let date = $(this).data('date-value'); // 날짜 값 추출
			    let dateReplace = date.replace(/-/g, '');
			    let obj = {
			    	"storeId" : storeId,
			    	"holiday" : dateReplace
			    };
			    
			    // 중복 확인
			    let isDuplicate = selectHolidayDate.list.some(item => item.holiday === dateReplace);
			    
			    if(!isDuplicate) {
			    	selectHolidayDate.list.push(obj);
			    }
			    
			    
			});
		}
		console.log(selectHolidayDate);
    
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 12;
            currentYear--;
        }
        generateCalendar(currentMonth, currentYear);
    }); // End prevMonthBtn click

    nextMonthBtn.on('click', function () {
    	let storeId = $('#storeId').val();
    
    	if(scheduleType != "reserv") {
	    	$('div.selected').each(function () {
			    let date = $(this).data('date-value'); // 날짜 값 추출
			    let dateReplace = date.replace(/-/g, '');
			    let obj = {
			    	"storeId" : storeId,
			    	"holiday" : dateReplace,
			    };
			    
			    // 중복 확인
			    let isDuplicate = selectHolidayDate.list.some(item => item.holiday === dateReplace);
			    
			    if(!isDuplicate) {
			    	selectHolidayDate.list.push(obj);
			    }
			    
			    
			});
		}
		console.log(selectHolidayDate);
    
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentMonth, currentYear);
    }); // End nextMonthBtn click
	
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
	function choiceDayReservList(dateReplace) {
		console.log("currentYear : " + currentYear);
		console.log("currentMonth : " + currentMonth);
		console.log("currentDate : " + currentDate);
		console.log("todayDate : " + todayDate);
		console.log("new Date : " + new Date());
		console.log("today : " + today);
		
		let storeId = $('#storeId').val();
		// const date = currentDay;
		$.ajax({
			url : '/reserv/choiceDate/' + storeId + '/' + dateReplace,
			type : 'get',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response) {
				console.log("reservList : " + response.list);
				console.log(response);
				if(response.list == "") {
					console.log("해당 일의 예약이 없습니다.");
					reservList = {};
				} else {
					console.log("해당 일의 예약이 존재합니다.");
					reservList = response.list;
				}
				seletedDateText = dateReplace;
				generateReservTimeSlots();
			},
			error : function() {
				alert("예약 목록 조회중 오류가 발생하였습니다.");
			}
		});
	} // End choiceDayReservList
	
	// 선택한 일자의 예약 목록을 예약 시간 별 버튼 생성 함수
	function generateReservTimeSlots() {
		console.log("z");
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
			
		} else {
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
		}
		
	} // End generateReservTimeSlots
	
	// 선택된 시간의 예약자 상세 정보 조회 함수
	function choiceDayReservInfo() {
		let timeSplit = timeValue.split(':');
		let hour = timeSplit[0];
		let min = timeSplit[1];
		let storeId = $('#storeId').val();
		let dateReplace = dateText.replace(/-/g, '');
		$.ajax({
			url : '/reserv/choiceDay/' + storeId + '/' + dateReplace + '/' + hour + '/' + min,
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
					'<li>' +
					'<span>' + (i + 1) + '. 예약자 명 : ' + reservInfoList[i].name + '</span>' +
					'<span>, 연락처 : ' + reservInfoList[i].phone + '</span>' +
					'<span>, 인원 : ' + reservInfoList[i].reservPersonnel + '</span>' +
					'<span>, 접수일 : ' + reservInfoList[i].reservDateCreated + '</span>' +
					reservCancelBtn
					+ '</li>';
			} else {
				reservInfo += 
					'<li>' +
					'<span>' + (i + 1) + '. 예약자 명 : ' + reservInfoList[i].name + '</span>' +
					'<span>, 연락처 : ' + reservInfoList[i].phone + '</span>' +
					'<span>, 인원 : ' + reservInfoList[i].reservPersonnel + '</span>' +
					'<span>, 접수일 : ' + reservInfoList[i].reservDateCreated + '</span>'
					+ '</li>';			
			}
			
			
		};
				
		let date = new Date();
		date.setHours(hour, min);
			
		let reservTimeText = date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
		let formattedReservInfoDate = String(dateReplace).replace(/(\d{4})(\d{2})(\d{2})/, "$1-$2-$3");
		
		
		$('#reservDay').text(formattedReservInfoDate + reservTimeText + " 예약 정보");
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
	
	// 휴무일 등록 버튼 클릭 이벤트
	$('#holidayRegisterBtn').click(function() {
		let storeId = $('#storeId').val();
		
		if(selectHolidayCount <= 20) {
			$('div.selected').each(function () {
				let date = $(this).data('date-value'); // 날짜 값 추출
				let dateReplace = date.replace(/-/g, '');
				let obj = {
					"storeId" : storeId,
				   	"holiday" : dateReplace
				};
				    
				// 중복 확인
				let isDuplicate = selectHolidayDate.list.some(item => item.holiday === date);
				    
				if(!isDuplicate) {
					console.log(selectHolidayDate.list);
					selectHolidayDate.list.push(obj);
				}
				
				
			});
			
			checkReservList()
				.then(cancelReservList)
				.then(holidayRegistration)
				.then(cancelHoliday)
				.then(holidayExceptionalMsg);
		} else if(selectHolidayCount > 20) {
			alert("한번에 20개의 휴무일 등록이 가능합니다.");
		}
		
	}); // End #holidayRegisterBtn button.click
	
	function holidayExceptionalMsg(status) {
		return new Promise((resolve) => {
			let msg = "";
			if(cancelReservStatus != 5) {
				if(reservStatus == 0) {
					if(registrationHolidayStatus == 1 && cancelHolidayStatus == 1) {
						msg = "휴무일 등록 및 취소가 완료되었습니다.";
					} else if((registrationHolidayStatus == 0 || registrationHolidayStatus == 3) && (cancelHolidayStatus == 0 || cancelHolidayStatus == 3)) {
						msg = "휴무일 등록 및 취소에 실패하였습니다.";
					} else if(registrationHolidayStatus == 1 && (cancelHolidayStatus == 0 || cancelHolidayStatus == 3)) {
						msg = "휴무일 등록은 성공하였지만 취소에는 실패하였습니다.";
					} else if((registrationHolidayStatus == 0 || registrationHolidayStatus == 3) && cancelHolidayStatus == 1) {
						msg = "휴무일 취소에는 성공하였지만 등록에는 실패하였습니다.";
					} else if(registrationHolidayStatus == 1) {
						msg = "휴무일 등록 완료";
					} else if(registrationHolidayStatus == 0 || registrationHolidayStatus == 3) {
						msg = "휴무일 등록 실패";
					} else if(cancelHolidayStatus == 1) {
						msg = "휴무일 취소 완료";
					} else if(cancelHolidayStatus == 0 || cancelHolidayStatus == 3) {
						msg = "휴무일 취소 실패";
					}
				} else if(reservStatus == 1) {
					if(cancelReservStatus == 1) {
						if(registrationHolidayStatus == 1 && cancelHolidayStatus == 1) {
							msg = "휴무일 등록 및 취소가 완료되었습니다.";
						} else if((registrationHolidayStatus == 0 || registrationHolidayStatus == 3) && (cancelHolidayStatus == 0 || cancelHolidayStatus == 3)) {
							msg = "휴무일 등록 및 취소에 실패하였습니다.";
						} else if(registrationHolidayStatus == 1 && (cancelHolidayStatus == 0 || cancelHolidayStatus == 3)) {
							msg = "휴무일 등록은 성공하였지만 취소에는 실패하였습니다.";
						} else if((registrationHolidayStatus == 0 || registrationHolidayStatus == 3) && cancelHolidayStatus == 1) {
							msg = "휴무일 취소에는 성공하였지만 등록에는 실패하였습니다.";
						} else if(registrationHolidayStatus == 1) {
							msg = "휴무일 등록 완료";
						} else if(registrationHolidayStatus == 0 || registrationHolidayStatus == 3) {
							msg = "휴무일 등록 실패";
						} else if(cancelHolidayStatus == 1) {
							msg = "휴무일 취소 완료";
						} else if(cancelHolidayStatus == 0 || cancelHolidayStatus == 3) {
							msg = "휴무일 취소 실패";
						}
					} else if(cancelReservStatus == 0) {
						msg = "선택된 일자의 예약 목록 취소에 실패 하였습니다.";
					} else if(cancelReservStatus == 3) {
						msg = "선택된 일자의 예약 목록 취소 중 오류가 발생하였습니다.";
					}
				} else if(reservStatus == 3){
					msg = "오류가 발생하였습니다.";
				}
				resolve(msg);
			} else if(cancelReservStatus == 5) {
				msg = "취소 되었습니다.";
				resolve(msg);
			}
			
			alert(msg);
			if(status != 0 && cancelReservStatus != 5) {
				location.reload(true);
			}
		});
		
	}
	
	function checkReservList() {
		return new Promise((resolve) => {
			let joinList;
			let list = [];
			let checkUrl;
			let storeId = $('#storeId').val();
			
			if(selectHolidayDate.list.length > 0 && cancelHolidayDate.list.length > 0) {
				const selectHolidayList = selectHolidayDate.list;
				const cancelHolidayList = cancelHolidayDate.list;
				for(let i = 0; i < selectHolidayList.length; i++){
					for(let j = 0; j < cancelHolidayList.length; j++){
						if(selectHolidayList[i] != cancelHolidayList[j]) {
							list.push(cancelHolidayList[j]);
						}
					}
					list.push(selectHolidayList[i]);
				}
				console.log("checkReservList : " + list);
				joinList = list.map(obj => obj.holiday).join(",");
				
				checkUrl = '/store/holiday/check/reservList/' + storeId + '/' + joinList;
			} else if(selectHolidayDate.list.length > 0){
				joinList = selectHolidayDate.list.map(obj => obj.holiday).join(",");
				checkUrl = '/store/holiday/check/reservList/' + storeId + '/' + joinList;
			} else if(cancelHolidayDate.list.length > 0) {
				joinList = cancelHolidayDate.list.map(obj => obj.holiday).join(",");
				checkUrl = '/store/holiday/check/reservList/' + storeId + '/' + joinList;
			}
			
			$.ajax({
				url : checkUrl,
				type : 'get',
				headers : {
					"Content-Type" : "application/json"
				},
				success : function(response) {
					reservStatus = response.reservStatus;
					cancelReservInfoList = response.reservList;
					resolve(reservStatus);
				},
				error : function() {
					reservStatus = 3;
					resolve(reservStatus);
				},
			});
		});
		
	} // End checkReservList
	
	// 휴무일 등록 함수
	function holidayRegistration(status) {
		return new Promise((resolve) => {
			if(status != 0 && status != 3 && status != 5) {
				if(selectHolidayDate.list.length > 0) {
					$.ajax({
						url : '/store/holiday/registration',
						type : 'post',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify(selectHolidayDate.list),
						success : function(response){
							if(response.result > 0) {
								registrationHolidayStatus = 1;
							} else if(response.result == 0) {
								registrationHolidayStatus = 0;
							}
							resolve(registrationHolidayStatus);
						},
						error : function() {
							registrationHolidayStatus = 3;
							resolve(registrationHolidayStatus);
						}
						
					});
				} else if(selectHolidayDate.list.length == 0) {
					registrationHolidayStatus = 2;
					console.log("holidayRegistration() registrationHolidayStatus : " + registrationHolidayStatus);
					resolve(registrationHolidayStatus);
				}
			} else {
				registrationHolidayStatus = 0;
				resolve(registrationHolidayStatus);
			}
		});
		//return reservStatus;
		
	} // End holidayRegistration
	
	function cancelHoliday(status) {
		return new Promise((resolve) => {
			if(status != 0 && status != 3) {
				console.log("cancelHoliday() cancelHolidayDate.list.length : " + cancelHolidayDate.list.length);
				if(cancelHolidayDate.list.length > 0) {
					$.ajax({
						url : '/store/holiday/delete',
						type : 'delete',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify(cancelHolidayDate.list),
						success : function(response) {
							if(response.result == 1) {
								cancelHolidayStatus = 1;
								console.log("cancelHoliday() success cancelHolidayStatus : " + cancelHolidayStatus);
							} else if(response.result == 0) {
								cancelHolidayStatus = 0;
							}
							resolve(cancelHolidayStatus);
						},
						error : function() {
							cancelHolidayStatus = 3;
							resolve(cancelHolidayStatus);
						}
					});
				} else if(cancelHolidayDate.list.length == 0) {
					cancelHolidayStatus = 2;
					resolve(cancelHolidayStatus);
				}
			} else {
				cancelHolidayStatus = 0;
				resolve(cancelHolidayStatus);
			}
		});
	}
	
	// 휴무일 조회 함수
	function searchHoliday() {
		let storeId = $('#storeId').val();
		$.ajax({
			url : '/store/holiday/search/list/' + storeId,
			type : 'get',
			contentType : "application/json",
			success : function(response) {
				holidayList = response.holidayList;
				generateCalendar(currentMonth, currentYear);
			},
			error : function() {
				console.log("휴무일 목록을 불러오는중 오류가 발생하였습니다.");
			}
		});
	} // End searchHoliday
	
	function cancelReservList(status) {
		return new Promise((resolve) => {
			if(status == 1) {
				let confirmMsg = confirm("선택된 휴무일 중에 예약일정이 확인되었습니다. \n 예약일정을 취소하여 휴무일을 등록 하시겠습니까?");
				if(confirmMsg) {
					const cancelList = cancelReservInfoList;
					let requestData = [];
					for(let i = 0; i < cancelList.length; i++) {
						obj = {
							"reservId" : cancelList[i].reservId,
							"cancelComment" : "가게 휴무일로 인하여 예약이 취소되었습니다."
						}
						requestData.push(obj);
					}
					
					console.log("cancelReservList : " + cancelList);
					let requestType = "STORE";
					$.ajax({
						url : '/reserv/cancel/' + requestType,
						type : 'post',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify(requestData),
						success : function(response) {
							if(response.result == 1) {								
								$.ajax({
									url : '/reserv/cancel/list',
									type : 'put',
									headers : {
										"Content-Type" : "application/json"
									},
									data : JSON.stringify(requestData),
									success : function(response) {
										if(response == 1) {
											cancelReservStatus = 1;											
										} else if(response == 0) {
											cancelReservStatus = 0;	
										}
										resolve(cancelReservStatus);
									},
									error : function() {
										cancelReservStatus = 3;
										resolve(cancelReservStatus);
									}
								});
							} else if(response.result == 0) {
								cancelReservStatus = 0;
							}
							resolve(cancelReservStatus);
						},
						error : function() {
							cancelReservStatus = 3;
							resolve(cancelReservStatus);
						}
					});
				} else {
					cancelReservStatus = 5;
					resolve(cancelReservStatus);
				}
			} else if(status == 0) {
				cancelReservStatus = 2;
				console.log("cancelReservList() cancelReservStatus : " + cancelReservStatus);
				resolve(cancelReservStatus);
			} else if(status == 3) {
				cancelReservStatus = 0;
				resolve(cancelReservStatus);
			}
		});
	}
	
    searchHoliday();
    //generateCalendar(currentMonth, currentYear);
    choiceDayReservList(dateReplace);
    updateSelectionDisplay();

});