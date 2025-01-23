<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- css 파일 불러오기 -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page/image.css">
<title>리뷰 수정</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
</head>
<body>
	<h2>리뷰 수정 페이지</h2>
	<form id="updateReviewForm" action="../page/updateReview" method="POST">
		<div>
			<input type="hidden" name="reviewId" value="${reviewVO.reviewId }">
		</div>
		<div>
			<p>아이디 : <input type="text" name="userId" readonly="readonly" value="${reviewVO.userId }"></p>
		</div>
		<div>
			<p>별점 : <input type="number" name="reviewStar" value="${reviewVO.reviewStar }" min="1" max="5"></p>
		</div>
		<div>
			<p>태그 : <input type="text" name="reviewTag" value="${reviewVO.reviewTag }"></p>
		</div>
		<div>
			<p>내용 : <textarea id="reviewContent" placeholder="${reviewVO.reviewContent }" ></textarea></p>
		</div>
	
	</form>
	
	<hr>
	
	<button id="change-upload">이미지 변경</button>
	
	<div class="image-view">
		<div class="review-image">
			<c:forEach var="reviewImageVO" items="${reviewVO.reviewImageList }">
			<a href="../image/get/${reviewImageVO.reviewImageId }/reviewImageExtension/${reviewImageVO.reviewImageExtension }" target="_blank">
			<img width="100px" height="100px" 
			src="../image/get/${reviewImageVO.reviewImageId }/reviewImageExtension/${reviewImageVO.reviewImageExtension }" />
			</a>
			</c:forEach>
	 	</div>
	 	
	 	<div class="image-upload" style="display : none;">
			<h2>이미지 파일 업로드</h2>
			<p>* 이미지 파일은 최대 3개까지 가능합니다.</p>
			<p>* 최대 용량은 10MB 입니다.</p>
			<div class="image-drop"></div>
			<h2>선택한 이미지 파일 :</h2>
			<div class="image-list"></div>
		</div>
	</div>
	
	<div class="reviewImg-list">
    </div>
	
	<button id="updateReviewImage">등록</button>
	
	<script src="<%=request.getContextPath()%>/resources/js/page/image.js"></script>
	
	<script type="text/javascript">
	
		$(document).ajaxSend(function(e, xhr, opt){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			
			xhr.setRequestHeader(header, token);
		});
		
		$(document).ready(function() {
			// 이미지 변경 버튼 클릭 시
			$('#change-upload').click(function(){
				if(!confirm('기존에 이미지들은 삭제됩니다. 계속 하시겠습니까?')){
					return;
				}
				$('.image-upload').show();
				$('.review-image').hide();
			});
		});
		
		// updateReviewForm 데이터 전송
		$('#updateReviewImage').click(function() {
			// form 객체 참조
			var updateReviewForm = $('#updateReviewForm');
			
			// 리뷰 이미지 처리
            var reviewImageList = [];
            var i = 0;
	           
         	// reviewImg-list의 각 input 태그 접근
           $('.reviewImg-list input[name="reviewImageVO"]').each(function() {
           	var reviewImageVO = JSON.parse($(this).val());
           	
           	// JSON 객체로 ReviewImageVO 배열 구성
               reviewImageList.push({
                    reviewImagePath: reviewImageVO.reviewImagePath,
                    reviewImageRealName: reviewImageVO.reviewImageRealName,
                    reviewImageChgName: reviewImageVO.reviewImageChgName,
                    reviewImageExtension: reviewImageVO.reviewImageExtension
               });

               i++;
           });
           updateReviewForm.submit();
			
		});
	
	</script>

</body>
</html>