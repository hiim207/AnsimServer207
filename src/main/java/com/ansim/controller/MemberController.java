package com.ansim.controller;

import com.ansim.dto.MemberDTO;
import com.ansim.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.io.File;
import java.util.Map;
import java.time.LocalDate;
import java.util.UUID;
import java.net.URLEncoder;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class MemberController {

	@Autowired
	private BCryptPasswordEncoder pwdEncoder;


	private final MemberService service;

	//로그인 화면 보기
	@GetMapping("/member/login")
	public void getLogin() { }

	//로그인
	@ResponseBody
	@PostMapping("/member/login")
	public String postLogin(MemberDTO member, HttpSession session, @RequestParam("autologin") String autologin) throws Exception {

		String authkey = "";

		//로그인 시 authkey 생성
		if(autologin.equals("NEW")) {
			authkey = UUID.randomUUID().toString().replaceAll("-", "");
			member.setAuthkey(authkey);
			service.authkeyUpdate(member);
		}

		//authkey가 클라이언트에 쿠키로 존재할 경우 로그인 과정 없이 세션 생성 후 게시판 목록 페이지로 이동
		if(autologin.equals("PASS")) {
			MemberDTO memberInfo = service.memberInfoByAuthkey(member);
			if(memberInfo != null) {
				//세션 생성
				session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
				session.setAttribute("userid", memberInfo.getUser_nm());
				session.setAttribute("username", memberInfo.getUser_nm());
				session.setAttribute("role", memberInfo.getRole());

				return "{\"message\":\"GOOD\"}";
			}
		}

		//아이디 존재 여부 확인
		if(service.idCheck(member.getUser_id()) == 0) {
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}

		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(member.getPassword(), service.memberInfo(member.getUser_id()).getPassword())) {
			//잘못된 패스워드 일때
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}else {
			//제대로 된 아이디와 패스워드가 입력되었을 때

			//마지막 로그인 날짜 등록
			member.setLast_login_date(LocalDate.now());
			service.lastlogindateUpdate(member);

			LocalDate lastPwDate = service.memberInfo(member.getUser_id()).getLast_pw_date();
			int pwCheck = service.memberInfo(member.getUser_id()).getPw_chk();

			//세션 생성
			session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
			session.setAttribute("userid", service.memberInfo(member.getUser_id()).getUser_id());
			session.setAttribute("username", service.memberInfo(member.getUser_id()).getUser_nm());
			session.setAttribute("role", service.memberInfo(member.getUser_id()).getRole());

			//패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
			if(LocalDate.now().isAfter(lastPwDate.plusDays(pwCheck*30))){
				return "{\"message\":\"PASSWORD_CHANGE\"}";
			}

			return "{\"message\":\"GOOD\",\"authkey\":\"" + member.getAuthkey() + "\"}";
		}
	}

	// 회원 등록 화면 보기
	@GetMapping("/member/signup")
		public void getSignup() {

	}

	// 회원 등록 하기
	@ResponseBody
	@PostMapping("/member/signup")
	public Map<String, String> postSignup(MemberDTO member, @RequestParam("fileUpload") MultipartFile multipartFile) throws Exception{

		String path="c:\\Repository\\profile\\";
		File targetFile;

		if(!multipartFile.isEmpty()) {
			String org_filename = multipartFile.getOriginalFilename();
			// hello.png
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
			// askdjfklasjdkfljaskldfasdf + .png
			String stroed_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

			try {
				targetFile = new File(path + stroed_filename);

				multipartFile.transferTo(targetFile);

				member.setOrg_file_nm(org_filename);
				member.setStored_file_nm(stroed_filename);
				member.setFile_size(multipartFile.getSize());

			} catch(Exception e) {
				e.printStackTrace();
			}

			String inputPassword = member.getPassword();
			String pwd = pwdEncoder.encode(inputPassword); // 단방향 암호화
			member.setPassword(pwd);
			member.setLast_pw_date(LocalDate.now());


		}
		service.memberInfoRegistry(member);


		Map<String, String> data = new HashMap<>();
		data.put("message", "GOOD");
		//data.put("username", member.getUsername());
		data.put("username", URLEncoder.encode(member.getUser_nm(),"UTF-8"));

		return data;

		//return "{\"message\":\"GOOD\",\"username\":\""+ URLEncoder.encode(member.getUsername(),"UTF-8") + "\"}";
	}

	// 회원 가입 시 아이디 중복 화인
	@ResponseBody
	@PostMapping("/member/idCheck")
	public int postIdCheck(@RequestParam String userid) throws Exception {
		int result = service.idCheck(userid);

		return result;
		//if(result == 0)
		//   return "{\"status\":\"GOOD\"}"; //{"status":"GOOD"}
		//else return "{\"status\":\"BAD\"}";

	}

}
