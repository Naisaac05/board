package com.himedia.board.service;

import com.himedia.board.dao.IBoardDao;
import com.himedia.board.dao.IReplyDao;
import com.himedia.board.dto.BoardDto;
import com.himedia.board.dto.Paging;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class BoardService {

    @Autowired
    IBoardDao bdao;

    @Autowired
    IReplyDao rdao;


    public HashMap<String, Object> selectBoard(HttpServletRequest request) {

        HashMap<String, Object> result = new HashMap<>(); //controller로 리턴할 해시 맵
        HttpSession session = request.getSession(); //paging 작업에 사용할 session
        int page = 1;
        if(request.getParameter("page") != null){
            page = Integer.parseInt(request.getParameter("page"));
            session.setAttribute("page", page);
        }else if(session.getAttribute("page") != null){
            page = (Integer)session.getAttribute("page");
        }

        Paging paging = new Paging();
        paging.setPage(page);
        int count = bdao.getAllCount();
        paging.setTotalCount(count);
        paging.calPaging(); //수동 호출

        ArrayList<BoardDto> list = bdao.selectBoard(paging);

        for(BoardDto bdto : list){
            int cnt = rdao.getReplyCount(bdto.getNum());
            bdto.setReplycnt(cnt);
        }
        result.put("BoardList", list);
        result.put("paging", paging);
        return result;
    }

    public HashMap<String, Object> getBoard(int num) {
        HashMap<String, Object> result = new HashMap<>();

        bdao.addReadCount(num);


        //BoardDto board = bdao.getBoard();
        //result.put("board", board);
        result.put("board",bdao.getBoard(num));

        //ArrayList<Reply Dto> list = rdao.selectReply(num);
        //result.put("replyList", list);
        result.put("replyList", rdao.selectReply(num));

        return result;

    }

    public void insert(@Valid BoardDto boarddto) {
        bdao.insert(boarddto);
    }

    public BoardDto getBoardOne(int num) {
        //BoardDto bdto = bdao.getBoard(num);
        //return bdto;
        return bdao.getBoard(num);
    }

    public void update(BoardDto boarddto) {
        bdao.update(boarddto);
    }

    public HashMap<String, Object> getBoardWithoutCnt(int num) {
        HashMap<String, Object> result = new HashMap<>();

        result.put("board",bdao.getBoard(num));

        result.put("replyList", rdao.selectReply(num));

        return result;
    }

    public void delete(int num) {
        bdao.delete(num);
    }

    public void updatePass(int num, String newPass) {
        bdao.updatePass(num,newPass);
    }
}
