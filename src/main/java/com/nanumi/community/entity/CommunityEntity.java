package com.nanumi.community.entity;

import com.nanumi.community.dto.CommunityDTO;
import com.nanumi.community.entity.ImageEntity.ImageEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column
    protected Long user_seq; //회원 일련번호

    @Column
    protected String nickName; // 사용자 닉네임

    @Column(columnDefinition = "LONGTEXT")
    protected String UserImageUrl; // 사용자 프로필 이미지

    @Column
    protected String UserGrade; // 사용자 등급

    /*
    Entity 에서는 이미지 정보를 ImageEntity 객체 리스트로 관리하고 있습니다. 따라서 이 두 타입 사이에 변환이 필요합니다.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<ImageEntity> boardImages; // 게시글 첨부 이미지 :

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

        /*
        이 코드는 CommunityDTO 객체의 '이미지 URL 문자열 리스트(boardImageUrls)' 를 가져와서
        이를 CommunityEntity 객체의 '이미지 Entity 리스트(boardImages)' 에 설정하는 부분입니다.
         */
        if (communityDTO.getBoardImageUrls() != null) { //CommunityDTO 객체의 boardImageUrls 필드가 null 이 아닌지 확인
            List<ImageEntity> imageEntities = communityDTO.getBoardImageUrls().stream() // String 리스트(boardImageUrls)를 Stream 으로 변환
                    //.map 함수 : 스트림의 각 요소에 대해 주어진 함수를 적용하고 그 결과로 구성된 새로운 스트림을 반환
                    .map(url -> {
                        ImageEntity image = new ImageEntity();
                        image.setBoardImageUrl(url);
                        return image;
                    }) // 각 URL을 사용하여 ImageEntity 객체를 생성
                    .collect(Collectors.toList());
            communityEntity.setBoardImages(imageEntities);
        } else {
            communityEntity.setBoardImages(Collections.emptyList());
        }
        return communityEntity;
    }



}
