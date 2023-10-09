package com.nanumi.community.entity.CommentEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nanumi.community.dto.CommentDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
댓글과 대댓글을 고려한 엔티티 클래스를 만들 때, 자기 참조(self-reference) 관계를 사용하면 됩니다.
댓글이 다른 댓글(즉, 대댓글)을 가질 수 있으므로,
CommentEntity 클래스 내에서 다른 CommentEntity 객체를 참조해야 합니다.
 */
@Entity
@Getter
@Setter
@Table(name = "comment_table")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_seq")
    private String userSeq;

    @Column(name = "content", length = 1000)
    private String content;

    @ElementCollection
    @CollectionTable(name="comment_image", joinColumns=@JoinColumn(name="comment_id"))
    @Column(name="image_url")
    private List<String> contentImageUrls = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false) //이 컬럼은 null 값을 허용하지 않고(nullable = false) 업데이트가 불가능(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedDate;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "user_image_url")
    private String userImageUrl;

/*    @Column(name = "parent_comment_id")
      private Long parentCommentId;

   =>
  CommentEntity 클래스에서 parentCommentId 필드는 사용하지 않아도 됩니다.
  이미 childComments 필드를 통해 부모 댓글과 자식 댓글 간의 관계를 설정하고 있기 때문입니다.
  대신, parentCommentId가 아닌 parentCommentEntity 라는 이름의 필드를 추가하고, 이를 이용해
  양방향 매핑을 설정합니다.

*/
    // 자기 참조 관계 설정: 한 댓글은 여러 개의 자식 댓글을 가질 수 있으며, 한 자식 댓글은 하나의 부모 댓글을 가집니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentCommentId")
    private CommentEntity parentCommentEntity;


    // 현재 부모 댓글에 속하는, 대댓글 목록 리스트 - OneToMany 관계 설정
    // mappedBy를 통해 반대편 매핑을 지정한다. (양방향 연관관계 설정)
    //mappedBy 속성 값은 엔티티 내의 필드명으로 설정해야 합니다
    @OneToMany(mappedBy="parentCommentEntity", cascade=CascadeType.ALL, orphanRemoval=true )
    @JsonManagedReference  // 이 줄 추가: 순환 참조 방지용 어노테이션
    List<CommentEntity> childComments = new ArrayList<>();
    /*
    mappedBy="parentCommentEntity": 이 속성은 양방향 관계에서 주인이 아닌 쪽에서 사용하며, 연관관계의 주인 필드 이름을 값으로 설정합니다. 여기서는 parentCommentEntity가 연관관계의 주인입니다.
    cascade=CascadeType.ALL: 이 속성은 부모 엔티티가 수행하는 데이터베이스 작업을 자식 엔티티에게도 전파시킵니다. 여기서는 부모 댓글이 삭제될 때 자식 댓글들도 함께 삭제되도록 설정하였습니다.
    orphanRemoval=true: 이 속성은 부모 엔티티와 연결되지 않은 (즉, 고아가 된) 자식 엔티티를 자동으로 삭제합니다.
     */


    //[DTO 객체 -> Entity 객체 변환]
    public static CommentEntity toSaveEntity(CommentDTO commentDTO) {
        CommentEntity commentEntity = new CommentEntity(); // 부모객체 선언

        commentEntity.setBoardId(commentDTO.getBoard_id() != null ? commentDTO.getBoard_id() : null);
        commentEntity.setUserSeq(commentDTO.getUser_seq() != null ? commentDTO.getUser_seq() : "");
        commentEntity.setId(commentDTO.getComment_id() != null ? commentDTO.getComment_id() : 0L);
        commentEntity.setContent(commentDTO.getContent() != null ? commentDTO.getContent() : "");
        commentEntity.setNickName(commentDTO.getNickName() != null ? commentDTO.getNickName() : "");
        commentEntity.setUserImageUrl(commentDTO.getUserImageUrl() != null ? commentDTO.getUserImageUrl() : "");
        commentEntity.setContentImageUrls(commentDTO.getContentImageUrls() != null ? new ArrayList<>(commentDTO.getContentImageUrls()) : new ArrayList<>());
//
//        // 자식 댓글 목록 (DTO 의 childComments 필드가 null 이 아니며, 빈 리스트가 아닐 경우 = 부모댓글일때)
//        if (commentDTO.getChildComments() != null && !commentDTO.getChildComments().isEmpty()) {
//            //각각의 자식 댓글 DTO 를 다시 toSaveEntity 메서드를 통해 엔티티로 변환
//            for (CommentDTO childDTO : commentDTO.getChildComments()) {
//                CommentEntity childEntity = toSaveEntity(childDTO);
//
//                //부모-자식 관계 설정
//                commentEntity.getChildComments().add(childEntity);
//            }
//        }

        return commentEntity;
    }

    public void setParent(CommentEntity parentCommentEntity) {
        this.parentCommentEntity = parentCommentEntity;
    }


    //[#14 .댓글 수정]
    public void updateFromDTO(CommentDTO commentDTO) {
        // CommentEntity 의 각 필드를 CommentDTO 로부터 받은 값으로 업데이트
        // 만약 DTO 에 해당 필드의 값이 없다면 (null), 기존 값을 그대로 유지

        if (commentDTO.getComment_id() != null) {
            this.setId(commentDTO.getComment_id());
        }

        if (commentDTO.getBoard_id() != null) {
            this.setBoardId(commentDTO.getBoard_id());
        }

        if (commentDTO.getUser_seq() != null) {
            this.setUserSeq(commentDTO.getUser_seq());
        }

        if (commentDTO.getContent() != null) {
            this.setContent(commentDTO.getContent());
        }

        if (commentDTO.getNickName() != null) {
            this.setNickName(commentDTO.getNickName());
        }

        if (commentDTO.getUserImageUrl() != null) {
            this.setUserImageUrl(commentDTO.getUserImageUrl());
        }

        // contentImageUrls는 List<String> 형태이므로, 새 ArrayList 인스턴스를 생성하여 대입
        // 만약 DTO에 이미지 URL 리스트가 없다면, 기존 값을 그대로 유지
        //, 이미지 URL 리스트(contentImageUrls)도 비어있는 리스트가 아닌 경우에만 업데이트합니다. 이렇게 하면 빈 리스트가 제공되었을 때 기존 이미지 URL들을 삭제하는 것을 방지할 수 있습니다.
        if (commentDTO.getContentImageUrls() != null && !commentDTO.getContentImageUrls().isEmpty()) {
            this.setContentImageUrls(new ArrayList<>(commentDTO.getContentImageUrls()));
        }
        /*
        부모 댓글(parentCommentEntity)과 자식 댓글 목록(childComments)은 별도의 처리가 필요하며, 이 부분은 당신의 애플리케이션 로직에 따라 다르게 구현될 수 있습니다.
         */
    }

}
