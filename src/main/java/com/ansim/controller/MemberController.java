package com.ansim.controller;

import com.ansim.dto.MemberDTO;
import com.ansim.dto.OptionDTO;
import com.ansim.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.UUID;
import java.net.URLEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;

@CrossOrigin("http://localhost:3000/")
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
			service.modifyAuthkey(member);
		}

		//authkey가 클라이언트에 쿠키로 존재할 경우 로그인 과정 없이 세션 생성 후 게시판 목록 페이지로 이동
		if(autologin.equals("PASS")) {
			MemberDTO memberInfo = service.findAuthkey(member);
			if(memberInfo != null) {
				//세션 생성
				session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
				session.setAttribute("user_id", memberInfo.getUser_nm());
				session.setAttribute("user_nm", memberInfo.getUser_nm());
				session.setAttribute("role", memberInfo.getRole());

				return "{\"message\":\"GOOD\"}";
			}
		}

		//아이디 존재 여부 확인
		if(service.findIdCheck(member.getUser_id()) == 0) {
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}

		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(member.getPassword(), service.findMember(member.getUser_id()).getPassword())) {
			//잘못된 패스워드 일때
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}else {
			//제대로 된 아이디와 패스워드가 입력되었을 때

			//마지막 로그인 날짜 등록
			member.setLast_login_date(LocalDate.now());
			service.modifyLastLoginDate(member);

			LocalDate lastPwDate = service.findMember(member.getUser_id()).getLast_pw_date();
			int pwCheck = service.findMember(member.getUser_id()).getPw_chk();

			//세션 생성
			session.setMaxInactiveInterval(3600*24*7);//세션 유지 기간 설정
			session.setAttribute("user_id", service.findMember(member.getUser_id()).getUser_id());
			session.setAttribute("user_nm", service.findMember(member.getUser_id()).getUser_nm());
			session.setAttribute("role", service.findMember(member.getUser_id()).getRole());

			//패스워드 확인 후 마지막 패스워드 변경일이 30일이 경과 되었을 경우 ...
			if(LocalDate.now().isAfter(lastPwDate.plusDays(pwCheck*30))){
				return "{\"message\":\"PASSWORD_CHANGE\"}";
			}

			return "{\"message\":\"GOOD\",\"authkey\":\"" + member.getAuthkey() + "\"}";
		}
	}

	// 회원 등록 화면 보기
	@GetMapping("/member/signup")
		public void getSignup(Model model) {

		List<String> genderOptions = service.findGender(2);
		model.addAttribute("gender", genderOptions);

	}

	// 회원 등록 하기
	@ResponseBody
	@PostMapping("/member/signup")
	public Map<String, String> postSignup(MemberDTO member,Model model,@RequestParam("fileUpload") MultipartFile multipartFile) throws Exception{

		String path="c:\\Repository\\profile\\";
		File targetFile;

		if(!multipartFile.isEmpty()) {
			String org_file_nm = multipartFile.getOriginalFilename();
			// hello.png
			String org_fileExtension = org_file_nm.substring(org_file_nm.lastIndexOf("."));
			// askdjfklasjdkfljaskldfasdf + .png
			String stored_file_nm = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;

			try {
				targetFile = new File(path + stored_file_nm);

				multipartFile.transferTo(targetFile);

				member.setOrg_file_nm(org_file_nm);
				member.setStored_file_nm(stored_file_nm);
				member.setFile_size(multipartFile.getSize());

			} catch(Exception e) {
				e.printStackTrace();
			}

			String inputPassword = member.getPassword();
			String pwd = pwdEncoder.encode(inputPassword); // 단방향 암호화
			member.setPassword(pwd);
			member.setLast_pw_date(LocalDate.now());
		}
		service.addMember(member);


		Map<String, String> data = new HashMap<>();
		data.put("message", "GOOD");
		//data.put("username", member.getUsername());
		data.put("user_nm", URLEncoder.encode(member.getUser_nm(),"UTF-8"));

		return data;

	}

	// 회원 가입 시 아이디 중복 화인
	@ResponseBody
	@PostMapping("/member/idCheck")
	public int postIdCheck(@RequestBody String user_id) throws Exception {
		int result = service.findIdCheck(user_id);
		System.out.println(result);
		return result;
	}

	// 회원 정보 보기
	@GetMapping("/member/memberInfo")
	public void getMemberInfo(HttpSession session, Model model) throws Exception{
		String user_id = (String)session.getAttribute("user_id");
		model.addAttribute("memberInfo", service.findMember(user_id));
	}

	//회원 기본 정보 변경
	@GetMapping("/member/memberInfoModify")
	public void getMemberInfoModify(HttpSession session, Model model) throws Exception{
		String userid = (String)session.getAttribute("user_id");
		model.addAttribute("memberInfo", service.findMember(userid));
	}

	//회원 기본 정보 변경
	@ResponseBody
	@PostMapping("/member/memberInfoModify")
	public Map<String, String> postMemberInfoModify(HttpSession session, MemberDTO member, @RequestParam("fileUpload") MultipartFile multipartFile) throws Exception {

		String userid = (String)session.getAttribute("user_id");


		String path="c:\\Repository\\profile\\";
		File targetFile;

		MemberDTO members = service.findMember(userid);
		members.setGender(member.getGender());
		members.setMbti(member.getMbti());
		members.setAge(member.getAge());
		members.setGender(member.getGender());
		members.setTel_no(member.getTel_no());
		members.setOrg_file_nm(member.getOrg_file_nm());
		members.setStored_file_nm(member.getStored_file_nm());
		members.setFile_size(member.getFile_size());

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

		}

		service.modifyMember(member);


		Map<String, String> data = new HashMap<>();
		data.put("message", "GOOD");
		return data;
	}

	//로그아웃
	@GetMapping("/member/logout")
	public void getLogout(HttpSession session,Model model) throws Exception {
		String userid = (String)session.getAttribute("user_id");
		String username = (String)session.getAttribute("user_nm");

		MemberDTO member = new MemberDTO();
		member.setUser_id(userid);
		member.setLast_logout_date(LocalDate.now());

		service.modifyLastLoginDate(member);

		model.addAttribute("user_id", userid);
		model.addAttribute("user_nm", username);
		session.invalidate(); //모든 세션 종료 --> 로그아웃...
	}

	//아이디 찾기
	@GetMapping("/member/searchID")
	public void getSearchID() {}

	//아이디 찾기
	@ResponseBody
	@PostMapping("/member/searchID")
	public String postSearchID(MemberDTO member) {

		String userid = service.findId(member) == null?"ID_NOT_FOUND":service.findId(member);
		return "{\"message\":\"" + userid + "\"}";
	}

	//임시 패스워드 생성
	@GetMapping("/member/searchPassword")
	public void getSearchPassword() {}

	//임시 패스워드 생성
	@ResponseBody
	@PostMapping("/member/searchPassword")
	public String postSearchPassword(MemberDTO member) throws Exception{
		//아이디 존재 여부 확인
		if(service.findIdCheck(member.getUser_id()) == 0)
			return "{\"status\":\"ID_NOT_FOUND\"}";
		//TELNO 확인
		if(!service.findMember(member.getUser_id()).getTel_no().equals(member.getTel_no()))
			return "{\"status\":\"TELNO_NOT_FOUND\"}";

		//임시 패스워드 생성
		String rawTempPW = service.tempPasswordMaker();
		member.setPassword(pwdEncoder.encode(rawTempPW));
		member.setLast_pw_date(LocalDate.now());
		service.modifyPassword(member);

		return "{\"status\":\"GOOD\",\"password\":\"" + rawTempPW + "\"}";
	}

	//회원 패스워드 변경
	@GetMapping("/member/memberPasswordModify")
	public void getMemberPasswordModify() throws Exception { }

	//회원 패스워드 변경
	@ResponseBody
	@PostMapping("/member/memberPasswordModify")
	public String postMemberPasswordModify(@RequestParam("old_password") String old_password,
										   @RequestParam("new_password") String new_password, HttpSession session) throws Exception {

		String userid = (String)session.getAttribute("user_id");

		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(old_password, service.findMember(userid).getPassword())) {
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}

		//신규 패스워드로 업데이트
		MemberDTO member = new MemberDTO();
		member.setUser_id(userid);
		member.setPassword(pwdEncoder.encode(new_password));
		member.setLast_pw_date(LocalDate.now());
		service.modifyPassword(member);

		return "{\"message\":\"GOOD\"}";
	}

	//패스워드 변경 후 세션 종료
	@GetMapping("/member/memberSessionOut")
	public String getMemberSessionOut(HttpSession session) {

		MemberDTO member = new MemberDTO();
		member.setUser_id((String)session.getAttribute("user_id"));
		member.setLast_logout_date(LocalDate.now());
		service.modifyLogoutDate(member);
		session.invalidate();

		return "redirect:/";
	}

	//회원 탈퇴
	@GetMapping("/member/deleteMember")
	public void getMemberOut() {}

	@ResponseBody
	@PostMapping("/member/deleteMember")
	public Map<String,String> postMemberOut(HttpSession session) throws Exception {
		String userid = (String)session.getAttribute("user_id");

		service.removeMember(userid);

		//return "{\"message\":\"GOOD\"}";

		Map<String, String> data = new HashMap<>();
		data.put("message", "GOOD");
		return data;

	}



}
