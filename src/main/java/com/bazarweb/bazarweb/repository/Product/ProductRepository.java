package com.bazarweb.bazarweb.repository.Product;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(int id);
    Optional<Product> findByName(String name);
    boolean existsById(int id);
}