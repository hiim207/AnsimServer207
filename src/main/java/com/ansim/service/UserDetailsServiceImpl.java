package com.ansim.service;

import java.util.List;

import com.ansim.dto.MemberDTO;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

import com.ansim.dto.MemberDTO;
//import com.board.entity.MemberEntity;
//import com.board.entity.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

//	private final MemberRepository memberRepository;
	private final MemberService service;

	@SneakyThrows
	@Override
//	public UserDetails loadUserByUsername(String user_nm) throws UsernameNotFoundException {
	public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
		
		//username은 스프링 시큐리티가 필터로 작동하면서 로그인 요청에서 가로채온 userid임.
//		MemberEntity memberInfo = memberRepository.findById(username).get();
//		MemberDTO member = service.findMember(user_nm);
		MemberDTO member = service.findMember(user_id);

		if(member == null) {
			throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
		}
		
		//SimpleGrantedAuthority : 여러개의 사용자 Role값을 받는 객체
		List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
		SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
		grantedAuthorities.add(grantedAuthority);
		
		User user;
        user = new User(user_id, member.getPassword(), grantedAuthorities);

        return user;
	}

}
