package com.blog.blog.service;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.Reply;
import com.blog.blog.domain.User;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    public void addReply(ReplyDto replyDto, User user, Board board){
        Reply reply = new Reply();
        reply.setBoard(board);
        reply.setContent(replyDto.getContent());
        reply.setUser(user);

        System.out.println("reply = " + reply);
        replyRepository.save(reply);
    }
}
