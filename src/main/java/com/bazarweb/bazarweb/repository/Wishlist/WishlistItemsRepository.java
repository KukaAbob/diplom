package com.bazarweb.bazarweb.repository.Wishlist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Wishlist.WishlistItems;

@Repository
public interface WishlistItemsRepository extends JpaRepository<WishlistItems, Integer> {
    
    // Стандартный метод Spring Data JPA
    void deleteByWishlistUserEmailAndProductId(String email, int productId);
    
    // Или если нужно найти сначала
    Optional<WishlistItems> findByWishlistUserEmailAndProductId(String email, int productId);
}