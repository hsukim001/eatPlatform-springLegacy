<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main.css">
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/main/recommendSlider.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/main/config.js"></script>
	<script>
		$(document).ajaxSend(function(e, xhr, opt){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");			
			xhr.setRequestHeader(header, token);
		});
		
		$(document).ready(function () {
		    recommendStore();
		});
		
		function recommendStore() {
			const userId = ${userId}
			
			if (userId === 0) {
				loadTopRatedStoresByScore(); // 다른 함수 실행
		        return; // recommendStore 실행 중단
		    }
			
			$.ajax({
				type : 'POST',
				url : CONFIG.API_BASE_URL + '/recommend',
				headers : {
					"Content-Type" : "application/json"
				},
				data : JSON.stringify({ "userId": userId }),
				success : function (result) {
					console.log("회원 카테고리 추천순 : ", result);
					
					const pattern = /(?<rank>\d+)\.\s(?<storeName>.+?)\s\(ID:\s(?<storeId>\d+),\s전화번호:\s(?<storePhone>\d+),\s코멘트:\s(?<storeComment>[\s\S]+?),\s주소:\s(?<storeRoadAddress>.+?),\s상세 주소:\s(?<storeDetailAddress>.+?),\s이미지:\s(?<storeImageExtension>\w+),\s유사도 점수:\s(?<similarityScore>\d+\.\d+)\)/g;
					
					const stores = [];
					let match;
					while ((match = pattern.exec(result)) !== null) {
					    stores.push({
					        storeName: match.groups.storeName.trim(),
					        storeId: Number(match.groups.storeId),
					        storePhone: match.groups.storePhone.trim(),
					        storeComment: match.groups.storeComment.trim(),
					        storeRoadAddress: match.groups.storeRoadAddress,
					        storeDetailAddress: match.groups.storeDetailAddress,
					        storeImageExtension: match.groups.storeImageExtension,
					        similarityScore: match.groups.similarityScore ? parseFloat(match.groups.similarityScore) : null
					    });
					}
					console.log(stores);
					
					let html = ''; // HTML을 담을 변수 초기화
		            stores.forEach(store => {
		                html += '<div class="best_item">'
		                    + '<div id="best_store_img">'
		                    + '<img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">'
		                    + '</div>'
		                    + '<p id="best_store_represent"></p>'
		                    + '<p id="best_store_name">' + store.storeName + '</p>'
		                    + '<div id="best_store_info">'
		                    + '<p id="store_info_tel">' + store.storePhone + '</p>'
		                    + '<p id="store_info_address">' + store.storeRoadAddress 
		                    + (store.storeDetailAddress ? store.storeDetailAddress : '') + '</p>'
		                    + '</div>'
		                    + '<pre id="best_store_comment">' + store.storeComment + '</pre>'
		                    + '</div>';
		            });

		            $("#best_box").html(html); // 동적으로 생성된 HTML을 삽입
		        },
				error: function(xhr, status, error) {
	                console.error("오류 발생:", status, error);
	            }
			});
			
		}
		
		function loadTopRatedStoresByScore() {
			$.ajax({
				type : 'GET',
				url : '/store/score',
				dataType: "json",
				success : function (stores) {
					console.log("가게 별점순 : ", stores);
					
					let html = ''; // HTML을 담을 변수 초기화
		            stores.forEach(store => {
		                html += '<div class="best_item">'
		                    + '<div id="best_store_img">'
		                    + '<img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">'
		                    + '</div>'
		                    + '<p id="best_store_represent"></p>'
		                    + '<p id="best_store_name">' + store.storeName + '</p>'
		                    + '<div id="best_store_info">'
		                    + '<p id="store_info_tel">' + store.storePhone + '</p>'
		                    + '<p id="store_info_address">' + store.storeAddressVO.roadAddress 
		                    + (store.storeAddressVO.detailAddress ? store.storeAddressVO.detailAddress : '') + '</p>'
		                    + '</div>'
		                    + '<pre id="best_store_comment">' + store.storeComment + '</pre>'
		                    + '</div>';
		            });

		            $("#best_box").html(html); // 동적으로 생성된 HTML을 삽입
		        },
				error: function(xhr, status, error) {
	                console.error("오류 발생:", status, error);
	            }
			});
		}
	</script>
	<title>Eat PlatForm</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />			
        <div id="container">
            <!-- 리뷰 추천 리스트 -->
            <div id="recommend_container">
                <!-- 리뷰 컨텐츠 -->
                <div id="recommend_box">
                    <div id="recommend_slider">

                        <div class="recommend_slide">
                            <div id="recommend_store">
                                <div id="recommend_store_img">
                                    <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">
                                </div>
                                <div id="recommend_store_ment">
                                    <p id="recommend_store_title">storeName</p>
                                    <p id="recommend_store_time">(00:00 ~ 00:00)</p>
                                    <p id="recomment_store_info">
                                        <span>#카테고리</span><br>
                                        <span>대표메뉴1</span><br>
                                        <span>대표메뉴2</span><br>
                                    </p>
                                </div>
                            </div>
                            <div id="recommend_review">
                                <div id="recommend_review_top_layer">
                                    <div id="recommend_user_layer">
                                        <img src="<%=request.getContextPath()%>/resources/img/common/user_icon.png" alt="사용자 프로필 이미지">
                                        <span>UserName</span>
                                    </div>
                                    <div id="recommend_info_layer">
                                        <div id="recommend_info_heart">
                                            <span>523</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fulllHeart.png" alt="하트 이미지">
                                        </div>
                                        <div id="recommend_info_star">
                                            <span>5</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star1" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star2" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star3" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star4" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star5" alt="별 이미지">
                                        </div>
                                    </div>
                                </div>
                                <div id="recommend_review_comment">
                                    <p>
                                        첫 번째 리뷰 내용이 적힌 곳입니다.<br><br>
                                        여기 넘무 맛있어서 스물마흔서른번 갔다나 뭐라나<br>
                                        아무튼 그런내용 한 500자 주저리 주저리<br>
                                    </p>
                                </div>
                            </div>
                        </div> <!-- .slide -->

                        <div class="recommend_slide">
                            <div id="recommend_store">
                                <div id="recommend_store_img">
                                    <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">
                                </div>
                                <div id="recommend_store_ment">
                                    <p id="recommend_store_title">storeName</p>
                                    <p id="recommend_store_time">(00:00 ~ 00:00)</p>
                                    <p id="recomment_store_info">
                                        <span>#카테고리</span><br>
                                        <span>대표메뉴1</span><br>
                                        <span>대표메뉴2</span><br>
                                    </p>
                                </div>
                            </div>
                            <div id="recommend_review">
                                <div id="recommend_review_top_layer">
                                    <div id="recommend_user_layer">
                                        <img src="<%=request.getContextPath()%>/resources/img/common/user_icon.png" alt="사용자 프로필 이미지">
                                        <span>UserName</span>
                                    </div>
                                    <div id="recommend_info_layer">
                                        <div id="recommend_info_heart">
                                            <span>523</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fulllHeart.png" alt="하트 이미지">
                                        </div>
                                        <div id="recommend_info_star">
                                            <span>5</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star1" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star2" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star3" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star4" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star5" alt="별 이미지">
                                        </div>
                                    </div>
                                </div>
                                <div id="recommend_review_comment">
                                    <p>
                                        두 번째 리뷰 내용이 적힌 곳입니다.<br><br>
                                        여기 넘무 맛있어서 스물마흔서른번 갔다나 뭐라나<br>
                                        아무튼 그런내용 한 500자 주저리 주저리
                                    </p>
                                </div>
                            </div>
                        </div> <!-- .slide -->

                        <div class="recommend_slide">
                            <div id="recommend_store">
                                <div id="recommend_store_img">
                                    <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">
                                </div>
                                <div id="recommend_store_ment">
                                    <p id="recommend_store_title">storeName</p>
                                    <p id="recommend_store_time">(00:00 ~ 00:00)</p>
                                    <p id="recomment_store_info">
                                        <span>#카테고리</span><br>
                                        <span>대표메뉴1</span><br>
                                        <span>대표메뉴2</span><br>
                                    </p>
                                </div>
                            </div>
                            <div id="recommend_review">
                                <div id="recommend_review_top_layer">
                                    <div id="recommend_user_layer">
                                        <img src="<%=request.getContextPath()%>/resources/img/common/user_icon.png" alt="사용자 프로필 이미지">
                                        <span>UserName</span>
                                    </div>
                                    <div id="recommend_info_layer">
                                        <div id="recommend_info_heart">
                                            <span>523</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fulllHeart.png" alt="하트 이미지">
                                        </div>
                                        <div id="recommend_info_star">
                                            <span>5</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star1" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star2" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star3" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star4" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star5" alt="별 이미지">
                                        </div>
                                    </div>
                                </div>
                                <div id="recommend_review_comment">
                                    <p>
                                        세 번째 리뷰 내용이 적힌 곳입니다.<br><br>
                                        여기 넘무 맛있어서 스물마흔서른번 갔다나 뭐라나<br>
                                        아무튼 그런내용 한 500자 주저리 주저리
                                    </p>
                                </div>
                            </div>
                        </div> <!-- .slide -->

                        <div class="recommend_slide">
                            <div id="recommend_store">
                                <div id="recommend_store_img">
                                    <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">
                                </div>
                                <div id="recommend_store_ment">
                                    <p id="recommend_store_title">storeName</p>
                                    <p id="recommend_store_time">(00:00 ~ 00:00)</p>
                                    <p id="recomment_store_info">
                                        <span>#카테고리</span><br>
                                        <span>대표메뉴1</span><br>
                                        <span>대표메뉴2</span><br>
                                    </p>
                                </div>
                            </div>
                            <div id="recommend_review">
                                <div id="recommend_review_top_layer">
                                    <div id="recommend_user_layer">
                                        <img src="<%=request.getContextPath()%>/resources/img/common/user_icon.png" alt="사용자 프로필 이미지">
                                        <span>UserName</span>
                                    </div>
                                    <div id="recommend_info_layer">
                                        <div id="recommend_info_heart">
                                            <span>523</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fulllHeart.png" alt="하트 이미지">
                                        </div>
                                        <div id="recommend_info_star">
                                            <span>5</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star1" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star2" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star3" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star4" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star5" alt="별 이미지">
                                        </div>
                                    </div>
                                </div>
                                <div id="recommend_review_comment">
                                    <p>
                                        네 번째 리뷰 내용이 적힌 곳입니다.<br><br>
                                        여기 넘무 맛있어서 스물마흔서른번 갔다나 뭐라나<br>
                                        아무튼 그런내용 한 500자 주저리 주저리
                                    </p>
                                </div>
                            </div>
                        </div> <!-- .slide -->

                        <div class="recommend_slide">
                            <div id="recommend_store">
                                <div id="recommend_store_img">
                                    <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="추천 가게 사진">
                                </div>
                                <div id="recommend_store_ment">
                                    <p id="recommend_store_title">storeName</p>
                                    <p id="recommend_store_time">(00:00 ~ 00:00)</p>
                                    <p id="recomment_store_info">
                                        <span>#카테고리</span><br>
                                        <span>대표메뉴1</span><br>
                                        <span>대표메뉴2</span><br>
                                    </p>
                                </div>
                            </div>
                            <div id="recommend_review">
                                <div id="recommend_review_top_layer">
                                    <div id="recommend_user_layer">
                                        <img src="<%=request.getContextPath()%>/resources/img/common/user_icon.png" alt="사용자 프로필 이미지">
                                        <span>UserName</span>
                                    </div>
                                    <div id="recommend_info_layer">
                                        <div id="recommend_info_heart">
                                            <span>523</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fulllHeart.png" alt="하트 이미지">
                                        </div>
                                        <div id="recommend_info_star">
                                            <span>5</span>
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star1" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star2" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star3" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star4" alt="별 이미지">
                                            <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" class="star5" alt="별 이미지">
                                        </div>
                                    </div>
                                </div>
                                <div id="recommend_review_comment">
                                    <p>
                                        다섯 번째 리뷰 내용이 적힌 곳입니다.<br><br>
                                        여기 넘무 맛있어서 스물마흔서른번 갔다나 뭐라나<br>
                                        아무튼 그런내용 한 500자 주저리 주저리
                                    </p>
                                </div>
                            </div>
                        </div> <!-- .slide -->
                    </div> <!-- #slider -->
                </div>
                <!-- End 리뷰 컨텐츠 -->

                <!-- 슬라이드 도트 리모컨 -->
                <div id="slider_dots"></div>
            </div>

            <!-- End 리뷰 추천  리스트-->

            <!-- 맛집 별점 랭킹 -->
            <div id="best_container">
                <p>
                    <img src="<%=request.getContextPath()%>/resources/img/common/fullStar.png" alt="별 이미지">
                    만인이 선택한 BEST SELLER
                </p>
                <div id="best_box">
                
                </div>
            </div>
            <!-- End 맛집 별점 랭킹 -->

            <div id="category_container">
                <p>
                    <img src="<%=request.getContextPath()%>/resources/img/common/category_icon.png" alt="카테고리 아이콘">
                    카테고리 별 맛집 순위
                </p>
                <div id="category_box">
                    <ul id="category_list">
                        <li>#한식</li>
                        <li>#일식</li>
                        <li>#중식</li>
                    </ul>
                    <div class="category_item">
                        <div>
                            <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="카테고리별 맛집 사진">
                        </div>
                        <p>storeName</p>
                    </div> <!-- End Category Item -->
                    <div class="category_item">
                        <div>
                            <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="카테고리별 맛집 사진">
                        </div>
                        <p>storeName</p>
                    </div> <!-- End Category Item -->
                    <div class="category_item">
                        <div>
                            <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="카테고리별 맛집 사진">
                        </div>
                        <p>storeName</p>
                    </div> <!-- End Category Item -->
                    <div class="category_item">
                        <div>
                            <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="카테고리별 맛집 사진">
                        </div>
                        <p>storeName</p>
                    </div> <!-- End Category Item -->                    
                    <div class="category_item">
                        <div>
                            <img src="<%=request.getContextPath()%>/resources/img/main/foodSample.png" alt="카테고리별 맛집 사진">
                        </div>
                        <p>storeName</p>
                    </div> <!-- End Category Item -->
                </div> <!-- End Category Box-->
            </div> <!-- End Category Container-->
        </div> <!-- End Container -->
        
		<jsp:include page="/include/footer.jsp" />		
	</div> <!-- End Wrap -->
	<!-- 로그아웃 메시지 -->
	<div>
		<p style="display: none" id="message">${message }</p>
	</div>
</body>
</html>
