package com.bazarweb.bazarweb.repository.Product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Product.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer>{
    Optional<Color> findByName(String name);
}
