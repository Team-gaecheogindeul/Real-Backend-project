package com.nanumi.community.dto;

import com.nanumi.community.entity.CommunityEntity;
import com.nanumi.community.entity.ImageEntity.ImageEntity;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    private Long user_seq; //회원 일련번호
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
        communityDTO.setUser_seq(communityEntity.getUser_seq() != null ? communityEntity.getUser_seq() : 0L);
        communityDTO.setNickName(communityEntity.getNickName() != null ? communityEntity.getNickName() : "");
        communityDTO.setUserImageUrl(communityEntity.getUserImageUrl() != null ? communityEntity.getUserImageUrl() : "");
        communityDTO.setUserGrade(communityEntity.getUserGrade() != null ? communityEntity.getUserGrade() : "");

        /*
        여기서 communityEntity.getBoardImages()가 null이 아닌 경우,
        즉 게시글에 첨부된 이미지들이 실제로 있는 경우에는 각 ImageEntity에서 URL을 추출하여 새로운 String 리스트를 만들고, 이를 DTO의 boardImageUrls 필드에 설정합니다.
        만약 게시글에 첨부된 이미지가 없어서 boardImages가 null인 경우에는
        빈 리스트를 DTO의 boardImageUrls 필드에 설정합니다.
         */
        if (communityEntity.getBoardImages() != null) {
            List<String> imageUrls = communityEntity.getBoardImages().stream()
                    .map(ImageEntity::getBoardImageUrl)
                    .collect(Collectors.toList());
            communityDTO.setBoardImageUrls(imageUrls);
        } else {
            communityDTO.setBoardImageUrls(Collections.emptyList());
        }
        return communityDTO; //서비스 클래스로 반환
    }

}
