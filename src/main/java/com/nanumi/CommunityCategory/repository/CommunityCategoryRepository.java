package com.nanumi.CommunityCategory.repository;

import com.nanumi.CommunityCategory.entity.CommunityCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategoryEntity,Long> {
}
