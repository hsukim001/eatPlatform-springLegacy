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

	
	$('#timeTest').click(function(){
	    updateSelectedTime(); 
	    alert(selectTimeHour);
	    alert(selectTimeMinutes);
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
                    $(this).addClass('selected');
                    selectedDate = new Date(year, month, day);
                    generateTimeSlots();  // 날짜 선택 시 시간 슬롯 갱신
                    updateSelectionDisplay();
                }
            });

            calendarDays.append(dayCell);
        }

        updateSelectionDisplay();
    }

	function generateTimeSlots() {
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

    generateCalendar(currentMonth, currentYear);
    generateTimeSlots();
    updateSelectionDisplay();

    startTimeInput.on('change', generateTimeSlots);
    endTimeInput.on('change', generateTimeSlots);

});
