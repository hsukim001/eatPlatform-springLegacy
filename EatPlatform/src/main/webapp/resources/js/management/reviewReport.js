/**
 * 
 */

$(function(){

	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");	       
		xhr.setRequestHeader(header, token);
	}); // End document ajaxSend
	
	$('#tableBody').on('click', 'tr', function() {
		let msg = $(this).data('id-value');
		alert(msg);
	});
	
});