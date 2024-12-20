<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
       
        <!-- Header -->
        <header>
            <div class="logo">
                <img src="<%=request.getContextPath()%>/resources/img/common/logo.png" alt="로고 이미지">
            </div>
            <div class="search_form">
                <input type="text" name="search_input" id="search_input" placeholder="#논현동 맛집">
            </div>
            <div class="login_form">
                <ul>
                    <li>로그인</li>
                    <li>마이페이지</li>
                </ul>
            </div>
        </header>
        <!-- Header 여백 공간 채우기 -->
        <div id="empty_header"></div>
        <!-- End Header-->