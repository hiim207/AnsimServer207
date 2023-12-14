package com.ansim;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ansim.dto.MemberDTO;
import com.ansim.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
//	private final MemberRepository memberRepository;
	private  final MemberService service;

	//로그인 성공 시 해야할 명령문
	@SneakyThrows
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//authentication.getName() --> 로그인 시 입력된 email 값을 가져옴.
		MemberDTO memberInfo = service.findMember(authentication.getName());
		
		//마지막 로그인 날짜 등록
		memberInfo.setLast_login_date(LocalDate.now());
		service.addMember(memberInfo);
//		memberRepository.save(memberInfo);
		
		//패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
		
		
		//세션 생성
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
		session.setAttribute("email", memberInfo.getUser_id());
		session.setAttribute("username", memberInfo.getUser_nm());
		session.setAttribute("role", memberInfo.getRole());
		
		log.info("****************** FormLogin 성공 ******************");
		
		setDefaultTargetUrl("/board/list?page=1");
		super.onAuthenticationSuccess(request, response, authentication);
		
	}

}
