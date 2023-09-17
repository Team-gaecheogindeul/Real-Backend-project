package com.nanumi.community.repository;

import com.nanumi.community.entity.UsersLikesEntity.CommunityLikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersLikesCommunityRepository extends JpaRepository<CommunityLikesEntity, String>  {
    Optional<CommunityLikesEntity> findByUserSeq(String userSeq);
}
