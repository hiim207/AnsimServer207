package com.ansim.service;

import com.ansim.dto.MemberDTO;
import com.ansim.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper mapper;

    // 회원 등록
    @Override
    public void addMember(MemberDTO member) throws Exception {
        mapper.insertMember(member);
    }

    @Override
    public int findIdCheck(String user_id) throws Exception {
        return mapper.selectIdCheck(user_id);
    }

    @Override
    public MemberDTO findMember(String user_id) throws Exception {
        return mapper.selectMember(user_id);
    }

    @Override
    public void modifyLastLoginDate(MemberDTO member) throws Exception {
        mapper.updateLastLoginDate(member);
    }

    @Override
    public void modifyAuthkey(MemberDTO member) throws Exception {
        mapper.updateAuthkey(member);
    }

    @Override
    public MemberDTO findAuthkey(MemberDTO member) throws Exception {
        return mapper.selectAuthkey(member);
    }
}
