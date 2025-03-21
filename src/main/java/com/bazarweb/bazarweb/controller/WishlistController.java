package com.bazarweb.bazarweb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.DTO.WishlistDTO;
import com.bazarweb.bazarweb.DTO.WishlistItemDTO;
import com.bazarweb.bazarweb.DTO.Requests.Wishlist.AddToWishlistRequest;
import com.bazarweb.bazarweb.DTO.Requests.Wishlist.ClearWishlistRequest;
import com.bazarweb.bazarweb.DTO.Requests.Wishlist.MoveToCartRequest;
import com.bazarweb.bazarweb.DTO.Requests.Wishlist.RemoveFromWishlistRequest;
import com.bazarweb.bazarweb.model.Wishlist.Wishlist;
import com.bazarweb.bazarweb.service.Cart.CartService;
import com.bazarweb.bazarweb.service.Wishlist.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final CartService cartService;

    public WishlistController(WishlistService wishlistService, CartService cartService) {
        this.wishlistService = wishlistService;
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<WishlistDTO> getWishlist(@RequestParam String email) {
        Wishlist wishlist = wishlistService.findOrCreateWishlistByEmail(email);
        WishlistDTO wishlistDTO = toDTO(wishlist);
        return ResponseEntity.ok(wishlistDTO);
    }

    private WishlistDTO toDTO(Wishlist wishlist) {
        List<WishlistItemDTO> items = wishlist.getItems().stream()
            .map(item -> new WishlistItemDTO(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getProduct().getPrice(),
                    item.getProduct().getProductStatus()))
            .toList();

        return new WishlistDTO(wishlist.getId(), wishlist.getUser().getEmail(), items);
    }

    @PostMapping("/add")
    public ResponseEntity<WishlistDTO> addToWishlist(@RequestBody AddToWishlistRequest request) {
        Wishlist wishlist = wishlistService.addToWishlist(request.getEmail(), request.getProductId());
        return ResponseEntity.ok(toDTO(wishlist));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromWishlist(@RequestBody RemoveFromWishlistRequest request) {
        wishlistService.removeWishlistItem(request.getEmail(), request.getProductId());
        return ResponseEntity.ok("Товар удален из списка желаний");
    }

    @PostMapping("/clear")
    public ResponseEntity<WishlistDTO> clearWishlist(@RequestBody ClearWishlistRequest request) {
        Wishlist wishlist = wishlistService.clearWishlist(request.getEmail());
        return ResponseEntity.ok(toDTO(wishlist));
    }

    @PostMapping("/move-to-cart")
    public ResponseEntity<String> moveToCart(@RequestBody MoveToCartRequest request) {
        wishlistService.moveToCart(request.getEmail(), request.getProductId(), cartService);
        return ResponseEntity.ok("Товар перемещен в корзину");
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkProductInWishlist(@RequestParam String email, @RequestParam int productId) {
        boolean isInWishlist = wishlistService.isProductInWishlist(email, productId);
        return ResponseEntity.ok(isInWishlist);
    }
}