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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/detail.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/calendar.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/page/image.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/calendar.css">
	<style>
		##menuContainer ul li {
			height: 200px !important;
		}
		##menuContainer ul li img {
			height: 200px;
		}
		.bottom_layer {
			width: calc(100% - 195px);
			float: left;
			display: flex;
			justify-content: space-between;
		}
		.bottom_layer .menuText {
			margin: 0;
			width: auto !important;
		}
		.createdMenuBtn {
			width: 90%;
		    margin: 0 auto 20px;
		    background: #1b47b3;
		    color: #fff;
		    font-size: 30px;
		    font-weight: bold;
		    border: none;
		    border-radius: 10px;
		    letter-spacing: 16px;
		    padding: 5px 0;
		    cursor: pointer;
		    transition: 0.5s;
		}
	</style>
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/priceSeparate.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/management/ReviewReplyAPI.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/management/calendar.js"></script>
	<script type="text/javascript">
		$(function(){
			let menuId;
			let modalMenuId;
			
			// ajax CSRF 토큰
			$(document).ajaxSend(function(e, xhr, opt){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");			
				xhr.setRequestHeader(header, token);
			}); // end ajaxSend
			
			$('#reservBtn').click(function(){
				fetch('../../access/auth/status', { method : 'get', credentials: 'include' })
					.then(response => response.json())
					.then(isAuthenticated => {
						if(isAuthenticated) {
							$('#calendar-wrap > div').stop().fadeToggle(400);
							$('#calendar-wrap').stop().slideToggle(500);							
						} else {
							alert('로그인이 필요합니다.');
						}
					})
					.catch(error => {
						console.error("Error checking authentication status:", error);
					});
			}); // End #reservBtnWrap input.click
			
			// 예약 모달창 닫기 버튼 이벤트 리스너
			$('#reservScheduleModal #topCloseBtn, #reservScheduleModal #bottomCloseBtn').click(function() {
				$('#reservScheduleModal').hide();
			}); // end reservScheduleModal closeBtn click
			
			// 메뉴 삭제 버튼 이벤트 리스너
			$(document).on('click', '.deleteMenuBtn', function() {
				
				let isDelte = confirm("메뉴를 정말로 삭제하시겠습니까?");
				if(isDelte) {
					menuId = $(this).data("id-value");
					console.log("true");
					deleteMenu();
				}
				
			}); // end deleteMenuBtn click
			
			// 메뉴 삭제 함수
			function deleteMenu() {
				$.ajax({
					url : '/store/menu/delete/' + menuId,
					type : 'delete',
					headers : {
						"Content-Type" : "application/json"
					},
					success : function(response){
						if(response == 1) {
							menuId = "";
							alert("삭제가 완료되었습니다.");
							location.reload(true);
						} else {
							menuId = "";
							alert("삭제에 실패하였습니다.");
						}
					},
					errors : function(){
						menuId = "";
						alert("삭제를 진행중 오류가 발생하였습니다.");
					}
				});
			} // end deleteMenu
			
			// 메뉴 등록 버튼 이벤트 리스너
			$('#showMenuModal').click(function() {
				console.log("hoho");
				$('#menuCreatedModal').show();
			}); // end menuModal open click
			
			// 메뉴 모달창 닫기 버튼 이벤트 리스너
			$('#menuCreatedModal #topCloseBtn, #menuCreatedModal #bottomCloseBtn' ).click(function() {
				$('#menuCreatedModal').hide();
			}); // end menuCreatedModal closeBtn click
			
			// 대표 메뉴 체크 박스 이벤트 리스너
			$('#represent, #updateRepresent').on('click', function(){
				//let representCount = $('#represent').data("count-value");
				let representCount = $(this).data("count-value");
				console.log("대표 메뉴 갯수 : " + representCount);
				if(representCount >= 2) {
					alert("대표 메뉴는 2 종류까지 설정할 수 있습니다.");
					$('#represent').prop('checked', false);
					return false;
				}
			}); // end represent click
			
			// 메뉴 모달창 등록 버튼 이베트 리스너
			$('#createdMenuBtn').click(function() {
				createdMenu();
			}); // end createdMenuBtn click
			
			// 메뉴 등록 함수
			function createdMenu() {
				let represent;
				
				if ($("#represent").prop("checked")) {
				    console.log("체크됨");
				    represent = 1;
				} else {
				    console.log("체크 안 됨");
				    represent = 0;
				}

				
				let obj = {
						"storeId" : $('#storeId').val(),
						"menuName" : $('#menuName').val(),
						"menuPrice" : $('#menuPrice').val(),
						"menuComment" : $('#menuComment').val(),
						"represent" : represent
				};
				
				$.ajax({
					url : '/store/menu/created',
					type : 'post',
					headers : {
						"Content-Type" : "application/json"
					},
					data : JSON.stringify(obj),
					success : function(response){
						if(response == 1) {
							alert("메뉴 등록이 완료되었습니다.");
							location.reload(true);
						} else {
							alert("메뉴 등록에 실패하였습니다.");
							$('#menuCreatedModal').hide();
						}
					},
					error : function(){
						alert("메뉴 등록중에 오류가 발생하였습니다.");
						$('#menuCreatedModal').hide();
					}
				});
			} // end createdMenu
			
			// 수정 모달창 닫기 버튼 이벤트 리스너
			$('#menuUpdateModal #topCloseBtn, #menuUpdateModal #bottomCloseBtn').click(function(){
				$('#menuUpdateModal').hide();
			}); // end updateMenuModal close click event
			
			$('.updateMenuBtn').on('click', function() {
				menuId = $(this).data('id-value');
				searchMenuInfo();
			}); // end updateMenuBtn open click
			
			function searchMenuInfo() {
				$.ajax({
					url : '/store/menu/search/menu/' + menuId,
					type : 'get',
					headers : {
						"Content-Type" : "application/json"
					},
					success : function(response) {
						$('#updateMenuName').val(response.menuName);
						$('#updateMenuPrice').val(response.menuPrice);
						$('#updateMenuComment').val(response.menuComment);
						$('#modalMenuName').text(response.menuName);
						modalMenuId = response.menuId;
						
						if(response.represent == 1) {
							$("#updateRepresent").prop("checked", true);							
						} else {
							$("#updateRepresent").prop("checked", false);	
						}
						
						$('#menuUpdateModal').show();
						menuId = "";
						
					},
					error : function() {
						alert("모달창 호출중에 오류가 발생하였습니다.");
						menuId = "";
						$('#menuUpdateModal').hide();
					}
				});
			} // end searchMenuInfo
			
			// 메뉴 등록 버튼 이벤트 리스너
			$('#updateMenuBtn').click(function() {
				updateMenu();
			}); // end updateMenuBtn click event
			
			// 메뉴 수정 함수
			function updateMenu() {
				let represent;
				
				if ($("#updateRepresent").prop("checked")) {
				    console.log("체크됨");
				    represent = 1;
				} else {
				    console.log("체크 안 됨");
				    represent = 0;
				}

				
				let obj = {
						"menuId" : modalMenuId,
						"storeId" : $('#storeId').val(),
						"menuName" : $('#updateMenuName').val(),
						"menuPrice" : $('#updateMenuPrice').val(),
						"menuComment" : $('#updateMenuComment').val(),
						"represent" : represent
				};
				
				$.ajax({
					url : '/store/menu/update',
					type : 'put',
					headers : {
						"Content-Type" : "application/json"
					},
					data : JSON.stringify(obj),
					success : function(response) {
						if(response == 1) {
							alert("메뉴 수정이 완료되었습니다.");
							location.reload(true);
						} else {
							alert("메뉴 수정에 실패하였습니다.");
							modalMenuId = "";
						}
					},
					error : function() {
						alert("메뉴 수정중 오류가 발생하였습니다.");
						modalMenuId = "";
					}
				});
			} // end updateMenu
			
		});
	</script>
	
	<title>${storeInfo.storeName }</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
		<jsp:include page="/include/myPageLeft.jsp"/>
			<div id="storeContainer">
				<p class="storeTitle width100 textLeft mb20 bold">${storeInfo.storeName }</p>
				<div id="storeInfoBox" class="mb30">
					<div id="storeInfoImg">
						<img src="<%=request.getContextPath()%>/resources/img/sample2.png" alt="sample">
					</div>
					<div id="storeInfoText">
						<ul>
							<li>
								<span class="textTitle">영업 시간 </span> 
								<span class="colon">:</span> 
								<span class="textValue">${storeInfo.businessHour }</span>
							</li>
							<li>
								<span class="textTitle">연락처 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeInfo.storePhone }</span></li>
							<li>
								<span class="textTitle">대표명 </span>
								<span class="colon">:</span>
								<span class="textValue">${storeInfo.ownerName }</span></li>
							<li>
								<span class="textTitle">최근 등록일 </span> 
								<span class="colon">:</span> 
								<span class="textValue">${storeInfo.storeUpdateDate }</span>
							</li>
							<li>
								<span class="textTitle">별점 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeInfo.storeUpdateDate }</span></li>
							<li>
								<span class="textTitle">추천 수 </span> 
								<span class="colon">:</span>
								<span class="textValue">${storeInfo.storeUpdateDate }</span></li>
						</ul>
					</div>
				</div>
				<!-- end storeInfoBox -->
				
				<div id="reservBtnWrap" class="width100 mb30">
					<input type="button" id="reservBtn" value="예약 확인">	
					<jsp:include page="/include/scheduleCalendar.jsp" />
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
						<p class="descriptionTitle mb20">매장 소개</p>
					    <c:if test="${empty storeInfo.description}">
					        <pre class="descriptionContent">작성된 소개글이 없습니다.</pre>
					    </c:if>
					   
				    	<c:if test="${not empty storeInfo.description}">
					        <pre class="descriptionContent">${storeInfo.description }</pre>
					    </c:if>
					</div>
					<!-- End storeContentDescription -->
					
					<div id="menuContainer">
						<ul class="width100">
						    <c:if test="${empty menuInfo}">
					        	<pre class="descriptionContent">등록된 메뉴가 없습니다.</pre>
					   		</c:if>
							<c:forEach var="menu" items="${menuInfo}" varStatus="status">
								<li>
									<img src="<%=request.getContextPath()%>/resources/img/sample3.png" alt="메뉴사진 ${status.index + 1 }">
									<p class="menuTitle menuText">${menu.menuName }</p>
									<p class="menuComment menuText">${menu.menuComment }</p>
									<input type="hidden" class="menuRepresent" value="${menu.represent }">
									<div class="bottom_layer">
										<div>
											<button class="updateMenuBtn" data-id-value="${menu.menuId }">수정</button>
											<button class="deleteMenuBtn" data-id-value="${menu.menuId }">삭제</button>
										</div>
										<p class="menuPrice menuText">${menu.menuPrice } \</p>
									</div>
								</li>
							</c:forEach>
						</ul>
					</div>
					<div>
						<button class="createdMenuBtn" id="showMenuModal">메뉴 등록</button>
					</div>
					<!--  End MenuConteiner -->
					
					<div id="reviewContainer">
						<sec:authorize access="isAuthenticated()">
							<p style="display: none;"><span id="loginId"><sec:authentication property="principal.user.username" /></span></p>
						</sec:authorize>
						<p>
							<input id="reviewBtn" type="button" value="리뷰 확인">
						</p>
						
						<input type="hidden" id="storeId" value="${storeInfo.storeId }">
						<input type="hidden" id="storeUser" value="${storeInfo.storeUserId }">
						
						<div style="text-align: center;">
							<div id="reviews">
							</div>
							<br>
							<!-- 더보기 버튼 추가 -->
							<button id="loadMoreBtn">더보기</button>
						</div>
							
							<!-- 모달 포함 -->
    						<jsp:include page="/include/modal/reservSchedule.jsp" />
    						<jsp:include page="/include/modal/menuCreated.jsp" />
    						<jsp:include page="/include/modal/menuUpdate.jsp" />
						<ul>

						</ul>
					</div>
					<!-- End reviewContainer -->
				
				</div>
				<!-- end storeContent -->
			</div>
			<!-- end storeContiner -->
		</div>	
		<!-- end container -->
	</div>
	<!-- end wrap -->
</body>
</html>