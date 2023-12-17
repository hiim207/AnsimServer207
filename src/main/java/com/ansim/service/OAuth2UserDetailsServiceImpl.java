package com.ansim.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.ansim.dto.MemberDTO;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ansim.dto.MemberOAuth2DTO;
//import com.ansim.entity.MemberEntity;
//import com.ansim.entity.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2UserDetailsServiceImpl extends DefaultOAuth2UserService{

	private final PasswordEncoder pwdEncoder;
//	private final MemberRepository memberRepository;
	private final MemberService service;
	private final HttpSession session;
	
	@SneakyThrows
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		String provider = userRequest.getClientRegistration().getRegistrationId();
//		String providerId = oAuth2User.getAttribute("sub");
//		String user_id = oAuth2User.getAttribute("email");
		String providerId = "";
		String user_id = "";

		if (provider.equals("naver")) {  // 네이버 로그인인 경우
			Map<String, Object> attributes = oAuth2User.getAttributes();
			Map<String, Object> response = (Map<String, Object>) attributes.get("response");
			providerId = response.get("id").toString();
			user_id = (String) response.get("email");
		} else if (provider.equals("google")) {  // 구글 로그인인 경우
			providerId = oAuth2User.getAttribute("sub");
			user_id = oAuth2User.getAttribute("email");
		} else if (provider.equals("kakao")) { //카카오 로그인인 경우
			Map<String, Object> attributes = oAuth2User.getAttributes();
			Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
			System.out.println(response);
			providerId = response.get("profile").toString();
			user_id = response.get("email").toString();
		}
		
		log.info("provider = {}", provider);
		log.info("providerId = {}", providerId);
		log.info("user_id = {}", user_id);
		
		oAuth2User.getAttributes().forEach((k,v)-> { 
			log.info(k + ":" + v);
		});

		MemberDTO member = saveSocialMember(user_id, provider);
		
		//Role 값 읽어 들임...
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
		grantedAuthorities.add(grantedAuthority);
		
		MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();
		//attributes, authorities, name을 MemberOAuth2DTO에 넣어 줌. 
		memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
		memberOAuth2DTO.setAuthorities(grantedAuthorities);
		memberOAuth2DTO.setName(member.getUser_nm());
		
		session.setAttribute("user_id", member.getUser_id());
		session.setAttribute("user_nm", member.getUser_nm());
		session.setAttribute("role", member.getRole());
		session.setAttribute("fromSocial","Y");

		return memberOAuth2DTO;		
		
	}
	
	private MemberDTO saveSocialMember(String user_id, String provider) throws Exception {
		
		//구글 회원 계정으로 로그인 한 회원의 경우 사이트 운영에 필요한 최소한의 정보를 
		//가공해서 tbl_member에 입력해야 함.

//		Optional<MemberEntity> result = memberRepository.findById(email);
		Optional<MemberDTO> result = Optional.ofNullable(service.findMember(user_id));
		if(result.isPresent()) {
			return result.get();
		}

		String username = "";
		if (provider.equals("naver")) {
			username = "네이버회원";
		} else if (provider.equals("google")) {
			username = "구글회원";
		} else if (provider.equals("kakao")) {
			username = "카카오회원";
		}
		
		MemberDTO member = MemberDTO.builder()
										.user_id(user_id)
										.user_nm(username)
										.password(pwdEncoder.encode("12345"))
										.role("USER")
										.regdate(LocalDate.now())
										.fromSocial("Y")
										.build();
		service.addMember(member);
		
		return member;
	}

}
