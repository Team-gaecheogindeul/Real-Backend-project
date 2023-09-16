package com.nanumi.board_give.repository;

import com.nanumi.board_give.entity.UsersLikesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersLikesRepository extends JpaRepository<UsersLikesEntity, Long> {

    Optional<UsersLikesEntity> findByUserSeq(String userSeq);

}
