<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>       
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
       
        <!-- Footer 여백 공간 채우기 -->
        <div id="empty_footer"></div>
        <!-- Footer -->
        <footer>
            <div id="footer_container">
                <div class="footer_logo">
                    <img src="<%=request.getContextPath()%>/resources/img/common/logo.png" alt="하단 로고 이미지">
                </div>
                <div id="text_container">
                    <p>
                        아무튼 긴 내용이 들어가면 됩니다. 네 아무튼 긴 내용이 들어가면 됩니다. 네 아무튼 긴 내용이 들어가면 됩니다. 네 아무튼 긴 내용이 들어가면 됩니다. 네 아무튼 긴
                        내용이
                        들어가면 됩니다. 네 아무튼 긴 내용이 들어가면 됩니다. 네 <br>
                        아무튼 긴 내용이 들어가면 됩니다. 네 <br>
                        아무튼 긴 내용이 들어가면 됩니다. 네 <br>
                        아무튼 긴 내용이 들어가면 됩니다. 네 <br>
                    </p>
                    <div id="icon_box">
                        <ul>
                            <li>
                                <a href="https://www.notion.so/Eat-Platform-Project-15826c81cd358099bcf8c1a96b72785d">
                                    <img src="<%=request.getContextPath()%>/resources/img/common/notion_icon.png" alt="노션 링크">
                                </a>
                            </li>
                            <li>
                                <a href="https://github.com/hsukim001/eatPlatform-springLegacy">
                                    <img src="<%=request.getContextPath()%>/resources/img/common/github_icon.png" alt="깃허브 링크">
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </footer>
        <!-- End Footer-->