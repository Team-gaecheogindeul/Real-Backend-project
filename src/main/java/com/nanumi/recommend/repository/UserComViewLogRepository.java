package com.nanumi.recommend.repository;

import com.nanumi.recommend.entity.UserComViewLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserComViewLogRepository extends JpaRepository<UserComViewLogEntity,String> {
    @Query("SELECT b FROM UserComViewLogEntity b WHERE b.user_seq = :user_seq")
    UserComViewLogEntity findByUserSeq(@Param("user_seq") String user_seq);
}

