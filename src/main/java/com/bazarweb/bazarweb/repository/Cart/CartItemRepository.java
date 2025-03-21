package com.bazarweb.bazarweb.repository.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bazarweb.bazarweb.model.Cart.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
