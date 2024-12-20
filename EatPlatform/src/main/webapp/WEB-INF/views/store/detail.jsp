<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/store/detail.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script
	src="<%=request.getContextPath()%>/resources/js/headerFooterEmptySpaceController.js"></script>

<title>${storeVO.storeName }</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<div id="storeContainer">
				<p class="storeTitle width100">${storeVO.storeName }</p>
				<div id="storeInfoBox">
					<div id="storeInfoImg">
						<img src="<%=request.getContextPath()%>/resources/img/sample2.png"
							alt="sample">
					</div>
					<div id="storeInfoText">
						<div id="storeInfoTextLeft">
							<p>${storeVO.businessHour }</p>
							<p>${storeVO.storePhone }</p>
							<p>${storeVO.ownerName }</p>
							<p>${storeVO.storeRegDate }</p>
						</div>
						<div id="storeInfoTextRight">
							<p>5</p>
							<p>345</p>
						</div>
					</div>
				</div>
				<input type="button" id="reservBtn"
					onclick="location.href='../reserv/register?storeId=${storeVO.storeId }'"
					value="온라인 예약">
				<div id="remoteBar">
					<ul>
						<li><a href="#"> 매장 소개 </a></li>
						<li><a href="#"> 메뉴 </a></li>
						<li><a href="#"> 리뷰 </a></li>
					</ul>
				</div> <!--  End RemoteBar -->
			</div> <!--  End StoreContainer -->
		</div> <!--  End Container -->









		<c:forEach var="menu" items="${menuVO}">
			${menu.menuName } <br>
			${menu.menuPrice } <br>
			${menu.menuComment } <br>
			${menu.represent } <br>
		</c:forEach>

	</div> <!--  End Wrap -->
</body>
</html>