package com.nanumi.recommend.repository;

import com.nanumi.recommend.entity.FinalDistributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FinalDistributionRepository extends JpaRepository<FinalDistributionEntity,String> {
    @Query("SELECT u FROM FinalDistributionEntity u WHERE u.user.user_seq = :user_seq")
    FinalDistributionEntity findByUserSeq(@Param("user_seq") String user_seq);

}




