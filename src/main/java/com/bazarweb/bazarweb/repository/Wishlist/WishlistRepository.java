package com.bazarweb.bazarweb.repository.Wishlist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Wishlist.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer>{
    Optional<Wishlist> findByUserId(int id);
} 
