package com.bazarweb.bazarweb.repository.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Wishlist.WishlistItems;

@Repository
public interface WishlistItemsRepository extends JpaRepository<WishlistItems, Integer>{
} 
