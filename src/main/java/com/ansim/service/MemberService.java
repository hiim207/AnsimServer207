package com.ansim.service;

import com.ansim.dto.MemberDTO;
import org.springframework.stereotype.Service;


public interface MemberService {

    // 회원 등록 insertMember
    public void addMember(MemberDTO member) throws Exception;

    // 아이디 중복 체크 selectIdCheck
    public int findIdCheck(String user_id) throws Exception;

    // 회원 정보 찾기 selectMember
    public MemberDTO findMember(String user_id) throws Exception;

    // 회원정보 변경 updateMember
    public void modifyMember(MemberDTO member) throws Exception;

    //마지막 로그인시간 수정 updateLastLoginDate
    public void modifyLastLoginDate(MemberDTO member) throws Exception;

    //authkey 수정 updateAuthkey
    public void modifyAuthkey(MemberDTO member) throws Exception;

    //authkey를 존재 여부 확인 selectAuthkey
    public MemberDTO findAuthkey(MemberDTO member) throws Exception;

    //아이디 찾기 selectSearchId
    public String findId(MemberDTO member);

    //패스워드 찾기 updateSearchPassword
    public void modifyPassword(MemberDTO member);

    //임시 패스워드 생성
    public String tempPasswordMaker();

    //마지막 로그아웃 날짜 등록 하기 lastlogoutdateUpdate
    public void modifyLogoutDate(MemberDTO member);

    //회원 탈퇴
    public void removeMember(String user_id);

}
