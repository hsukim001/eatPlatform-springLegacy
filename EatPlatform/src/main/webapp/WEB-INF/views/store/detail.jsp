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
	<script>
		$(function(){
			$('#reservBtnWrap input').click(function(){
				$('#calendar-wrap > div').fadeToggle(400);
				$('#calendar-wrap').slideToggle(500);
			});
		});
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
						<p>리뷰 (???)</p>
						<ul>
							<li>
								
							</li>
							<li></li>
							<li></li>
							<li></li>
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