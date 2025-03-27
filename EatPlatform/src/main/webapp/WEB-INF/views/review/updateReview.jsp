<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/detail.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page/image.css">
<title>리뷰 수정</title>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script>
	$(function(){
	    let originScore = $('#reviewStar').val();
	    let originTag = $('#reviewTag').val();
	    
		$('#scoreWrap img').click(function () {
		    let currentScore = $(this).data('score'); 
		    console.log(currentScore);

		    $('#scoreWrap img').each(function () {
		        let imgScore = $(this).data('score'); 
		        if (imgScore <= currentScore) {
		            $(this).attr('src', '<%=request.getContextPath()%>/resources/img/common/fullStar.png');
		        } else {
		            $(this).attr('src', '<%=request.getContextPath()%>/resources/img/common/emptyStar.png');
		        }
		    }); // End #scoreWrap img.each 

		    $('#reviewStar').val(currentScore);
		    console.log($('#reviewStar').val());
		}); // End #scoreWrap img.click

	    $('#scoreWrap img').each(function() {
	        if ($(this).data('score') == originScore) {
	            $(this).trigger('click');
	        }
	    });
		
		const tagData = [
		    { icon: 1, text: "음식이 맛있어요 !" },
		    { icon: 2, text: "친절해요" },
		    { icon: 3, text: "분위기가 좋아요." },
		    { icon: 4, text: "재료가 신선해요." },
		    { icon: 5, text: "청결해요." },
		    { icon: 6, text: "재방문 의사가 있어요." }
		];
		console.log(tagData);
		const listContainer = $('#tagList');
		const tagContainer = $('#viewTag');

		tagData.forEach(tag => {
		    const listItem =
		    	'<li data-tag-id=' + tag.icon +'>' +
               		'<img src="<%=request.getContextPath()%>/resources/img/store/detail/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
               		'<span>' + tag.text + '</span>' +
          			 '</li>';
		    listContainer.append(listItem);
		}); // End tagData.forEach
		
		$(document).click(function(event) {
		    if (!$(event.target).closest('.tagBtn').length && !$(event.target).closest('#tagList').length) {
		        $('#tagList').slideUp(200); 
		    }
		}); // End document.click

		$(".tagBtn").click(function(event) {
		    event.stopPropagation();  
		    $('#tagList').stop().slideToggle(200); 
		}); // End .tagBtn.click
		
		$("#tagList li").click(function () {
		    $(this).toggleClass("tagActive");

		    const tagId = $(this).data("tag-id");
		    const tag = tagData.find(tag => tag.icon === tagId); 
		    let currentTags = $("#reviewTag").val().split(",").filter(Boolean); 

		    if ($(this).hasClass("tagActive")) {
		        if (!currentTags.includes(String(tagId))) {
		            currentTags.push(tagId);
		        }
		        const tagItem =
		            '<li data-sticker-id=' + tag.icon + '>' +
		            	'<img src="<%=request.getContextPath()%>/resources/img/store/detail/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
		            	'<span>' + tag.text + '</span>' +
		            '</li>';
		        tagContainer.append(tagItem);
		    } else {
		        currentTags = currentTags.filter(tag => tag !== String(tagId));
		        $('#viewTag li').each(function() {
		            const stickerId = $(this).data('sticker-id'); 
		            if (stickerId === tagId) {
		                $(this).remove();
		            }
		        });
		    }

		    $("#reviewTag").val(currentTags.join(","));

		    let tags = $("#reviewTag").val().split(",").filter(Boolean).map(Number);
		    tags.sort((a, b) => a - b);

		    $("#reviewTag").val(tags.join(","));
		    console.log($("#reviewTag").val());
		});
		

		$('#tagList li').each(function(){
		    const tagId = $(this).data('tag-id');

		    if (originTag.includes(String(tagId))) {
		        $(this).addClass("tagActive");

		        const tag = tagData.find(tag => tag.icon === tagId);
		        const tagItem =
		            '<li data-sticker-id=' + tag.icon + '>' +
		            '<img src="<%=request.getContextPath()%>/resources/img/store/detail/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
		            '<span>' + tag.text + '</span>' +
		            '</li>';
		        tagContainer.append(tagItem);
		    }
		});


	});
