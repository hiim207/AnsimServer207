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

    //마지막 로그인시간 수정 updateLastLoginDate
    public void modifyLastLoginDate(MemberDTO member) throws Exception;

    //authkey 수정 updateAuthkey
    public void modifyAuthkey(MemberDTO member) throws Exception;

    //authkey를 존재 여부 확인 selectAuthkey
    public MemberDTO findAuthkey(MemberDTO member) throws Exception;

}
