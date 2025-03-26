$(function () {
    $('.img_box').each(function() {
        let imgCount = $(this).find('.img_item img').length;

        if (imgCount < 2) {
            $(this).siblings('.prev-btn, .next-btn').hide();
        } else {
            $(this).siblings('.prev-btn, .next-btn').show();
        }
    });

    $('.prev-btn, .next-btn').click(function() {
        let imgItem = $(this).closest('.slider-wrapper').find('.img_item');
        let imgCount = imgItem.find('img').length;
        let currentIndex = imgItem.data('currentIndex') || 0;

        if ($(this).hasClass('prev-btn')) {
            currentIndex = (currentIndex - 1 + imgCount) % imgCount;
        } else {
            currentIndex = (currentIndex + 1) % imgCount;
        }

        imgItem.css('transform', 'translateX(' + (-100 * currentIndex) + '%)');
        imgItem.data('currentIndex', currentIndex);
    });
});
