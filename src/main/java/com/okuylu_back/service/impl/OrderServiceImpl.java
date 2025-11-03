package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.OrderRequest;
import com.okuylu_back.dto.responses.*;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.CartItem;
import com.okuylu_back.model.Order;
import com.okuylu_back.model.OrderItem;
import com.okuylu_back.model.User;
import com.okuylu_back.model.enums.OrderStatus;
import com.okuylu_back.repository.BookRepository;
import com.okuylu_back.repository.CartItemRepository;
import com.okuylu_back.repository.OrderRepository;
import com.okuylu_back.repository.UserRepository;
import com.okuylu_back.security.service.OrderNotificationService;
import com.okuylu_back.service.DiscountService;
import com.okuylu_back.service.OrderService;
import com.okuylu_back.service.StripeService;
import com.okuylu_back.utils.exceptions.OrderProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.stripe.model.checkout.Session;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final OrderNotificationService emailService;
    private final StripeService stripeService;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Value("${exchange.rate.som-to-usd}")
    private BigDecimal exchangeRate;

    @Value("${client.url}")
    private String clientUrl;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartItemRepository cartItemRepository,
            BookRepository bookRepository,
            UserRepository userRepository,
            DiscountService discountService,
            OrderNotificationService emailService, StripeService stripeService) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.discountService = discountService;
        this.emailService = emailService;
        this.stripeService = stripeService;
    }

    @Override
    @Transactional
    public PaymentResponse createOrder(String userEmail, OrderRequest orderRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Please add books before placing an order.");
        }


        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setSelfPickup(orderRequest.isSelfPickup());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setAdditionalNotes(orderRequest.getAdditionalNotes());

        if (!orderRequest.isSelfPickup()) {
            if (orderRequest.getDeliveryAddress() == null || orderRequest.getDeliveryAddress().isEmpty()) {
                throw new IllegalArgumentException("Delivery address is required for delivery orders.");
            }
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        }

        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal discountedPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> itemsToRemove = new ArrayList<>();


        for (CartItem cartItem : cartItems) {
            Book book = cartItem.getBook();

            if (book.getStockQuantity() < cartItem.getQuantity()) {
                continue; // Пропускаем эту книгу
            }
            
            BigDecimal itemPrice = book.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(itemPrice);

            BigDecimal discountedPricePerBook = discountService.calculateDiscountedPrice(book);
            BigDecimal itemDiscountedPrice = discountedPricePerBook.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            discountedPrice = discountedPrice.add(itemDiscountedPrice);


            bookRepository.save(book);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(itemDiscountedPrice);
            orderItems.add(orderItem);


        }

        if (orderItems.isEmpty()) {
            throw new IllegalStateException("None of the books in your cart are available in sufficient quantity.");
        }

        order.setTotalAmount(totalPrice);
        order.setDiscountedPrice(discountedPrice);
        order.setOrderItems(orderItems);
        // Сохранение заказа
        Order savedOrder = orderRepository.save(order);

        try {

            BigDecimal amountInUSD = discountedPrice.divide(exchangeRate, 2, RoundingMode.HALF_UP);
            Session checkoutSession = stripeService.createCheckoutSession(savedOrder.getOrderId(), amountInUSD);


            return new PaymentResponse(
                    checkoutSession.getId(),  // Use session ID instead of client secret
                    savedOrder.getOrderId(),
                    "USD",
                    amountInUSD.doubleValue(),
                    checkoutSession.getUrl()  // Add the checkout URL
            );
        } catch (Exception e) {
            logger.error("Error while creating payment for order: {}", savedOrder.getOrderId(), e);
            throw new OrderProcessingException("Failed to create payment: " + e.getMessage());
        }
    }



    @Override
    public OrderResponse confirmPaymentBySessionId(String sessionId) {
        try {
            Session session = stripeService.retrieveSession(sessionId);  // получаем сессию из Stripe
            if (!"complete".equals(session.getStatus())) {
                throw new OrderProcessingException("Checkout session not completed.");
            }

            Long orderId = Long.valueOf(session.getMetadata().get("orderId"));
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found for ID: " + orderId));

            order.setStatus(OrderStatus.PROCESSING);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);

            try {
                emailService.sendOrderStatusUpdateEmail(order);
            } catch (Exception e) {
                logger.error("Failed to send order status update email", e);
            }

            return mapToOrderResponse(order);
        } catch (Exception e) {
            logger.error("Error while confirming payment", e);
            throw new OrderProcessingException("Error while confirming payment: " + e.getMessage());
        }
    }




    @Override
    public PageResponse<OrderResponse> getUserOrders(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return PageResponse.fromPage(
                orderRepository.findByUserOrderByCreatedAtDesc(user, pageable)
                        .map(this::mapToOrderResponse)
        );
    }




    @Override
    public PageResponse<OrderResponse> getOrdersByStatusForManager(OrderStatus status, String managerEmail, int page, int size) {
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findByStatusAndAssignedManager(status, manager, pageable);

        return PageResponse.fromPage(orders.map(this::mapToOrderResponse));
    }


    @Override
    public OrderResponse assignOrderToManager(Long orderId, String managerEmail) {
        Order order = getOrderByIdForManager(orderId);
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        order.setAssignedManager(manager);
        orderRepository.save(order);
        emailService.sendOrderStatusUpdateEmail(order);

        return mapToOrderResponse(order);
    }




    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderByIdForManager(orderId);
        boolean isSelfPickup = order.isSelfPickup(); // или order.getSelfPickup(), если используется такой геттер

        Set<OrderStatus> allowedStatuses = isSelfPickup
                ? Set.of(OrderStatus.PROCESSING, OrderStatus.PICKED_UP, OrderStatus.CANCELLED, OrderStatus.PAYMENT_FAILED)
                : Set.of(OrderStatus.PROCESSING, OrderStatus.SHIPPED, OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.PAYMENT_FAILED);

        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status for the selected delivery method (self-pickup or shipping).");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        try {
            emailService.sendOrderStatusUpdateEmail(order);
        } catch (Exception e) {
            logger.error("Failed to send order status update", e);
        }

        return mapToOrderResponse(order);
    }



    @Override
    public PageResponse<OrderResponse> getUnassignedOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findUnassignedOrdersOrderByCreatedAtDesc(OrderStatus.PROCESSING, pageable);

        return PageResponse.fromPage(orders.map(this::mapToOrderResponse));
    }



    @Override
    public PageResponse<OrderResponse> getManagerOrders(String managerEmail, int page, int size) {
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Order> orders = orderRepository.findByAssignedManagerOrderByCreatedAtDesc(manager, pageable);

        return PageResponse.fromPage(orders.map(this::mapToOrderResponse));
    }

    @Override
    public Order getOrderByIdForManager(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found"));
    }


    @Override
    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getOrderItemId(),
                orderItem.getOrder().getOrderId(),
                orderItem.getBook().getBookId(),
                orderItem.getBook().getTitle(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    @Override
    public OrderResponse getOrderByIdAndEmail(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getEmail().equals(email) &&
                (order.getAssignedManager() == null || !order.getAssignedManager().getEmail().equals(email))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied to this order");        }

        return mapToOrderResponse(order);
    }

    @Override
    public OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getUser().getEmail(),
                order.getStatus(),
                order.isSelfPickup(),
                order.getTotalAmount(),
                order.getDiscountedPrice(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getDeliveryAddress(),
                order.getPhoneNumber(),
                order.getAdditionalNotes(),
                order.getAssignedManager() != null ? order.getAssignedManager().getEmail() : null,
                order.getAssignedManager() != null ? order.getAssignedManager().getPhone() : null,
                order.getOrderItems().stream()
                        .map(this::mapToOrderItemResponse)
                        .collect(Collectors.toList()),
                order.getDeliveryCost(),
                order.isReceived()
        );
    }

    @Override
    @Transactional
    public OrderResponse setDeliveryCost(Long orderId, BigDecimal deliveryCost, String managerEmail) {
        Order order = getOrderByIdForManager(orderId);

        if (order.getAssignedManager() == null || !order.getAssignedManager().getEmail().equals(managerEmail)) {
            throw new RuntimeException("Access denied. This order is not assigned to the current manager.");
        }

        if (order.isSelfPickup()) {
            throw new RuntimeException("Cannot set delivery cost for pickup orders.");
        }

        if (deliveryCost.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Delivery cost cannot be negative.");
        }

        order.setDeliveryCost(deliveryCost);
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);


        try {
            emailService.sendOrderStatusUpdateEmail(savedOrder);
        } catch (Exception e) {
            logger.error("Failed to send delivery cost update email", e);
        }

        return mapToOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmOrderDelivery(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied to this order");
        }

        if (order.getStatus() != OrderStatus.SHIPPED&&order.getStatus()!=OrderStatus.DELIVERED&&order.getStatus()!=OrderStatus.PICKED_UP) {
            throw new RuntimeException("Order must be in 'SHIPPED' or 'DELIVERED'  or 'PICKED_UP' status to confirm receipt.");
        }

        if (order.getStatus()==OrderStatus.PICKED_UP)
            order.setStatus(OrderStatus.PICKED_UP);
        else
            order.setStatus(OrderStatus.DELIVERED);
        order.setReceived(true);
        orderRepository.save(order);

        try {
            emailService.sendOrderStatusUpdateEmail(order);
        } catch (Exception e) {
            logger.error("Failed to send order delivery confirmation email", e);
        }

        return mapToOrderResponse(order);
    }



    @Override
    @Transactional
    public PageResponse<OrderResponse> getAllOrdersForAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> result = orderRepository.findAll(pageable)
                .map(this::mapToOrderResponse);

        return PageResponse.fromPage(result);
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersByStatusForAdmin(OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderResponse> result = orderRepository.findByStatus(status, pageable)
                .map(this::mapToOrderResponse);

        return PageResponse.fromPage(result);
    }


    @Override
    @Transactional
    public OrderResponse updateOrderStatusByManager(Long orderId, OrderStatus newStatus, String managerEmail) {
        Order order = getOrderByIdForManager(orderId);

        if (order.getAssignedManager() == null || !order.getAssignedManager().getEmail().equals(managerEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not assigned to this order.");

        }

        boolean isSelfPickup = order.isSelfPickup();

        Set<OrderStatus> allowedStatuses = isSelfPickup
                ? Set.of(OrderStatus.PROCESSING, OrderStatus.PICKED_UP, OrderStatus.CANCELLED, OrderStatus.PAYMENT_FAILED)
                : Set.of(OrderStatus.PROCESSING, OrderStatus.SHIPPED, OrderStatus.DELIVERED, OrderStatus.CANCELLED, OrderStatus.PAYMENT_FAILED);

        if (!allowedStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("This status is not allowed for the selected delivery method.");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        try {
            emailService.sendOrderStatusUpdateEmail(order);
        } catch (Exception e) {
            logger.error("Failed to send order status update", e);
        }

        return mapToOrderResponse(order);
    }


    @Override
    @Transactional
    public ManagerStatisticsDto getManagerStatisticsByDate(String managerEmail, LocalDate startDate, LocalDate endDate) {
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Order> orders;

        if (startDate != null && endDate != null) {
            LocalDateTime from = startDate.atStartOfDay();
            LocalDateTime to = endDate.atTime(LocalTime.MAX);
            orders = orderRepository.findByAssignedManagerAndCreatedAtBetween(manager, from, to);
        } else {
            orders = orderRepository.findByAssignedManager(manager);
        }

        int totalOrders = orders.size();
        int deliveredOrders = 0;
        int shippedOrders = 0;
        int processingOrders = 0;
        int cancelledOrders = 0;
        int pickedUpOrders = 0;
        int deliveredAndPickedUp = 0;

        long totalProcessingTimeMinutes = 0;
        int completedOrders = 0;

        for (Order order : orders) {
            switch (order.getStatus()) {
                case DELIVERED -> deliveredOrders++;
                case SHIPPED -> shippedOrders++;
                case PROCESSING -> processingOrders++;
                case CANCELLED -> cancelledOrders++;
                case PICKED_UP -> pickedUpOrders++;
            }

            if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.PICKED_UP) {
                deliveredAndPickedUp++;
            }

            if (order.getCreatedAt() != null && order.getUpdatedAt() != null &&
                    (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.PICKED_UP)) {
                long minutes = Duration.between(order.getCreatedAt(), order.getUpdatedAt()).toMinutes();
                totalProcessingTimeMinutes += minutes;
                completedOrders++;
            }
        }

        double averageProcessingTimeInHours = completedOrders == 0
                ? 0
                : (double) totalProcessingTimeMinutes / 60 / completedOrders;

        return new ManagerStatisticsDto(
                totalOrders,
                deliveredOrders,
                shippedOrders,
                processingOrders,
                cancelledOrders,
                pickedUpOrders,
                averageProcessingTimeInHours,
                deliveredAndPickedUp
        );
    }


    @Override
    @Transactional
    public void processSuccessfulPayment(Long orderId) {
        logger.info("Processing successful payment for order: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        List<CartItem> cartItems = cartItemRepository.findByUser(order.getUser());


        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);

            order.setUpdatedAt(LocalDateTime.now());

            List<CartItem> itemsToRemove = new ArrayList<>();
            for (CartItem cartItem : cartItems) {
                Book book = cartItem.getBook();
                if (book.getStockQuantity() < cartItem.getQuantity()) {
                    continue; // Пропускаем эту книгу
                }
                itemsToRemove.add(cartItem);
            }
            cartItemRepository.deleteAll(itemsToRemove);

            for (OrderItem item : order.getOrderItems()) {
                Book book = item.getBook();
                book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
                bookRepository.save(book);
            }
            emailService.sendOrderConfirmationEmail(order);

            logger.info("Order status updated to PROCESSING for order: {}", orderId);
        } else {
            logger.warn("Order {} is not in PENDING status, current status: {}", orderId, order.getStatus());
        }
    }

    public OrderStatusResponse getOrderStatus(String userEmail, Long orderId) {
        Order order = orderRepository.findByOrderIdAndUserEmail(orderId, userEmail)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        return new OrderStatusResponse(
                order.getOrderId(),
                order.getStatus().toString(),
                order.getStripePaymentIntentId() != null
        );
    }


}
