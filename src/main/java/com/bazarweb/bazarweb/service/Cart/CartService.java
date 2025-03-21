package com.bazarweb.bazarweb.service.Cart;

import org.springframework.stereotype.Service;
import com.bazarweb.bazarweb.enums.ProductStatus;
import com.bazarweb.bazarweb.model.Cart.Cart;
import com.bazarweb.bazarweb.model.Cart.CartItem;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.repository.Cart.CartItemRepository;
import com.bazarweb.bazarweb.repository.Cart.CartRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;
import com.bazarweb.bazarweb.repository.User.UserRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart findOrCreateCartByEmail(String email) {
        User user = userRepository.getUserByEmail(email)
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
    

    public Cart clearCart(String email) {
        Cart cart = findOrCreateCartByEmail(email);

        // Удаляем все элементы из корзины
        cart.getItems().clear();

        // Обновляем общую стоимость
        cart.setTotalPrice(BigDecimal.ZERO);

        return cartRepository.save(cart);
    }

    

}
