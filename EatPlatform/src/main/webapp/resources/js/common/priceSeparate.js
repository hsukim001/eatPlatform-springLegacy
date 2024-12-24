        $(function () {
	        function formatPrice(price) {
	            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
	        }

	        $('p.menuPrice').each(function() {
	            var menuPrice = $(this).text().trim();
	            menuPrice = menuPrice.replace(/\D/g, ''); 
	            var formattedPrice = formatPrice(menuPrice); 
	            $(this).text(formattedPrice + ' \\'); 
	        });
        });