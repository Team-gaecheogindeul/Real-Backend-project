package com.nanumi.community.entity.UsersLikesEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "community_likes_table") // 사용자 마다, 좋아요를 누른 게시글 번호 목록을 관리하는 테이블
public class CommunityLikesEntity {

    @Id // pk 컬럼 지정. 필수
    @Column(name = "user_seq")
    private String userSeq;

    // 좋아요 누른 게시글 고유 번호 목록
    @ElementCollection(fetch = FetchType.EAGER) // 실제 값을 포함하는 엔티티를 조인 테이블로 설정
    @Column(name = "board_id")
    private List<Long> boardId = new ArrayList<>();


    // 좋아요한 게시글 고유 번호를 추가하는 메소드
    public void addLikedBoardId(Long boardId) {

        this.boardId.add(boardId);
    }

    // 좋아요한 게시글 고유 번호를 삭제하는 메소드
    public void removeLikedBoardId(Long boardId) {

        this.boardId.remove(boardId);
    }


    //특정 게시글 id를 board_id 리스트에서 조회하여 반환 하는 메소드
    public boolean findByBoard_id(Long boardId) {
        // for-each 문을 통해서 게시글을 찾는다.
        for (Long board_id : this.boardId) {
            //게시글이 존재한다면
            if (board_id.equals(boardId)) {
                return true;
            }
        } return false;  // 게시글이 존재하지 않으면 0L 반환
    }

}
