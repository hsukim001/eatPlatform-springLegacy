<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<table>
	<thead>
		<tr>
			<th>번호</th>
			<th>식당명</th>
			<th>예약 일자</th>
			<th>예약 등록일</th>
		</tr>
	</thead>
	<tbody id="nextReserv">
		<c:forEach var="nextList" items="${nextList }">
			<tr>
				<td>${nextList.reservId }</td>
				<td>${nextList.storeName }</td>
				<td>${nextList.reservDate }${item.reservTime }</td>
				<td>${nextList.reservCreateDate }</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<ul id="nextPagination">
	<!-- 이전 버튼 생성을 위한 조건문 -->
	<c:if test="${pageMaker.isPrev() }">
		<li><a
			href="toDay/${sessionScope.userId }?pageNum=${pageMaker.startNum - 1}">이전</a></li>
	</c:if>
	<!-- 반복문으로 시작 번호부터 끝 번호까지 생성 -->
	<c:forEach begin="${pageMaker.startNum }" end="${pageMaker.endNum }"
		var="num">
		<li><a href="toDay/${sessionScope.userId }?pageNum=${num }">${num }</a></li>
	</c:forEach>
	<!-- 다음 버튼 생성을 위한 조건문 -->
	<c:if test="${pageMaker.isNext() }">
		<li><a
			href="toDay/${sessionScope.userId }?pageNum=${pageMaker.endNum + 1}">다음</a></li>
	</c:if>
</ul>