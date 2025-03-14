		$(function () {
		    setHeights();
		    function setHeights() {
		        let headerHeight = $('header').outerHeight();
		        let footerHeight = $('footer').outerHeight();
		        console.log($("footer").height()); 
		        let fullException = headerHeight - footerHeight;
		
		        $('#empty_header').css('height', headerHeight);
		        $('#empty_footer').css('height', footerHeight);
		
		        $('.fullException').css('height', fullException);
		        $('.exceptionHeader').css('height', 'calc(100vh - ' + headerHeight + 'px)');
		    }
		
		
		    $(window).resize(function () {
		        let headerHeight = $('header').outerHeight();
		        let footerHeight = $('footer').outerHeight();
		        let fullException = headerHeight - footerHeight;
		
		        $('#empty_header').css('height', headerHeight);
		        $('#empty_footer').css('height', footerHeight);
		
		        $('.fullExceptionResize').css('height', fullException);
		        $('.exceptionHeaderResize').css('height', 'calc(100vh - ' + headerHeight + 'px)');
		    });
		    
		    $('#gnb_bar > ul > li').mouseenter(function(){
				$(this).children('a').addClass('bold');
		    	$(this).find('.lnb_list').stop(true, true).slideDown(500);
		    });		
		        
		    $('#gnb_bar > ul > li').mouseleave(function(){
		    	$(this).children('a').removeClass('bold');
		    	$(this).find('.lnb_list').stop(true, true).slideUp(500);
		    });
		});
