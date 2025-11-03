package com.okuylu_back.service;

import com.okuylu_back.dto.request.DiscountRequest;
import com.okuylu_back.dto.responses.DiscountResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.model.Discount;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {

    List<DiscountResponse> getAllDiscounts();

    Discount getDiscountByIdAsDiscount(Long id);

    DiscountResponse getDiscountById(Long id);

    DiscountResponse createDiscount(DiscountRequest discountRequest);

    DiscountResponse updateDiscount(Long id, DiscountRequest updatedDiscount);

    void deleteDiscount(Long id);

    BigDecimal calculateDiscountedPrice(Book book);

    boolean isDiscountActive(Discount discount);

    List<DiscountResponse> getActiveDiscounts();
}
