<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<style>
#otherCategory {
    display: none;
}
</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	function noBack(){window.history.forward(); alert('잘못된 접근 입니다.');}	
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
    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
            	let postCode = data.zonecode;
            	let jibunAddress = data.jibunAddress;
                let roadAddress = data.roadAddress;
                let sido = data.sido;
                let sigungu = data.sigungu;
                let bname1 = data.bname1;
                let bname2 = data.bname2;
                let extraAddress = '';
                

                $('#postCode').val(postCode);
				$('#jibunAddress').val(jibunAddress);
                $('#sido').val(sido);
				$('#sigungu').val(sigungu);
                $('#bname2').val(bname2);
				
                if (roadAddress !== '') {
                	$('#roadAddress').val(roadAddress);
                } else {
                	$('#roadAddress').val(jibunAddress);
                }

                if(bname1 !== ''){
                    $('#bname1').val(bname1);
                } else {
                	 $('#bname1').val("");;
                }           
                
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                	extraAddress += data.bname;
                }
                                           
                if(data.buildingName !== '' && data.apartment === 'Y'){
                	extraAddress += (extraAddress !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                
                if(extraAddress !== ''){
                	extraAddress = ' (' + extraAddress + ')';
                	$('#extraAddress').val(extraAddress)
                }
               

            }
        }).open({
        		autoClose: true,
        		popupTitle: '우편번호 검색'
        	}
        );
    }
</script>
<title>식당 수정 페이지</title>
</head>
<body onpageshow="if(event.persisted) noBack();">
    <h2>여기는 수정 페이지입니다.</h2>
    <form action="modify" method="POST">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
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
               
		<input type="text" id="postCode" name="postCode" placeholder="우편번호" value="${storeAddressVO.postCode }" required>
		<input type="button" onclick="execDaumPostcode()" value="우편번호 찾기"><br>
		<input type="text" id="roadAddress" name="roadAddress" placeholder="도로명주소"  value="${storeAddressVO.roadAddress }">
		<input type="text" id="jibunAddress" name="jibunAddress" placeholder="지번주소"required  value="${storeAddressVO.jibunAddress }">
		<input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소"  value="${storeAddressVO.detailAddress }">
		<input type="text" id="extraAddress" name="extraAddress" placeholder="참고항목" value="${storeAddressVO.extraAddress }">
		<br>
		<input type="text" id="sido" name="sido" placeholder="sido" required value="${storeAddressVO.sido }">
		<input type="text" id="sigungu" name="sigungu" placeholder="sigungu" required value="${storeAddressVO.sigungu }">
		<input type="text" id="bname1" name="bname1" placeholder="bname1" value="${storeAddressVO.bname1 }">
		<input type="text" id="bname2" name="bname2" placeholder="bname2" value="${storeAddressVO.bname2 }">
		<br><br>
		
        <input type="submit" value="식당 정보 수정">
    </form>
</body>
</html>
