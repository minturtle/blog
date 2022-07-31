package com.blog.blog.service;

import com.blog.blog.domain.Board;
import com.blog.blog.domain.Reply;
import com.blog.blog.domain.User;
import com.blog.blog.dto.BoardDto;
import com.blog.blog.dto.BoardPreviewDto;
import com.blog.blog.dto.ReplyDto;
import com.blog.blog.repository.BoardRepository;
import com.blog.blog.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    /*
    * input : page, 최소 1이상의 값이 들어와야함.
    * output: (page-1) * 5+1 번째 Board부터 5개의 Board 찾아서 DTO로 변환 후 리스트로 반환.
    *
    * exception : page < 1, 또는 page > maxPage 이라면 IllegalArgumentException 발생,
    * 1, Pageable 객체를 생성후, dao로 부터 5개의 값을 조회한 후 리턴한다.
    *
    * */
    public List<BoardPreviewDto> getBoardsByPage(Integer page) throws IllegalArgumentException{
        if(page < 1 || page > getMaxPage()) throw new IllegalArgumentException("page 입력이 잘못되었습니다.");


        //1, 이때 PageRequest는 0부터 시작하므로 page-1을 해준다.
        PageRequest pageRequest = PageRequest.of(page-1, 5);
        return boardRepository.findAll(pageRequest).getContent().stream().map(b->new BoardPreviewDto(b.getId(), b.getTitle(),
                b.getWriter() == null ? "Someone" : b.getWriter().getUsername(), b.getCreatedAt())).collect(Collectors.toList());
    }


    /*
    * board객체를 save하는 매서드
    * input : boardDTO, writer
    * output: none
    * exception: boardDTO, 또는 유저(Writer) 정보를 찾을 수 없을때 IllegalArgumentException을 리턴한다.
    * 1, boardDTO로 board객체를 만든다.
    * 2, DAO(DB)에 저장한다.
    * */
    public void save(BoardDto boardDto, User writer) throws IllegalArgumentException{
        if(boardDto == null) throw new IllegalArgumentException("필수 값이 들어오지 않았습니다.");
        else if(writer == null) throw new IllegalArgumentException("유저 정보를 찾을 수 없습니다.");

        //1
        Board board = new Board(boardDto.getTitle(), boardDto.getContent(), writer);
        //2
        boardRepository.save(board);
    }


    /*
    * 현재 저장되어 있는 board의 총 갯수를 리턴하는 메서드
    *
    * */
    public Integer getBoardCount(){
        return boardRepository.findAll().size();
    }

    /*
    * board의 id값으로 board를 찾아 리턴하는 메서드
    * input: board id
    * output : 찾은 board 객체
    *
    * exception: 입력된 id로 board를 찾을 수 없는 경우. IllegalArgumentException 발생
    *
    * */
    public Board getBoardById(Integer id) throws IllegalArgumentException{
        return boardRepository.findById(id).orElseThrow(()->{throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");});
    }

    /*
    * board의 조회수를 증가시키는 메서드
    * input: board id
    *
    * exception: 입력된 id로 board를 찾을 수 없는 경우. IllegalArgumentException 발생
    * 1, board를 DAO로 부터 조회한다.
    * 2, board의 값을 바꾼다. 이때 객체의 값을 바꾸면 자동으로 JPA가 UPDATE처리를 해준다.
    * */
    public void addBoardCount(Integer id) throws IllegalArgumentException{
        //1
        Board board = boardRepository.findById(id).orElseThrow(()->{throw new IllegalArgumentException("데이터를 찾을 수 없습니다.");});;

        //2
        board.setCount(board.getCount()+1);
    }

    /*
    * 어떤 board 객체에 reply을 추가하는 메서드
    *
    * exception: boardDTO, 유저(Writer) 정보를 찾을 수 없을때 또는  입력된 id로 board를 찾을 수 없을 때 IllegalArgumentException을 리턴한다.
    * */
    public void addReply(ReplyDto replyDto, User writer)throws IllegalArgumentException{
        if(replyDto == null) throw new IllegalArgumentException("필수 값이 들어오지 않았습니다.");
        else if(writer == null) throw new IllegalArgumentException("유저 정보를 찾을 수 없습니다.");

        Board board = getBoardById(replyDto.getBoardId()); //throwable illegalArgumentException

        Reply reply = new Reply(replyDto.getContent(), writer, board);

        replyRepository.save(reply);
    }



    /*
    * board의 갯수를 조회해 5개씩 자른 Page의 갯수를 리턴함.
    * */
    public int getMaxPage(){
        Integer boardCount = getBoardCount();
        return boardCount % 5 == 0 && boardCount != 0 ? boardCount /5 : boardCount /5 + 1;
    }
}
