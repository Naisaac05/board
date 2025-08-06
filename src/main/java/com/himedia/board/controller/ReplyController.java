package com.himedia.board.controller;

import com.himedia.board.dto.ReplyDto;
import com.himedia.board.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReplyController {
    @Autowired
    ReplyService rs;

    @PostMapping("/addReply")
    public String addReply( ReplyDto replydto){
        rs.insert(replydto);
        return "redirect:/boardViewWithoutCnt?num="+replydto.getBoardnum();
    }

    @GetMapping("/deleteReply")
    public String deleteReply(@RequestParam("replynum") int replynum, @RequestParam("boardnum") int boardnum ) {
        rs.delete(replynum);
        return "redirect:/boardViewWithoutCnt?num="+boardnum;
    }
}
