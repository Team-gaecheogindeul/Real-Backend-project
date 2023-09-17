package com.nanumi.community.repository;

import com.nanumi.community.entity.FreeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreeRepository extends JpaRepository<FreeEntity,Long> {

    //[#4. 게시글 전체 조회]
    @Query("SELECT b FROM FreeEntity b WHERE b.user_seq = :user_seq")
    Page<FreeEntity> findAllByUserSeq(@Param("user_seq") String user_seq, Pageable pageable);
    //위 코드는 user_seq에 해당하는 모든 게시글(boardEntity)을 조회하되, 결과를 페이지 단위로 반환하도록 하는 JPQL 쿼리이다.

    //[#6. 게시글 수정]
    @Query("SELECT b FROM FreeEntity b WHERE b.user_seq = :user_seq")
    FreeEntity findByUserSeq(@Param("user_seq") String user_seq);

    // [#9. 검색기능 ]
    @Query("SELECT b FROM FreeEntity b WHERE b.board_title LIKE %:keyword% OR b.board_story LIKE %:keyword%")
    Page<FreeEntity> findByKeyword(String keyword, Pageable pageable);


}
