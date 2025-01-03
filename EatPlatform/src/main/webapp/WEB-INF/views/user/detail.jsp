<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>가입 정보</title>
		<style type="text/css">
			.no-cursor {
			    background: #aaa;
			    pointer-events: none; /* 클릭 및 커서 비활성화 */
			    user-select: none;   /* 텍스트 선택 방지 */
			}
		</style>
		<script src="https://code.jquery.com/jquery-latest.min.js"></script>
		<script type="text/javascript">
		
			// 전화번호 정규 표현식
			const autoHyphen = (target) => {
				target.value = target.value
					.replace(/[^0-9]/g, '')
			   		.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
			}
		
			$(document).ready(function(){
				
				// 회원 탈퇴 버튼 액션
				$('#memWithdrawal').click(function(){
					let isWithdrawal = confirm('회원탈퇴를 진행 하시겠습니까?');
					if(isWithdrawal == true) {
						withdrawal();
					}
				});
				
				function withdrawal() {
					let status = 'N';
					
					$.ajax({
						url : 'delete/' + status,
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
		<h1>가입 정보</h1>
		<form action="modify" method="post">
			<div>
				<span>아이디 : </span>
				<input class="no-cursor" type="text" id="userId" name="userId" readonly="readonly" value="${sessionScope.userId }">
			</div>
			<div>
				<span>이름 : </span>
				<input type="text" id="userName" name="userName" required="required" value="${vo.userName }">
			</div>
			<div>
				<span>이메일 : </span>
				<input class="no-cursor" type="email" id="userEmail" name="userEmail" required="required" readonly="readonly" value="${vo.userEmail }">
			</div>
			<div>
				<span>휴대폰 : </span>
				<input type="tel" id="userPhone" name="userPhone" required="required" oninput="autoHyphen(this)" value="${vo.userPhone }" maxlength="13">
			</div>
			<input type="submit" value="수정">	
		</form>
		<div>
			<button onclick="location.href='modifyPw'">비밀번호 변경</button>
			<button onclick="location.href='../'">취소</button>
		</div>
		<div>
			<button id="memWithdrawal">회원 탈퇴</button>
		</div>
	</body>
</html>