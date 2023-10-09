package com.nanumi.recommend.repository;

import com.nanumi.recommend.entity.UserViewLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserViewLogRepository extends JpaRepository<UserViewLogEntity, String> {
    @Query("SELECT b FROM UserViewLogEntity b WHERE b.user_seq = :user_seq")
    UserViewLogEntity findByUserSeq(@Param("user_seq") String user_seq);

    Optional<UserViewLogEntity> findById(String userSeq);
}
