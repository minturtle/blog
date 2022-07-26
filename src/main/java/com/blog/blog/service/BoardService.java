package com.blog.blog.service;

import com.blog.blog.domain.Board;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.exceptions.NoBoardWithIdException;
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

    /*
    * input : page, 최소 1이상의 값이 들어와야함.
    * output: (page-1) * 5+1 번째 Board부터 5개의 Board 찾아서 DTO로 변환 후 리스트로 반환.
    *
    * 1, page < 1이라면 IllegalArgumentException 발생
    * 2, Pageable 객체를 생성후, dao로 부터 5개의 값을 조회한다.
    *
    * */
    public List<BoardPreviewDto> getBoardsByPage(Integer page) throws IllegalArgumentException{
        if(page < 1) throw new IllegalArgumentException("page 입력이 잘못되었습니다.");

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

    public Board getBoardById(Integer id) throws NoBoardWithIdException{
        return boardRepository.findById(id).orElseThrow(()->{throw new NoBoardWithIdException("데이터를 찾을 수 없습니다.");});
    }

    public void addBoardCount(Integer id) throws NoBoardWithIdException{
        Board board = boardRepository.findById(id).orElseThrow(()->{throw new NoBoardWithIdException("데이터를 찾을 수 없습니다.");});;
        board.setCount(board.getCount()+1);
    }
}
