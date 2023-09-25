package com.nanumi.community.repository;


import com.nanumi.community.entity.CommentEntity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity,Long> {


    //[#13 댓글 상세조회]
    // "게시글 ID(boardId)가 주어진 값과 일치하는 모든 CommentEntity 객체를 찾아서 리스트로 반환하라"는 쿼리가 생성됩니다.
    List<CommentEntity> findByBoardId(Long boardId);

    // 부모 댓글의 번호(parentCommentId)를 가지는 모든 자식 댓글 객체를 찾아서 리스트로 반환한다.
    List<CommentEntity> findByParentCommentEntity_Id(Long parentCommentId);
}
