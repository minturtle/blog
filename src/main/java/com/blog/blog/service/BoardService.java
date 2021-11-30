package com.blog.blog.service;

import com.blog.blog.domain.Board;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardPreviewDto> getBoardsByPage(Integer page){
        PageRequest pageRequest = PageRequest.of(page, 5);
        return boardRepository.findAll(pageRequest).getContent().stream().map((b)->{return new BoardPreviewDto(b.getId(), b.getTitle(),
                b.getWriter() == null ? "Someone" : b.getWriter().getUsername(), b.getCreatedAt());}).collect(Collectors.toList());
    }

    public void save(BoardDto boardDto){
        Board board = new Board(boardDto.getTitle(), boardDto.getContent(), boardDto.getWriter());
        boardRepository.save(board);
    }

    public Integer getBoardCount(){
        return boardRepository.findAll().size();
    }
}
