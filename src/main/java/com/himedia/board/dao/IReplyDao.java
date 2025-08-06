package com.himedia.board.dao;

import com.himedia.board.dto.ReplyDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface IReplyDao {

    ArrayList<ReplyDto> selectReply(int num);

    void insert(ReplyDto replydto);

    void delete(int replydto);

    int getReplyCount(int num);
}
