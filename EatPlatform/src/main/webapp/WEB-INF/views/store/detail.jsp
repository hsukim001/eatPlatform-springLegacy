<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/detail.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/calendar.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/priceSeparate.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/calendar.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/store/ReviewReplyAPI.js"></script>
	<script>
		$(function(){
			
			$('#reservBtnWrap input').click(function(){
				$('#calendar-wrap > div').stop().fadeToggle(400);
				$('#calendar-wrap').stop().slideToggle(500);
			}); // End #reservBtnWrap input.click
			
			$('#scoreWrap img').click(function () {
			    let currentScore = $(this).data('score'); 
			    console.log(currentScore);

			    $('#scoreWrap img').each(function () {
			        let imgScore = $(this).data('score'); 
			        if (imgScore <= currentScore) {
			            $(this).attr('src', '<%=request.getContextPath()%>/resources/img/sample3.png');
			        } else {
			            $(this).attr('src', '<%=request.getContextPath()%>/resources/img/sample3bk.png');
			        }
			    }); // End #scoreWrap img.each 

			    $(this).siblings('.scoreText').find('#reviewStar').text(currentScore);
			}); // End #scoreWrap img.click
			
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
                		'<img src="<%=request.getContextPath()%>/resources/img/store/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
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

		        const tagId = $(this).data("tag-id")
		        const tag = tagData.find(tag => tag.icon === tagId); 
		        let currentTags = $("#reviewTag").val().split(",").filter(Boolean); 

		        if ($(this).hasClass("tagActive")) {
		            if (!currentTags.includes(String(tagId))) {
		                currentTags.push(tagId);
		            }
		            const tagItem =
		                '<li data-sticker-id=' + tag.icon + '>' +
		                	'<img src="<%=request.getContextPath()%>/resources/img/store/tag_icon_' + tag.icon + '.png" alt="태그_' + tag.text + '">' +
		                	'<span>' + tag.text + '</span>' +
		                '</li>';
		            tagContainer.append(tagItem);
		        } else {
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
		    }); // End #tagList li.click

		    $("#reviewWrite").hide();
		    
		    $("#reviewBtn").click(function(){
		    	$("#reviewWrite").slideToggle('300');
		    });		
		}); // End $function
	</script>

	<title>${storeVO.storeName }</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<div id="storeContainer">
				<p class="storeTitle width100 textLeft mb20 bold">${storeVO.storeName }</p>
				<div id="storeInfoBox" class="mb30">
					<div id="storeInfoImg">
						<img src="<%=request.getContextPath()%>/resources/img/sample2.png" alt="sample">
					</div>
					<div id="storeInfoText">
						<ul>
							<li>
								<span class="textTitle">영업 시간 </span> 
								<span class="colon">:</span> 
								<span class="textValue">${storeVO.businessHour }</span>
							</li>
							<li>
								<span class="textTitle">연락처 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeVO.storePhone }</span></li>
							<li>
								<span class="textTitle">대표명 </span>
								 <span class="colon">:</span>
								<span class="textValue">${storeVO.ownerName }</span></li>
							<li>
								<span class="textTitle">최근 등록일 </span> 
								<span class="colon">:</span> 
								<span class="textValue">${storeVO.storeUpdateDate }</span>
							</li>
							<li>
								<span class="textTitle">별점 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeVO.storeUpdateDate }</span></li>
							<li>
								<span class="textTitle">추천 수 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeVO.storeUpdateDate }</span></li>
						</ul>

					</div>
				</div> 
				<!-- End StoreInfoBox -->
				<div id="reservBtnWrap" class="width100 mb30">
					<input type="button" id="reservBtn" value="온라인 예약">	
					<jsp:include page="/include/calendar.jsp" />
				</div>
				<div id="remoteBar" class="mb30">
					<ul>
						<li><a href="#storeContent"> 매장 소개 </a></li>
						<li><a href="#"> 메뉴 </a></li>
						<li><a href="#"> 리뷰 </a></li>
					</ul>
				</div>
				<!--  End RemoteBar -->
				
				<div id="storeContent" class="width100 mb30">
					
					<div id="storeContentDescription" class="textLeft width100 mb30">
						<p class="descriptionTitle mb20 bold">매장 소개</p>
					    <c:if test="${empty storeVO.description}">
					        <pre class="descriptionContent">작성된 소개글이 없습니다.</pre>
					    </c:if>
					   
				    	<c:if test="${not empty storeVO.description}">
					        <pre class="descriptionContent">${storeVO.description }</pre>
					    </c:if>
					</div>
					<!-- End storeContentDescription -->
					
					<div id="menuContainer">
						<ul class="width100">
						    <c:if test="${empty menuVO}">
					        	<pre class="descriptionContent">등록된 메뉴가 없습니다.</pre>
					   		</c:if>
							<c:forEach var="menu" items="${menuVO}" varStatus="status">
								<li>
									<img src="<%=request.getContextPath()%>/resources/img/sample3.png" alt="메뉴사진 ${status.index + 1 }">
									<p class="menuTitle menuText">${menu.menuName }</p>
									<p class="menuComment menuText">${menu.menuComment }</p>
									<p class="menuPrice menuText">${menu.menuPrice } \</p>
								</li>
							</c:forEach>
						</ul>
					</div>
					<!--  End MenuConteiner -->
					
					<div id="reviewContainer">
						<p>
							리뷰 (???)
							<input id="reviewBtn" type="button" value="작성하기 &nbsp;&nbsp;Ⅴ">
						</p>
						
						<input type="hidden" id="storeId" value="${storeVO.storeId }">
						<input type="hidden" id="userId" value="${sessionScope.userId }" readonly >
						<div id="reviewWrite">
							<div id="scoreWrap">
								<img src="<%=request.getContextPath()%>/resources/img/sample3bk.png" alt="추천점수" data-score="1">
								<img src="<%=request.getContextPath()%>/resources/img/sample3bk.png" alt="추천점수" data-score="2">
								<img src="<%=request.getContextPath()%>/resources/img/sample3bk.png" alt="추천점수" data-score="3">
								<img src="<%=request.getContextPath()%>/resources/img/sample3bk.png" alt="추천점수" data-score="4">
								<img src="<%=request.getContextPath()%>/resources/img/sample3bk.png" alt="추천점수" data-score="5">
							
								<div class="scoreText">
									<span id="reviewStar">0 </span>
									<span class="scoreNum"> / 5 점</span>
								</div>
							</div>
							<div id="tagSelect">
								<div class="tagBtn">
									태그 추가
								</div>
								<ul id="tagList"></ul>
							</div>
							<ul id="viewTag"></ul>
							
							<input type="hidden" id="reviewTag" placeholder="태그">
					        <textarea id="reviewContent" placeholder="리뷰 내용을 작성하세요"></textarea>								<button id="btnAdd">작성</button>
						</div>
							<div style="text-align: center;">
								<div id="reviews"></div>
								<br>
								<!-- 더보기 버튼 추가 -->
								<button id="loadMoreBtn">더보기</button>
							</div>
							
							<!-- 리뷰 신고 모달 포함 -->
    						<jsp:include page="/include/reportModal.jsp" />	
						<ul>

						</ul>
					</div>
					<!-- End ReviewContainer -->
				</div>
				<!--  End StoreContent -->
			</div>
			<!--  End StoreContainer -->
		</div>
		<!--  End Container -->

	</div>
	<!--  End Wrap -->
</body>
</html>