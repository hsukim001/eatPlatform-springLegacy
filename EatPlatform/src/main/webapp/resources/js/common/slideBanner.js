$(function () {
    const slider = $('#recommend_slider');
    const slides = $('.recommend_slide');
    const totalSlides = slides.length;
    let currentIndex = 0;

    // 도트 리모컨
    for (let i = 0; i < totalSlides; i++) {
        $('#slider_dots').append('<span></span>');
    }
    const dots = $('#slider_dots span');
    dots.eq(currentIndex).addClass('active');

    // 슬라이드 이동
    function moveSlide(index) {
        const offset = -100 * index + '%';
        slider.css('transform', 'translateX(' + offset + ')');
        dots.removeClass('active').eq(index).addClass('active');
        currentIndex = index;
    }

    // 자동 슬라이드
    setInterval(() => {
        const nextIndex = (currentIndex + 1) % totalSlides;
        moveSlide(nextIndex);
    }, 3000);

    // 도트 클릭 이벤트
    dots.on('click', function () {
        const index = $(this).index();
        moveSlide(index);
    });
});
