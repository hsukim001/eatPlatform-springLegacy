<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>사업자 등록 요청 상세</title>
	</head>
	<body>
		<div>
			<input type="hidden" id="businessRequestId" name="businessRequestId" value="${info.businessRequestId }">
			<input type="hidden" id="storeId" name="storeId" value="${info.storeId }">
		</div>
		<div>
			<p>식당명 : ${info.storeName }</p>
			<p>사업자명 : ${info.ownerName }</p>
			<p>전화번호 : ${info.storePhone }</p>
			<p>카테고리 : ${info.foodCategory }</p>
			<p>영업 시간 : ${info.businessHour }</p>
			<p>간단 소개 : ${info.storeComment }</p>
			<p>상세 설명 : ${info.description }</p>
			<p>도로명 주소 : ${info.roadAddress }</p>
			<p>지번 주소 : ${info.jibunAddress }</p>
		</div>
		<div>
			<button>승인</button>
			<button>거부</button>
			<button>목록</button>
		</div>
	</body>
</html>