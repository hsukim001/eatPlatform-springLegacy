<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
#otherCategory {
    display: none;
}
</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script>
    $(function() { 
        // "기타" 선택 관련 input 컨트롤
        $('#foodCategory').change(function() {  
            let selectedCategory = $(this).val();
            
            if (selectedCategory === '기타') {
                $('#otherCategory').show(); 
            } else {
                $('#otherCategory').hide(); 
                $('#otherCategoryInput').val('');
            }
        }); // End foodCategory.change

        // 폼 제출 시 "기타" 입력값을 hidden input에 저장
        $('form').submit(function() { 
            let category = $('#foodCategory').val();
            if (category === '기타') {
                var otherCategoryInput = $('#otherCategoryInput').val();
                $('#hiddenFoodCategory').val(otherCategoryInput);
            } else {
                $('#hiddenFoodCategory').val(category);
            }
            
            const startTime = $("#startTime").val();
            const endTime = $("#endTime").val();
            const businessHour = startTime + " - " + endTime;

            // 이 값을 서버로 전송
            $("#businessHour").val(businessHour);
        }); // End form.submit
    }); // End $function
</script>
<title>식당 수정 페이지</title>
</head>
<body>
    <h2>여기는 수정 페이지입니다.</h2>
    <form action="modify" method="POST">
        <input type="hidden" value="${storeVO.userId }" id="userId" name="userId"> 
        <input type="hidden" value="${param.storeId }" id="storeId" name="storeId">
    	<input type="hidden" id="businessHour" name="businessHour">  
        
        <input type="hidden" id="hiddenFoodCategory" name="foodCategory">
        <p>${param.storeId }</p>
        <label for="storeName"> 
            식당 이름 : <input type="text" id="storeName" name="storeName" value="${storeVO.storeName }">
        </label>
        <label for="storePhone"> 
            연락처 : <input type="number" id="storePhone" name="storePhone" value="${storeVO.storePhone }">
        </label> 
        <label for="ownerName">
            대표명 : <input type="text" id="ownerName" name="ownerName" value="${storeVO.ownerName }"> 
        </label>
		<label for="foodCategory">
		    카테고리: 
		    <select id="foodCategory" name="foodCategorySelect">
		        <c:forEach var="category" items="${categories}">
		            <option value="${category}" ${storeVO.foodCategory == category ? 'selected' : ''}>${category}</option>
		        </c:forEach>
		        <option value="기타" ${!categories.contains(storeVO.foodCategory) ? 'selected' : ''}>기타</option>
		    </select>
		</label>
		<div id="otherCategory" ${!categories.contains(storeVO.foodCategory) ? 'style="display: block;"' : ''}>
		    <label for="otherCategoryInput">
		        기타 카테고리:
		    </label> 
		    <input type="text" id="otherCategoryInput" name="foodCategoryInput" 
		           placeholder="기타 카테고리 입력" 
		           value="${!categories.contains(storeVO.foodCategory) ? storeVO.foodCategory : ''}">
		</div>
        <label for="reservLimit">
            시간별 예약 제한: <input type="number" max="99999"  id="reservLimit" name="reservLimit" value="${storeVO.reservLimit }">
        </label> 
        <label for="seat"> 
            좌석 수 : <input type="number" max="99999"  id="seat" name="seat" value="${storeVO.seat }">
        </label> 
        <label for="businessHour"> 
            영업시간 : <input type="time" id="startTime" name="startTime" value="${startTime }"> - <input type="time" id="endTime" name="endTime" value="${endTime }">
        </label> 
        <label for="storeComment"> 
            식당 소개: 
            <textarea id="storeComment" name="storeComment" maxlength="125" placeholder="125자까지 입력 가능합니다.">${storeVO.storeComment }
            </textarea>
        </label>
        <br>
        <label for="description"> 
            식당 상세 설명: 
            <textarea id="description" name="description" maxlength="250" placeholder="250자까지 입력 가능합니다.">${storeVO.description }
            </textarea>
        </label>
        <input type="submit" value="식당 정보 수정">
    </form>
</body>
</html>
