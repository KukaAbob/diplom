package com.bazarweb.bazarweb.repository.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Product.Image;
import com.bazarweb.bazarweb.model.Product.Product;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByProductId(int productId);
    List<Image> findAllByProduct(Product product);
}
