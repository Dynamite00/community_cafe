package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository <Board, Integer> {      // JpaRepository 상속을 받음, <,> 에 어떠한 entity를 넣을 것인가
                                                                               // <entity , 컬럼의 기본키 타입을 지정>

    Page<Board> findByTitleContaining(String searchkeyword, Pageable pageable);

}
