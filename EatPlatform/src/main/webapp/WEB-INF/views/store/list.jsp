<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resources/store/css/list.css">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="container">
		<div id="list_box">
			<ul id="category_list">
				<li>한식</li>
				<li>일식</li>
				<li>중식</li>
			</ul>
			<div id="item_container">
				<c:if test="${not empty recentStores}">
					<c:forEach var="store" items="${recentStores}">
						<div id="item_box">
							<div class="img_box">
								<img
									src="<%=request.getContextPath()%>/resources/img/sample.png"
									alt="sample">
							</div>
							<div class="text_box">
								<p class="store_title">${store.storeName}</p>
								<p class="store_tag">
									<span>#${store.foodCategory}</span>
								</p>
								<p class="store_comment">${store.storeComment}</p>
								<a href="../reserv/register?storeId=${store.storeId }">
									<button>예약</button>
								</a>
							</div>
						</div>
					</c:forEach>
				</c:if>
			</div>
			<c:if test="${empty recentStores}">
				<p>최근 매장이 없습니다.</p>
			</c:if>

			<div class="pagination">
				<c:set var="currentPage" value="${currentPage}" />
				<c:set var="totalPages" value="${totalPages}" />

				<!-- 이전 버튼 -->
				<c:choose>
					<c:when test="${currentPage > 3}">
						<a href="?pageNum=${currentPage - 3}">이전</a>
						<!-- 3페이지 이상이면 3페이지 뒤로 이동 -->
					</c:when>
					<c:otherwise>
						<a href="?pageNum=1">이전</a>
						<!-- 3페이지 미만이면 첫 페이지로 이동 -->
					</c:otherwise>
				</c:choose>

				<!-- 페이지 번호 출력 (현재 페이지 기준으로 앞뒤 2개 페이지) -->
				<c:forEach var="i" begin="1" end="${totalPages}" varStatus="status">
					<c:choose>
						<c:when test="${i == currentPage}">
							<a href="?pageNum=${i}" class="current">${i}</a>
							<!-- 현재 페이지 강조 -->
						</c:when>
						<c:when test="${i >= currentPage - 2 && i <= currentPage + 2}">
							<a href="?pageNum=${i}">${i}</a>
							<!-- 현재 페이지를 기준으로 앞뒤 2개 페이지 -->
						</c:when>
						<c:otherwise>
							<!-- 해당 범위 외의 페이지는 숨김 -->
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<!-- 다음 버튼 -->
				<c:choose>
					<c:when test="${currentPage < totalPages - 2}">
						<a href="?pageNum=${currentPage + 3}">다음</a>
						<!-- 3페이지 이상이면 3페이지 앞으로 이동 -->
					</c:when>
					<c:otherwise>
						<a href="?pageNum=${totalPages}">다음</a>
						<!-- 3페이지 미만이면 마지막 페이지로 이동 -->
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>