</script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/page/image.js"></script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<p class="title">리뷰 수정</p>
			<div id="review_container">
				<form id="updateReviewForm" action="/review/updateReview" method="POST">
					<div>
						<input type="hidden" name="storeId" value="${reviewVO.storeId }" >
						<input type="hidden" name="reviewId" value="${reviewVO.reviewId }" >
					</div>
					<div id="scoreWrap">
						<img src="<%=request.getContextPath()%>/resources/img/common/emptyStar.png" alt="추천점수" data-score="1">
						<img src="<%=request.getContextPath()%>/resources/img/common/emptyStar.png" alt="추천점수" data-score="2">
						<img src="<%=request.getContextPath()%>/resources/img/common/emptyStar.png" alt="추천점수" data-score="3">
						<img src="<%=request.getContextPath()%>/resources/img/common/emptyStar.png" alt="추천점수" data-score="4">
						<img src="<%=request.getContextPath()%>/resources/img/common/emptyStar.png" alt="추천점수" data-score="5">							
						<div class="scoreText">
							<input type="text" id="reviewStar" name="reviewStar" value="${reviewVO.reviewStar }" readonly>
							<span class="scoreNum"> / 5 점</span>
						</div>
					</div>
					<div id="tagSelect">
						<div class="tagBtn">태그 추가</div>
						<ul id="tagList"></ul>
					</div>
					<ul id="viewTag"></ul>
					<input type="hidden" id="reviewTag" name="reviewTag" value="${reviewVO.reviewTag }">
					<textarea id="reviewContent" name="reviewContent" maxlength="250" >${reviewVO.reviewContent }</textarea>		
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
				
				<button id="change-upload">이미지 변경</button>
				
				<div class="image-view">
					<div class="review-image">
						<c:forEach var="reviewImageVO" items="${reviewVO.reviewImageList }">
						<a href="/image/get/${reviewImageVO.reviewImageId }/reviewImageExtension/${reviewImageVO.reviewImageExtension }" target="_blank">
						<img width="100px" height="100px" 
						src="/image/get/${reviewImageVO.reviewImageId }/reviewImageExtension/${reviewImageVO.reviewImageExtension }" />
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
				<button onclick="history.back()">취소</button>
				
				<script type="text/javascript">
				// ajaxSend() : AJAX 요청이 전송되려고 할 때 실행할 함수를 지정
			    // ajax 요청을 보낼 때마다 CSRF 토큰을 요청 헤더에 추가하는 코드
			    $(document).ajaxSend(function(e, xhr, opt){
			       var token = $("meta[name='_csrf']").attr("content");
			       var header = $("meta[name='_csrf_header']").attr("content");
			       
			       xhr.setRequestHeader(header, token);
			    });
					
				// reviewStar 입력 제한
			    document.getElementById("reviewStar").addEventListener("input", function() {
			        const inputValue = this.value;
			
			     	// 숫자가 아닌 문자가 입력되었을 때 입력을 제거
			        if (isNaN(inputValue) || inputValue < 1 || inputValue > 5) {
			            alert("별점은 1부터 5까지의 숫자만 입력 가능합니다.");
				        this.value = ''; // 입력된 값 초기화
			        }
			       
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
						
						if($('#reviewContent').val() == "") {
							alert("내용을 입력해주세요.");
							$('#reviewContent').focus();
							return;
						}
						
						// form 객체 참조
						var updateReviewForm = $('#updateReviewForm');
						
			         	// reviewImg-list의 각 input 태그 접근
			         	var i = 0;
			            $('.reviewImg-list input[name="reviewImageVO"]').each(function() {
			           	var reviewImageVO = JSON.parse($(this).val());
			           	
			           	var inputPath = $('<input>').attr('type','hidden').attr('name','reviewImageList[' + i + '].reviewImagePath');
			           	inputPath.val(reviewImageVO.reviewImagePath);
			           	var inputRealName = $('<input>').attr('type','hidden').attr('name','reviewImageList[' + i + '].reviewImageRealName');
			           	inputRealName.val(reviewImageVO.reviewImageRealName);
			           	var inputChgName = $('<input>').attr('type','hidden').attr('name','reviewImageList[' + i + '].reviewImageChgName');
			           	inputChgName.val(reviewImageVO.reviewImageChgName);
			           	var inputExtension = $('<input>').attr('type','hidden').attr('name','reviewImageList[' + i + '].reviewImageExtension');
			           	inputExtension.val(reviewImageVO.reviewImageExtension);
			           	
			           	updateReviewForm.append(inputPath);
			           	updateReviewForm.append(inputRealName);
			           	updateReviewForm.append(inputChgName);
			           	updateReviewForm.append(inputExtension);
			           	
			           	i++;
			           	
			            });
			            updateReviewForm.submit();
			           
			        });
				
				</script>
		
			</div>
		</div>
		<jsp:include page="/include/footer.jsp" />	
	</div>

</body>
</html>