package com.himedia.board.service;

import com.himedia.board.dao.IReplyDao;
import com.himedia.board.dto.ReplyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    @Autowired
    IReplyDao rdao;

    public void insert(ReplyDto replydto) {
        rdao.insert(replydto);
    }

    public void delete(int replydto) {
        rdao.delete(replydto);
    }
}
