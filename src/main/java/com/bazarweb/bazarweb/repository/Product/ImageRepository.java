package com.bazarweb.bazarweb.repository.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Product.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByName(String name);
    List<Image> findByProductId(int productId);
    boolean existsByName(String name);
    void deleteByProductId(int productId);
    List<Image> findByNameAndContentType(String name, String contentType);
}
