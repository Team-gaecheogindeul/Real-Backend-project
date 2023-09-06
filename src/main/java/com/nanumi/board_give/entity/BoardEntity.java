package com.nanumi.board_give.entity;

import com.nanumi.board_give.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


// DB의 테이블 역할을 하는 클래스
@Entity
@Getter
@Setter
@Table(name = "board_give_table") //[#1. 나눔 상세 게시글 테이블]
public class BoardEntity {

    //[#1. 상세 나눔게시글 테이블 각 컬럼 생성]
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(name = "board_give_id")
    private Long id; //나눔 게시글 아이디

    @Column
    private String board_title; // 게시글 제목

    @Column
    private Long user_seq; //회원 일련번호

    @Column
    private String category_id; //책 카테고리 아이디

    @Column
    private String sub_category_id; //책 카테고리 아이디

    @Column
    private String state_underscore; // 상태_밑줄 흔적

    @Column(columnDefinition = "LONGTEXT")
    private String book_story; // 책 내용

    @Column
    private String state_notes;// 상태_필기 흔적

    @Column
    private String state_cover; // 상태_겉표지 상태

    @Column
    private String state_written_name; // 상태_이름 기입

    @Column
    private String state_page_color_change; //상태_페이지 변색

    @Column
    private String state_page_damage; //상태_페이지_손상

    @Column
    private String city_id; //지역 아이디

    @Column
    private String meet_want_location; // 거래 희망 지역

    @Column
    private String parcel_index; // 택배 가능

    @Column
    private String direct_index; // 직거래 가능

    @Column
    private String user_name; // 사용자 이름

    @Column
    private String date; // 게시글 작성 시간(날짜)

    @Column(columnDefinition = "LONGTEXT")
    private String imageUrl; // 이미지 URL

    @Column   // 해당 게시글의 좋아요 받은 '총 갯수'
    private Long likeCount;

    //[#1. DTO 객체 -> Entity 객체 변환]
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getBoard_give_id());
        boardEntity.setBoard_title(boardDTO.getBoard_title());
        boardEntity.setUser_seq(boardDTO.getUser_seq());
        boardEntity.setCategory_id(boardDTO.getCategory_id());
        boardEntity.setSub_category_id(boardDTO.getSub_category_id());
        boardEntity.setBook_story(boardDTO.getBook_story());
        boardEntity.setState_underscore(boardDTO.getState_underscore());
        boardEntity.setState_notes(boardDTO.getState_notes());
        boardEntity.setState_cover(boardDTO.getState_cover());
        boardEntity.setState_written_name(boardDTO.getState_written_name());
        boardEntity.setState_page_color_change(boardDTO.getState_page_color_change());
        boardEntity.setState_page_damage(boardDTO.getState_page_damage());
        boardEntity.setCity_id(boardDTO.getCity_id());
        boardEntity.setMeet_want_location(boardDTO.getMeet_want_location());
        boardEntity.setParcel_index(boardDTO.getParcel_index());
        boardEntity.setDirect_index(boardDTO.getDirect_index());
        boardEntity.setUser_name(boardDTO.getUser_name());
        boardEntity.setDate(boardDTO.getDate());
        boardEntity.setLikeCount(boardDTO.getLikeCount());
        boardEntity.setImageUrl(boardDTO.getImageUrl());
        return boardEntity;
    }

}

















