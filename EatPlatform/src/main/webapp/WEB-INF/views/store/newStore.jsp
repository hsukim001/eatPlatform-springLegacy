<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
<style>
#otherCategory {
    display: none;
}

.insert {
	padding: 10px 20px;
	display: block;
	width: 300px;
	border: 1px solid #dbdbdb;
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

.insert .file-list {
	height: 200px;
	overflow: auto;
	border: 1px solid #989898;
	padding: 10px;
}

.insert .file-list .filebox p {
	font-size: 14px;
	margin-top: 10px;
	display: inline-block;
}

.insert .file-list .filebox .delete i {
	color: #ff5353;
	margin-left: 5px;
}

#storeImg {
	display: none;
}

.uploadLabel {
	display: inline-block;
	padding: 6px 25px;
	margin: 5px 0;
	background-color:navy;
	border-radius: 4px;
	color: white;
	cursor: pointer;
}

</style>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/store/ImageUpload.js" defer></script>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	function noBack(){window.history.forward(); alert('잘못된 접근 입니다.');}	
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");			
		xhr.setRequestHeader(header, token);
	});4
	
    $(function() { 
        $('#foodCategory').change(function() {  
            var selectedCategory = $(this).val();
            
            if (selectedCategory === '기타') {
                $('#otherCategory').show(); 
            } else {
                $('#otherCategory').hide(); 
                $('#otherCategoryInput').val('');
            }
        }); // End foodCategory.change
        
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
<title>식당 등록 페이지</title>
</head>
<body onpageshow="if(event.persisted) noBack();">
    <h2>여기는 가게 등록 페이지입니다.</h2>
    <form action="register" method="POST" enctype="multipart/form-data">
    	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    	<input type="hidden" id="businessHour" name="businessHour"> 
        <input type="hidden" id="hiddenFoodCategory" name="foodCategory" required>
        
        <label for="storeName">식당 이름 :  </label>
        <input type="text" id="storeName" name="storeName" required>
       
        <label for="storePhone">연락처 : </label> 
         <input type="number" id="storePhone" name="storePhone" required>
        
        <label for="ownerName">대표명 : </label>
        <input type="text" id="ownerName" name="ownerName" required> 
        <label for="foodCategory">카테고리: </label>
            <select id="foodCategory" name="foodCategorySelect">
                <option value="한식">한식</option>
                <option value="중식">중식</option>
                <option value="일식">일식</option>
                <option value="양식">양식</option>
                <option value="기타">기타</option>
            </select>
        <div id="otherCategory">
            <label for="otherCategoryInput">기타 카테고리: </label> 
            <input type="text" id="otherCategoryInput" name="foodCategoryInput" placeholder="기타 카테고리 입력">
        </div> 
        <label for="reservLimit">시간별 예약 제한: </label>
        <input type="number" max="99999" id="reservLimit" name="reservLimit" required>
        
        <label for="seat">좌석 수 : </label> <input type="number" max="99999" id="seat" name="seat">
        <label for="businessHour">영업시간 :  </label> 
        <input type="time" id="startTime" name="startTime"> - <input type="time" id="endTime" name="endTime">
       
        <label for="storeComment">식당 소개: </label>
        <textarea id="storeComment" name="storeComment" maxlength="125" placeholder="125자까지 입력 가능합니다."></textarea>
        
        <br>
        <label for="description">식당 상세 설명:  </label>
        <textarea id="description" name="description" maxlength="250" placeholder="250자까지 입력 가능합니다."></textarea>
        <br><br>

		<div class="insert">
			<label class="uploadLabel" for="storeImg">
				업로드
			</label>
			<input id="storeImg" name="storeImg" type="file" onchange="addFile(this);" accept="image/*" multiple />
			<div class="file-list"></div>
        	<div id="thumbnail-container"></div>
		</div>
        <br><br>
        	
		<input type="text" id="postCode" name="postCode" placeholder="우편번호" required>
		<input type="button" onclick="execDaumPostcode()" value="우편번호 찾기"><br>
		<input type="text" id="roadAddress" name="roadAddress" placeholder="도로명주소">
		<input type="text" id="jibunAddress" name="jibunAddress" placeholder="지번주소"required>
		<input type="text" id="detailAddress" name="detailAddress" placeholder="상세주소">
		<input type="text" id="extraAddress" name="extraAddress" placeholder="참고항목">
		<br>
		<input type="text" id="sido" name="sido" placeholder="sido" required>
		<input type="text" id="sigungu" name="sigungu" placeholder="sigungu" required>
		<input type="text" id="bname1" name="bname1" placeholder="bname1">
		<input type="text" id="bname2" name="bname2" placeholder="bname2">
		<br><br>
		
        <input type="submit" value="식당 등록">
    </form>
    
    

</body>
</html>
