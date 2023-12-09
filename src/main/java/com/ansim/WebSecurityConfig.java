package com.ansim;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

   //여기 있는 암호화를 사용???/ 
   //리턴되는 객체가 스피링 빈이 됨
   
   //spring-security에서 암호화 관련 객체를 가져다가 스프링빈으로 등록
   @Bean
   public BCryptPasswordEncoder pwdEncoder() {
      
      return new BCryptPasswordEncoder();
   }
   
   //스프링 시큐리티 로그인 화면 사용 비활성화, CSRF/CORS 공격 방어용 보안 설정 비활성화
   @Bean
   public SecurityFilterChain filter(HttpSecurity http) throws Exception{
      //기존 로그인 화면 비활성화
      http.formLogin((login) -> login.disable())
         .csrf((csrf) -> csrf.disable())
         .cors((cors) -> cors.disable());
      
      return http.build();
   }
}