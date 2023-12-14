package com.ansim.mapper;

import com.ansim.dto.MemberDTO;
import com.ansim.dto.OptionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 회원 등록
    public void insertMember(MemberDTO member) throws Exception;

    // 아이디 중복 체크
    public int selectIdCheck(String user_id) throws Exception;

    // 회원 정보 찾기
    public MemberDTO selectMember(String user_id) throws Exception;

    // 회원정보 변경
    public void updateMember(MemberDTO member) throws Exception;

    //마지막 로그인시간 수정
    public void updateLastLoginDate(MemberDTO member) throws Exception;

    //authkey 수정
    public void updateAuthkey(MemberDTO member) throws Exception;

    //authkey를 존재 여부 확인
    public MemberDTO selectAuthkey(MemberDTO member) throws Exception;

    //아이디 찾기
    public String selectSearchId(MemberDTO member);

    //패스워드 찾기
    public void updateSearchPassword(MemberDTO member);

    //임시 패스워드 생성
    public String tempPasswordMaker();

    //마지막 로그아웃 날짜 등록 하기
    public void updateLastLogoutDate(MemberDTO member);

    //회원 탈퇴
    public void deleteMember(String user_id);


    // gender code 가져오기
    public List<String> selectGender(int group_cd);


}
