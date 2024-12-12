package com.eatplatform.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

// servlet-context.xml과 동일
@Configuration // Spring Container에서 관리하는 설정 클래스
@EnableWebMvc // Spring MVC 기능 사용
@ComponentScan(basePackages = { "com.eatplatform.web" })
// component scan 설정
public class ServletConfig implements WebMvcConfigurer {
	
	// ViewResolver 설정 메소드
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		registry.viewResolver(viewResolver);
	}

	// ResourceHandlers 설정 메소드
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// resources 디렉토리 설정
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
} // end ServletConfig
