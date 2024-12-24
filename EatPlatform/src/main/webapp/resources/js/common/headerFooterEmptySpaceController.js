        $(function () {
            let headerHeight = $('header').outerHeight();
            $('#empty_header').css('height', headerHeight);

            $(window).resize(function () {
                let headerHeight = $('header').outerHeight();
                $('#empty_header').css('height', headerHeight);
            });

            let footerHeight = $('footer').outerHeight();
            $('#empty_footer').css('height', footerHeight);

            $(window).resize(function () {
                let footerHeight = $('footer').outerHeight();
                $('#empty_footer').css('height', footerHeight);
            });
        });