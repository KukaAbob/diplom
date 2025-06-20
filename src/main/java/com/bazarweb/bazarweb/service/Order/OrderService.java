package com.bazarweb.bazarweb.service.Order;

import com.bazarweb.bazarweb.dto.OrderDTO;
import com.bazarweb.bazarweb.dto.OrderItemDTO;
import com.bazarweb.bazarweb.enums.OrderStatus;
import com.bazarweb.bazarweb.exception.EmptyCartException;
import com.bazarweb.bazarweb.exception.UserNotFoundException;
import com.bazarweb.bazarweb.model.Cart.Cart;
import com.bazarweb.bazarweb.model.Order.Order;
import com.bazarweb.bazarweb.model.Order.OrderItem;
import com.bazarweb.bazarweb.model.Product.Product;
import com.bazarweb.bazarweb.model.User.Address;
import com.bazarweb.bazarweb.model.User.Payment;
import com.bazarweb.bazarweb.model.User.User;
import com.bazarweb.bazarweb.repository.Cart.CartRepository;
import com.bazarweb.bazarweb.repository.Order.OrderRepository;
import com.bazarweb.bazarweb.repository.Product.ProductRepository;
import com.bazarweb.bazarweb.repository.User.AddressRepository;
import com.bazarweb.bazarweb.repository.User.PaymentRepository;
import com.bazarweb.bazarweb.repository.User.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    
    public List<OrderDTO> getUserOrders(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> orderRepository.findByUser(u)
            .stream()
            .map(this::toOrderDto)
            .toList())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private OrderDTO toOrderDto(Order order) {
        return OrderDTO.builder()
            .userId(order.getUser().getId()) // Исправлено: order.getId() -> order.getUser().getId()
            .addressId(order.getAddress().getId())
            .paymentId(order.getPayment().getId())
            .date(order.getDate())
            .status(order.getStatus())
            .total(order.getTotal())
            .executed(order.isExecuted())
            .orderItems(order.getOrderItems().stream()
                .map(this::toOrderItemDto)
                .collect(Collectors.toList()))
            .build();
    }

    private OrderItemDTO toOrderItemDto(OrderItem orderItem) {
        return OrderItemDTO.builder()
            .productId(orderItem.getProduct().getId()) // Убрал дублированную строку
            .quantity(orderItem.getQuantity())
            .price(orderItem.getPrice())
            .build();
    }

    public Optional<Order> getOrder(int id) {
        return orderRepository.findById(id);
    }

    public Page<Order> fetchFiltered(String executed, String orderAgeInDays, PageRequest request) {
        LocalDateTime startTime = LocalDateTime.now();
        if (!"all".equals(orderAgeInDays)) {
            int days = Integer.parseInt(orderAgeInDays);
            startTime = startTime.minusDays(days);
        }

        if (!"all".equals(executed) && !"all".equals(orderAgeInDays)) {
            boolean executedState = Boolean.parseBoolean(executed);
            return orderRepository.findByExecutedAndDateAfter(executedState, startTime, request);
        } else if (!"all".equals(executed)) {
            boolean executedState = Boolean.parseBoolean(executed);
            return orderRepository.findByExecuted(executedState, request);
        } else if (!"all".equals(orderAgeInDays)) {
            return orderRepository.findByDateAfter(startTime, request);
        } else {
            return orderRepository.findAll(request);
        }
    }

    // Старый метод для работы с корзиной (оставляем для обратной совместимости)
    public Order createUserOrder(int id, LocalDateTime date, OrderStatus status, BigDecimal total) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new EmptyCartException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Order order = createNewOrder(user, cart);
        orderRepository.save(order);

        fillOrderItems(cart, order);
        cartRepository.delete(cart);

        return order;
    }

    // Новый метод с расширенными параметрами
    public Order createUserOrder(int userId, int addressId, int paymentId, 
                               LocalDateTime date, OrderStatus status, 
                               BigDecimal total, List<OrderItemDTO> orderItemDTOs) {
        
        // Validate user exists
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        
        // Validate address exists and belongs to user
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new IllegalArgumentException("Address with ID " + addressId + " not found"));
        
        if (address.getUser().getId() != userId) {
            throw new IllegalArgumentException("Address does not belong to the user");
        }
        
        // Validate payment method exists and belongs to user
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment method with ID " + paymentId + " not found"));
        
        if (payment.getUser().getId() != userId) {
            throw new IllegalArgumentException("Payment method does not belong to the user");
        }
        
        // Validate order items
        if (orderItemDTOs == null || orderItemDTOs.isEmpty()) {
            throw new EmptyCartException("Order must contain at least one item");
        }
        
        // Convert DTOs to entities and validate products
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        
        for (OrderItemDTO itemDTO : orderItemDTOs) {
            Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + itemDTO.getProductId() + " not found"));
            
            if (itemDTO.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0 for product " + product.getName());
            }
            
            // Check stock availability
            // if (product.getStock() < itemDTO.getQuantity()) {
            //     throw new IllegalArgumentException("Insufficient stock for product " + product.getName());
            // }
            
            OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(itemDTO.getQuantity())
                .price(product.getPrice())
                .build();
            
            orderItems.add(orderItem);
            calculatedTotal = calculatedTotal.add(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }
        
        // Validate total matches calculated total
        if (total.compareTo(calculatedTotal) != 0) {
            throw new IllegalArgumentException("Provided total does not match calculated total");
        }
        
        // Create the order
        Order order = Order.builder()
            .user(user)
            .address(address)
            .payment(payment)
            .date(date)
            .status(status)
            .total(total)
            .orderItems(orderItems)
            .build();
        
        // Set order reference in order items
        orderItems.forEach(item -> item.setOrder(order));
        
        // Update product stock
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            //product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }
        
        // Save and return the order
        return orderRepository.save(order);
    }

    public void updateOrderStatus(int orderId, boolean executed) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setExecuted(executed);
        orderRepository.save(order);
    }

    // Вспомогательный метод для создания заказа из корзины
    private Order createNewOrder(User user, Cart cart) {
        return Order.builder()
                .user(user)
                .date(LocalDateTime.now())
                .status(OrderStatus.IN_PROGRESS)
                .total(cart.getTotalPrice())
                .executed(false)
                .orderItems(null) // Items will be filled later
                .build();
    }

    private void fillOrderItems(Cart cart, Order order) {
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .product(item.getProduct())
                        .quantity(item.getQuantity())
                        .price(item.getProduct().getPrice())
                        .build())
                .toList();
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }

        public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(OrderDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderDTO.fromEntity(order);
    }

    public void updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}