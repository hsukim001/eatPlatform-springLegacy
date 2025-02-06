<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/store/list.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
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
							<div id="item_box" onclick="location.href='detail?storeId=${store.storeId }'">
								<div class="img_box">
									<img src="<%=request.getContextPath()%>/resources/img/sample2.png" alt="sample">
								</div>
								<div class="text_box">
									<p class="store_title">${store.storeName}</p>
									<p class="store_tag">
										<span>#${store.foodCategory}</span>
									</p>
									<p class="store_comment">${store.storeComment}</p>
								</div>
								<a href="updateStore?storeId=${store.storeId }">
									<button>수정</button>
								</a>
								<a href="menu/newMenu?storeId=${store.storeId }">
									<button>메뉴등록</button>
								</a>
							</div>
						</c:forEach>
					</c:if>
				</div>
				<c:if test="${empty recentStores}">
					<c:if test="${empty keyword}">
						<p>최근 매장이 없습니다.</p>
					</c:if>
					<c:if test="${not empty keyword}">
						<p>검색 결과가 없습니다.</p>
					</c:if>
				</c:if>

				<div class="pagination">
					<c:set var="currentPage" value="${currentPage}" />
					<c:set var="totalPages" value="${totalPages}" />

					<c:choose>
						<c:when test="${currentPage > 3}">
							<a href="?pageNum=${currentPage - 3}&keyword=${keyword}">이전</a>
						</c:when>
						<c:otherwise>
							<a href="?pageNum=1&keyword=${keyword}">이전</a>
						</c:otherwise>
					</c:choose>

					<c:forEach var="i" begin="1" end="${totalPages}" varStatus="status">
						<c:choose>
							<c:when test="${i == currentPage}">
								<a href="?pageNum=${i}&keyword=${keyword}" class="current">${i}</a>
							</c:when>
							<c:when test="${i >= currentPage - 2 && i <= currentPage + 2}">
								<a href="?pageNum=${i}&keyword=${keyword}">${i}</a>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</c:forEach>

					<c:choose>
						<c:when test="${currentPage < totalPages - 2}">
							<a href="?pageNum=${currentPage + 3}&keyword=${keyword}">다음</a>
						</c:when>
						<c:otherwise>
							<a href="?pageNum=${totalPages}&keyword=${keyword}">다음</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</body>
</html>