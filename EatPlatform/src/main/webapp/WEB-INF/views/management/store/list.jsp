<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<style type="text/css">
			table, th, td {
				border-style: solid;
				border-width: 1px;
				text-align: center;
			}
			
			ul {
				list-style-type: none;
				text-align: center;
			}
			
			li {
				display: inline-block;
			}
		</style>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<script type="text/javascript">
			$(function() {
				$('.list_row').on('click', function() {
					let storeId = $(this).closest('li').data('id-value');
					location.href = "/management/store/detail?storeId=" + storeId;
				});
				
				$('.store_delete_btn').on('click', function() {
					let storeId = $(this).closest('li').data('id-value');
					console.log("storeId : " + storeId);
					if(confirm("선택하신 매장을 정말 삭제 하시겠습니까?")) {
						$.ajax({
							url : '/store/delete/' + storeId,
							type : 'delete',
							success : function(response) {
								if(response == 1) {
									alert("매장 삭제가 완료되었습니다.");
									location.reload(true);
								} else if(response == 0) {
									alert("매장 삭제에 실패하였습니다.");
								}
							},
							error : function() {
								alert("매장 삭제 진행 중 오류가 발생하였습니다.");
							}
						});
						
					}
				});
			});
		</script>
		<title>업장 목록</title>
	</head>
	<body>
		<div id="wrap">
			<jsp:include page="/include/header.jsp" />
			
			<div id="container">
				<jsp:include page="/include/myPageLeft.jsp"/>
				
				<h2>업장 목록</h2>
				<ul>
					<li>
						<div>번호</div>
						<div>식당명</div>
						<div>사업자명</div>
						<div>카테고리</div>
						<div>식당소개</div>
						<div>수정</div>
						<div>삭제</div>
					</li>
				</ul>
				<ul>
					<c:forEach var="list" items="${list }">
						<li data-id-value="${list.storeId }">
							<div class="list_row">
								<div>${list.storeId }</div>
								<div>${list.storeName }</div>
								<div>${list.ownerName }</div>
								<div>
					                <c:forEach var="categoryList" items="${categoryList}">
					                    <c:if test="${list.storeId eq categoryList.storeId}">
					                        ${categoryList.mainCategoryName}
					                    </c:if>
					                </c:forEach>
					            </div>
								<div>${list.storeComment }</div>						
							</div>
							<div class="btn_row">
								<a href="/store/updateStore?storeId=${list.storeId }" >수정</a>
								<div class="store_delete_btn">삭제</div>							
							</div>
						</li>
					</c:forEach>
				</ul>
					
				<ul>
					<!-- 이전 버튼 생성을 위한 조건문 -->
					<c:if test="${pageMaker.isPrev() }">
						<li><a href="list?pageNum=${pageMaker.startNum - 1}">이전</a></li>
					</c:if>
					<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
					<c:forEach begin="${pageMaker.startNum }"
						end="${pageMaker.endNum }" var="num">
						<li><a href="list?pageNum=${num }">${num }</a></li>
					</c:forEach>
					<!-- 다음 버튼 생성을 위한 조건문 -->
					<c:if test="${pageMaker.isNext() }">
						<li><a href="list?pageNum=${pageMaker.endNum + 1}">다음</a></li>
					</c:if>
					<li>
						<button onclick="location.href='/store/newStore'">매장 등록</button>					
					</li>
				</ul>
			
			</div>
		</div>
	</body>
</html>