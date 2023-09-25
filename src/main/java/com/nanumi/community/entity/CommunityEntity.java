package com.nanumi.community.entity;

import com.nanumi.community.dto.CommunityDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "communityBoard")
public class CommunityEntity {

    //[게시글 테이블 각 컬럼 생성]
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    @Column(name = "board_id")
    protected Long id; //게시글 아이디

    @Column
    protected String board_title; // 게시글 제목

    @Column
    protected String category_id; //카테고리 아이디

    @Column(columnDefinition = "LONGTEXT")
    protected String board_story; // 게시글 내용

    @Column
    protected String date; // 게시글 작성 시간(날짜)

    @Column   // 해당 게시글의 좋아요 받은 '총 갯수'
    protected Long likeCount;

    @Column(length = 255)
    private String user_seq; //회원 일련번호

    @Column
    protected String nickName; // 사용자 닉네임

    @Column(columnDefinition = "LONGTEXT")
    protected String UserImageUrl; // 사용자 프로필 이미지

    @Column
    protected String UserGrade; // 사용자 등급

    // 게시글 1개에 포함된 이미지 목록
    @ElementCollection(fetch = FetchType.EAGER) // 어노테이션이 사용된 것은 해당 필드가 EAGER 전략을 사용하여 즉시 로딩되도록 설정하기 위함입니다. 즉, 게시글 엔티티를 조회할 때 해당 게시글과 연관된 이미지 URL들도 함께 조회됩니다.
    @Column(columnDefinition = "LONGTEXT")//해당 컬럼을 MySQL LONGTEXT 타입으로 설정하며, 이는 매우 큰 문자열(최대 4GB)을 저장할 수 있는 타입입니다.
    protected List<String> boardImages = new ArrayList<>();



    //[DTO 객체 -> Entity 객체 변환]
    public static CommunityEntity toSaveEntity(CommunityDTO communityDTO) {
        CommunityEntity communityEntity = new CommunityEntity();

        communityEntity.setId(communityDTO.getBoard_id());
        communityEntity.setBoard_title(communityDTO.getBoard_title());
        communityEntity.setUser_seq(communityDTO.getUser_seq());
        communityEntity.setCategory_id(communityDTO.getCategory_id());
        communityEntity.setBoard_story(communityDTO.getBoard_story());
        communityEntity.setUserGrade(communityDTO.getUserGrade());
        communityEntity.setNickName(communityDTO.getNickName());
        communityEntity.setDate(communityDTO.getDate());
        communityEntity.setLikeCount(communityDTO.getLikeCount());
        communityEntity.setUserImageUrl(communityDTO.getUserImageUrl());
        communityEntity.setBoardImages(new ArrayList<>(communityDTO.getBoardImageUrls()));
        return communityEntity;
    }



}
