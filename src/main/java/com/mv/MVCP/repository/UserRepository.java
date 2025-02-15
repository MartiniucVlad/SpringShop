package com.mv.MVCP.repository;

import com.mv.MVCP.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);

    @Query("SELECT u FROM users u WHERE u.id IN (" +
            "SELECT uf.id FROM users ue JOIN ue.partialFriendList uf WHERE ue.id = :userId " +
            "UNION " +
            "SELECT ue.id FROM users ue JOIN ue.partialFriendList uf WHERE uf.id = :userId)")
    List<UserEntity> findFullFriendList(@Param("userId") Long userId);

}
