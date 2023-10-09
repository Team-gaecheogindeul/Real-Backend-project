package com.nanumi.recommend.repository;

import com.nanumi.recommend.entity.FinalComDistributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FinalComDistributionRepository extends JpaRepository<FinalComDistributionEntity,String> {
    @Query("SELECT u FROM FinalComDistributionEntity u WHERE u.user.user_seq = :user_seq")
    FinalComDistributionEntity findByUserSeq(@Param("user_seq") String user_seq);
}
