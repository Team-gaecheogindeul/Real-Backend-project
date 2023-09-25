package com.nanumi.community.dto;

import com.nanumi.community.entity.CommentEntity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class CommentDTO {

    //게시글과 댓글은 일반적으로 별도의 DTO 를 사용하는 것이 좋습니다. 이는 게시글과 댓글이 각각 다른 생명주기와 특성을 가지고 있기 때문이다.

    private Long board_id; //게시글 아이디
    private String user_seq; //회원 일련번호
    private String nickName; // 사용자 닉네임
    private String UserImageUrl; // 사용자 프로필 이미지

    private Long comment_id; //댓글 고유 아이디
    private String content; // 댓글 내용
    private List<String> contentImageUrls; // 댓글 첨부 이미지
    private LocalDateTime createdDate; // 댓글 생성시간
    private LocalDateTime updatedDate; // 댓글 수정시간

    /* 대댓글 관련 변수 */
    private Long parentCommentId; // 부모 댓글 아이디 (대댓글인 경우 존재한다.)
    private List<CommentDTO> childComments = new ArrayList<>(); 	// 현재 부모 댓글에 속하는, 대댓글 목록 리스트


    // [댓글 : Entity -> DTO 변환]
    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO(); // 빈 객체 생성

        // 각 속성에 대해 null 검사를 수행한 후 해당 속성을 설정하며, 값이 null인 경우 기본값을 설정합니다.

        commentDTO.setBoard_id(commentEntity.getBoardId() != null ? commentEntity.getBoardId() : 0L);
        commentDTO.setUser_seq(commentEntity.getUserSeq() != null ? commentEntity.getUserSeq() : "");
        commentDTO.setComment_id(commentEntity.getId() != null ? commentEntity.getId() : 0L);
        commentDTO.setContent(commentEntity.getContent() != null ? commentEntity.getContent() : "");
        commentDTO.setCreatedDate(commentEntity.getCreatedDate());
        commentDTO.setUpdatedDate(commentEntity.getUpdatedDate());
        commentDTO.setNickName(commentEntity.getNickName() != null ? commentEntity.getNickName() : "");
        commentDTO.setUserImageUrl(commentEntity.getUserImageUrl() != null ? commentEntity.getUserImageUrl() : "");
        commentDTO.setContentImageUrls(commentEntity.getContentImageUrls() != null ? new ArrayList<>(commentEntity.getContentImageUrls()) : new ArrayList<>());

        List<CommentDTO> childCommentDTO = new ArrayList<>();
        for( CommentEntity childCommentEntity: commentEntity.getChildComments()){
            childCommentDTO.add(toCommentDTO(childCommentEntity));
        }
        commentDTO.setChildComments(childCommentDTO);


        return commentDTO; //서비스 클래스로 '수정된 원본 댓글' 반환
    }


}

