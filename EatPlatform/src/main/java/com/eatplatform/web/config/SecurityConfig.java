package com.eatplatform.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.eatplatform.web.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	// 비밀번호 암호화
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// HttpSecurity 객체를 통해 HTTP 보안 기능을 구성
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests()
			.antMatchers("/user/register").permitAll()
			.antMatchers("/user/created").permitAll()
			.antMatchers("/user/searchPw").permitAll()
			.antMatchers("/user/searchId").permitAll()
			.antMatchers("/user/modifyPw").permitAll()
			.antMatchers("/user/gradeUpgradeForm").access("hasRole('ROLE_MEMBER')")
			.antMatchers(HttpMethod.GET, "/user/business/requestInfo").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")
			.antMatchers(HttpMethod.POST, "/user/business/requestInfo").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/user/business/request/denialManagement/").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/user/*").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_STORE') or hasRole('ROLE_ADMIN')")
			.antMatchers("/store/list").permitAll()
			.antMatchers("/store/detail").permitAll()
			.antMatchers("/store/map").permitAll()
			.antMatchers("/store/map/").permitAll()
			.antMatchers("/store/request/list", "/store/request/list/*").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/store/request/info").access("hasRole('ROLE_STORE') or hasRole('ROLE_ADMIN')")
			.antMatchers("/store/**").access("hasRole('ROLE_STORE')")
			.antMatchers("/approval/store").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/approval/denialManagement/").access("hasRole('ROLE_STORE') or hasRole('ROLE_ADMIN')")
			.antMatchers("/review/*").permitAll()
			.antMatchers("/reserv/list").access("hasRole('ROLE_MEMBER') or hasRole('ROLE_STORE')")
			.antMatchers("/email/**").permitAll();

		// 접근 제한 경로 설정
		httpSecurity.exceptionHandling().accessDeniedPage("/access/accessDenied");

		httpSecurity.formLogin().loginPage("/access/login") // 커스텀 로그인 url 설정
				.defaultSuccessUrl("/"); // 접근 제한 설정이 되어 있지 않은 url에서 로그인 성공 시 이동할 url 설정

		httpSecurity.logout().logoutUrl("/access/logout") // logout url 설정
				.logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 url 설정
				.invalidateHttpSession(true); // 세션 무효화 설정

		// header 정보에 xssProtection 기능 설정
		// 외부 링크 차단
//			httpSecurity.headers().xssProtection().block(true);
//			httpSecurity.headers().contentSecurityPolicy("script-src 'self' https://code.jquery.com 'unsafe-inline' 'unsafe-eval'");

		// encoding 필터를 Csrf 필터보다 먼저 실행
		httpSecurity.addFilterBefore(encodingFilter(), CsrfFilter.class);

	}

	// AuthenticationManagerBuilder 객체를 통해 인증 기능을 구성
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()); // CustomUserDetailsService 빈 적용
	}

	// 사용자 정의 로그인 클래스인 CustomUserDetailsService를 빈으로 생성
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	// CharacterEncodingFilter 빈 생성
	@Bean
	public CharacterEncodingFilter encodingFilter() {
		return new CharacterEncodingFilter("UTF-8");
	}
}
