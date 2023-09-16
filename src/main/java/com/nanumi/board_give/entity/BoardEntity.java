package com.nanumi.board_give.entity;

import com.nanumi.board_give.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @Column(length = 255)
    private String user_seq; //회원 일련번호

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

    // 게시글 1개에 포함된 이미지 목록
    @ElementCollection(fetch = FetchType.EAGER) // 어노테이션이 사용된 것은 해당 필드가 EAGER 전략을 사용하여 즉시 로딩되도록 설정하기 위함입니다. 즉, 게시글 엔티티를 조회할 때 해당 게시글과 연관된 이미지 URL들도 함께 조회됩니다.
    @Column(columnDefinition = "LONGTEXT")//해당 컬럼을 MySQL LONGTEXT 타입으로 설정하며, 이는 매우 큰 문자열(최대 4GB)을 저장할 수 있는 타입입니다.
    private List<String> imageUrls = new ArrayList<>();

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
        boardEntity.setImageUrls(new ArrayList<>(boardDTO.getImageUrls()));
        return boardEntity;
    }
/* 객체 변환시, 이미지 변환에서 굳이 new ArrayList<> 쓰는 이유

일반적으로 DTO와 Entity는 서로 다른 생명 주기와 책임을 가지므로, 이들 간에 데이터 동기화가 항상 필요한 것은 아닙니다.

예를 들어, 사용자의 요청을 처리하는 중에 DTO의 데이터가 변경되었다고 해봅시다.
이 경우 Entity까지 바로 영향을 받게 되면 예기치 않은 문제가 발생할 수 있습니다.
그래서 일반적으로 DTO의 변경 사항이 Entity에 바로 반영되지 않도록 분리하는 것이 좋습니다.
또한 JPA 같은 ORM 프레임워크를 사용할 때 Entity 인스턴스의 상태 변화는 트랜잭션 커밋 시점에 데이터베이스에 반영됩니다.
따라서 실수로 Entity 상태를 변경하더라도 해당 트랜잭션을 롤백하면 원래 상태로 복구할 수 있습니다.
하지만 만약 DTO와 Entity가 동일한 리스트 인스턴스를 공유한다면, 이런 방식으로 문제를 해결하기 어렵습니다.

따라서 위에서 제안드린 것처럼 new ArrayList<>(boardDTO.getImageUrl())와 같은 방식으로
새 리스트 인스턴스를 생성하여 사용하는 것이 안전합니다.







 */
}

















