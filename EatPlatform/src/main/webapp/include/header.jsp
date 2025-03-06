<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
       
        <meta name="_csrf" content="${_csrf.token}"/>
		<meta name="_csrf_header" content="${_csrf.headerName}"/>
        <!-- Header -->
        <header>
            <div class="logo">
            	<a href="/">
                	<img src="<%=request.getContextPath()%>/resources/img/common/logo.png" alt="로고 이미지">
                </a>
            </div>
            <div class="search_form">
                <input type="text" name="search_input" id="search_input" placeholder="#논현동 맛집">
            </div>
            <div class="login_form">
                <sec:authorize access="isAnonymous()">
	                <ul>
	    	            <li class="login">
		                    <a href="/access/login">
		                    	로그인
		                	</a>                    		
		            	</li>
	    	            <li>
		                    <a href="/user/register">
		                    	회원가입
		                	</a>                    		
		            	</li>
	                </ul>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                	<ul>
                		<li  class="logout">
                			<form action="/access/logout" method="post">
                				<input type="submit" value="로그아웃">
                				<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
                			</form>
                		</li>
                    	<li>
                    		<a href="/user/detail">마이페이지</a>
                    	</li>         	
                	</ul>
                	<ul>
           				<!-- 알림 버튼을 헤더에 추가 -->
            			<li class="review_noti">
                			<a id="notificationIcon" class="nav-link text-white notification-dot">
                				🔔
                				<span id="notificationBadge" class="notification-badge"></span> <!-- 빨간 점 -->
    						</a>
            			</li>
        			</ul>
        			<ul id="notifications" class="notifications-dropdown" style="display: none;">
            			<!-- 실시간 알림이 여기에 표시됩니다 -->
        			</ul>
                </sec:authorize>
            </div>
            <div id="gnb_bar">
            	<ul>
            		<li> 
            			<a href="/shop/list">
            				Shop 
            			</a>
            		</li>
            		<li> 
            			<a href="/store/list">
            				맛집 목록 
            			</a>
            		</li>
            		<li> 
            			<a href="/store/map">
            			맛집 로드맵 
            			</a>
            		</li>
            		<li> 
            			<a href="/user/detail">
            				마이페이지 
            			</a>
            			<ul class="lnb_list">
            				<li>
            					<a href="/user/detail"> 
            						회원 정보 
            					</a>
            				</li>
            				
            				<sec:authorize access="hasAuthority('ROLE_MEMBER')">
            				<!-- 일반 회원 -->
	            				<li>
	            					<a href="/reserv/list">
	            						예약 현황 확인
	            					</a>
	            				</li>
            					<li>
            						<a href="/user/business/requestForm">
            							사업자 등록 신청
            						</a>
            					</li>
            				</sec:authorize>
            				
            				<sec:authorize access="hasAuthority('ROLE_STORE')">
            				<!-- 사업자 회원 -->
	            				<li>
	            					<a href="/reserv/list">
	            						예약 현황 확인
	            					</a>
	            				</li>
            					<li>
            						<a href="#" onclick="alert('아직 준비 중 입니다.');">
            							매장 관리
            						</a>
            					</li>
            					<li>
            						<a href="/shop/product/newProduct">
            							상품 등록
            						</a>
            					</li>
            					<li>
	            					<a href="/store/newStore">
	            						신규 업장 등록
	            					</a>
	            				</li>
            				</sec:authorize>
            				
            				<sec:authorize access="hasAuthority('ROLE_ADMIN')">
            				<!--  관리자 -->
            					<li>
            						<a href="#" onclick="alert('아직 준비 중 입니다.');">
            							신고 리뷰 관리
            						</a>
            					</li>
            					<li>
            						<a href="/user/business/requestList">
            							사업자 신청 관리
            						</a>
            					</li>
            					<li>
            						<a href="/store/request/list">
            							가게 등록 관리
            						</a>
            					</li>
            					<li>
            						<a href="/shop/product/category/management">
            							상품 카테고리 관리
            						</a>
            					</li>
            				</sec:authorize>
            			</ul>
            		</li>
            	</ul>
            </div>
            
            
            <script>
            document.addEventListener("DOMContentLoaded", function () {
			    // 로그인 상태 확인 후 알림 처리
			    checkLoginStatus()
			        .then(userInfo => {
			            if (userInfo.isAuthenticated) {
			                const username = userInfo.username;

			                // 로그인된 사용자에게 읽지 않은 알림을 가져와서 화면에 표시
			                loadUnreadNotifications(username);

			                // SSE를 통해 실시간 알림 받기
			                setupRealTimeNotifications(username);

			                // 알림 아이콘의 마우스 오버/아웃 이벤트 처리
			                setupNotificationIcon();
			            }
			        })
			        .catch(error => {
			            console.error("사용자 정보를 가져오는 데 실패했습니다.", error);
			        });
			});
            
			/**
			 * 로그인 상태를 확인하는 함수
			 */
			function checkLoginStatus() {
			    return fetch('/access/auth/username')
			        .then(response => response.json())
			        .catch(error => {
			            console.error('로그인 상태 확인 실패:', error);
			            throw error;
			        });
			}

			/**
			 * 로그인된 사용자의 읽지 않은 알림을 화면에 표시하는 함수
			 */
			function loadUnreadNotifications(username) {
			    fetch("/notifications/getUnread")
			        .then(response => response.json())
			        .then(notifications => {
			            displayNotifications(notifications);
			        })
			        .catch(error => {
			            console.error('알림을 조회하는 데 실패했습니다:', error);
			        });
			}

			/**
			 * 알림 목록을 화면에 표시하는 함수
			 */
			function displayNotifications(notifications) {
			    const notificationIcon = document.getElementById("notificationIcon");
			    const notificationsElement = document.getElementById("notifications");
				
			    if (notifications.length > 0) {
			        notificationIcon.classList.add("has-notifications");

			        notifications.forEach(notification => {
			            const li = document.createElement("li");
			            li.textContent = notification.message;
			            li.dataset.notificationId = notification.notificationId;
			            notificationsElement.appendChild(li);
			        });
			    } else {
			        notificationIcon.classList.remove("has-notifications");
			    }
			}

			/**
			 * 실시간 알림을 받기 위한 SSE 설정 함수
			 */
			 function setupRealTimeNotifications(username) {
				    const eventSource = new EventSource("/notifications/subscribe/" + username);

				    eventSource.addEventListener("messageEvent", function (event) {
				    	const data = JSON.parse(event.data);
				    	console.log("새 알림: ", data.message);
				        appendNewNotification(event.data);  
				    });

				    eventSource.addEventListener("error", function (event) {
				        console.error("SSE 오류 발생: ", event);
				        console.error("EventSource 상태: ", eventSource.readyState);  // 상태 코드 확인
				        eventSource.close();
				    });
			}
			/**
			 * 새 알림을 화면에 추가하는 함수
			 */
			function appendNewNotification(message) {
			    const notificationsElement = document.getElementById("notifications");
			    const li = document.createElement("li");
			    li.textContent = message;
			    notificationsElement.appendChild(li);
			    
			}

			/**
			 * 알림 아이콘에 마우스가 올라가면 알림 목록을 표시하는 함수
			 */
			function setupNotificationIcon() {
				
				const notificationIcon = document.getElementById("notificationIcon");
			    const notifications = document.getElementById("notifications");
			    
			    // 알림 아이콘에 마우스를 올리면 알림 목록 보이기
			    notificationIcon.addEventListener("mouseover", function () {
			        notifications.style.display = "block"; // 알림 목록 보이기
			    });

			    // 알림 목록에 마우스를 올리면 목록이 계속 보이도록 설정
			    notifications.addEventListener("mouseenter", function () {
			        notifications.style.display = "block"; // 알림 목록이 계속 보이도록 유지
			    });

			    // 알림 목록에서 마우스를 뗄 때 목록 숨기기
			    notifications.addEventListener("mouseleave", function () {
			        notifications.style.display = "none"; // 알림 목록 숨기기
			        $('#notifications').find('li').each(function() {
			            var notificationId = $(this).data('notification-id');
			            
			            $.ajax({
			            	url : '/notifications/updateRead',
			            	method : 'POST',
			            	headers : {
								"Content-Type" : "application/json"
						 	},
			            	data : JSON.stringify({"notificationId" : notificationId}),
			            	success : function(response) {
		                    	console.log('Notification status updated');
		                    	$('#notifications').remove()
		                    	$('#notificationBadge').css('display', 'none');
			            	},
		                  	error : function(error) {
		                    	console.error('Error:', error);
		                	}
			            });
			        });
			    });
			    
			    // 알림 아이콘에서 마우스를 뗄 때 목록 숨기기
			    notificationIcon.addEventListener("mouseout", function () {
			        // 마우스가 알림 목록 안에 없으면 목록을 숨김
			        notifications.style.display = "none"; // 알림 목록 숨기기
			    });
			}
			
			/**
			 * AJAX 요청을 위한 CSRF 토큰 설정
			 */
			 $(document).ajaxSend(function(e, xhr, opt){
	     	       var token = $("meta[name='_csrf']").attr("content");
	     	       var header = $("meta[name='_csrf_header']").attr("content");
	     	       
	     	       xhr.setRequestHeader(header, token);
	     	 });
            </script>
            
        </header>
        <!-- Header 여백 공간 채우기 -->
        <div id="empty_header"></div>
        <!-- End Header-->