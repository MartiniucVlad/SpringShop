package com.mv.MVCP.repository;

import com.mv.MVCP.models.Product;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Registered
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByTitle(String title);

    @Query("SELECT p from Product p WHERE p.title LIKE CONCAT('%',:title,'%')")
     List<Product> searchProducts(String title);
}
