$(document).ready(function() {
    var currentIndex = 0;
    var slideWrapper = $('.img_slide_wrapper');
    var slides = $('.img_slide_item');
    var slideCount = slides.length;
    var slideInterval;
    
    slideWrapper.css('width', (100 * slideCount) + '%');
    slides.css('width', (100 / slideCount) + '%'); 

    $('#slider_dots').empty();
    for (var i = 0; i < slideCount; i++) {
        $('#slider_dots').append('<span class="slider_dot"></span>');
    }

    var dots = $('.slider_dot');
    dots.eq(0).addClass('active');

    function goToSlide(index) {
        if (index >= slideCount) {
            index = 0;
        } else if (index < 0) {
            index = slideCount - 1;
        }
        currentIndex = index;

        var slideWidth = 100 / slideCount;
        slideWrapper.css('transform', 'translateX(-' + (slideWidth * index) + '%)');
        dots.removeClass('active').eq(index).addClass('active');
    }

    function startSlideShow() {
        slideInterval = setInterval(function() {
            goToSlide(currentIndex + 1);
        }, 3000);
    }

    function stopSlideShow() {
        clearInterval(slideInterval);
    }

    // 도트 리모컨
    dots.on('click', function() {
        stopSlideShow();
        goToSlide($(this).index());
        startSlideShow();
    });

    startSlideShow();
});
