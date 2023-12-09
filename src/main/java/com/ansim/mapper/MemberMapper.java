package com.ansim.mapper;

import com.ansim.dto.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    // 회원 등록
    public void memberInfoRegistry(MemberDTO member) throws Exception;

    // 아이디 중복 체크
    public int idCheck(String user_id) throws Exception;

    // 회원 정보 찾기
    public MemberDTO memberInfo(String user_id) throws Exception;

    //마지막 로그인시간 수정
    public void lastlogindateUpdate(MemberDTO member) throws Exception;

    //authkey 수정
    public void authkeyUpdate(MemberDTO member) throws Exception;

    //authkey를 존재 여부 확인
    public MemberDTO memberInfoByAuthkey(MemberDTO member) throws Exception;



}
