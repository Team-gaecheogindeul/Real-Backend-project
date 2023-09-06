package com.nanumi.board_give.repository;

import com.nanumi.board_give.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// [상세 나눔 게시글 저장소] : 이를 통해 BoardEntity 클래스와 관련된 데이터베이스 연산을 처리할 수 있습니다.
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {


    //[#4. (개인) 나눔 게시글 전체 조회]
    @Query("SELECT b FROM BoardEntity b WHERE b.user_seq = :user_seq")
    List<BoardEntity> findAllByUserSeq(@Param("user_seq") Long user_seq); //명시적으로 열 이름을 지정


    //[#6. (개인) 나눔 게시글 수정]
    @Query("SELECT b FROM BoardEntity b WHERE b.user_seq = :user_seq")
    BoardEntity findByUserSeq(@Param("user_seq") Long user_seq);

    // [#10. 검색기능 : 게시글 제목과 책 내용으로 검색하는 쿼리]
    @Query("SELECT b FROM BoardEntity b WHERE b.board_title LIKE %:keyword% OR b.book_story LIKE %:keyword%")
    List<BoardEntity> findByKeyword(String keyword);

}














