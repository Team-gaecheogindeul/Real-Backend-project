package com.nanumi.community.repository;

import com.nanumi.community.entity.CollegeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollegeRepository extends JpaRepository<CollegeEntity,Long> {
    //[#4. (개인) 나눔 게시글 전체 조회]
    @Query("SELECT b FROM CollegeEntity b WHERE b.user_seq = :user_seq")
    Page<CollegeEntity> findAllByUserSeq(@Param("user_seq") String user_seq, Pageable pageable);
    //위 코드는 user_seq에 해당하는 모든 게시글(boardEntity)을 조회하되, 결과를 페이지 단위로 반환하도록 하는 JPQL 쿼리이다.

    //[#6. 게시글 수정]
    @Query("SELECT b FROM CollegeEntity b WHERE b.user_seq = :user_seq")
    CollegeEntity findByUserSeq(@Param("user_seq") String user_seq);

    // [#9. 검색기능 ]
    @Query("SELECT b FROM CollegeEntity b WHERE b.board_title LIKE %:keyword% OR b.board_story LIKE %:keyword%")
    Page<CollegeEntity> findByKeyword(String keyword, Pageable pageable);

    @Query(nativeQuery = true, value =
            "SELECT cb.* FROM community_board cb " +
                    "INNER JOIN chat_board ch ON cb.board_id = ch.post_id " +
                    "WHERE cb.DTYPE = 'CollegeEntity' AND ch.new_category1 = :categoryId " +
                    "ORDER BY RAND() LIMIT 3")
    List<CollegeEntity> findTop3ByCategoryIdRandom(@Param("categoryId") String categoryId);
}
