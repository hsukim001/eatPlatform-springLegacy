<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String errHandler = (String) request.getAttribute("errHandler"); 
%>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
    $(document).ready(function() {
        let errHandler = "<%= errHandler %>";

       if (errHandler == "otherUser") {
            alert("정보 수정 권한이 없습니다.");
            window.location.href = "/web/store/list";
        } else if (errHandler == "invalidTimeFormat" ) {
        	alert("허용되지 않은 시간 포맷입니다.\n확인 후 다시 시도해주세요.");
        	history.back();
        } else {
            alert("잘못된 접근입니다.");
            history.back();
        }
    });
</script>
