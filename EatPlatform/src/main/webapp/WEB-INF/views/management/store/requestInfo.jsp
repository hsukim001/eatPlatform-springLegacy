<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<script type="text/javascript">
		
			// ajax CSRF 토큰
			$(document).ajaxSend(function(e, xhr, opt){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");			
				xhr.setRequestHeader(header, token);
			});
		
			$(function() {
				let businessRequestId = $('#businessRequestId').val();
				let storeId = $('#storeId').val();
				let buttonType;
				
				// 요청 승인 버튼 이벤트리스너
				$('#approvalBtn').on('click', function(){
					approval();
				});
				
				// 요청 거부 버튼 이벤트리스너
				$('#deniedBtn').on('click', function(){
					buttonType = 1;
					denialManagement();
				}); // end refusalBtn
				
				// 요청 취소 버튼 이벤트리스너
				$('#requestCancelBtn').on('click', function(){
					buttonType = 2;
					denialManagement();
				}); // end requestCancelBtn
				
				// 요청 승인 함수
				function approval() {
					$.ajax({
						url : "/approval/store",
						type : 'PUT',
						headers : {
							"Content-Type" : "application/json"
						},
						data : JSON.stringify({
							"storeId" : storeId
						}),
						success : function(response) {
							if(response == 1) {
								alert("승인 완료 되었습니다.");
								location.href="list";
							} else {
								alert("가게 등록 요청에 대한 승인에 실패하였습니다.");
							}
						},
						error : function() {
							alert("승인 과정 중에 오류가 발생하였습니다.");
						}
					});
				}
								
				// 요청 거부 함수
				function denialManagement() {
					$.ajax({
						url : "/approval/denialManagement/" + storeId,
						type : 'put',
						success : function(response) {
							if(response == 1) {
								if(buttonType == 1) {
									alert("등록 거부가 완료되었습니다.");
									location.href="/management/store/requestList";
								} else if(buttonType == 2) {
									alert("가게 등록 요청이 취소되었습니다.");
									location.href="../../user/detail"
								}
							} else {
								if(buttonType == 1) {
									alert("등록 거부에 실패하였습니다.");									
								} else if(buttonType == 2) {
									alert("가게 등록 요청에 대한 취소가 실패하였습니다.");
								}
							}
						},
						error : function() {
							if(buttonType == 1) {
								alert("승인 거부중 오류가 발생하였습니다.");								
							} else if(buttonType == 2) {
								alert("가게 등록 요청 취소 중 오류가 발생하였습니다.");
							}
						}
					});
				} // end refusal()
				
			}); // end function
		</script>
		<title>가게 등록 요청 정보</title>
	</head>
	<body>
		<div id="wrap">
			<jsp:include page="/include/header.jsp" />
			<fmt:formatDate value="${info.storeApprovalsRegDate }" pattern="yyyy-MM-dd" var="createdDate" />
			
			<h2>가게 등록 요청 정보</h2>
			<div>
				<p>요청자 아이디 : ${info.storeUserId }</p>
				<p>이메일 : ${info.email }</p>
				<p>요청자 연락처 : ${info.phone }</p>
				<p>매장명 : ${info.storeName }</p>
				<p>신청일 : ${createdDate }</p>
				<p>매니저명 : ${info.ownerName }</p>
				<p>매장 연락처 : ${info.storePhone }</p>
				<p>영업 시간 : ${info.businessHour }</p>
				<p>소개 : ${info.storeComment }</p>
				<p>상세 설명 : ${info.description }</p>
				<p>도로명 주소 : ${info.roadAddress }</p>
				<p>지번 주소 : ${info.jibunAddress }</p>
				<p>상세 주소 : ${info.detailAddress }</p>
				<c:choose>
					<c:when test="${info.approvals == 0 }">
						<p>상태 : 대기</p>						
					</c:when>
					<c:when test="${info.approvals == 1 }">
						<p>상태 : 승인</p>
					</c:when>
				</c:choose>
			</div>
			<div>
				<sec:authorize access="hasAuthority('ROLE_ADMIN')">
					<c:if test="${info.approvals == 0 }">
						<button id="approvalBtn" type="button">승인</button>
						<button id="deniedBtn" type="button">거부</button>						
					</c:if>
					<button onclick="location.href='/management/store/requestList?pageNum=${pageNum }'" >목록</button>
				</sec:authorize>
				<sec:authorize access="hasAuthority('ROLE_STORE')">
					<c:if test="${info.approvals == 0 }">
						<button id="requestCancelBtn" type="button">요청 취소</button>												
					</c:if>
				</sec:authorize>
				<div>
					<input type="hidden" id="storeId" name="storeId" value="${info.storeId }">
					<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
				</div>					
			</div>
		</div>
	</body>
</html>