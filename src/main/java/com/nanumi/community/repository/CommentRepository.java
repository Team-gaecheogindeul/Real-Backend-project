package com.nanumi.community.repository;


import com.nanumi.community.entity.CommentEntity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<CommentEntity,Long> {


}
