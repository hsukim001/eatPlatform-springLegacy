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

    function updateSelectionDisplay() {
        const dateText = selectedDate ? `${selectedDate.getFullYear()}-${(selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${selectedDate.getDate().toString().padStart(2, '0')}` : '날짜를 선택하세요';
        const timeText = selectedTime || '시간을 선택하세요';
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
            const dayCell = $('<div>').text(day);
            const currentDay = new Date(year, month, day);

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
        timeSlotsContainer.empty();

        let startTime = startTimeInput.val().split(':');
        let startHour = parseInt(startTime[0]);
        let startMinutes = parseInt(startTime[1]);

        if (startMinutes < 30) {
            startMinutes = 30;
        } else {
            startMinutes = 0;
            startHour++;
        }

        const endTime = endTimeInput.val().split(':');
        const endHour = parseInt(endTime[0]);
        const endMinutes = parseInt(endTime[1]);

        let currentTime = new Date();
        currentTime.setHours(startHour);
        currentTime.setMinutes(startMinutes);

        const endTimeObject = new Date();
        endTimeObject.setHours(endHour);
        endTimeObject.setMinutes(endMinutes);

        while (currentTime <= endTimeObject) {
            const timeSlotText = currentTime.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit', hour12: true });
            const timeSlot = $('<button>').text(timeSlotText);

            // 오늘 날짜의 시간만 disabled 처리
            if (selectedDate.getDate() === today.getDate() && selectedDate.getMonth() === today.getMonth() && selectedDate.getFullYear() === today.getFullYear()) {
                // 현재 시간이 지나면 disabled 처리
                if (currentTime < today) {
                    timeSlot.addClass('disabled');
                }
            } else if (selectedDate < today) {
                timeSlot.addClass('disabled');
            }

            timeSlot.on('click', function () {
                if (!$(this).hasClass('disabled')) {
                    $('.time-slots .selected').removeClass('selected');
                    $(this).addClass('selected');
                    selectedTime = $(this).text();
                    updateSelectionDisplay();
                }
            });

            timeSlotsContainer.append(timeSlot);

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

    generateCalendar(currentMonth, currentYear);
    generateTimeSlots();
    updateSelectionDisplay();

    startTimeInput.on('change', generateTimeSlots);
    endTimeInput.on('change', generateTimeSlots);
});
