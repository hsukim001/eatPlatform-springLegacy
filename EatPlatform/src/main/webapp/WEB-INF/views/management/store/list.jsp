<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/management/store/list.css">
<script src="https://code.jquery.com/jquery-latest.min.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
<script type="text/javascript">
$(function() {
	$('.list_row li').on('click', function() {
	    if ($(this).hasClass('not_detail')) {
	        return;
	    }

	    let storeId = $(this).closest('ul').data('id-value');
	    location.href = "/management/store/detail?storeId=" + storeId;
	});


	
	$('.store_delete_btn').on('click', function() {
		let storeId = $(this).parent('ul').data('id-value');
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
				<p class="list_title">업장 목록</p>
				<div id="table_container">
					<ul id="tableHead">
						<li>번호</li>
						<li>식당명</li>
						<li>사업자명</li>
						<li>카테고리</li>
						<li>수정</li>
						<li>삭제</li>
					</ul>
					
					<div id="tableBody">
						<c:forEach var="list" items="${list }">
							<ul class="list_row" data-id-value="${list.storeId }">
								<li>${list.storeId }</li>
								<li>
									<c:choose>
								        <c:when test="${list.approvals == 0}">
								            <span class="wait">승인 대기 중</span><br>
								        </c:when>
								        <c:when test="${list.approvals == 2}">
								            <span class="refuse">승인 거절</span><br>
								        </c:when>
								    </c:choose>
									${list.storeName }
								</li>
								<li>${list.ownerName }</li>
								<li>
					                <c:forEach var="categoryList" items="${categoryList}">
					                    <c:if test="${list.storeId eq categoryList.storeId}">
					                        ${categoryList.mainCategoryName}
					                    </c:if>
					                </c:forEach>
							    </li>
								<li class="not_detail"><a class="link_block" href="/store/updateStore?storeId=${list.storeId }" ><i class="fas fa-cogs"></i></a></li>
								<li class="store_delete_btn not_detail"><i class="fas fa-trash-alt"></i></li>
							</ul>
						</c:forEach>
					</div>
					<div id="pagination">
						<c:forEach begin="${pageMaker.startNum }"
							end="${pageMaker.endNum }" var="num">
							<a href="list?pageNum=${num }">${num }</a>
						</c:forEach>
					</div>	
				</div>
				
				<button onclick="location.href='/store/newStore'">신규 매장 등록 신청</button>
			</div>
			<jsp:include page="/include/footer.jsp" />	
		</div>
	</body>
</html>