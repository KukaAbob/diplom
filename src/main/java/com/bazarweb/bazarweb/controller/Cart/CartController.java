package com.bazarweb.bazarweb.controller.Cart; 
 
import java.util.List; 
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.CartDTO;
import com.bazarweb.bazarweb.dto.CartItemDTO;
import com.bazarweb.bazarweb.dto.Requests.Cart.AddToCartRequest;
import com.bazarweb.bazarweb.dto.Requests.Cart.UpdateCartRequest;
import com.bazarweb.bazarweb.model.Cart.Cart;
import com.bazarweb.bazarweb.service.Cart.CartService;

import lombok.RequiredArgsConstructor; 
 
 
 
@RestController 
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController { 
 
    private final CartService cartService; 
 
    @GetMapping 
    public ResponseEntity<CartDTO> getCart(@RequestParam String email) { 
        Cart cart = cartService.findOrCreateCartByEmail(email); 
        CartDTO cartDTO = toDTO(cart); 
        return ResponseEntity.ok(cartDTO); 
    } 
 
    private CartDTO toDTO(Cart cart) { 
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
 
    @PostMapping("/add") 
    public ResponseEntity<CartDTO> addToCart(@RequestBody AddToCartRequest request) { 
        Cart cart = cartService.addToCart(request.getEmail(), request.getProductId(), request.getQuantity()); 
        return ResponseEntity.ok(toDTO(cart)); 
    } 

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFromCart(@PathVariable int id) {
        cartService.removeCartItem(id);
        return ResponseEntity.ok("Товар удален из корзины");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable int id, @RequestBody UpdateCartRequest request){
        CartDTO updatedCart = cartService.updateCartItem(id, request.getQuantity());
        return ResponseEntity.ok(updatedCart);
    }
     
    
}