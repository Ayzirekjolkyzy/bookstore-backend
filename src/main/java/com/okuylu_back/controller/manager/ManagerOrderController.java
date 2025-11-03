package com.okuylu_back.controller.manager;


import com.okuylu_back.dto.responses.ManagerStatisticsDto;
import com.okuylu_back.dto.responses.OrderResponse;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.model.User;
import com.okuylu_back.model.enums.OrderStatus;
import com.okuylu_back.security.dto.request.ManagerProfileDto;
import com.okuylu_back.security.entity.PersonDetails;
import com.okuylu_back.security.service.UserService;
import com.okuylu_back.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/manager")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller")
public class ManagerOrderController {

    private final OrderService orderService;
    private final UserService userService;

    public ManagerOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @Operation(summary = "Получить список нераспределенных заказов (с пагинацией)")
    @GetMapping("/orders/unassigned")
    public ResponseEntity<PageResponse<OrderResponse>> getUnassignedOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderService.getUnassignedOrders(page, size));
    }



    @Operation(summary = "Получить список заказов по статусу (только для текущего менеджера, с пагинацией)")
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<PageResponse<OrderResponse>> getOrdersByStatusForManager(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        return ResponseEntity.ok(orderService.getOrdersByStatusForManager(status, managerEmail, page, size));
    }



    @Operation(summary = "Получить список заказов, назначенных менеджеру (с пагинацией)")
    @GetMapping("/orders/my-orders")
    public ResponseEntity<PageResponse<OrderResponse>> getManagerOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        return ResponseEntity.ok(orderService.getManagerOrders(managerEmail, page, size));
    }


    @Operation(summary = "Назначить заказ себе")
    @PostMapping("/orders/{orderId}/assign")
    public ResponseEntity<OrderResponse> assignOrderToSelf(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        OrderResponse order = orderService.assignOrderToManager(orderId, managerEmail);
        return ResponseEntity.ok(order);
    }



    @Operation(summary = "Обновить статус заказа")
    @PostMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus newStatus,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        OrderResponse order = orderService.updateOrderStatusByManager(orderId, newStatus, managerEmail);
        return ResponseEntity.ok(order);
    }


    @Operation(summary = "Установить стоимость доставки для заказа")
    @PostMapping("/orders/{orderId}/delivery-cost")
    public ResponseEntity<OrderResponse> setDeliveryCost(
            @PathVariable Long orderId,
            @RequestParam BigDecimal deliveryCost,
            Authentication authentication
    ) {
        String managerEmail = authentication.getName();
        OrderResponse order = orderService.setDeliveryCost(orderId, deliveryCost, managerEmail);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get user profile.",
            description = "Returns the profile data of the currently authenticated user.")
    @GetMapping("/profile")
    public ResponseEntity<ManagerProfileDto> getUserProfile(@AuthenticationPrincipal PersonDetails userDetails) {
        User user = userService.getUserById(userDetails.getUser().getUserId());
        ManagerProfileDto managerProfileDto = new ManagerProfileDto(user);
        return ResponseEntity.ok(managerProfileDto);
    }



    @GetMapping("/orders/statistics")
    public ManagerStatisticsDto getStatisticsWithDate(Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        String managerEmail = authentication.getName();
        return orderService.getManagerStatisticsByDate(managerEmail,startDate, endDate);
    }


}