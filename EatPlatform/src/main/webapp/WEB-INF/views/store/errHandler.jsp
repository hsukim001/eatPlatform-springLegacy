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
        } else {
            alert("잘못된 접근입니다.");
            history.back();
        }
    });
</script>
