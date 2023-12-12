package com.ansim.service;

import com.ansim.dto.MemberDTO;
import com.ansim.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper mapper;

    // 회원 등록
    @Override
    public void addMember(MemberDTO member) throws Exception {
        mapper.insertMember(member);
    }

    // 아이디 중복 체크 selectIdCheck
    @Override
    public int findIdCheck(String user_id) throws Exception {
        return mapper.selectIdCheck(user_id);
    }

    // 회원 정보 찾기 selectMember
    @Override
    public MemberDTO findMember(String user_id) throws Exception {
        return mapper.selectMember(user_id);
    }

    //마지막 로그인시간 수정 updateLastLoginDate
    @Override
    public void modifyLastLoginDate(MemberDTO member) throws Exception {
        mapper.updateLastLoginDate(member);
    }

    //authkey 수정 updateAuthkey
    @Override
    public void modifyAuthkey(MemberDTO member) throws Exception {
        mapper.updateAuthkey(member);
    }

    //authkey를 존재 여부 확인 selectAuthkey
    @Override
    public MemberDTO findAuthkey(MemberDTO member) throws Exception {
        return mapper.selectAuthkey(member);
    }

    //아이디 찾기 selectSearchId
    public String findId(MemberDTO member) {
        return mapper.selectSearchId(member);
    }

    //패스워드 찾기 updateSearchPassword
    public void modifyPassword(MemberDTO member) {
        mapper.updateSearchPassword(member);
    }

    //임시 패스워드 생성
    @Override
    public String tempPasswordMaker() {
        //숫자 + 영문대소문자 7자리 임시패스워드 생성
        StringBuffer tempPW = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < 7; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    // a-z : 아스키코드 97~122
                    tempPW.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    // A-Z : 아스키코드 65~122
                    tempPW.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    // 0-9
                    tempPW.append((rnd.nextInt(10)));
                    break;
            }
        }
        return tempPW.toString();
    }

    //마지막 로그아웃 날짜 등록 하기 lastlogoutdateUpdate
    @Override
    public void modifyLogoutDate(MemberDTO member) {
        mapper.updateLastLogoutDate(member);
    }

    //회원 탈퇴
    @Override
    public void removeMember(String user_id) {
        mapper.deleteMember(user_id);

    }

}
