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
	         
	        // 식당 리뷰 전체 가져오기
			function getAllReview() {
			
				var storeId = $('#storeId').val();
	        	 		
				var url = '/review/all/' + storeId + '?page=' + pageNumber + '&pageSize=' + pageSize; // 페이지 파라미터
				
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
							var reviewUpdateDate = new Date(this.reviewUpdateDate).toLocaleString();
							// 수정된 날짜 표시
							var reviewDateText = (reviewDate !== reviewUpdateDate) ? reviewUpdateDate + '<span>(수정됨) </span>' : reviewDate;
				        	
				        	let loginId = $('#loginId').text();
							var username = this.userVO.username
							console.log("로그인 ID: " + loginId + "사용자: " + username); 
				        	
							list += '<div class="review_item">'
								+ '<pre>'
								+ '<input type="hidden" id="reviewId" value="'+ this.reviewId +'">'
								+ username
								+ '&nbsp;&nbsp;' // 공백
								+ '<input type="text" id="reviewStar" value="'+ this.reviewStar +'" style="pointer-events: none;" >'
								+ '&nbsp;&nbsp;'
								+ '<input type="text" id="reviewContent" value="'+ this.reviewContent +'" style="pointer-events: none;">'
								+ '&nbsp;&nbsp;'
								+ '<input type="text" id="reviewTag" value="'+ this.reviewTag +'" style="pointer-events: none;">'
								+ '&nbsp;&nbsp;'
								+ reviewDateText
								+'&nbsp;&nbsp;'
								+ '<span>추천 </span>' // 추천 수 표시
								+ this.reviewLike
								+ '<br>'
								
						// 버튼들 (추천, 신고)
						if(loginId && username && loginId != username) {
							list +=  '<div class="review_buttons">'
								+ '<button class="btn_like" data-review-id="'+ this.reviewId + '" >추천</button>'
								+ '<button class="btn_report" data-review-id="'+ this.reviewId + '" >신고</button>'
								+ '</div>'; // review_buttons div 끝
								+ '<br>'
						}
							// 이미지 조회
							list += '<div class="review-images">'
							$(this.reviewImageList).each(function(){
								console.log(this);
									
								var reviewImageList = this.reviewImageList;
									
								var ReviewImgUrl = '../image/get/' + this.reviewImageId + '/reviewImageExtension/' + this.reviewImageExtension
									
							list += '<div class="review-image">'
								+ '<a href="' + ReviewImgUrl + '"  target="_blank">'
								+ '<img width="100px" height="100px" src="' + ReviewImgUrl + '" />'
								+ '</a>'
								+ '</div>'
							});
							list += '</div>'
				
							// 리뷰 댓글 표시
							list += '<div class="review_replies" id="review_' + this.reviewId + '_replies"></div>'
						        + '<div  class="review_reply" style="display: none;">'
						        + '<textarea id="replyContent" placeholder="댓글 내용을 작성하세요" maxlength="50"></textarea>'
						        + '<button class="btn_reply">댓글 작성</button>'
						        + '</div>'; // review_reply div 끝
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
						replyset();
					}); // end getJSON()
				
			} // end getAllReiview()
			
			// 더보기 버튼 클릭 시
            $('#loadMoreBtn').click(function() {
                pageNumber++;  // 페이지 번호 증가
                getAllReview();  // 다음 페이지의 리뷰 로드
            });
            
			// 댓글 권한(사업자)
			function replyset() {
				let loginId = $('#loginId').text();
				let storeUserId = $('#storeUser').val();
				console.log("로그인 ID: " + loginId + "사장님: " + storeUserId);
				if (loginId == storeUserId) {
				let buttons = $('.reply_buttons');
					$('.review_reply').show();
				}
			}
			
			// 리뷰에 대한 댓글을 가져오는 함수
			function getReplies(reviewId) {
				
				var url = '/reply/all/' + reviewId;
				$.getJSON(
					url,
					function(data) {
						console.log(data);
						var repliesList = '';
						
						$(data).each(function(){
							var replyDate = new Date(this.replyDate).toLocaleString();
							var replyUpdateDate = new Date(this.replyUpdateDate).toLocaleString();
							
							let loginId = $('#loginId').text();
							let storeUserId = $('#storeUser').val();
							var username = this.userVO.username
							
							var replyDateText = (replyDate !== replyUpdateDate) ? replyUpdateDate + '<span>(수정됨) </span>' : replyDate;
													
								repliesList += '<div class="reply_item">'
									+ '<input type="hidden" id="reviewId" value="'+ this.reviewId +'">'
									+ username
									+ '&nbsp;&nbsp;'
									+ '<input type="hidden" id="replyId" value="'+ this.replyId +'">'
									+ '<input type="text" id="replyContent" value="'+ this.replyContent +'" readonly="readonly" >'
									+ '&nbsp;&nbsp;'
									+ replyDateText
							
							// 댓글 수정, 저장, 취소, 삭제 버튼들
							if (loginId == storeUserId) {	
								repliesList += '<div class="reply_buttons">'
									+ '<button class="btn_save_reply" style="display: none;">저장</button>'
									+ '<button class="btn_update_reply">수정</button>'
									+ '<button class="btn_return_reply" style="display: none;">취소</button>'
									+ '<button class="btn_delete_reply">삭제</button>'
									+ '</div>'
									+ '</div>';
							}
						
						}); // end each()
						
						$('#review_' + reviewId + '_replies').html(repliesList);
					} // end function()
				); // end getJSON()
				
			} // end getReplies()
			
			// 리뷰 추천
			$('#reviews').on('click', '.review_item .btn_like', function(){
				console.log(this);
				
				var reviewId = $(this).data('review-id');
				
				$.ajax({
					type : 'POST', 
					url : '/review/like/' + reviewId,
					headers : {
						'Content-Type' : 'application/json'
					}, 
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('리뷰 추천!');
							getAllReview();
						} else if(result != 1) {
							alert('이미 추천된 리뷰입니다.');
						}
					},
					error: function(xhr, status, error) {
                        alert('추천할 수 없습니다.');
                    }
					
				}); 
				
			}); // end reviews.on()
			
			// 리뷰 신고 버튼 클릭 시 모달 열기
			$(document).on('click', '.btn_report', function() {
	
			  var reviewId = $(this).data('review-id');
			  var reviewContent = $(this).closest('.review_item').find('#reviewContent').val();
			  
			// 신고여부 확인
			  $.ajax({
				  type : 'GET',
				  url : '/review/report/' + reviewId,
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
				  },
					error: function(xhr, status, error) {
                        alert('신고할 수 없습니다.');
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
							'reviewReportMessage' : reviewReportMessage,
							'reviewContent' : reviewContent
					}
					console.log(obj3);
			  
				// 서버로 신고 데이터 보내기
			      $.ajax({
			    	  type: 'POST',
			          url: '/review/report/' + reviewId, // 실제 서버 URL로 변경
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
				
			var reviewId = $(this).closest('.review_item').find('#reviewId').val();  // 리뷰 아이디 가져오기
	        var replyContent = $(this).siblings('#replyContent').val();  // 댓글 내용 가져오기

	        if (!replyContent) {
                alert("댓글을 입력해주세요.");
                return;
            }
	        
				var obj4 = {
						'reviewId' : reviewId,
						'replyContent' : replyContent
				}
				console.log(obj4);
				
				$.ajax({
					type : 'POST',
					url : '/reply',
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj4),
					success : function(result) {
						console.log(result);
						if(result == 1) {
							alert('댓글 입력 성공');
							getReplies(reviewId);
							$('#replyContent').val("");
						}
					},
					error: function(xhr, status, error) {
	                     if (xhr.status == 400) {
	                         alert('댓글 내용은 50자 이하로 작성해주세요.');
	                     } else {
	                         alert('댓글 등록에 실패했습니다.');
	                     }
	                 }
				});
				
			}); // end reviews.on()
			
			// 선택된 댓글 수정
			$('#reviews').on('click', '.reply_item .btn_update_reply', function(){
				console.log(this);
				
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();
				var replyId = $(this).closest('.review_item').find('#replyId').val();
				var replyContent = $(this).closest('.reply_item').find('#replyContent').val();
				
				// 선택된 버튼을 포함한 .reply_item에서만 수정
				var replyItem = $(this).closest('.reply_item');
				
				replyItem.find('#replyContent').attr('readonly', false);
				replyItem.find('#replyContent').focus();
				
				replyItem.find('.btn_update_reply').hide();
				replyItem.find('.btn_delete_reply').hide();
				replyItem.find('.btn_save_reply').show();
				replyItem.find('.btn_return_reply').show();
				
				// 저장 버튼 눌렀을 때
				replyItem.find('.btn_save_reply').on('click', function(){
					let updateReplyId = $(this).parent().siblings('#replyId').val();
					var updatedContent = replyItem.find('#replyContent').val();
					console.log("리뷰 번호 : " + reviewId + "댓글 번호 : " + updateReplyId + ", 댓글 내용 : " + updatedContent);
					
					if(replyContent == updatedContent) {
						alert("수정할 댓글을 입력해주세요.");
		                return;
					}
					
					$.ajax({
						type : 'PUT', 
						url : '/reply/' + updateReplyId,
						headers : {
							'Content-Type' : 'application/json'
						},
						data : JSON.stringify({"replyContent" : updatedContent}),  
						success : function(result) {
							console.log(result);
							if(result == 1) {
								alert('댓글 수정 성공!');
								getReplies(reviewId);
							}
						}
						
					}); 
				}); // end btn_save_reply.on()
				
				// 취소 버튼 눌렀을 때
				replyItem.find('.btn_return_reply').on('click', function(){
				
					replyItem.find('#replyContent').attr('readonly', true);
					replyItem.find('.btn_update_reply').show();
					replyItem.find('.btn_delete_reply').show();
					replyItem.find('.btn_save_reply').hide();
					replyItem.find('.btn_return_reply').hide();
				}); // end btn_return_reply.on()
				
			}); // end reviews.on() 
			
			
		// 선택된 댓글 삭제
			$('#reviews').on('click', '.reply_item .btn_delete_reply', function(){
				console.log(this);
				
				var reviewId = $(this).closest('.review_item').find('#reviewId').val();
				var replyId = $(this).closest('.reply_item').find('#replyId').val();
				console.log("리뷰 번호 : " + reviewId + ", 댓글 번호 : " + replyId);
				
				$.ajax({
					type : 'DELETE', 
					url : '/reply/' + replyId,
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