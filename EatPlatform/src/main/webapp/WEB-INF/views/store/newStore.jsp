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
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
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
                let otherCategoryInput = $('#otherCategoryInput').val();
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
<script>
    //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                let sido = data.sido;
                let sigungu = data.sigungu;
                let bname = data.bname;
                let bname1 = data.bname1;
                let bname2 = data.bname2;
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;
                
                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }
                
                if(bname !== ''){
                    document.getElementById("sample4_bname").value = bname;
                    console.log(bname);
                } else {
                    document.getElementById("sample4_bname").value = '';
                    console.log(bname);
                }
                
                if(bname1 !== ''){
                    document.getElementById("sample4_bname1").value = bname1;
                    console.log(bname1);
                } else {
                    document.getElementById("sample4_bname1").value = '';
                    console.log(bname1);
                }
                
                if(bname2 !== ''){
                    document.getElementById("sample4_bname2").value = bname2;
                    console.log(bname2);
                } else {
                    document.getElementById("sample4_bname2").value = '';
                    console.log(bname2);
                }
                
                if(sido !== ''){
                    document.getElementById("sample4_sido").value = sido;
                    console.log(sido);
                } else {
                    document.getElementById("sample4_sido").value = '';
                    console.log(sido);
                }
                
                if(sigungu !== ''){
                    document.getElementById("sample4_sigungu").value = sigungu;
                    console.log(sigungu);
                } else {
                    document.getElementById("sample4_sigungu").value = '';
                    console.log(sigungu);
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }).open();
    }
</script>
<title>식당 등록 페이지</title>
</head>
<body>
    <h2>여기는 가게 등록 페이지입니다.</h2>
    <form action="register" method="POST">
        <input type="hidden" value="${storeVO.userId}" id="userId" name="userId">
    	<input type="hidden" id="businessHour" name="businessHour"> 
        
        <input type="hidden" id="hiddenFoodCategory" name="foodCategory" required>
        
        <label for="storeName"> 
            식당 이름 : <input type="text" id="storeName" name="storeName" required>
        </label>
        <label for="storePhone"> 
            연락처 : <input type="number" id="storePhone" name="storePhone" required>
        </label> 
        <label for="ownerName">
            대표명 : <input type="text" id="ownerName" name="ownerName" required> 
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
            시간별 예약 제한: <input type="number" max="99999" id="reservLimit" name="reservLimit" required>
        </label> 
        <label for="seat"> 
            좌석 수 : <input type="number" max="99999" id="seat" name="seat">
        </label> 
        <label for="businessHour"> 
            영업시간 : <input type="time" id="startTime" name="startTime"> - <input type="time" id="endTime" name="endTime">
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
    
    
    
<input type="text" id="sample4_postcode" placeholder="우편번호">
<input type="button" onclick="sample4_execDaumPostcode()" value="우편번호 찾기"><br>
<input type="text" id="sample4_roadAddress" placeholder="도로명주소">
<input type="text" id="sample4_jibunAddress" placeholder="지번주소">
<span id="guide" style="color:#999;display:none"></span>
<input type="text" id="sample4_detailAddress" placeholder="상세주소">
<input type="text" id="sample4_extraAddress" placeholder="참고항목">
<br>
<input type="text" id="sample4_sido" placeholder="sido">
<input type="text" id="sample4_sigungu" placeholder="sigungu">
<input type="text" id="sample4_bname1" placeholder="bname1">
<input type="text" id="sample4_bname2" placeholder="bname2">
<input type="text" id="sample4_bname" placeholder="bname">
</body>
</html>
