<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // StoreController에서 전달된 result 값을 가져옴
    int result = (Integer) request.getAttribute("result"); // result는 숫자 (1 이상이면 성공, 0 이하이면 실패)
%>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
    $(document).ready(function() {
        var result = <%= result %>; 

        if (result > 0) {
            alert("가게 등록이 성공적으로 완료되었습니다.");
            window.location.href = "newStore";
        } else {
            alert("가게 등록에 실패했습니다. 다시 시도해주세요.");
            history.back();
        }
    });
</script>