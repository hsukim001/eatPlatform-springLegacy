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
		<input type="text" id="userId">
		<input type="text" id="reviewStar"> 
		<input type="text" id="reviewContent">
		<input type="text" id="reviewTag">
		<button id="btnAdd">작성</button>
	</div>

	<hr>
	<div style="text-align: center;">
		<div id="reviews"></div>
	</div>

	<script type="text/javascript">
		$(document).ready(function(){
			getAllReview();
			
			// 리뷰 등록
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
					success : function(result) {
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
								+ '<input type="text" id="reviewStar" value="'+ this.reviewStar +'">'
								+ '&nbsp;&nbsp;'
								+ '<input type="text" id="reviewContent" value="'+ this.reviewContent +'">'
								+ '&nbsp;&nbsp;'
								+ '<input type="text" id="reviewTag" value="'+ this.reviewTag +'">'
								+ '&nbsp;&nbsp;'
								+ reviewDate
								+ '&nbsp;&nbsp;'
								+ this.reviewLike // 추천 수 표시
								+ '&nbsp;&nbsp;'
								+ '<button class="btn_update" >리뷰 수정</button>'
								+ '<button class="btn_delete" >리뷰 삭제</button>'
								+ '<button class="btn_like" >리뷰 추천</button>'
								+ '<button class="btn_report" >리뷰 신고</button>'
								+ '</pre>'
								+ '<div class="review_replies" id="review_'+ this.reviewId + '_replies">' // 리뷰 댓글 표시
								+ '</div>' 
								+ '<div class="review_reply">' // 리뷰 댓글 입력창
								+ '<input type="text" id="replyContent" value="'+ this.replyContent +'">'
								+ '&nbsp;&nbsp;'
								+ '<button class="btn_reply" >댓글 입력</button>'
								+ '</div>'
								+ '</div>';
								
								getReplies(this.reviewId);
						}); // end each()
						
						$('#reviews').html(list);
					} // end function()
				); // end getJSON()
				
			} // end getAllReiview()
			
			// 리뷰에 대한 댓글을 가져오는 함수
			function getReplies(reviewId) {
				
				var url = '../reply/all/' + reviewId;
				$.getJSON(
					url,
					function(data) {
						console.log(data);
						var repliesList = '';
						
						$(data).each(function(){
							
							var replyDate = new Date(this.replyDate);
						
							repliesList += '<div class="reply_item">'
								+ '<input type="hidden" id="reviewId" value="'+ this.reviewId +'">'
								+ '<input type="hidden" id="replyId" value="'+ this.replyId +'">'
								+ '<input type="text" id="replyContent" value="'+ this.replyContent +'">'
								+ '&nbsp;&nbsp;'
								+ replyDate
								+ '&nbsp;&nbsp;'
								+ '<button class="btn_update_reply" >댓글 수정</button>'
								+ '<button class="btn_delete_reply" >댓글 삭제</button>'
								+ '</div>';
						}); // end each()
						
						$('#review_' + reviewId + '_replies').html(repliesList);
					} // end function()
				); // end getJSON()
				
			} // end getReplies()
			
			// 리뷰 수정
			$('#reviews').on('click', '.review_item .btn_update', function(){
				console.log(this);
				
				var reviewId = $(this).prevAll('#reviewId').val();
				var reviewStar = $(this).prevAll('#reviewStar').val();
				var reviewContent = $(this).prevAll('#reviewContent').val();
				var reviewTag = $(this).prevAll('#reviewTag').val();

				var obj2 = {
						'reviewId' : reviewId,
						'reviewStar' : reviewStar,
						'reviewContent' : reviewContent,
						'reviewTag' : reviewTag
				}
				console.log(obj2);
				
				$.ajax({
					type : 'PUT', 
					url : '../review/' + reviewId,
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj2), 
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('리뷰 수정 성공!');
							getAllReview();
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
			// 리뷰 삭제
			$('#reviews').on('click', '.review_item .btn_delete', function(){
				console.log(this);
				
				var reviewId = $(this).prevAll('#reviewId').val();
				
				$.ajax({
					type : 'DELETE', 
					url : '../review/' + reviewId,
					headers : {
						'Content-Type' : 'application/json'
					}, 
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('리뷰 삭제 성공!');
							getAllReview();
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
			// 리뷰 추천
			$('#reviews').on('click', '.review_item .btn_like', function(){
				console.log(this);
				
				var reviewId = $(this).prevAll('#reviewId').val();
				var userId = $(this).prevAll('#userId').val();
				console.log("리뷰번호 : " + reviewId + ", 회원 : " + userId);
				
				$.ajax({
					type : 'POST', 
					url : '../review/like/' + reviewId,
					headers : {
						'Content-Type' : 'application/json'
					},
					data : userId,
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('리뷰 추천!');
							getAllReview();
						} else if(result != 1) {
							alert('이미 추천');
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
			// 선택된 리뷰에 댓글 등록(사업자)
			$('#reviews').on('click', '.review_item .btn_reply', function(){
				console.log(this);
				
				var storeId = $('#storeId').val();
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();  // 리뷰 아이디 가져오기
	            var replyContent = $(this).siblings('#replyContent').val();  // 댓글 내용 가져오기

				var obj3 = {
						'storeId' : storeId,
						'reviewId' : reviewId,
						'replyContent' : replyContent
				}
				console.log(obj3);
				
				$.ajax({
					type : 'POST',
					url : '../reply',
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj3),
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('댓글 입력 성공');
							getReplies(reviewId);
						}
					}
				});
				
			}); // end reviews.on()
			
			// 선택된 댓글 수정
			$('#reviews').on('click', '.reply_item .btn_update_reply', function(){
				console.log(this);
				
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();
				var replyId = $(this).closest('.reply_item').find('#replyId').val();
				var replyContent = $(this).closest('.reply_item').find('#replyContent').val(); 
				console.log("리뷰 번호 : " + reviewId + "댓글 번호 : " + replyId + ", 댓글 내용 : " + replyContent);
				
				$.ajax({
					type : 'PUT', 
					url : '../reply/' + replyId,
					headers : {
						'Content-Type' : 'application/json'
					},
					data : replyContent,  
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('댓글 수정 성공!');
							getReplies(reviewId);
						}
					}
					
				}); 
				
			}); // end reviews.on() 
			
			// 선택된 댓글 삭제
			$('#reviews').on('click', '.reply_item .btn_delete_reply', function(){
				console.log(this);
				
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();
				var replyId = $(this).closest('.reply_item').find('#replyId').val();
				console.log("리뷰 번호 : " + reviewId + ", 댓글 번호 : " + replyId);
				
				$.ajax({
					type : 'DELETE', 
					url : '../reply/' + replyId,
					headers : {
						'Content-Type' : 'application/json'
					}, 
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('댓글 삭제 성공!');
							getReplies(reviewId);
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
		}); // end document()
	</script>


</body>
</html>