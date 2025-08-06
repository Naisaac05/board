package com.himedia.board.dao;

import com.himedia.board.dto.MemberDto;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberDao {
    MemberDto getMember(String userid);

    void insert(@Valid MemberDto memberdto);

    void update(@Valid MemberDto memberdto);

    void deleteMember(String userid);
}
