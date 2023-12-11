package com.ansim.mapper;

import com.ansim.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // 회원 등록
    public void insertMember(MemberDTO member) throws Exception;

    // 아이디 중복 체크
    public int selectIdCheck(String user_id) throws Exception;

    // 회원 정보 찾기
    public MemberDTO selectMember(String user_id) throws Exception;

    //마지막 로그인시간 수정
    public void updateLastLoginDate(MemberDTO member) throws Exception;

    //authkey 수정
    public void updateAuthkey(MemberDTO member) throws Exception;

    //authkey를 존재 여부 확인
    public MemberDTO selectAuthkey(MemberDTO member) throws Exception;



}
