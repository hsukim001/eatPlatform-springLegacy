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
	
	// 예약 인원
	let personnel = parseInt($('#personnel').text());
	let reservLimit = parseInt($('#reservLimit').text());
	let inputPersonnel;
	$('#personnel').text(personnel);
	
	// 예약
	let activeTimeList;
	
	// 휴무일
	let holidayList;
	
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
				
		xhr.setRequestHeader(header, token);
	});
	
	$('#createdReserv').click(function(){	
		let isConfirm = confirm(dateText + " " + timeValue + "에 예약을 진행하시겠습니까?");
		if(isConfirm) {
		    createdReserv();			
		}
	});
	
	$(document).on('click', '.calendar div', function(){
		selectedDateValue = $(this).data('dateValue');
		reservSchedule(selectedDateValue);
		
	});
	
    function updateSelectionDisplay() {
        dateText = selectedDate ? `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')}` : '날짜를 선택하세요';
        timeText = selectedTime || '시간을 선택하세요';
        selectionDisplay.text(`선택한 날짜: ${dateText}, 선택한 시간: ${timeText}`);
    }

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
            if (currentDay < today && !(currentDay.getDate() === today.getDate() && currentDay.getMonth() === today.getMonth() && currentDay.getFullYear() === today.getFullYear())) {
                dayCell.addClass('disabled');
            } else {
            	if(holidayList.length > 0) {
	            	const dateReplace = dateValue.replace(/-/g, '');
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
                year === today.getFullYear()
            ) {
                dayCell.addClass('today selected');
                selectedDate = currentDay;
            }


            dayCell.on('click', function () {
                if (!$(this).hasClass('disabled') && !$(this).hasClass('holiday')) {
                    $('.calendar .selected').removeClass('selected');
                    $(this).addClass('selected');;
                    selectedDate = new Date(year, month, day);
                    updateSelectionDisplay();
                    
                }
            });

            calendarDays.append(dayCell);
        }

        updateSelectionDisplay();
    }

	function generateTimeSlots() {
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
		
			 if (selectedDate.getDate() === today.getDate() &&
		        selectedDate.getMonth() === today.getMonth() &&
		        selectedDate.getFullYear() === today.getFullYear()) {
		        
			    if (currentTime.getHours() < now.getHours() || 
			        (currentTime.getHours() === now.getHours() && currentTime.getMinutes() < now.getMinutes())) {
			        timeSlot.addClass('disabled');
			    }
			    			    
			    if(holidayList.length > 0) {
					for(let i = 0; i < holidayList.length; i++) {
						const date = (holidayList[i].holiday.slice(0, 4) + "-" + holidayList[i].holiday.slice(4)).slice(0, 7) + "-" + (holidayList[i].holiday.slice(0, 4) + "-" + holidayList[i].holiday.slice(4)).slice(7);
						if(dateText == date) {
							timeSlot.addClass('disabled');
						}
					}
				}

		    }
		    
		    		    
		    if(activeTimeList !== null && activeTimeList !== undefined) {
		    
		    	for(let i = 0; i < activeTimeList.length; i++){
		    		if(timeSlotText24Hour === activeTimeList[i].hour && timeSlotText24Minutes === activeTimeList[i].min){
		    			if(activeTimeList[i].active == false){
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
	}
    prevMonthBtn.on('click', function () {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        generateCalendar(currentMonth, currentYear);
    });

    nextMonthBtn.on('click', function () {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentMonth, currentYear);
    });
    
	function updateSelectedTime() {
	    let timeValueSplit = timeValue.split(':');
	    selectTimeHour = timeValueSplit[0];
	    selectTimeMinutes = timeValueSplit[1];
	}
	
	// "-" 버튼 클릭 이벤트
	$('#minerBtn').click(function(){
		if(personnel == 1) {
			personnel = 1;
			$('#personnel').text(personnel);
			observeDOMChanges();
		} else {
			personnel -= 1;
			$('#personnel').text(personnel);
			observeDOMChanges();
		}
		reservSchedule(dateText);
	});
		
	// "+" 버튼 클릭 이벤트
	$('#plusBtn').click(function(){
		if(personnel == reservLimit) {
			personnel = reservLimit
			$('#personnel').text(personnel);
			observeDOMChanges();
		} else {
			personnel += 1;
			console.log(activeTimeList);
			$('#personnel').text(personnel);
			observeDOMChanges();
		}
		reservSchedule(dateText);
	});
		
	// 적용 버튼 클릭 이벤트
	$('#applyBtn').click(function(){
		inputPersonnel = $('#inputPersonnel').val()
		personnelCheck();
		reservSchedule(dateText);
		observeDOMChanges();
	});
	
	// 예약 인원 input 이벤트
	$('#inputPersonnel').keydown(function(event){
		if(event.keyCode == 13) {
			$('#applyBtn').trigger('click');
		}
	});
		
	// 예약인원 직접입력 함수
	function personnelCheck(){
		if(inputPersonnel === null || inputPersonnel === ''){
			inputPersonnel = 1;
		}
		
		if(parseInt(inputPersonnel) < 1 || parseInt(inputPersonnel) > reservLimit) {
			alert("예약 인원은 1명 이상 " + reservLimit + "명 이하로만 입력 가능합니다.");
			$('#inputPersonnel').val("");
		} else {
			personnel = parseInt(inputPersonnel);
			$('#personnel').text(personnel);
			$('#inputPersonnel').val("");
		}
	}
	
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
	}
	
	// 예약 가능 유무 조회
	function reservSchedule(dateValue){
		let storeId = $('#storeId').val();
		
		$.ajax({
			url : '../reserv/schedule/' + storeId + '/' + reservLimit + '/' + personnel + '/' + dateValue,
			type : 'get',
			headers : {
				"Content-Type" : "application/json"
			},
			success : function(response){
				console.log(personnel);
				activeTimeList = response;
				console.log("고양이 : " + activeTimeList);
				generateTimeSlots();  // 날짜 선택 시 시간 슬롯 갱신
			}
		});
	}
	
	// 예약 등록
	function createdReserv(){
		let storeId = $('#storeId').val();
		if(timeValue) {
			let timeSplit = timeValue.split(':');
			$.ajax({
				url : '../reserv/created/' + reservLimit,
				type : 'post',
				headers : {
					"Content-Type" : "application/json"
				},
				data : JSON.stringify({
					"storeId" : storeId,
					"reservDate" : dateText,
					"reservHour" : timeSplit[0],
					"reservMin" : timeSplit[1],
					"reservPersonnel" : personnel
				}),
				success : function(response){
					if(response == 1) {
						alert("예약이 완료되었습니다.");
						location.href='detail?storeId=' + storeId;
					} else {
						alert("예약에 실패하였습니다. 다시 시도해주세요");
						
						$('#calendar-days div.selected').trigger('click');
					}
				},
				error : function() {
					alert("예약을 진행하는 중 오류가 발생하였습니다.");
				}
			});
		} else {
			alert("예약 시간을 선택해주세요.");
		}
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
				reservSchedule(dateText);
			},
			error : function() {
				console.log("휴무일 목록을 불러오는중 오류가 발생하였습니다.");
			}
		});
	} // End searchHoliday

	searchHoliday();
    //generateCalendar(currentMonth, currentYear);
    //reservSchedule(dateText);
    updateSelectionDisplay();

    startTimeInput.on('change', generateTimeSlots);
    endTimeInput.on('change', generateTimeSlots);

});