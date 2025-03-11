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
	<script src="<%=request.getContextPath()%>/resources/js/management/schedule.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/management/menu.js"></script>
	
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
					<input type="button" id="reservBtn" value="일정 확인">	
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
    						<jsp:include page="/include/modal/reservCancelInfo.jsp" />
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