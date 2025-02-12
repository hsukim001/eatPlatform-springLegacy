<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
		<title>가입 정보</title>
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/reset.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/common.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/myPageLeft.css">
		<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/user/detail.css">
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script src="<%=request.getContextPath()%>/resources/js/common/headerFooterEmptySpaceController.js"></script>
		<script type="text/javascript">
		
			// 전화번호 정규 표현식
			const autoHyphen = (target) => {
				target.value = target.value
					.replace(/[^0-9]/g, '')
			   		.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
			}
			
			// ajax CSRF 토큰
			$(document).ajaxSend(function(e, xhr, opt){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				
				xhr.setRequestHeader(header, token);
			});
		
			$(document).ready(function(){
				
				// 회원 탈퇴 버튼 액션
				$('#memWithdrawal').click(function(){
					let isWithdrawal = confirm('회원탈퇴를 진행 하시겠습니까?');
					if(isWithdrawal == true) {
						withdrawal();
					}
				});
				
				function withdrawal() {					
					$.ajax({
						url : 'withdrawal',
						type : 'put',
						headers : {
							"Content-Type" : "application/json"
						},
						success : function(response){
							if(response.result == 1){
								alert(response.msg);
								location.href='../';								
							} else {
								alert(response.msg);
							}
						}
					});
				}
			});
		</script>
	</head>
	<body>
		<sec:authentication property="principal" var="principal"/>
		<div id="wrap">
			<jsp:include page="/include/header.jsp"></jsp:include>
			
			<div id="container">
				<jsp:include page="/include/myPageLeft.jsp" />
				
				<div class="right">
					<h1>가입 정보</h1>
					<form action="modify" method="post">
						<div>
							<span>아이디 : </span>
							<input class="no-cursor" type="text" id="username" name="username" readonly="readonly" value="${userInfo.username }">
						</div>
						<div>
							<span>이름 : </span>
							<input type="text" id="name" name="name" required="required" value="${userInfo.name }">
						</div>
						<div>
							<span>이메일 : </span>
							<input class="no-cursor" type="email" id="email" name="email" required="required" readonly="readonly" value="${userInfo.email }">
						</div>
						<div>
							<span>휴대폰 : </span>
							<input type="tel" id="phone" name="phone" required="required" oninput="autoHyphen(this)" value="${userInfo.phone }" maxlength="13">
						</div>
						<input type="submit" value="수정">
						<!-- CSRF 토큰 -->
				   		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">	
					</form>
					<div>
						<button onclick="location.href='modifyPw'">비밀번호 변경</button>
						<button onclick="location.href='../'">취소</button>
						<sec:authorize access="hasAuthority('ROLE_MEMBER')">
							<button onclick="location.href='business/requestForm'">사업자 등록 신청</button>
						</sec:authorize>
					</div>
					<div>
						<button id="memWithdrawal">회원 탈퇴</button>
					</div>		
				</div>
			</div>
		</div>
	</body>
</html>