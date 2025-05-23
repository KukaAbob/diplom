package com.bazarweb.bazarweb.controller.Order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazarweb.bazarweb.dto.OrderDTO;
import com.bazarweb.bazarweb.model.Order.Order;
import com.bazarweb.bazarweb.service.Order.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

@PostMapping("/create")
public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO request) {
    try {
        Order order = orderService.createUserOrder(
            request.getUserId(),
            request.getAddressId(),
            request.getPaymentId(),
            request.getDate(),
            request.getStatus(),
            request.getTotal(),
            request.getOrderItems()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
        
    } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
    
    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@RequestParam String email) {
        try {
            List<OrderDTO> orders = orderService.getUserOrders(email);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Получение информации о конкретном заказе
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable int orderId) {
        return orderService.getOrder(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Обновление статуса заказа (например, выполнен или нет)
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable int orderId,
            @RequestParam boolean executed) {
        try {
            orderService.updateOrderStatus(orderId, executed);
            return ResponseEntity.ok("Order status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
    }

    // Получение списка заказов с фильтрацией
    @GetMapping
    public ResponseEntity<Page<Order>> fetchFilteredOrders(
            @RequestParam(defaultValue = "all") String executed,
            @RequestParam(defaultValue = "all") String orderAgeInDays,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orders = orderService.fetchFiltered(executed, orderAgeInDays, pageRequest);
        return ResponseEntity.ok(orders);
    }
    
}
