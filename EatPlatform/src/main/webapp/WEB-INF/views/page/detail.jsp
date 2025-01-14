<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- css 파일 불러오기 -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page/image.css">
<title>리뷰</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
</head>
<body>
	
	<input type="hidden" id="storeId" value="1">
	
	<input type="text" id="userId" readonly="readonly" value="test21" >
	<div id="reviewDiv" style="text-align: center;">
		<input type="number" id="reviewStar" placeholder="별점" min="1" max="5">
		<input type="text" id="reviewTag" placeholder="태그">
		<br>
		<br>
        <textarea id="reviewContent" placeholder="리뷰 내용을 작성하세요"></textarea>
		<button id="btnAdd">작성</button>
	</div>
	
	<div class="image-upload">
      <h2>이미지 파일 업로드</h2>
      <p>* 이미지 파일은 최대 3개까지 가능합니다.</p>
      <p>* 최대 용량은 10MB 입니다.</p>
      <div class="image-drop"></div>
      <h2>선택한 이미지 파일 :</h2>
      <div class="image-list"></div>
   	</div>
   	
   	<div class="reviewImg-list">
    </div>
   
   	<script src="<%=request.getContextPath()%>/resources/js/page/image.js"></script>

	<hr>
	<div style="text-align: center;">
		<div id="reviews"></div>
		<br>
		<!-- 더보기 버튼 추가 -->
		<button id="loadMoreBtn">더보기</button>
	</div>
	
	<!-- 리뷰 신고 모달 포함 -->
    <jsp:include page="reportModal.jsp" />

	<script type="text/javascript">
		// ajaxSend() : AJAX 요청이 전송되려고 할 때 실행할 함수를 지정
	    // ajax 요청을 보낼 때마다 CSRF 토큰을 요청 헤더에 추가하는 코드
	    $(document).ajaxSend(function(e, xhr, opt){
	       var token = $("meta[name='_csrf']").attr("content");
	       var header = $("meta[name='_csrf_header']").attr("content");
	       
	       xhr.setRequestHeader(header, token);
	    });
		
		$(document).ready(function(){
			var pageNumber = 1;  // 페이지 번호
            var pageSize = 5;  // 한 번에 가져올 리뷰의 개수
            var totalReviews = 0; // 전체 리뷰 개수
			getAllReview();
            
            let userId = $('#userId').val();
			
			// 리뷰 등록
			$('#btnAdd').click(function(){
				var storeId = $('#storeId').val();
				var reviewStar = $('#reviewStar').val();
				var reviewContent = $('#reviewContent').val();
				var reviewTag = $('#reviewTag').val();
				
				if (!reviewStar || !reviewContent || !reviewTag) {
                    alert("모든 항목을 입력해주세요.");
                    return;
                }
				
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
	            
	         	// JSON 데이터 객체 생성
	            var obj = {
	            		"storeId" : storeId,
		                "userId" : userId,
		                "reviewStar" : reviewStar,
		                "reviewContent" : reviewContent,
		                "reviewTag" : reviewTag,
		                "reviewImageList" : reviewImageList  // 이미지 리스트 추가	
	         	};
				console.log(obj);
	         
	         	// JSON 데이터를 전송
	             $.ajax({
	                 type: 'POST',
	                 url: '../review',  // 서버의 URL
	                 headers : {
							"Content-Type" : "application/json"
					 },
	                 data: JSON.stringify(obj),  // JSON 문자열로 변환하여 전송
	                 success: function(result) {
	                     if (result == 1) {
	                         alert('리뷰 등록 성공');
	                         $('#reviewDiv input, #reviewDiv textarea').val(''); // reviewDiv 값 초기화
	                         $('.image-list').html(''); // image-list 초기화
	                         getAllReview();  // 리뷰 목록 다시 불러오기
	                     } else {
	                         alert('리뷰 등록에 실패했습니다.');
	                     }
	                 },
	                 error: function(xhr, status, error) {
	                     if (xhr.status == 400) {
	                         alert('리뷰 내용은 250자 이하로 작성해주세요.');
	                     } else {
	                         alert('리뷰 등록에 실패했습니다. 다시 시도해주세요.');
	                     }
	                 }
	             });
	         }); // end btnAdd.click()

	        // 식당 리뷰 전체 가져오기
			function getAllReview() {
				
				var storeId = $('#storeId').val();
				
				var url = '../review/all/' + storeId + '?page=' + pageNumber + '&pageSize=' + pageSize; // 페이지 파라미터
				
				$.getJSON(
					url,
					function(data) {				
						console.log(data);
						
						if(data.totalReviews === 0) {
							if (pageNumber === 1) {
                                $('#reviews').html('<p>현재 리뷰가 없습니다.</p>'); // 첫 페이지에 데이터가 없을 때
                             }
                             $('#loadMoreBtn').hide();
                             return;
						}
						
						var list = '';
		
						$(data.list).each(function(){
							console.log(this); // 인덱스 데이터
					
							// 문자열 형태를 날짜 형태로 변환
							var reviewDate = new Date(this.reviewDate).toLocaleString();
							var reviewImageDate = new Date(this.reviewImageDate).toLocaleString();
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
								+ '<button class="btn_update" >수정</button>'
								+ '<button class="btn_delete" >삭제</button>'
								+ '<button class="btn_like" >추천</button>'
								+ '<button class="btn_report" data-review-id="'+ this.reviewId + '" >신고</button>'
							
							// 이미지 조회
							$(this.reviewImageList).each(function(){
								console.log(this);
								
							list += '<div class="image-view">'
								+ '<a href="../image/get/' + this.reviewId + '/reviewImageExtension/' + this.reviewImageExtension + '"  target="_blank">'
								+ '<img width="100px" height="100px" src="../image/get/' + this.reviewId + '/reviewImageExtension/' + this.reviewImageExtension + '" />'
								+ '</a>'
								+ '</div>'
								+ '</pre>'
								});
								
							// 리뷰 댓글 표시
							list += '<div class="review_replies" id="review_'+ this.reviewId + '_replies">' 
								+ '</div>' 
								+ '<div class="review_reply">' // 리뷰 댓글 입력창
								+ '<input type="text" id="replyContent" value="'+ this.replyContent +'">'
								+ '&nbsp;&nbsp;'
								+ '<button class="btn_reply" >댓글 입력</button>'
								+ '</div>'
								+ '</div>';
						
							getReplies(this.reviewId);

						}); // end each()
						
						// 첫 페이지 = 초기화 / 그 외는 붙이기
                        if (pageNumber === 1) {
                           $('#reviews').html(list);
                        } else {
                           $('#reviews').append(list); 
                        }
						
                     	// 페이징 버튼 처리
                        let endPage = pageNumber * pageSize;
                        if (data.totalReviews <= endPage) {
                           $('#loadMoreBtn').hide(); 
                        } else {
                           $('#loadMoreBtn').show(); 
                        }

					}); // end getJSON()
				
			} // end getAllReiview()
			
			// 더보기 버튼 클릭 시
            $('#loadMoreBtn').click(function() {
                pageNumber++;  // 페이지 번호 증가
                getAllReview();  // 다음 페이지의 리뷰 로드
            });
			
		
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
							alert('이미 추천된 리뷰입니다.');
						}
					}
					
				}); 
				
			}); // end reviews.on()
			
			// 리뷰 신고 버튼 클릭 시 모달 열기
			$(document).on('click', '.btn_report', function() {
	
			  var reviewContent = $(this).prevAll('#reviewContent').val();
			  var reviewId = $(this).data('review-id');
			  
			// 신고여부 확인
			  $.ajax({
				  type : 'GET',
				  url : '../review/report/' + reviewId + '/user/' + userId,
				  headers : {
					  'Content-Type' : 'application/json'
				  },
				  success : function(response) {
					  console.log(response);
					  if(response === 0) {
						  $('#reportModal').show();
						  $('#reportText').val(''); // 기존 입력값 초기화
					  } else {
						  alert('이미 신고된 리뷰입니다.');
					  }
				  }
			  });
			
			  
			// 신고 제출 버튼 클릭 시 처리
			  $('input[name="reportReason"]').on('change', function() {
				  // 선택한 신고 사유를 처리할 로직은 이미 submitReport에서 처리됨
			  });
			  
			// 신고 제출 버튼 클릭 시 처리
			  $('#submitReport').off('click').on('click', function() {
			    var selectedReason = $('input[name="reportReason"]:checked').val();

			    if (selectedReason) {
			      // 신고 내용 처리 (예: 서버로 보내기)
			      var reviewReportMessage = selectedReason;
			      
			      var obj3 = {
			    		  'reviewId' : reviewId,
							'userId' : userId,
							'reviewReportMessage' : reviewReportMessage,
							'reviewContent' : reviewContent
					}
					console.log(obj3);
			  
			// 서버로 신고 데이터 보내기
		      $.ajax({
		    	  type: 'POST',
		          url: '../review/report/' + reviewId, // 실제 서버 URL로 변경
		          headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj3),
		        	success: function(result) {
		        		if (result === 1) {
		        			alert('리뷰 신고가 제출되었습니다.');
		        			$('#reportModal').hide(); // 신고 후 모달 닫기
		        		}
		        	},
		          error: function(error) {
		          alert('신고 제출에 실패했습니다. 다시 시도해주세요.');
		          $('#reportModal').hide(); 
		        }
		      });
		    } else {
		      alert('신고 사유를 선택해주세요.');
		    }
		  });
		});

		// 모달 닫기 버튼 클릭 시 모달 닫기
		$('#closeBtn').on('click', function() {
		  $('#reportModal').hide();
		});

		// 모달 외부 클릭 시 모달 닫기
		$(window).on('click', function(event) {
		  if (event.target == document.getElementById('reportModal')) {
		    $('#reportModal').hide();
		  }
		});
					
			// 선택된 리뷰에 댓글 등록(사업자)
			$('#reviews').on('click', '.review_item .btn_reply', function(){
				console.log(this);
				
				var storeId = $('#storeId').val();
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();  // 리뷰 아이디 가져오기
	            var replyContent = $(this).siblings('#replyContent').val();  // 댓글 내용 가져오기

				var obj4 = {
						'storeId' : storeId,
						'reviewId' : reviewId,
						'replyContent' : replyContent
				}
				console.log(obj4);
				
				$.ajax({
					type : 'POST',
					url : '../reply',
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj4),
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