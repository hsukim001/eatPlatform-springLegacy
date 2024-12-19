<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    int result = (Integer) request.getAttribute("result");
%>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
    $(document).ready(function() {
        let result = <%= result %>; 

        if (result > 0) {
            alert("메뉴 등록이 완료되었습니다.");
            window.location.href = "../list";
        } else {
            alert("메뉴 등록에 실패했습니다. 다시 시도해주세요.");
            history.back();
        }
    });
</script>
