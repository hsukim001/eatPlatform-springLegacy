<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리뷰</title>
<script src="https://code.jquery.com/jquery-3.7.1.js">
</script>
</head>
<body>
	<input type="hidden" id="storeId" value="1">

	<div style="text-align: center;">
		<input type="text" id="userId"> <input type="text"
			id="reviewContent">
		<button id="btnAdd">작성</button>
	</div>

	<hr>
	<div style="text-align: center;">
		<div id="reviews"></div>
	</div>

	<script type="text/javascript">
		$(document).ready(function(){
			getAllReview();
			
			// 리뷰 작성 기능
			$('#btnAdd').click(function(){
				var storeId = $('#storeId').val();
				var userId = $('#userId').val();
				var reviewStar = $('#reviewStar').val();
				var reviewContent = $('#reviewContent').val();
				var reviewTag = $('#reviewTag').val();
				
				var obj = {
						'storeId' : storeId,
						'userId' : userId,
						'reviewStar' : reviewStar,
						'reviewContent' : reviewContent,
						'reviewTag' : reviewTag
				}
				console.log(obj);
				
				$.ajax({
					type : 'POST',
					url : '../review',
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj),
					success : funtion(result) {
						console.log(result);
						if(result == 1) {
							alert('리뷰 입력 성공');
							getAllReview();
						}
					}
				});
			}); // end btnAdd.click()
			
			// 식당 리뷰 전체 가져오기
			function getAllReview() {
				var storeId = $('#storeId').val();
				
				var url = '../review/all/' + storeId;
				$.getJSON(
					url,
					function(data) {
						console.log(data);
						var list = '';
						
						$(data).each(function(){
							console.log(this); // 인덱스 데이터
							
							// 문자열 형태를 날짜 형태로 변환
							var reviewDate = new Date(this.reviewDate);
							
							list += '<div class="review_item">'
								+ '<pre>'
								+ '<input type="hidden" id="reviewId" value="'+ this.reviewId +'">'
								+ this.userId
								+ '&nbsp;&nbsp;' // 공백
								+ '<input type="text" id="reviewContent" value="'+ this.reviewContent +'">'
								+ '&nbsp;&nbsp;'
								+ reviewDate
								+ '&nbsp;&nbsp;'
								+ '<button class="btn_update" >수정</button>'
								+ '<button class="btn_delete" >삭제</button>'
								+ '</pre>'
								+ '</div>';
						}); // end each()
						
						$('#reviews').html(list);
					} // end function()
				); // end getJSON()
				
			} // end getAllReiview()
			
			// 수정 버튼을 클릭하면 선택된 댓글 수정
			$('#reviews').on('click', '.review_item .btn_update', function(){
				console.log(this);
				
				var reviewId = $(this).prevAll('#reviewId').val();
				var reviewContent = $(this).prevAll('#reviewContent').val();
				console.log("선택된 리뷰 번호 : " + reviewId + ", 리뷰 내용 : " + reviewContent);
				
				$.ajax({
					type : 'PUT', 
					url : '../review/' + reviewId,
					headers : {
						'Content-Type' : 'application/json'
					},
					data : reviewContent, 
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('댓글 수정 성공!');
							getAllReview();
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
			// 삭제 버튼을 클릭하면 선택된 댓글 삭제
			
			
		}); // end document()
		
		
	</script>


</body>
</html>