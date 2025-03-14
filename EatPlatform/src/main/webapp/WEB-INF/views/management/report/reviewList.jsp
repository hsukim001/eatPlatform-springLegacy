<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
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
	<script src="https://code.jquery.com/jquery-latest.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/common/listSearch.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/management/reviewReport.js"></script>
	<script type="text/javascript">
		$(document).ajaxSend(function(e, xhr, opt){
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");	       
			xhr.setRequestHeader(header, token);
		});
		$(function(){
		
		});
	</script>
</head>
<body>
	<div id="wrap">
		<jsp:include page="/include/header.jsp" />
		<div id="container">
			<jsp:include page="/include/myPageLeft.jsp"/>
			<h2>리뷰 신고 목록</h2>
			
			<p>총 : ${pageMaker.totalCount }건</p>
			<table>
				<thead>
					<tr>
						<th>번호</th>
						<th>신고유형</th>
						<th>신고자 명</th>
						<th>신고 등록일</th>
					</tr>
				</thead>
				<tbody id="tableBody">
					<c:forEach var="list" items="${list }">
						<tr data-id-value="${list.reviewReportListId }">
							<td>${list.reviewReportListId }</td>
							<td>${list.reviewReportMessage }</td>
							<td>${list.name }</td>
							<td><fmt:formatDate value="${list.reviewReportDate}" pattern="yyyy-MM-dd" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<ul>
				<!-- 이전 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isPrev() }">
					<li><a href="list?pageNum=${pageMaker.startNum - 1}">이전</a></li>
				</c:if>
				<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
				<c:forEach begin="${pageMaker.startNum }"
					end="${pageMaker.endNum }" var="num">
					<li>${num }</li>
				</c:forEach>
				<!-- 다음 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isNext() }">
					<li><a href="list?pageNum=${pageMaker.endNum + 1}">다음</a></li>
				</c:if>
			</ul>
		</div>
	</div>
</body>

</html>