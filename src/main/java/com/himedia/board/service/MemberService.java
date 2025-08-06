package com.himedia.board.service;

import com.himedia.board.dao.IMemberDao;
import com.himedia.board.dto.MemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    
    @Autowired
    IMemberDao mdao;
    
    public MemberDto getMember(String userid) {
        MemberDto mdto = mdao.getMember(userid);
        return mdto;
    }

    public void insert(@Valid MemberDto memberdto) {
        mdao.insert(memberdto);
    }

    public void update(@Valid MemberDto memberdto) {
        mdao.update(memberdto);
    }

    public void deleteMember(String userid) {
        mdao.deleteMember(userid);
    }
}
