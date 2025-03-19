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
		<title>사업자 등록 요청</title>
	</head>
	<body>
		<div id="wrap">
			<jsp:include page="/include/header.jsp" />
			
			<h2>사업자 등록 요청 목록</h2>
			<table>
				<thead>
					<tr>
						<th>번호</th>
						<th>아이디</th>
						<th>이름</th>
						<th>신청일</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="list" items="${list }">
						<tr>
							<td>${list.businessRequestId }</td>
							<td><a href="requestInfo?businessRequestId=${list.businessRequestId }">${list.userId }</a></td>
							<td>${list.name }</td>
							<fmt:formatDate value="${list.businessReqeustUpdateDate }" pattern="yyyy-MM-dd" var="updateDate" />
							<td>${updateDate }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<ul>
				<!-- 이전 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isPrev() }">
					<li><a href="requestList?pageNum=${pageMaker.startNum - 1}">이전</a></li>
				</c:if>
				<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
				<c:forEach begin="${pageMaker.startNum }"
					end="${pageMaker.endNum }" var="num">
					<li><a href="requestList?pageNum=${num }">${num }</a></li>
				</c:forEach>
				<!-- 다음 버튼 생성을 위한 조건문 -->
				<c:if test="${pageMaker.isNext() }">
					<li><a href="requestList?pageNum=${pageMaker.endNum + 1}">다음</a></li>
				</c:if>
			</ul>
			
		</div>
	</body>
</html>