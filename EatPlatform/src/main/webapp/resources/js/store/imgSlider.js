$(function () {
	$('.prev-btn, .next-btn').click(function() {
	    var imgItem = $(this).closest('.img_box').find('.img_item');
	    var imgCount = imgItem.find('img').length;
	    var currentIndex = imgItem.data('currentIndex') || 0;
	
	    if ($(this).hasClass('prev-btn')) {
	        currentIndex = (currentIndex - 1 + imgCount) % imgCount;
	    } else {
	        currentIndex = (currentIndex + 1) % imgCount;
	    }
	
	    imgItem.css('transform', 'translateX(' + (-100 * currentIndex) + '%)');
	    imgItem.data('currentIndex', currentIndex);
	});



});
