package com.bazarweb.bazarweb.service.Cart;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.bazarweb.bazarweb.dto.CartDTO;
import com.bazarweb.bazarweb.dto.CartItemDTO;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Cart.Cart;
import com.bazarweb.bazarweb.model.Cart.CartItem;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.repository.Cart.CartItemRepository;
import com.bazarweb.bazarweb.repository.Cart.CartRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;
import com.bazarweb.bazarweb.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public Cart findOrCreateCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        
        // Ищем корзину по userId, чтобы избежать дублирования
        Optional<Cart> cartOptional = cartRepository.findByUserId(user.getId());  // Предположим, что у вас есть метод findByUserId
        return cartOptional.orElseGet(() -> createCart(user));  // Если корзины нет - создаём новую
    }
    

    private Cart createCart(User user) {
        return cartRepository.save(new Cart(user));
    }

    public Cart addToCart(String email, int productId, int quantity) {
        Cart cart = findOrCreateCartByEmail(email);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getProductStatus() == ProductStatus.IN_STOCK) {
            cart.update(product, quantity);
            return cartRepository.save(cart);
        } else {
            return cart; // Продукт недоступен, корзина остаётся без изменений
        }
    }

    public void removeCartItem(int cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Товар в корзине не найден");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public CartDTO updateCartItem(int cartItemId, int newQuantity) {
        // Находим элемент корзины по ID
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Элемент корзины не найден"));
        
        // Обновляем количество
        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);
        
        // Пересчитываем общую стоимость корзины
        Cart cart = cartItem.getCart();
        recalculateTotalPrice(cart);
        cart = cartRepository.save(cart);
        
        // Преобразуем в DTO и возвращаем
        return convertToDTO(cart);
    }

    private void recalculateTotalPrice(Cart cart) {
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
    }
    
    private CartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> items = cart.getItems().stream()
            .map(item -> new CartItemDTO(
                    item.getId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getProduct().getPrice(),
                    item.getProduct().getId()))
            .toList();

        return new CartDTO(cart.getId(), cart.getUser().getEmail(), cart.getTotalPrice(), items);
    }
}
