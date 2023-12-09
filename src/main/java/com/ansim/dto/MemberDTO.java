package com.ansim.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDTO {
    private String user_id;
    private String user_nm;
    private String password;
    private String age;
    private String gender;
    private String mbti;
    private String tel_no;
    private LocalDate regdate;
    private LocalDate last_login_date;
    private LocalDate last_logout_date;
    private LocalDate last_pw_date;
    private int pw_chk;
    private String role;
    private String org_file_nm;
    private String stored_file_nm;
    private long file_size;
    private String authkey;
    private int ansim_cnt;

}
