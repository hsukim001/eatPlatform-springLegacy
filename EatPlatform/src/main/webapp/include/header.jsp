<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
       
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
        </header>
        <!-- Header 여백 공간 채우기 -->
        <div id="empty_header"></div>
        <!-- End Header-->