		$(function () {
		    function setHeights() {
		        let headerHeight = $('header').outerHeight();
		        let footerHeight = $('footer').outerHeight();
		        let fullException = headerHeight - footerHeight;
		
		        $('#empty_header').css('height', headerHeight);
		        $('#empty_footer').css('height', footerHeight);
		
		        $('.fullException').css('height', fullException);
		        $('.exceptionHeader').css('height', 'calc(100vh - ' + headerHeight + 'px)');
		    }
		
		    setHeights();
		
		    $(window).resize(function () {
		        let headerHeight = $('header').outerHeight();
		        let footerHeight = $('footer').outerHeight();
		        let fullException = headerHeight - footerHeight;
		
		        $('#empty_header').css('height', headerHeight);
		        $('#empty_footer').css('height', footerHeight);
		
		        $('.fullExceptionResize').css('height', fullException);
		        $('.exceptionHeaderResize').css('height', 'calc(100vh - ' + headerHeight + 'px)');
		    });
		});
