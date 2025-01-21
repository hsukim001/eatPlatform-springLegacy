<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
       
        <!-- Header -->
        <header>
            <div class="logo">
            	<a href="/web">
                	<img src="<%=request.getContextPath()%>/resources/img/common/logo.png" alt="로고 이미지">
                </a>
            </div>
            <div class="search_form">
                <input type="text" name="search_input" id="search_input" placeholder="#논현동 맛집">
            </div>
            <div class="login_form">
                <sec:authorize access="isAnonymous()">
	                <ul>
	    	                <li>
		                    	<a href="/web/access/login">
		                    		로그인
		                    	</a>                    		
		                    </li>
	                </ul>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                	<ul>
                		<li>
                			<form action="/web/access/logout" method="post">
                				<input type="submit" value="로그아웃">
                				<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
                			</form>
                		</li>
                    	<li>
                    		<a href="/web/user/detail">마이페이지</a>
                    	</li>         	
                	</ul>
                </sec:authorize>
            </div>
        </header>
        <!-- Header 여백 공간 채우기 -->
        <div id="empty_header"></div>
        <!-- End Header-->