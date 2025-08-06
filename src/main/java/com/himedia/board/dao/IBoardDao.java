package com.himedia.board.dao;

import com.himedia.board.dto.BoardDto;
import com.himedia.board.dto.Paging;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface IBoardDao {

    int getAllCount();


    ArrayList<BoardDto> selectBoard(Paging paging);

    BoardDto getBoard(int num);

    void addReadCount(int num);

    void insert(@Valid BoardDto boarddto);

    void update(BoardDto boarddto);

    void delete(int num);

    void updatePass(int num, String newPass);
}
