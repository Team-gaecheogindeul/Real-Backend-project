package com.nanumi.community.dto;

import com.nanumi.community.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// DTO(Data Transfer Object), VO, Bean, Entity
@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class CommunityDTO {

    //[게시글 인스턴스 변수 11개 선언]

    private Long board_id; //게시글 아이디
    private String board_title; // 게시글 제목
    private String category_id; // 게시글 카테고리 아이디
    private String board_story; // 게시글 내용
    private List<String> boardImageUrls; // 게시글 첨부 이미지 : DTO에서는 이미지 URL들을 문자열 리스트로 관리한다.
    private String date; // 게시글 작성 시간(날짜)
    private Long likeCount; //좋아요 갯수

    private String user_seq; //회원 일련번호
    private String nickName; // 사용자 닉네임
    private String UserImageUrl; // 사용자 프로필 이미지
    private String UserGrade; // 사용자 등급



    // [#2. 게시글 : Entity -> DTO 변환]
    public static CommunityDTO toCommunityDTO(CommunityEntity communityEntity) {
        CommunityDTO communityDTO = new CommunityDTO(); // 빈 객체 생성

        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
        communityDTO.setBoard_id(communityEntity.getId() != null ? communityEntity.getId() : 0L);
        communityDTO.setBoard_title(communityEntity.getBoard_title() != null ? communityEntity.getBoard_title() : "");
        communityDTO.setCategory_id(communityEntity.getCategory_id() != null ? communityEntity.getCategory_id() : "");
        communityDTO.setBoard_story(communityEntity.getBoard_story() != null ? communityEntity.getBoard_story() : "");
        communityDTO.setDate(communityEntity.getDate() != null ? communityEntity.getDate() : "");
        communityDTO.setLikeCount(communityEntity.getLikeCount() != null ? communityEntity.getLikeCount() : 0L);
        communityDTO.setUser_seq(communityEntity.getUser_seq() != null ? communityEntity.getUser_seq() : "");
        communityDTO.setNickName(communityEntity.getNickName() != null ? communityEntity.getNickName() : "");
        communityDTO.setUserImageUrl(communityEntity.getUserImageUrl() != null ? communityEntity.getUserImageUrl() : "");
        communityDTO.setUserGrade(communityEntity.getUserGrade() != null ? communityEntity.getUserGrade() : "");
        communityDTO.setBoardImageUrls(communityEntity.getBoardImages() != null ? new ArrayList<>(communityEntity.getBoardImages()) : new ArrayList<>());
        return communityDTO; //서비스 클래스로 반환
    }
//
//    public static CommunityDTO toCommunityDTO(CollegeEntity collegeEntity) {
//        CommunityDTO communityDTO = new CommunityDTO(); // 빈 객체 생성
//
//        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
//        communityDTO.setBoard_id(collegeEntity.getId() != null ? collegeEntity.getId() : 0L);
//        communityDTO.setBoard_title(collegeEntity.getBoard_title() != null ? collegeEntity.getBoard_title() : "");
//        communityDTO.setCategory_id(collegeEntity.getCategory_id() != null ? collegeEntity.getCategory_id() : "");
//        communityDTO.setBoard_story(collegeEntity.getBoard_story() != null ? collegeEntity.getBoard_story() : "");
//        communityDTO.setDate(collegeEntity.getDate() != null ? collegeEntity.getDate() : "");
//        communityDTO.setLikeCount(collegeEntity.getLikeCount() != null ? collegeEntity.getLikeCount() : 0L);
//        communityDTO.setUser_seq(collegeEntity.getUser_seq() != null ? collegeEntity.getUser_seq() : "");
//        communityDTO.setNickName(collegeEntity.getNickName() != null ? collegeEntity.getNickName() : "");
//        communityDTO.setUserImageUrl(collegeEntity.getUserImageUrl() != null ? collegeEntity.getUserImageUrl() : "");
//        communityDTO.setUserGrade(collegeEntity.getUserGrade() != null ? collegeEntity.getUserGrade() : "");
//        communityDTO.setBoardImageUrls(collegeEntity.getBoardImages() != null ? new ArrayList<>(collegeEntity.getBoardImages()) : new ArrayList<>());
//        return communityDTO; //서비스 클래스로 반환
//    }
//    public static CommunityDTO toCommunityDTO(FreeEntity freeEntity) {
//        CommunityDTO communityDTO = new CommunityDTO(); // 빈 객체 생성
//
//        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
//        communityDTO.setBoard_id(freeEntity.getId() != null ? freeEntity.getId() : 0L);
//        communityDTO.setBoard_title(freeEntity.getBoard_title() != null ? freeEntity.getBoard_title() : "");
//        communityDTO.setCategory_id(freeEntity.getCategory_id() != null ? freeEntity.getCategory_id() : "");
//        communityDTO.setBoard_story(freeEntity.getBoard_story() != null ? freeEntity.getBoard_story() : "");
//        communityDTO.setDate(freeEntity.getDate() != null ? freeEntity.getDate() : "");
//        communityDTO.setLikeCount(freeEntity.getLikeCount() != null ? freeEntity.getLikeCount() : 0L);
//        communityDTO.setUser_seq(freeEntity.getUser_seq() != null ? freeEntity.getUser_seq() : "");
//        communityDTO.setNickName(freeEntity.getNickName() != null ? freeEntity.getNickName() : "");
//        communityDTO.setUserImageUrl(freeEntity.getUserImageUrl() != null ? freeEntity.getUserImageUrl() : "");
//        communityDTO.setUserGrade(freeEntity.getUserGrade() != null ? freeEntity.getUserGrade() : "");
//        communityDTO.setBoardImageUrls(freeEntity.getBoardImages() != null ? new ArrayList<>(freeEntity.getBoardImages()) : new ArrayList<>());
//        return communityDTO; //서비스 클래스로 반환
//    }
//    public static CommunityDTO toCommunityDTO(SchoolEntity schoolEntity) {
//        CommunityDTO communityDTO = new CommunityDTO(); // 빈 객체 생성
//
//        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
//        communityDTO.setBoard_id(schoolEntity.getId() != null ? schoolEntity.getId() : 0L);
//        communityDTO.setBoard_title(schoolEntity.getBoard_title() != null ? schoolEntity.getBoard_title() : "");
//        communityDTO.setCategory_id(schoolEntity.getCategory_id() != null ? schoolEntity.getCategory_id() : "");
//        communityDTO.setBoard_story(schoolEntity.getBoard_story() != null ? schoolEntity.getBoard_story() : "");
//        communityDTO.setDate(schoolEntity.getDate() != null ? schoolEntity.getDate() : "");
//        communityDTO.setLikeCount(schoolEntity.getLikeCount() != null ? schoolEntity.getLikeCount() : 0L);
//        communityDTO.setUser_seq(schoolEntity.getUser_seq() != null ? schoolEntity.getUser_seq() : "");
//        communityDTO.setNickName(schoolEntity.getNickName() != null ? schoolEntity.getNickName() : "");
//        communityDTO.setUserImageUrl(schoolEntity.getUserImageUrl() != null ? schoolEntity.getUserImageUrl() : "");
//        communityDTO.setUserGrade(schoolEntity.getUserGrade() != null ? schoolEntity.getUserGrade() : "");
//        communityDTO.setBoardImageUrls(schoolEntity.getBoardImages() != null ? new ArrayList<>(schoolEntity.getBoardImages()) : new ArrayList<>());
//        return communityDTO; //서비스 클래스로 반환
//    }
//    public static CommunityDTO toCommunityDTO(LearnEntity learnEntity) {
//        CommunityDTO communityDTO = new CommunityDTO(); // 빈 객체 생성
//
//        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.
//        communityDTO.setBoard_id(learnEntity.getId() != null ? learnEntity.getId() : 0L);
//        communityDTO.setBoard_title(learnEntity.getBoard_title() != null ? learnEntity.getBoard_title() : "");
//        communityDTO.setCategory_id(learnEntity.getCategory_id() != null ? learnEntity.getCategory_id() : "");
//        communityDTO.setBoard_story(learnEntity.getBoard_story() != null ? learnEntity.getBoard_story() : "");
//        communityDTO.setDate(learnEntity.getDate() != null ? learnEntity.getDate() : "");
//        communityDTO.setLikeCount(learnEntity.getLikeCount() != null ? learnEntity.getLikeCount() : 0L);
//        communityDTO.setUser_seq(learnEntity.getUser_seq() != null ? learnEntity.getUser_seq() : "");
//        communityDTO.setNickName(learnEntity.getNickName() != null ? learnEntity.getNickName() : "");
//        communityDTO.setUserImageUrl(learnEntity.getUserImageUrl() != null ? learnEntity.getUserImageUrl() : "");
//        communityDTO.setUserGrade(learnEntity.getUserGrade() != null ? learnEntity.getUserGrade() : "");
//        communityDTO.setBoardImageUrls(learnEntity.getBoardImages() != null ? new ArrayList<>(learnEntity.getBoardImages()) : new ArrayList<>());
//        return communityDTO; //서비스 클래스로 반환
//    }

}
