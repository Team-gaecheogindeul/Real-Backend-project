package com.nanumi.board_give.dto;


import com.nanumi.board_give.entity.BoardEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// DTO(Data Transfer Object), VO, Bean, Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {

    //[나눔 게시글 인스턴스 변수 선언]
    private Long board_give_id; //나눔 게시글 아이디
    private String board_title; // 게시글 제목
    private String user_seq; //회원 일련번호
    private String category_id; //책 카테고리 아이디
    private String sub_category_id; //책 카테고리 아이디
    private String book_story; // 책 내용
    private String state_underscore; // 상태_밑줄 흔적
    private String state_notes;// 상태_필기 흔적
    private String state_cover; // 상태_겉표지 상태
    private String state_written_name; // 상태_이름 기입
    private String state_page_color_change; //상태_페이지 변색
    private String state_page_damage; //상태_페이지_손상
    private String city_id; //지역 아이디
    private String meet_want_location; // 거래 희망 지역
    private String parcel_index; // 택배 가능
    private String direct_index; // 직거래 가능
    private String user_name; // 사용자 이름
    private String date; // 게시글 작성 시간(날짜)
    private List<String> imageUrls; // 이미지 바이너리 데이터
    private Long likeCount; //좋아요 갯수


    // [#2. 상세나눔게시글 : Entity -> DTO 변환]
    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO(); // 빈 객체 생성

        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
        boardDTO.setBoard_give_id(boardEntity.getId() != null ? boardEntity.getId() : 0L);
        boardDTO.setBoard_title(boardEntity.getBoard_title() != null ? boardEntity.getBoard_title() : "");
        boardDTO.setUser_seq(boardEntity.getUser_seq() != null ? boardEntity.getUser_seq() : "");
        boardDTO.setCategory_id(boardEntity.getCategory_id() != null ? boardEntity.getCategory_id() : "");
        boardDTO.setSub_category_id(boardEntity.getSub_category_id() != null ? boardEntity.getSub_category_id() : "");
        boardDTO.setBook_story(boardEntity.getBook_story() != null ? boardEntity.getBook_story() : "");
        boardDTO.setState_underscore(boardEntity.getState_underscore() != null ? boardEntity.getState_underscore() : "");
        boardDTO.setState_notes(boardEntity.getState_notes() != null ? boardEntity.getState_notes() : "");
        boardDTO.setState_cover(boardEntity.getState_cover() != null ? boardEntity.getState_cover() : "");
        boardDTO.setState_written_name(boardEntity.getState_written_name() != null ? boardEntity.getState_written_name() : "");
        boardDTO.setState_page_color_change(boardEntity.getState_page_color_change() != null ? boardEntity.getState_page_color_change() : "");
        boardDTO.setState_page_damage(boardEntity.getState_page_damage() != null ? boardEntity.getState_page_damage() : "");
        boardDTO.setCity_id(boardEntity.getCity_id() != null ? boardEntity.getCity_id() : "");
        boardDTO.setMeet_want_location(boardEntity.getMeet_want_location() != null ? boardEntity.getMeet_want_location() : "");
        boardDTO.setParcel_index(boardEntity.getParcel_index() != null ? boardEntity.getParcel_index() : "");
        boardDTO.setDirect_index(boardEntity.getDirect_index() != null ? boardEntity.getDirect_index() : "");
        boardDTO.setUser_name(boardEntity.getUser_name() != null ? boardEntity.getUser_name() : "");
        boardDTO.setDate(boardEntity.getDate() != null ? boardEntity.getDate() : "");
        boardDTO.setLikeCount(boardEntity.getLikeCount() != null ? boardEntity.getLikeCount() : 0L);
        boardDTO.setImageUrls(boardEntity.getImageUrls() != null ? new ArrayList<>(boardEntity.getImageUrls()) : new ArrayList<>());
        return boardDTO; //서비스 클래스로 반환
    }

}






