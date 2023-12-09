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
    public void memberInfoRegistry(MemberDTO member) throws Exception {
        mapper.memberInfoRegistry(member);
    }

    @Override
    public int idCheck(String user_id) throws Exception {
        return mapper.idCheck(user_id);
    }

    @Override
    public MemberDTO memberInfo(String user_id) throws Exception {
        return mapper.memberInfo(user_id);
    }

    @Override
    public void lastlogindateUpdate(MemberDTO member) throws Exception {
        mapper.lastlogindateUpdate(member);
    }

    @Override
    public void authkeyUpdate(MemberDTO member) throws Exception {
        mapper.authkeyUpdate(member);
    }

    @Override
    public MemberDTO memberInfoByAuthkey(MemberDTO member) throws Exception {
        return mapper.memberInfoByAuthkey(member);
    }
}
