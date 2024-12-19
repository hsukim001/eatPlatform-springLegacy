<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
            var selectedCategory = $(this).val();
            
            if (selectedCategory === '기타') {
                $('#otherCategory').show(); 
            } else {
                $('#otherCategory').hide(); 
                $('#otherCategoryInput').val('');
            }
        }); // End foodCategory.change

        // 폼 제출 시 "기타" 입력값을 hidden input에 저장
        $('form').submit(function() { 
            var category = $('#foodCategory').val();
            if (category === '기타') {
                var otherCategoryInput = $('#otherCategoryInput').val();
                $('#hiddenFoodCategory').val(otherCategoryInput);
            } else {
                $('#hiddenFoodCategory').val(category);
            }
        }); // End form.submit
    }); // End $function
</script>
<title>식당 등록 페이지</title>
</head>
<body>
    <h2>여기는 가게 등록 페이지입니다.</h2>
    <form action="register" method="POST">
        <input type="hidden" value="${storeVO.userId}" id="userId" name="userId"> 
        
        <input type="hidden" id="hiddenFoodCategory" name="foodCategory">
        
        <label for="storeName"> 
            식당 이름 : <input type="text" id="storeName" name="storeName">
        </label>
        <label for="storePhone"> 
            연락처 : <input type="number" id="storePhone" name="storePhone">
        </label> 
        <label for="ownerName">
            대표명 : <input type="text" id="ownerName" name="ownerName"> 
        </label>
        <label for="foodCategory">
            카테고리: 
            <select id="foodCategory" name="foodCategorySelect">
                <option value="한식">한식</option>
                <option value="중식">중식</option>
                <option value="일식">일식</option>
                <option value="양식">양식</option>
                <option value="기타">기타</option>
            </select>
        </label>
        <div id="otherCategory">
            <label for="otherCategoryInput">
                기타 카테고리:
            </label> 
            <input type="text" id="otherCategoryInput" name="foodCategoryInput" placeholder="기타 카테고리 입력">
        </div> 
        <label for="reservLimit">
            시간별 예약 제한: <input type="number" id="reservLimit" name="reservLimit">
        </label> 
        <label for="seat"> 
            좌석 수 : <input type="number" id="seat" name="seat">
        </label> 
        <label for="businessHour"> 
            영업시간 : <input type="text" id="businessHour" name="businessHour">
        </label> 
        <label for="storeComment"> 
            식당 소개: 
            <textarea id="storeComment" name="storeComment" maxlength="125" placeholder="125자까지 입력 가능합니다."></textarea>
        </label>
        <br>
        <label for="description"> 
            식당 상세 설명: 
            <textarea id="description" name="description" maxlength="250" placeholder="250자까지 입력 가능합니다."></textarea>
        </label>
        <input type="submit" value="식당 등록">
    </form>
</body>
</html>
