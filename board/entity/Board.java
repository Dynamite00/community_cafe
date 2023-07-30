package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity   // DB에 있는 테이블을 의미함
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // IDENTITY는 mariaDB에서 사용
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;
}
