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
	let selectedDateText;
	
	let reservList;
	let reservInfoList;
	
	// ajax CSRF 토큰
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
				
		xhr.setRequestHeader(header, token);
	});
	
	$(document).on('click', '.calendar div', function(){
		selectedDateValue = $(this).data('dateValue');
		
	});
	
	// 캘린더 조회 함수
    function updateSelectionDisplay() {
        dateText = selectedDate ? `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')}` : '날짜를 선택하세요';
        timeText = selectedTime || '시간을 선택하세요';
    }

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
            if (currentDay < today && !(currentDay.getDate() === today.getDate() && currentDay.getMonth() === today.getMonth() && currentDay.getFullYear() === today.getFullYear())) {
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
            }


            dayCell.on('click', function () {
                if (!$(this).hasClass('disabled')) {
                    $('.calendar .selected').removeClass('selected');
                    $(this).addClass('selected');;
                    selectedDate = new Date(year, month, day);
                    //generateTimeSlots();
                    updateSelectionDisplay();
                    getReservList();
                }
            });

            calendarDays.append(dayCell);
        }

        updateSelectionDisplay();
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
	
	// 선택된 날짜의 예약 목록 조회
	function getReservList() {
		console.log("dateText : " + dateText);
		console.log("seletedDateText");
		
		if(dateText != selectedDateText) {
			console.log("dateText != selectedDateText");
			choiceDayReservList();
		}
		
	}
	
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
	}
	
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
		
	}
	
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
	}
	
	// 예약자 목록 생성 함수
	function generateReservInfo() {
	
		let timeSplit = timeValue.split(':');
		let hour = timeSplit[0];
		let min = timeSplit[1];
		
		let reservInfo = "";
		for(let i = 0; i < reservInfoList.length; i++) {
			let reservId = reservInfoList[i].reservId;
		
			let reservCancelBtn = '<button id="reservCancelBtn" data-id-value="' + reservId + '">예약 취소</button>';
			
			reservInfo += 
				'<li>' + (i + 1) + '.' +
				'<span> 예약자 명 : ' + reservInfoList[i].name + '</span>' +
				'<span>, 연락처 : ' + reservInfoList[i].phone + '</span>' +
				'<span>, 인원 : ' + reservInfoList[i].reservPersonnel + '</span>' +
				'<span>, 접수일 : ' + reservInfoList[i].reservDateCreated + '</span>' +
				reservCancelBtn
				+ '</li>';
			
		};
				
		let date = new Date();
		date.setHours(hour, min);
			
		let reservTimeText = date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
				
		$('#reservDay').text(dateText + reservTimeText + " 예약 정보");
		$('#reservUserInfo').html(reservInfo);
	}
	
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
		
	});
	
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
					location.reload(true);
				}
			},
			errors : function() {
				alert("예약 취소중에 오류가 발생하였습니다.");
			}
		});
	}
	

    generateCalendar(currentMonth, currentYear);
    getReservList();
    updateSelectionDisplay();

});