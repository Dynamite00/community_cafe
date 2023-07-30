package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    // 게시물 작성 처리
    @Autowired                // 의존 객체의 타입에 해당하는 bean을 찾아 알아서 읽어와서 아래에 주입해줌 (의존성 주입, Dependency Injection)
    private BoardRepository boardrepository;
        public void write(Board board)  {     // Board 라는 클래스에 board 라는 변수를 넣어서 받아줌, throws Exception 예외 처리


        boardrepository.save(board);             // entity를 save () 안에 넣어줌

    }





    // 게시물 리스트 처리
    public Page<Board> boardList(Pageable pageable){

            return boardrepository.findAll(pageable);

    }

    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){

            return boardrepository.findByTitleContaining(searchKeyword, pageable);

    }


    // 특정 게시물 불러오기 처리
    public Board boardView(Integer id){      // id에 번호 값이 들어왔을 때 아래 메서드가 실행됨

            return boardrepository.findById(id).get();   // findAllById () 안에 Integer 값을 넣어줌, .get() 을 추가함
    }

    // 특정 게시물 삭제 처리

    public void boardDelete(Integer id){

            boardrepository.deleteById(id);
    }

}
