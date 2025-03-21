package com.bazarweb.bazarweb.repository.Cart;

import com.bazarweb.bazarweb.model.Cart.Cart;
import com.bazarweb.bazarweb.model.User.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findById(int id);
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(int userId);
}
