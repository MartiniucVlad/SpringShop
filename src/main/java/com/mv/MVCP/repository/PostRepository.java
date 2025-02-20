package com.mv.MVCP.repository;

import com.mv.MVCP.models.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findByTitle(String title);

    @Query("SELECT p from PostEntity p WHERE p.title LIKE CONCAT('%',:title,'%')")
     List<PostEntity> searchProducts(String title);
}
