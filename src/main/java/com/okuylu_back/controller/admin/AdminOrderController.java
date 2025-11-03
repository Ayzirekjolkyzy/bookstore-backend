package com.okuylu_back.controller.admin;

import com.okuylu_back.dto.responses.OrderResponse;
import com.okuylu_back.dto.responses.PageResponse;
import com.okuylu_back.model.enums.OrderStatus;
import com.okuylu_back.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

        private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @Operation(summary = "Получить все заказы с пагинацией")
    @GetMapping
    public ResponseEntity<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<OrderResponse> orders = orderService.getAllOrdersForAdmin(page, size);
        return ResponseEntity.ok(orders);
    }




    @Operation(summary = "Обновить статус заказа")
    @PostMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus newStatus
    ) {
        OrderResponse order = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(order);
    }



    @Operation(summary = "Получить список заказов по статусу с пагинацией")
    @GetMapping("/status/{status}")
    public ResponseEntity<PageResponse<OrderResponse>> getOrdersByStatusForAdmin(
            @PathVariable OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<OrderResponse> orders = orderService.getOrdersByStatusForAdmin(status, page, size);
        return ResponseEntity.ok(orders);
    }


}
