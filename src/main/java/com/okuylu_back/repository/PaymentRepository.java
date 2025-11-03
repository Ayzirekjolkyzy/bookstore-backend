package com.okuylu_back.repository;

import com.okuylu_back.model.Payment;
import com.okuylu_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentHolder(User paymentHolder);

    Optional<Payment> findByPaymentIntentId(String paymentIntentId);

    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByPaymentHolderAndIsPaidTrue(User paymentHolder);
}