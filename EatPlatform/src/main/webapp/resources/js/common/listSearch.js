$(function () {
	$('#search_input').on('keydown', function(e) {
      if (e.key === 'Enter') {
        e.preventDefault(); 
        let searchVal = $(this).val();
        console.log(searchVal);
        if (searchVal == "") {
        	alert("검색어를 입력해주세요.");
        	return;
        }
        location.href ="/web/store/list?pageNum=1&keyword=" + searchVal;
      }
    });
});
