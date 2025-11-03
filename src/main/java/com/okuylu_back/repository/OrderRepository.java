package com.okuylu_back.repository;

import com.okuylu_back.dto.responses.ManagerPerformanceDto;
import com.okuylu_back.model.enums.OrderStatus;
import com.okuylu_back.model.Order;
import com.okuylu_back.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    List<Order> findByAssignedManager(User manager);

    Page<Order> findByStatusAndAssignedManager(
            OrderStatus status,
            User assignedManager,
            Pageable pageable
    );

    List<Order> findByAssignedManagerAndCreatedAtBetween(User manager, LocalDateTime from, LocalDateTime to);

    Page<Order> findByAssignedManagerOrderByCreatedAtDesc(
            User assignedManager,
            Pageable pageable
    );


    @Override
    default List<Order> findAll() {
        return findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Query("SELECT o FROM Order o WHERE o.assignedManager IS NULL AND o.status = :status")
    List<Order> findUnassignedOrders(OrderStatus status);


    @Query("SELECT o FROM Order o WHERE o.assignedManager IS NULL AND o.status = :status")
    Page<Order> findUnassignedOrdersOrderByCreatedAtDesc(
            @Param("status") OrderStatus status,
            Pageable pageable
    );

    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = """
        SELECT AVG(EXTRACT(EPOCH FROM (o.updated_at - o.created_at))/60)
        FROM orders o
        WHERE o.status IN ('DELIVERED', 'PICKED_UP')
        AND o.created_at IS NOT NULL
        AND o.updated_at IS NOT NULL
        AND o.created_at BETWEEN :startDate AND :endDate
        """, nativeQuery = true)
    Double findAverageProcessingTimeInMinutes(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.okuylu_back.dto.responses.ManagerPerformanceDto(" +
            "u.email, u.phone, COUNT(o)) " +
            "FROM Order o JOIN o.assignedManager u " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY u.email, u.phone " +
            "ORDER BY COUNT(o) DESC")
    List<ManagerPerformanceDto> findTopManagersByCompletedOrders(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable);

    Optional<Order> findByOrderIdAndUserEmail(Long orderId, String userEmail);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);






}
