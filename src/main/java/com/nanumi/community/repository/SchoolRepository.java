package com.nanumi.community.repository;

import com.nanumi.community.entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolEntity,Long> {
}
